package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dao.FarolDAO;
import model.Farol;
import util.GerenciadorIdioma;
import util.MensagemErro;

/**
 * Janela de listagem do estoque (JDialog).
 * Apresenta os farois em JTable com duas opcoes de ordenacao (por modelo
 * A-Z ou por quantidade decrescente), aplicadas via ORDER BY no SQL.
 */
public class TelaListagemEstoque extends JDialog {

    private static final long serialVersionUID = 1L;

    private final FarolDAO farolDAO = new FarolDAO();
    private final DefaultTableModel modelo = new DefaultTableModel();
    private final JTable tabela = new JTable(modelo);
    private final JComboBox<String> cbOrdenar = new JComboBox<>();

    /**
     * Cria a janela de listagem do estoque.
     *
     * @param owner janela proprietaria (menu principal)
     */
    public TelaListagemEstoque(Frame owner) {
        super(owner, true);
        setTitle(GerenciadorIdioma.get("titulo.listagem"));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(680, 400);
        setLocationRelativeTo(owner);
        montarComponentes();
        atualizar();
    }

    private void montarComponentes() {
        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.add(new JLabel(GerenciadorIdioma.get("label.ordenarPor")));
        cbOrdenar.setModel(new DefaultComboBoxModel<>(new String[] {
                GerenciadorIdioma.get("label.modelo"),
                GerenciadorIdioma.get("label.quantidade") }));
        topo.add(cbOrdenar);
        JButton btAtualizar = new JButton(GerenciadorIdioma.get("botao.atualizar"));
        btAtualizar.addActionListener(e -> atualizar());
        topo.add(btAtualizar);

        modelo.addColumn(GerenciadorIdioma.get("label.modelo"));
        modelo.addColumn(GerenciadorIdioma.get("label.ano"));
        modelo.addColumn(GerenciadorIdioma.get("label.tipo"));
        modelo.addColumn(GerenciadorIdioma.get("label.marca"));
        modelo.addColumn(GerenciadorIdioma.get("label.quantidade"));
        modelo.addColumn(GerenciadorIdioma.get("label.preco"));

        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    /** Recarrega os dados aplicando a ordenacao escolhida. */
    private void atualizar() {
        String ordenarPor = cbOrdenar.getSelectedIndex() == 1
                ? "quantidade" : "modelo";
        try {
            modelo.setRowCount(0);
            List<Farol> lista = farolDAO.listar(ordenarPor);
            for (Farol f : lista) {
                modelo.addRow(new Object[] { f.getModelo(), f.getAno(),
                        f.getTipo(), f.getMarca().getNome(),
                        f.getQuantidade(), f.getPreco() });
            }
            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        GerenciadorIdioma.get("msg.lista.vazia"),
                        getTitle(), JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    MensagemErro.traduzir(ex),
                    getTitle(), JOptionPane.ERROR_MESSAGE);
        }
    }
}

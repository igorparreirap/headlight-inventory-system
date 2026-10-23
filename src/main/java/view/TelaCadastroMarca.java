package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import dao.MarcaDAO;
import model.Marca;
import util.GerenciadorIdioma;
import util.MensagemErro;
import util.Validador;

/**
 * Tela de cadastro de Marca (cadastro basico) em JDialog modal.
 * Possui abas de Cadastro (CRUD) e Pesquisa (JTable com duplo clique),
 * impede duplicidade pelo nome e usa textos do i18n.
 */
public class TelaCadastroMarca extends JDialog {

    private static final long serialVersionUID = 1L;

    private final MarcaDAO marcaDAO = new MarcaDAO();

    private final JTextField txtId = new JTextField(5);
    private final JTextField txtNome = new JTextField(25);
    private final JTextField txtBusca = new JTextField(20);
    private final DefaultTableModel modelo = new DefaultTableModel();
    private final JTable tabela = new JTable(modelo);
    private final JTabbedPane abas = new JTabbedPane();

    /**
     * Cria a tela de cadastro de marca.
     *
     * @param owner janela proprietaria (menu principal)
     */
    public TelaCadastroMarca(Frame owner) {
        super(owner, true);
        setTitle(GerenciadorIdioma.get("titulo.cadastro.marca"));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        txtId.setEditable(false);
        abas.addTab(GerenciadorIdioma.get("aba.cadastro"), criarAbaCadastro());
        abas.addTab(GerenciadorIdioma.get("aba.pesquisa"), criarAbaPesquisa());
        add(abas);
        pack();
        setLocationRelativeTo(owner);
        limpar();
    }

    /** Monta a aba de cadastro. */
    private JPanel criarAbaCadastro() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(new JLabel(GerenciadorIdioma.get("label.id")), gbc);
        gbc.gridx = 1; painel.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(new JLabel(GerenciadorIdioma.get("label.nome")), gbc);
        gbc.gridx = 1; painel.add(txtNome, gbc);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btNovo = new JButton(GerenciadorIdioma.get("botao.novo"));
        JButton btSalvar = new JButton(GerenciadorIdioma.get("botao.salvar"));
        JButton btAlterar = new JButton(GerenciadorIdioma.get("botao.alterar"));
        JButton btExcluir = new JButton(GerenciadorIdioma.get("botao.excluir"));
        JButton btLimpar = new JButton(GerenciadorIdioma.get("botao.limpar"));
        btNovo.addActionListener(e -> limpar());
        btLimpar.addActionListener(e -> limpar());
        btSalvar.addActionListener(e -> salvar());
        btAlterar.addActionListener(e -> alterar());
        btExcluir.addActionListener(e -> excluir());
        botoes.add(btNovo); botoes.add(btSalvar); botoes.add(btAlterar);
        botoes.add(btExcluir); botoes.add(btLimpar);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        painel.add(botoes, gbc);
        return painel;
    }

    /** Monta a aba de pesquisa. */
    private JPanel criarAbaPesquisa() {
        JPanel painel = new JPanel(new BorderLayout(6, 6));
        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.add(new JLabel(GerenciadorIdioma.get("label.nome")));
        topo.add(txtBusca);
        JButton btPesquisar = new JButton(GerenciadorIdioma.get("botao.pesquisar"));
        btPesquisar.addActionListener(e -> pesquisar());
        topo.add(btPesquisar);

        modelo.addColumn(GerenciadorIdioma.get("label.id"));
        modelo.addColumn(GerenciadorIdioma.get("label.nome"));
        tabela.getSelectionModel().addListSelectionListener(ev -> {
            if (!ev.getValueIsAdjusting()) {
                selecionarLinha();
            }
        });

        painel.add(topo, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return painel;
    }

    /** Limpa os campos para um novo cadastro. */
    private void limpar() {
        txtId.setText("");
        txtNome.setText("");
        txtNome.requestFocus();
    }

    /** Inclui uma nova marca. */
    private void salvar() {
        String erro = Validador.isObrigatorio(txtNome.getText(),
                GerenciadorIdioma.get("label.nome"));
        if (erro != null) {
            mostrar(erro, JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            if (marcaDAO.existePorNome(txtNome.getText().trim())) {
                mostrar(GerenciadorIdioma.get("erro.duplicidade"),
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            marcaDAO.inserir(new Marca(txtNome.getText().trim()));
            mostrar(GerenciadorIdioma.get("msg.sucesso.inserir"),
                    JOptionPane.INFORMATION_MESSAGE);
            limpar();
        } catch (Exception ex) {
            erroOperacao(ex);
        }
    }

        /** Altera o registro selecionado (ou localizado pelos dados). */
    private void alterar() {
        String erro = Validador.isObrigatorio(txtNome.getText(),
                GerenciadorIdioma.get("label.nome"));
        if (erro != null) {
            mostrar(erro, JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Integer id = obterIdSelecionado();
            if (id == null) {
                mostrar(GerenciadorIdioma.get("erro.nao.encontrado"),
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            marcaDAO.alterar(new Marca(id, txtNome.getText().trim()));
            mostrar(GerenciadorIdioma.get("msg.sucesso.alterar"),
                    JOptionPane.INFORMATION_MESSAGE);
            limpar();
        } catch (Exception ex) {
            erroOperacao(ex);
        }
    }

    /**
     * Resolve o id do registro: usa o ID carregado pela pesquisa ou, se vazio,
     * localiza a marca pelo nome informado. Retorna null se nao existir.
     */
    private Integer obterIdSelecionado() throws SQLException {
        if (!txtId.getText().trim().isEmpty()) {
            return Integer.parseInt(txtId.getText().trim());
        }
        return marcaDAO.obterIdPorNome(txtNome.getText().trim());
    }

        /** Exclui o registro selecionado (ou localizado pelos dados). */
    private void excluir() {
        try {
            Integer id = obterIdSelecionado();
            if (id == null) {
                mostrar(GerenciadorIdioma.get("erro.nao.encontrado"),
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            int op = JOptionPane.showConfirmDialog(this,
                    GerenciadorIdioma.get("msg.confirma.excluir"),
                    GerenciadorIdioma.get("titulo.confirmacao"),
                    JOptionPane.YES_NO_OPTION);
            if (op != JOptionPane.YES_OPTION) {
                return;
            }
            marcaDAO.excluir(id);
            mostrar(GerenciadorIdioma.get("msg.sucesso.excluir"),
                    JOptionPane.INFORMATION_MESSAGE);
            limpar();
        } catch (Exception ex) {
            erroOperacao(ex);
        }
    }

    /** Executa a pesquisa e popula a tabela. */
    private void pesquisar() {
        try {
            modelo.setRowCount(0);
            List<Marca> lista = marcaDAO.pesquisarPorNome(txtBusca.getText().trim());
            for (Marca m : lista) {
                modelo.addRow(new Object[] { m.getId(), m.getNome() });
            }
            if (lista.isEmpty()) {
                mostrar(GerenciadorIdioma.get("msg.lista.vazia"),
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            erroOperacao(ex);
        }
    }

    /** Leva a linha selecionada para a aba de cadastro. */
    private void selecionarLinha() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return;
        }
        txtId.setText(String.valueOf(modelo.getValueAt(linha, 0)));
        txtNome.setText(String.valueOf(modelo.getValueAt(linha, 1)));
        abas.setSelectedIndex(0);
    }

    private void mostrar(String msg, int tipo) {
        JOptionPane.showMessageDialog(this, msg, getTitle(), tipo);
    }

    private void erroOperacao(Exception ex) {
        mostrar(MensagemErro.traduzir(ex),
                JOptionPane.ERROR_MESSAGE);
    }
}

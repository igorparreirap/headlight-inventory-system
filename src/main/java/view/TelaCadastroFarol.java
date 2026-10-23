package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import dao.FarolDAO;
import dao.MarcaDAO;
import model.Farol;
import model.Marca;
import util.GerenciadorIdioma;
import util.MensagemErro;
import util.Validador;

/**
 * Tela de cadastro de Farol (cadastro intermediario) em JDialog modal.
 * Aba Cadastro com JComboBox de Marca e validacoes; aba de Pesquisa complexa
 * com combobox de filtros (Modelo, Ano, Tipo, Marca), validacao condicional
 * e JTable de resultados. Impede duplicidade por (modelo, ano, tipo, marca).
 */
public class TelaCadastroFarol extends JDialog {

    private static final long serialVersionUID = 1L;

    private final FarolDAO farolDAO = new FarolDAO();
    private final MarcaDAO marcaDAO = new MarcaDAO();

    private final JTextField txtId = new JTextField(5);
    private final JTextField txtModelo = new JTextField(20);
    private final JTextField txtAno = new JTextField(20);
    private final JTextField txtTipo = new JTextField(20);
    private final JTextField txtQuantidade = new JTextField(20);
    private final JTextField txtPreco = new JTextField(20);
    private final JComboBox<Marca> cbMarca = new JComboBox<>();

    private final JComboBox<String> cbFiltro = new JComboBox<>();
    private final JTextField txtValor = new JTextField(18);
    private final DefaultTableModel modelo = new DefaultTableModel();
    private final JTable tabela = new JTable(modelo);
    private final JTabbedPane abas = new JTabbedPane();

    /**
     * Cria a tela de cadastro de farol.
     *
     * @param owner janela proprietaria (menu principal)
     */
    public TelaCadastroFarol(Frame owner) {
        super(owner, true);
        setTitle(GerenciadorIdioma.get("titulo.cadastro.farol"));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        txtId.setEditable(false);
        abas.addTab(GerenciadorIdioma.get("aba.cadastro"), criarAbaCadastro());
        abas.addTab(GerenciadorIdioma.get("aba.pesquisa"), criarAbaPesquisa());
        add(abas);
        pack();
        setLocationRelativeTo(owner);
        carregarMarcas();
        limpar();
    }

    private JPanel criarAbaCadastro() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 6, 5, 6);
        gbc.anchor = GridBagConstraints.WEST;

        int y = 0;
        addCampo(painel, gbc, y++, "label.id", txtId);
        addCampo(painel, gbc, y++, "label.modelo", txtModelo);
        addCampo(painel, gbc, y++, "label.ano", txtAno);
        addCampo(painel, gbc, y++, "label.tipo", txtTipo);
        addCampo(painel, gbc, y++, "label.quantidade", txtQuantidade);
        addCampo(painel, gbc, y++, "label.preco", txtPreco);

        gbc.gridx = 0; gbc.gridy = y;
        painel.add(new JLabel(GerenciadorIdioma.get("label.marca")), gbc);
        gbc.gridx = 1; painel.add(cbMarca, gbc);
        y++;

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

        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        painel.add(botoes, gbc);
        return painel;
    }

    private void addCampo(JPanel painel, GridBagConstraints gbc, int y,
                          String chaveLabel, JTextField campo) {
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = y;
        painel.add(new JLabel(GerenciadorIdioma.get(chaveLabel)), gbc);
        gbc.gridx = 1; painel.add(campo, gbc);
    }

    private JPanel criarAbaPesquisa() {
        JPanel painel = new JPanel(new BorderLayout(6, 6));
        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.add(new JLabel(GerenciadorIdioma.get("label.filtro")));
        cbFiltro.setModel(new DefaultComboBoxModel<>(new String[] {
                GerenciadorIdioma.get("label.modelo"),
                GerenciadorIdioma.get("label.ano"),
                GerenciadorIdioma.get("label.tipo"),
                GerenciadorIdioma.get("label.marca") }));
        topo.add(cbFiltro);
        topo.add(new JLabel(GerenciadorIdioma.get("label.valor")));
        topo.add(txtValor);
        JButton btPesquisar = new JButton(GerenciadorIdioma.get("botao.pesquisar"));
        btPesquisar.addActionListener(e -> pesquisar());
        topo.add(btPesquisar);

        modelo.addColumn(GerenciadorIdioma.get("label.modelo"));
        modelo.addColumn(GerenciadorIdioma.get("label.ano"));
        modelo.addColumn(GerenciadorIdioma.get("label.tipo"));
        modelo.addColumn(GerenciadorIdioma.get("label.marca"));
        modelo.addColumn(GerenciadorIdioma.get("label.quantidade"));
        modelo.addColumn(GerenciadorIdioma.get("label.preco"));
        // coluna oculta com o id (auxilia a selecao)
        modelo.addColumn("idFarol");
        modelo.addColumn("idMarca");
        tabela.removeColumn(tabela.getColumnModel().getColumn(6));
        tabela.removeColumn(tabela.getColumnModel().getColumn(6));
        tabela.getSelectionModel().addListSelectionListener(ev -> {
            if (!ev.getValueIsAdjusting()) {
                selecionarLinha();
            }
        });

        painel.add(topo, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return painel;
    }

    /** Carrega o combobox de marcas (ordenado por nome). */
    private void carregarMarcas() {
        try {
            cbMarca.removeAllItems();
            for (Marca m : marcaDAO.listarOrdenadoPorNome()) {
                cbMarca.addItem(m);
            }
        } catch (Exception ex) {
            erroOperacao(ex);
        }
    }

    private void limpar() {
        txtId.setText("");
        txtModelo.setText("");
        txtAno.setText("");
        txtTipo.setText("");
        txtQuantidade.setText("");
        txtPreco.setText("");
        if (cbMarca.getItemCount() > 0) {
            cbMarca.setSelectedIndex(0);
        }
        txtModelo.requestFocus();
    }

    /** Valida todos os campos do cadastro; retorna true se validos. */
    private boolean validarCampos() {
        String erro = Validador.isObrigatorio(txtModelo.getText(),
                GerenciadorIdioma.get("label.modelo"));
        if (erro == null) {
            erro = Validador.isObrigatorio(txtTipo.getText(),
                    GerenciadorIdioma.get("label.tipo"));
        }
        if (erro == null) {
            erro = Validador.validarAno(txtAno.getText());
        }
        if (erro == null) {
            erro = Validador.isInteiro(txtQuantidade.getText());
        }
        if (erro == null) {
            erro = Validador.validarPreco(txtPreco.getText());
        }
        if (erro == null && cbMarca.getSelectedItem() == null) {
            erro = Validador.isObrigatorio("", GerenciadorIdioma.get("label.marca"));
        }
        if (erro != null) {
            mostrar(erro, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    /** Monta um Farol a partir dos campos da tela. */
    private Farol lerFormulario() {
        Marca marca = (Marca) cbMarca.getSelectedItem();
        Farol f = new Farol();
        f.setModelo(txtModelo.getText().trim());
        f.setAno(Integer.parseInt(txtAno.getText().trim()));
        f.setTipo(txtTipo.getText().trim());
        f.setQuantidade(Integer.parseInt(txtQuantidade.getText().trim()));
        f.setPreco(Double.parseDouble(txtPreco.getText().trim().replace(",", ".")));
        f.setMarca(marca);
        return f;
    }

    private void salvar() {
        if (!validarCampos()) {
            return;
        }
        try {
            Farol f = lerFormulario();
            if (farolDAO.existeFarol(f.getModelo(), f.getAno(), f.getTipo(),
                    f.getMarca().getId())) {
                mostrar(GerenciadorIdioma.get("erro.duplicidade"),
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            farolDAO.inserir(f);
            mostrar(GerenciadorIdioma.get("msg.sucesso.inserir"),
                    JOptionPane.INFORMATION_MESSAGE);
            limpar();
        } catch (Exception ex) {
            erroOperacao(ex);
        }
    }

        /** Altera o registro selecionado (ou localizado pela chave de unicidade). */
    private void alterar() {
        if (!validarCampos()) {
            return;
        }
        try {
            Integer id = obterIdSelecionado();
            if (id == null) {
                mostrar(GerenciadorIdioma.get("erro.nao.encontrado"),
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            Farol f = lerFormulario();
            f.setId(id);
            farolDAO.alterar(f);
            mostrar(GerenciadorIdioma.get("msg.sucesso.alterar"),
                    JOptionPane.INFORMATION_MESSAGE);
            limpar();
        } catch (Exception ex) {
            erroOperacao(ex);
        }
    }

    /**
     * Resolve o id do registro: usa o ID carregado pela pesquisa ou, se vazio,
     * localiza o farol pela chave (modelo, ano, tipo, marca). Null se nao existir.
     */
    private Integer obterIdSelecionado() throws SQLException {
        if (!txtId.getText().trim().isEmpty()) {
            return Integer.parseInt(txtId.getText().trim());
        }
        if (Validador.isInteiro(txtAno.getText()) != null
                || cbMarca.getSelectedItem() == null) {
            return null;
        }
        Marca marca = (Marca) cbMarca.getSelectedItem();
        return farolDAO.obterId(txtModelo.getText().trim(),
                Integer.parseInt(txtAno.getText().trim()),
                txtTipo.getText().trim(), marca.getId());
    }

        /** Exclui o registro selecionado (ou localizado pela chave de unicidade). */
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
            farolDAO.excluir(id);
            mostrar(GerenciadorIdioma.get("msg.sucesso.excluir"),
                    JOptionPane.INFORMATION_MESSAGE);
            limpar();
        } catch (Exception ex) {
            erroOperacao(ex);
        }
    }

    /** Executa a pesquisa complexa conforme o filtro selecionado. */
    private void pesquisar() {
        int idx = cbFiltro.getSelectedIndex();
        String[] chaves = { "modelo", "ano", "tipo", "marca" };
        String filtro = chaves[idx];
        String valor = txtValor.getText().trim();

        // Validacao condicional: o filtro Ano aceita apenas numeros.
        if ("ano".equals(filtro) && !valor.isEmpty()) {
            String erro = Validador.isInteiro(valor);
            if (erro != null) {
                mostrar(erro, JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        try {
            modelo.setRowCount(0);
            List<Farol> lista = farolDAO.pesquisar(filtro, valor);
            for (Farol f : lista) {
                modelo.addRow(new Object[] { f.getModelo(), f.getAno(),
                        f.getTipo(), f.getMarca().getNome(),
                        f.getQuantidade(), f.getPreco(),
                        f.getId(), f.getMarca().getId() });
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
        txtId.setText(String.valueOf(modelo.getValueAt(linha, 6)));
        txtModelo.setText(String.valueOf(modelo.getValueAt(linha, 0)));
        txtAno.setText(String.valueOf(modelo.getValueAt(linha, 1)));
        txtTipo.setText(String.valueOf(modelo.getValueAt(linha, 2)));
        txtQuantidade.setText(String.valueOf(modelo.getValueAt(linha, 4)));
        txtPreco.setText(String.valueOf(modelo.getValueAt(linha, 5)));
        int idMarca = Integer.parseInt(String.valueOf(modelo.getValueAt(linha, 7)));
        for (int i = 0; i < cbMarca.getItemCount(); i++) {
            if (cbMarca.getItemAt(i).getId() == idMarca) {
                cbMarca.setSelectedIndex(i);
                break;
            }
        }
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

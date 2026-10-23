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
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import dao.UsuarioDAO;
import model.Usuario;
import util.GerenciadorIdioma;
import util.MensagemErro;
import util.Validador;

/**
 * Tela de cadastro de Usuario (cadastro basico) em JDialog modal.
 * Possui abas de Cadastro (CRUD) e Pesquisa (JTable com duplo clique),
 * impede duplicidade pelo login e valida campos obrigatorios.
 */
public class TelaCadastroUsuario extends JDialog {

    private static final long serialVersionUID = 1L;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    private final JTextField txtId = new JTextField(5);
    private final JTextField txtLogin = new JTextField(20);
    private final JPasswordField txtSenha = new JPasswordField(20);
    private final JTextField txtBusca = new JTextField(20);
    private final DefaultTableModel modelo = new DefaultTableModel();
    private final JTable tabela = new JTable(modelo);
    private final JTabbedPane abas = new JTabbedPane();

    /**
     * Cria a tela de cadastro de usuario.
     *
     * @param owner janela proprietaria (menu principal)
     */
    public TelaCadastroUsuario(Frame owner) {
        super(owner, true);
        setTitle(GerenciadorIdioma.get("titulo.cadastro.usuario"));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        txtId.setEditable(false);
        abas.addTab(GerenciadorIdioma.get("aba.cadastro"), criarAbaCadastro());
        abas.addTab(GerenciadorIdioma.get("aba.pesquisa"), criarAbaPesquisa());
        add(abas);
        pack();
        setLocationRelativeTo(owner);
        limpar();
    }

    private JPanel criarAbaCadastro() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(new JLabel(GerenciadorIdioma.get("label.id")), gbc);
        gbc.gridx = 1; painel.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(new JLabel(GerenciadorIdioma.get("label.login")), gbc);
        gbc.gridx = 1; painel.add(txtLogin, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        painel.add(new JLabel(GerenciadorIdioma.get("label.senha")), gbc);
        gbc.gridx = 1; painel.add(txtSenha, gbc);

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

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        painel.add(botoes, gbc);
        return painel;
    }

    private JPanel criarAbaPesquisa() {
        JPanel painel = new JPanel(new BorderLayout(6, 6));
        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.add(new JLabel(GerenciadorIdioma.get("label.login")));
        topo.add(txtBusca);
        JButton btPesquisar = new JButton(GerenciadorIdioma.get("botao.pesquisar"));
        btPesquisar.addActionListener(e -> pesquisar());
        topo.add(btPesquisar);

        modelo.addColumn(GerenciadorIdioma.get("label.id"));
        modelo.addColumn(GerenciadorIdioma.get("label.login"));
        tabela.getSelectionModel().addListSelectionListener(ev -> {
            if (!ev.getValueIsAdjusting()) {
                selecionarLinha();
            }
        });

        painel.add(topo, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return painel;
    }

    private void limpar() {
        txtId.setText("");
        txtLogin.setText("");
        txtSenha.setText("");
        txtLogin.requestFocus();
    }

    private void salvar() {
        if (!validarCampos()) {
            return;
        }
        try {
            if (usuarioDAO.existePorLogin(txtLogin.getText().trim())) {
                mostrar(GerenciadorIdioma.get("erro.duplicidade"),
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            usuarioDAO.inserir(new Usuario(txtLogin.getText().trim(),
                    new String(txtSenha.getPassword())));
            mostrar(GerenciadorIdioma.get("msg.sucesso.inserir"),
                    JOptionPane.INFORMATION_MESSAGE);
            limpar();
        } catch (Exception ex) {
            erroOperacao(ex);
        }
    }

    /** Altera o registro selecionado (ou localizado pelo login). */
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
            usuarioDAO.alterar(new Usuario(id, txtLogin.getText().trim(),
                    new String(txtSenha.getPassword())));
            mostrar(GerenciadorIdioma.get("msg.sucesso.alterar"),
                    JOptionPane.INFORMATION_MESSAGE);
            limpar();
        } catch (Exception ex) {
            erroOperacao(ex);
        }
    }

    /**
     * Resolve o id do registro: usa o ID carregado pela pesquisa ou, se vazio,
     * localiza o usuario pelo login informado. Retorna null se nao existir.
     */
    private Integer obterIdSelecionado() throws SQLException {
        if (!txtId.getText().trim().isEmpty()) {
            return Integer.parseInt(txtId.getText().trim());
        }
        return usuarioDAO.obterIdPorLogin(txtLogin.getText().trim());
    }

    /** Exclui o registro selecionado (ou localizado pelo login). */
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
            usuarioDAO.excluir(id);
            mostrar(GerenciadorIdioma.get("msg.sucesso.excluir"),
                    JOptionPane.INFORMATION_MESSAGE);
            limpar();
        } catch (Exception ex) {
            erroOperacao(ex);
        }
    }

    private boolean validarCampos() {
        String erro = Validador.isObrigatorio(txtLogin.getText(),
                GerenciadorIdioma.get("label.login"));
        if (erro == null) {
            erro = Validador.isObrigatorio(new String(txtSenha.getPassword()),
                    GerenciadorIdioma.get("label.senha"));
        }
        if (erro != null) {
            mostrar(erro, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void pesquisar() {
        try {
            modelo.setRowCount(0);
            List<Usuario> lista =
                    usuarioDAO.pesquisarPorLogin(txtBusca.getText().trim());
            for (Usuario u : lista) {
                modelo.addRow(new Object[] { u.getId(), u.getLogin() });
            }
            if (lista.isEmpty()) {
                mostrar(GerenciadorIdioma.get("msg.lista.vazia"),
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            erroOperacao(ex);
        }
    }

    private void selecionarLinha() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return;
        }
        txtId.setText(String.valueOf(modelo.getValueAt(linha, 0)));
        txtLogin.setText(String.valueOf(modelo.getValueAt(linha, 1)));
        txtSenha.setText("");
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

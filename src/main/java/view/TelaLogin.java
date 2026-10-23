package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import dao.UsuarioDAO;
import model.Usuario;
import util.GerenciadorIdioma;

/**
 * Tela de login do sistema (JFrame).
 * Valida as credenciais via UsuarioDAO.autenticar e, em caso de sucesso,
 * abre a TelaMenu e encerra a propria janela. Textos vindos do i18n.
 */
public class TelaLogin extends JFrame {

    private static final long serialVersionUID = 1L;

    private final JTextField txtLogin = new JTextField(15);
    private final JPasswordField txtSenha = new JPasswordField(15);
    private final JButton btnEntrar = new JButton();

    /** Monta a interface da tela de login. */
    public TelaLogin() {
        setTitle(GerenciadorIdioma.get("titulo.login"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        montarComponentes();
        pack();
        setLocationRelativeTo(null);
    }

    /** Cria e posiciona os componentes da janela. */
    private void montarComponentes() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel(GerenciadorIdioma.get("label.login")), gbc);
        gbc.gridx = 1;
        add(txtLogin, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel(GerenciadorIdioma.get("label.senha")), gbc);
        gbc.gridx = 1;
        add(txtSenha, gbc);

        btnEntrar.setText(GerenciadorIdioma.get("botao.entrar"));
        btnEntrar.addActionListener(e -> autenticar());
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(btnEntrar, gbc);

        getRootPane().setDefaultButton(btnEntrar);
    }

    /** Executa a autenticacao e trata o resultado. */
    private void autenticar() {
        String login = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword());

        if (login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    GerenciadorIdioma.get("erro.campo.obrigatorio"),
                    GerenciadorIdioma.get("titulo.login"),
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Usuario usuario = new UsuarioDAO().autenticar(login, senha);
            if (usuario != null) {
                new TelaMenu(usuario).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        GerenciadorIdioma.get("erro.login.invalido"),
                        GerenciadorIdioma.get("titulo.login"),
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    GerenciadorIdioma.get("erro.operacao") + "\n" + ex.getMessage(),
                    GerenciadorIdioma.get("titulo.login"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}

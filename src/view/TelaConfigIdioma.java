package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import util.GerenciadorIdioma;

/**
 * Janela de configuracao de idioma (JDialog).
 * Permite escolher entre Portugues (pt_BR) e Ingles (en_US); ao aplicar,
 * altera o Locale em GerenciadorIdioma e recarrega o ResourceBundle.
 */
public class TelaConfigIdioma extends JDialog {

    private static final long serialVersionUID = 1L;

    private final JRadioButton rbPortugues =
            new JRadioButton(GerenciadorIdioma.get("idioma.portugues"));
    private final JRadioButton rbIngles =
            new JRadioButton(GerenciadorIdioma.get("idioma.ingles"));

    /**
     * Cria a janela de configuracao de idioma.
     *
     * @param owner janela proprietaria (menu principal)
     */
    public TelaConfigIdioma(Frame owner) {
        super(owner, true);
        setTitle(GerenciadorIdioma.get("titulo.config.idioma"));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        montarComponentes();
        pack();
        setLocationRelativeTo(owner);
    }

    private void montarComponentes() {
        // Marca o radio conforme o idioma atual.
        if ("en".equals(GerenciadorIdioma.getLocale().getLanguage())) {
            rbIngles.setSelected(true);
        } else {
            rbPortugues.setSelected(true);
        }
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbPortugues);
        grupo.add(rbIngles);

        JPanel opcoes = new JPanel(new GridLayout(0, 1, 4, 4));
        opcoes.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        opcoes.add(rbPortugues);
        opcoes.add(rbIngles);

        JButton btAplicar = new JButton(GerenciadorIdioma.get("botao.aplicar"));
        btAplicar.addActionListener(e -> aplicar());
        JButton btCancelar = new JButton(GerenciadorIdioma.get("botao.cancelar"));
        btCancelar.addActionListener(e -> dispose());
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoes.add(btAplicar);
        botoes.add(btCancelar);

        add(opcoes, BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);
    }

    /** Aplica o idioma escolhido e recarrega o ResourceBundle. */
    private void aplicar() {
        Locale novo = rbIngles.isSelected()
                ? new Locale("en", "US")
                : new Locale("pt", "BR");
        GerenciadorIdioma.setLocale(novo);
        // Reaplica o idioma na janela de menu que abriu esta configuracao,
        // para que o menu principal tambem mude de idioma imediatamente.
        java.awt.Window dono = getOwner();
        if (dono instanceof TelaMenu) {
            ((TelaMenu) dono).aplicarIdioma();
        }
        JOptionPane.showMessageDialog(this,
                GerenciadorIdioma.get("msg.sucesso.alterar"),
                GerenciadorIdioma.get("titulo.config.idioma"),
                JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}

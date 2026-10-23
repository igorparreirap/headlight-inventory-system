package app;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import view.TelaLogin;

/**
 * Classe principal da aplicacao (ponto de entrada).
 * Aplica o visual nativo do sistema operacional e inicia a TelaLogin
 * no Event Dispatch Thread do Swing.
 */
public final class Principal {

    private Principal() {
    }

    /**
     * Metodo principal: abre a tela de login.
     *
     * @param args argumentos de linha de comando (nao utilizados)
     */
    public static void main(String[] args) {
        aplicarVisualDoSistema();
        SwingUtilities.invokeLater(() -> new TelaLogin().setVisible(true));
    }

    /** Usa o look and feel nativo (Windows, macOS, GTK); mantem o padrao se falhar. */
    private static void aplicarVisualDoSistema() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Look and feel nativo indisponivel: " + e.getMessage());
        }
    }
}

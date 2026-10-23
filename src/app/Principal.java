package app;

import javax.swing.SwingUtilities;

import view.TelaLogin;

/**
 * Classe principal da aplicacao (ponto de entrada).
 * Inicia a TelaLogin no Event Dispatch Thread do Swing.
 */
public class Principal {

    /**
     * Metodo principal: abre a tela de login.
     *
     * @param args argumentos de linha de comando (nao utilizados)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaLogin().setVisible(true));
    }
}

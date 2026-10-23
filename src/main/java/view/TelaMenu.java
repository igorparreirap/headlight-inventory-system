package view;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import model.Usuario;
import util.GerenciadorIdioma;

/**
 * Menu principal do sistema (JFrame com JMenuBar).
 * Disponibiliza, apos o login, o acesso aos cadastros (Marca, Farol,
 * Usuario), a listagem de estoque, a configuracao de idioma e a saida.
 */
public class TelaMenu extends JFrame {

    private static final long serialVersionUID = 1L;

    /** Usuario autenticado na sessao atual. */
    private final Usuario usuarioLogado;

    /**
     * Monta o menu principal.
     *
     * @param usuarioLogado usuario que efetuou o login
     */
    public TelaMenu(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        setTitle(GerenciadorIdioma.get("titulo.menu")
                + " - " + usuarioLogado.getLogin());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(640, 420);
        setLocationRelativeTo(null);
        montarMenu();
    }

    /**
     * Reaplica o idioma atual a esta janela: atualiza o titulo e reconstroi
     * a barra de menus para refletir o idioma selecionado.
     */
    public void aplicarIdioma() {
        setTitle(GerenciadorIdioma.get("titulo.menu")
                + " - " + usuarioLogado.getLogin());
        montarMenu();
        revalidate();
        repaint();
    }

    /** Constroi a barra de menus e associa as acoes. */
    private void montarMenu() {
        JMenuBar barra = new JMenuBar();

        // Menu Cadastros
        JMenu menuCadastros = new JMenu(GerenciadorIdioma.get("menu.cadastros"));
        JMenuItem itemMarca = new JMenuItem(GerenciadorIdioma.get("menu.cadastros.marca"));
        JMenuItem itemFarol = new JMenuItem(GerenciadorIdioma.get("menu.cadastros.farol"));
        JMenuItem itemUsuario = new JMenuItem(GerenciadorIdioma.get("menu.cadastros.usuario"));
        itemMarca.addActionListener(e -> new TelaCadastroMarca(this).setVisible(true));
        itemFarol.addActionListener(e -> new TelaCadastroFarol(this).setVisible(true));
        itemUsuario.addActionListener(e -> new TelaCadastroUsuario(this).setVisible(true));
        menuCadastros.add(itemMarca);
        menuCadastros.add(itemFarol);
        menuCadastros.add(itemUsuario);

        // Menu Consultas
        JMenu menuConsultas = new JMenu(GerenciadorIdioma.get("menu.consultas"));
        JMenuItem itemEstoque = new JMenuItem(GerenciadorIdioma.get("menu.consultas.estoque"));
        itemEstoque.addActionListener(e -> new TelaListagemEstoque(this).setVisible(true));
        menuConsultas.add(itemEstoque);

        // Menu Configuracoes
        JMenu menuConfig = new JMenu(GerenciadorIdioma.get("menu.config"));
        JMenuItem itemIdioma = new JMenuItem(GerenciadorIdioma.get("menu.config.idioma"));
        itemIdioma.addActionListener(e -> new TelaConfigIdioma(this).setVisible(true));
        menuConfig.add(itemIdioma);

        // Menu Sair
        JMenu menuSair = new JMenu(GerenciadorIdioma.get("menu.sair"));
        JMenuItem itemSair = new JMenuItem(GerenciadorIdioma.get("menu.sair"));
        itemSair.addActionListener(e -> System.exit(0));
        menuSair.add(itemSair);

        barra.add(menuCadastros);
        barra.add(menuConsultas);
        barra.add(menuConfig);
        barra.add(menuSair);
        setJMenuBar(barra);
    }

    /** @return o usuario autenticado na sessao. */
    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
}

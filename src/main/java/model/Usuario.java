package model;

/**
 * Entidade que representa um usuario do sistema (credenciais de acesso).
 * Utilizada na autenticacao (login) e no cadastro de usuarios.
 * Espelha a tabela usuario(id, login, senha) do banco db_farois.
 */
public class Usuario {

    private int id;
    private String login;
    private String senha;

    /** Construtor vazio (necessario para preenchimento gradual). */
    public Usuario() {
    }

    /** Construtor completo. */
    public Usuario(int id, String login, String senha) {
        this.id = id;
        this.login = login;
        this.senha = senha;
    }

    /** Construtor sem id (uso em inclusoes, onde o id e gerado pelo banco). */
    public Usuario(String login, String senha) {
        this.login = login;
        this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return login;
    }
}

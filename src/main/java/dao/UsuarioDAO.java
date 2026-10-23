package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Usuario;
import util.ConexaoBD;
import util.HashSenha;

/**
 * Acesso a dados (DAO) da entidade Usuario via JDBC.
 * Responsavel pelo CRUD, pela autenticacao e pela verificacao de
 * unicidade do login, sempre com PreparedStatement.
 * As senhas sao persistidas apenas como hash (ver {@link HashSenha}).
 */
public class UsuarioDAO {

    /**
     * Insere um novo usuario.
     *
     * @param usuario objeto a inserir
     * @throws SQLException em caso de erro de banco
     */
    public void inserir(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (login, senha) VALUES (?, ?)";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, usuario.getLogin());
            st.setString(2, HashSenha.gerar(usuario.getSenha()));
            st.executeUpdate();
        }
    }

    /**
     * Altera um usuario existente.
     *
     * @param usuario objeto com id preenchido
     * @throws SQLException em caso de erro de banco
     */
    public void alterar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuario SET login = ?, senha = ? WHERE id = ?";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, usuario.getLogin());
            st.setString(2, HashSenha.gerar(usuario.getSenha()));
            st.setInt(3, usuario.getId());
            st.executeUpdate();
        }
    }

    /**
     * Exclui um usuario pelo id.
     *
     * @param id identificador do usuario
     * @throws SQLException em caso de erro de banco
     */
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM usuario WHERE id = ?";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, id);
            st.executeUpdate();
        }
    }

    /**
     * Lista todos os usuarios ordenados por login.
     *
     * @return lista de usuarios
     * @throws SQLException em caso de erro de banco
     */
    public List<Usuario> listar() throws SQLException {
        String sql = "SELECT id, login, senha FROM usuario ORDER BY login";
        List<Usuario> lista = new ArrayList<>();
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Usuario(rs.getInt("id"),
                            rs.getString("login"), rs.getString("senha")));
                }
            }
        }
        return lista;
    }

    /**
     * Pesquisa usuarios cujo login contenha o texto informado.
     *
     * @param filtro texto parcial (vazio retorna todos)
     * @return lista de usuarios encontrados
     * @throws SQLException em caso de erro de banco
     */
    public List<Usuario> pesquisarPorLogin(String filtro) throws SQLException {
        String sql = "SELECT id, login, senha FROM usuario "
                + "WHERE login LIKE ? ORDER BY login";
        List<Usuario> lista = new ArrayList<>();
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, "%" + (filtro == null ? "" : filtro) + "%");
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Usuario(rs.getInt("id"),
                            rs.getString("login"), rs.getString("senha")));
                }
            }
        }
        return lista;
    }

    /**
     * Autentica o usuario pelas credenciais informadas.
     *
     * @param login login digitado
     * @param senha senha digitada
     * @return o Usuario autenticado ou null se invalido
     * @throws SQLException em caso de erro de banco
     */
    public Usuario autenticar(String login, String senha) throws SQLException {
        String sql = "SELECT id, login, senha FROM usuario WHERE login = ?";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, login);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next() && HashSenha.verificar(senha, rs.getString("senha"))) {
                    return new Usuario(rs.getInt("id"), rs.getString("login"), null);
                }
                return null;
            }
        }
    }

    /**
     * Verifica se ja existe um usuario com o login informado.
     *
     * @param login login a verificar
     * @return true se o login ja existe
     * @throws SQLException em caso de erro de banco
     */
    public boolean existePorLogin(String login) throws SQLException {
        String sql = "SELECT 1 FROM usuario WHERE login = ?";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, login);
            try (ResultSet rs = st.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Obtem o id de um usuario pelo login.
     *
     * @param login login do usuario
     * @return o id encontrado ou null se nao existir
     * @throws SQLException em caso de erro de banco
     */
    public Integer obterIdPorLogin(String login) throws SQLException {
        String sql = "SELECT id FROM usuario WHERE login = ?";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, login);
            try (ResultSet rs = st.executeQuery()) {
                return rs.next() ? rs.getInt("id") : null;
            }
        }
    }

}

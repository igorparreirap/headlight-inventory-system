package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Usuario;
import util.ConexaoBD;

/**
 * Acesso a dados (DAO) da entidade Usuario via JDBC.
 * Responsavel pelo CRUD, pela autenticacao e pela verificacao de
 * unicidade do login, sempre com PreparedStatement.
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
        Connection con = null;
        PreparedStatement st = null;
        try {
            con = ConexaoBD.getConnection();
            st = con.prepareStatement(sql);
            st.setString(1, usuario.getLogin());
            st.setString(2, usuario.getSenha());
            st.executeUpdate();
        } finally {
            ConexaoBD.fechar(con, st);
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
        Connection con = null;
        PreparedStatement st = null;
        try {
            con = ConexaoBD.getConnection();
            st = con.prepareStatement(sql);
            st.setString(1, usuario.getLogin());
            st.setString(2, usuario.getSenha());
            st.setInt(3, usuario.getId());
            st.executeUpdate();
        } finally {
            ConexaoBD.fechar(con, st);
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
        Connection con = null;
        PreparedStatement st = null;
        try {
            con = ConexaoBD.getConnection();
            st = con.prepareStatement(sql);
            st.setInt(1, id);
            st.executeUpdate();
        } finally {
            ConexaoBD.fechar(con, st);
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
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = ConexaoBD.getConnection();
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while (rs.next()) {
                lista.add(new Usuario(rs.getInt("id"),
                        rs.getString("login"), rs.getString("senha")));
            }
        } finally {
            ConexaoBD.fechar(con, st, rs);
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
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = ConexaoBD.getConnection();
            st = con.prepareStatement(sql);
            st.setString(1, "%" + (filtro == null ? "" : filtro) + "%");
            rs = st.executeQuery();
            while (rs.next()) {
                lista.add(new Usuario(rs.getInt("id"),
                        rs.getString("login"), rs.getString("senha")));
            }
        } finally {
            ConexaoBD.fechar(con, st, rs);
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
        String sql = "SELECT id, login, senha FROM usuario "
                + "WHERE login = ? AND senha = ?";
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = ConexaoBD.getConnection();
            st = con.prepareStatement(sql);
            st.setString(1, login);
            st.setString(2, senha);
            rs = st.executeQuery();
            if (rs.next()) {
                return new Usuario(rs.getInt("id"),
                        rs.getString("login"), rs.getString("senha"));
            }
            return null;
        } finally {
            ConexaoBD.fechar(con, st, rs);
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
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = ConexaoBD.getConnection();
            st = con.prepareStatement(sql);
            st.setString(1, login);
            rs = st.executeQuery();
            return rs.next();
        } finally {
            ConexaoBD.fechar(con, st, rs);
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
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = ConexaoBD.getConnection();
            st = con.prepareStatement(sql);
            st.setString(1, login);
            rs = st.executeQuery();
            return rs.next() ? rs.getInt("id") : null;
        } finally {
            ConexaoBD.fechar(con, st, rs);
        }
    }

}

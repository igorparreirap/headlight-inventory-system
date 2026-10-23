package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Marca;
import util.ConexaoBD;

/**
 * Acesso a dados (DAO) da entidade Marca via JDBC.
 * Implementa o CRUD, a verificacao de unicidade do nome e a listagem
 * ordenada usada pelo combobox da tela de Farol. Usa PreparedStatement.
 */
public class MarcaDAO {

    /**
     * Insere uma nova marca.
     *
     * @param marca objeto a inserir
     * @throws SQLException em caso de erro de banco
     */
    public void inserir(Marca marca) throws SQLException {
        String sql = "INSERT INTO marca (nome) VALUES (?)";
        Connection con = null;
        PreparedStatement st = null;
        try {
            con = ConexaoBD.getConnection();
            st = con.prepareStatement(sql);
            st.setString(1, marca.getNome());
            st.executeUpdate();
        } finally {
            ConexaoBD.fechar(con, st);
        }
    }

    /**
     * Altera uma marca existente.
     *
     * @param marca objeto com id preenchido
     * @throws SQLException em caso de erro de banco
     */
    public void alterar(Marca marca) throws SQLException {
        String sql = "UPDATE marca SET nome = ? WHERE id = ?";
        Connection con = null;
        PreparedStatement st = null;
        try {
            con = ConexaoBD.getConnection();
            st = con.prepareStatement(sql);
            st.setString(1, marca.getNome());
            st.setInt(2, marca.getId());
            st.executeUpdate();
        } finally {
            ConexaoBD.fechar(con, st);
        }
    }

    /**
     * Exclui uma marca pelo id.
     *
     * @param id identificador da marca
     * @throws SQLException em caso de erro de banco (ex.: farois vinculados)
     */
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM marca WHERE id = ?";
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
     * Lista todas as marcas (ordenadas por nome).
     *
     * @return lista de marcas
     * @throws SQLException em caso de erro de banco
     */
    public List<Marca> listar() throws SQLException {
        return listarOrdenadoPorNome();
    }

    /**
     * Lista as marcas ordenadas alfabeticamente pelo nome.
     * Usada para popular o JComboBox da tela de Farol.
     *
     * @return lista de marcas ordenada
     * @throws SQLException em caso de erro de banco
     */
    public List<Marca> listarOrdenadoPorNome() throws SQLException {
        String sql = "SELECT id, nome FROM marca ORDER BY nome";
        List<Marca> lista = new ArrayList<>();
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = ConexaoBD.getConnection();
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while (rs.next()) {
                lista.add(new Marca(rs.getInt("id"), rs.getString("nome")));
            }
        } finally {
            ConexaoBD.fechar(con, st, rs);
        }
        return lista;
    }

    /**
     * Pesquisa marcas cujo nome contenha o texto informado.
     *
     * @param filtro texto parcial (vazio retorna todas)
     * @return lista de marcas encontradas
     * @throws SQLException em caso de erro de banco
     */
    public List<Marca> pesquisarPorNome(String filtro) throws SQLException {
        String sql = "SELECT id, nome FROM marca WHERE nome LIKE ? ORDER BY nome";
        List<Marca> lista = new ArrayList<>();
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = ConexaoBD.getConnection();
            st = con.prepareStatement(sql);
            st.setString(1, "%" + (filtro == null ? "" : filtro) + "%");
            rs = st.executeQuery();
            while (rs.next()) {
                lista.add(new Marca(rs.getInt("id"), rs.getString("nome")));
            }
        } finally {
            ConexaoBD.fechar(con, st, rs);
        }
        return lista;
    }

    /**
     * Verifica se ja existe uma marca com o nome informado.
     *
     * @param nome nome a verificar
     * @return true se o nome ja existe
     * @throws SQLException em caso de erro de banco
     */
    public boolean existePorNome(String nome) throws SQLException {
        String sql = "SELECT 1 FROM marca WHERE nome = ?";
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = ConexaoBD.getConnection();
            st = con.prepareStatement(sql);
            st.setString(1, nome);
            rs = st.executeQuery();
            return rs.next();
        } finally {
            ConexaoBD.fechar(con, st, rs);
        }
    }

    /**
     * Obtem o id de uma marca pelo nome.
     *
     * @param nome nome da marca
     * @return o id encontrado ou null se nao existir
     * @throws SQLException em caso de erro de banco
     */
    public Integer obterIdPorNome(String nome) throws SQLException {
        String sql = "SELECT id FROM marca WHERE nome = ?";
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = ConexaoBD.getConnection();
            st = con.prepareStatement(sql);
            st.setString(1, nome);
            rs = st.executeQuery();
            return rs.next() ? rs.getInt("id") : null;
        } finally {
            ConexaoBD.fechar(con, st, rs);
        }
    }

}

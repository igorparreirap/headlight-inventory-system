package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Farol;
import model.Marca;
import util.ConexaoBD;

/**
 * Acesso a dados (DAO) da entidade Farol via JDBC.
 * Implementa o CRUD, a verificacao de duplicidade, a listagem ordenada e a
 * pesquisa por filtro. O SELECT faz JOIN com marca para popular o objeto
 * Marca dentro de cada Farol. Usa sempre PreparedStatement.
 */
public class FarolDAO {

    /** SELECT base com JOIN para trazer a marca ja populada. */
    private static final String SELECT_BASE =
            "SELECT f.id, f.modelo, f.ano, f.tipo, f.quantidade, f.preco, "
          + "m.id AS marca_id, m.nome AS marca_nome "
          + "FROM farol f JOIN marca m ON m.id = f.id_marca ";

    /**
     * Monta um objeto Farol (com Marca) a partir da linha atual do ResultSet.
     *
     * @param rs resultset posicionado em uma linha
     * @return objeto Farol preenchido
     * @throws SQLException em caso de erro de leitura
     */
    private Farol montar(ResultSet rs) throws SQLException {
        Marca marca = new Marca(rs.getInt("marca_id"), rs.getString("marca_nome"));
        return new Farol(rs.getInt("id"), rs.getString("modelo"),
                rs.getInt("ano"), rs.getString("tipo"),
                rs.getInt("quantidade"), rs.getDouble("preco"), marca);
    }

    /**
     * Insere um novo farol.
     *
     * @param farol objeto a inserir (com marca preenchida)
     * @throws SQLException em caso de erro de banco
     */
    public void inserir(Farol farol) throws SQLException {
        String sql = "INSERT INTO farol "
                + "(modelo, ano, tipo, quantidade, preco, id_marca) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        Connection con = null;
        PreparedStatement st = null;
        try {
            con = ConexaoBD.getConnection();
            st = con.prepareStatement(sql);
            st.setString(1, farol.getModelo());
            st.setInt(2, farol.getAno());
            st.setString(3, farol.getTipo());
            st.setInt(4, farol.getQuantidade());
            st.setDouble(5, farol.getPreco());
            st.setInt(6, farol.getMarca().getId());
            st.executeUpdate();
        } finally {
            ConexaoBD.fechar(con, st);
        }
    }

    /**
     * Altera um farol existente.
     *
     * @param farol objeto com id preenchido
     * @throws SQLException em caso de erro de banco
     */
    public void alterar(Farol farol) throws SQLException {
        String sql = "UPDATE farol SET modelo = ?, ano = ?, tipo = ?, "
                + "quantidade = ?, preco = ?, id_marca = ? WHERE id = ?";
        Connection con = null;
        PreparedStatement st = null;
        try {
            con = ConexaoBD.getConnection();
            st = con.prepareStatement(sql);
            st.setString(1, farol.getModelo());
            st.setInt(2, farol.getAno());
            st.setString(3, farol.getTipo());
            st.setInt(4, farol.getQuantidade());
            st.setDouble(5, farol.getPreco());
            st.setInt(6, farol.getMarca().getId());
            st.setInt(7, farol.getId());
            st.executeUpdate();
        } finally {
            ConexaoBD.fechar(con, st);
        }
    }

    /**
     * Exclui um farol pelo id.
     *
     * @param id identificador do farol
     * @throws SQLException em caso de erro de banco
     */
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM farol WHERE id = ?";
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
     * Lista todos os farois (ordenado por modelo).
     *
     * @return lista de farois
     * @throws SQLException em caso de erro de banco
     */
    public List<Farol> listar() throws SQLException {
        return listar("modelo");
    }

    /**
     * Lista os farois aplicando a ordenacao via clausula ORDER BY no SQL.
     *
     * @param ordenarPor "modelo" (A-Z) ou "quantidade" (decrescente)
     * @return lista de farois ordenada
     * @throws SQLException em caso de erro de banco
     */
    public List<Farol> listar(String ordenarPor) throws SQLException {
        String orderBy;
        if ("quantidade".equalsIgnoreCase(ordenarPor)) {
            orderBy = "ORDER BY f.quantidade DESC";
        } else {
            orderBy = "ORDER BY f.modelo ASC";
        }
        String sql = SELECT_BASE + orderBy;
        List<Farol> lista = new ArrayList<>();
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = ConexaoBD.getConnection();
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while (rs.next()) {
                lista.add(montar(rs));
            }
        } finally {
            ConexaoBD.fechar(con, st, rs);
        }
        return lista;
    }

    /**
     * Pesquisa complexa: filtra os farois conforme o criterio escolhido.
     * Filtros de texto (modelo, tipo, marca) usam LIKE; ano usa igualdade.
     *
     * @param filtro "modelo", "ano", "tipo" ou "marca"
     * @param valor  valor digitado pelo usuario
     * @return lista de farois encontrados
     * @throws SQLException em caso de erro de banco
     */
    public List<Farol> pesquisar(String filtro, String valor) throws SQLException {
        String sql;
        boolean usaAno = "ano".equalsIgnoreCase(filtro);
        if ("modelo".equalsIgnoreCase(filtro)) {
            sql = SELECT_BASE + "WHERE f.modelo LIKE ? ORDER BY f.modelo";
        } else if ("tipo".equalsIgnoreCase(filtro)) {
            sql = SELECT_BASE + "WHERE f.tipo LIKE ? ORDER BY f.modelo";
        } else if ("marca".equalsIgnoreCase(filtro)) {
            sql = SELECT_BASE + "WHERE m.nome LIKE ? ORDER BY f.modelo";
        } else if (usaAno) {
            sql = SELECT_BASE + "WHERE f.ano = ? ORDER BY f.modelo";
        } else {
            // sem filtro reconhecido: retorna todos
            return listar();
        }

        List<Farol> lista = new ArrayList<>();
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = ConexaoBD.getConnection();
            st = con.prepareStatement(sql);
            if (usaAno) {
                st.setInt(1, Integer.parseInt(valor.trim()));
            } else {
                st.setString(1, "%" + (valor == null ? "" : valor.trim()) + "%");
            }
            rs = st.executeQuery();
            while (rs.next()) {
                lista.add(montar(rs));
            }
        } finally {
            ConexaoBD.fechar(con, st, rs);
        }
        return lista;
    }

    /**
     * Verifica se ja existe um farol identico (mesma chave de unicidade).
     *
     * @param modelo  modelo do farol
     * @param ano     ano de aplicacao
     * @param tipo    tipo do farol
     * @param idMarca id da marca vinculada
     * @return true se o farol ja existe
     * @throws SQLException em caso de erro de banco
     */
    public boolean existeFarol(String modelo, int ano, String tipo, int idMarca)
            throws SQLException {
        String sql = "SELECT 1 FROM farol "
                + "WHERE modelo = ? AND ano = ? AND tipo = ? AND id_marca = ?";
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = ConexaoBD.getConnection();
            st = con.prepareStatement(sql);
            st.setString(1, modelo);
            st.setInt(2, ano);
            st.setString(3, tipo);
            st.setInt(4, idMarca);
            rs = st.executeQuery();
            return rs.next();
        } finally {
            ConexaoBD.fechar(con, st, rs);
        }
    }

    /**
     * Obtem o id de um farol pela sua chave de unicidade.
     *
     * @param modelo  modelo do farol
     * @param ano     ano de aplicacao
     * @param tipo    tipo do farol
     * @param idMarca id da marca vinculada
     * @return o id encontrado ou null se nao existir
     * @throws SQLException em caso de erro de banco
     */
    public Integer obterId(String modelo, int ano, String tipo, int idMarca)
            throws SQLException {
        String sql = "SELECT id FROM farol "
                + "WHERE modelo = ? AND ano = ? AND tipo = ? AND id_marca = ?";
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = ConexaoBD.getConnection();
            st = con.prepareStatement(sql);
            st.setString(1, modelo);
            st.setInt(2, ano);
            st.setString(3, tipo);
            st.setInt(4, idMarca);
            rs = st.executeQuery();
            return rs.next() ? rs.getInt("id") : null;
        } finally {
            ConexaoBD.fechar(con, st, rs);
        }
    }

}

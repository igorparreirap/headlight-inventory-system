package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utilitario de acesso ao banco de dados MySQL via JDBC.
 * Centraliza a obtencao da conexao (getConnection) e a liberacao de
 * recursos (fechar), evitando duplicacao de codigo nas classes DAO.
 */
public class ConexaoBD {

    /** Dados de conexao do banco db_farois. Ajuste conforme o ambiente. */
    private static final String URL =
            "jdbc:mysql://localhost:3306/db_farois?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "";

    /** Construtor privado: classe puramente utilitaria. */
    private ConexaoBD() {
    }

    /**
     * Abre e retorna uma conexao JDBC com o banco db_farois.
     *
     * @return conexao ativa com o banco
     * @throws SQLException se o driver nao for encontrado ou a conexao falhar
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "Driver JDBC do MySQL nao encontrado no classpath.", e);
        }
    }

    /**
     * Libera, com seguranca, os recursos JDBC informados (ignora nulos).
     *
     * @param con conexao a fechar
     * @param st  statement a fechar
     * @param rs  resultset a fechar
     */
    public static void fechar(Connection con, Statement st, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Sobrecarga para fechar apenas conexao e statement. */
    public static void fechar(Connection con, Statement st) {
        fechar(con, st, null);
    }
}

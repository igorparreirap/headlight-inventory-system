package util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utilitario de acesso ao banco de dados MySQL via JDBC.
 * Centraliza a obtencao da conexao; as classes DAO liberam os recursos
 * com try-with-resources.
 *
 * <p>As credenciais NAO ficam no codigo-fonte. Elas sao lidas, nesta ordem
 * de prioridade:</p>
 * <ol>
 *   <li>Variaveis de ambiente {@code DB_URL}, {@code DB_USER} e {@code DB_PASSWORD};</li>
 *   <li>Arquivo {@code db.properties} no diretorio de execucao
 *       (copie de {@code db.properties.example});</li>
 *   <li>Valores padrao (localhost, usuario root, senha vazia).</li>
 * </ol>
 */
public final class ConexaoBD {

    private static final String ARQUIVO_CONFIG = "db.properties";

    private static final String URL_PADRAO =
            "jdbc:mysql://localhost:3306/db_farois?useSSL=false&serverTimezone=UTC";
    private static final String USER_PADRAO = "root";
    private static final String PASS_PADRAO = "";

    private static final Properties CONFIG = carregarConfiguracao();

    /** Construtor privado: classe puramente utilitaria. */
    private ConexaoBD() {
    }

    /**
     * Carrega o arquivo db.properties do diretorio atual ou, se nao existir,
     * do classpath. A ausencia do arquivo nao e erro: os valores padrao sao usados.
     */
    private static Properties carregarConfiguracao() {
        Properties props = new Properties();
        Path arquivo = Paths.get(ARQUIVO_CONFIG);
        try {
            if (Files.exists(arquivo)) {
                try (InputStream in = Files.newInputStream(arquivo)) {
                    props.load(in);
                }
            } else {
                try (InputStream in = ConexaoBD.class.getClassLoader()
                        .getResourceAsStream(ARQUIVO_CONFIG)) {
                    if (in != null) {
                        props.load(in);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Nao foi possivel ler " + ARQUIVO_CONFIG + ": " + e.getMessage());
        }
        return props;
    }

    /**
     * Resolve um parametro de configuracao: variavel de ambiente,
     * depois db.properties, depois o valor padrao.
     */
    private static String obter(String variavelAmbiente, String chave, String padrao) {
        String valor = System.getenv(variavelAmbiente);
        if (valor != null && !valor.isEmpty()) {
            return valor;
        }
        return CONFIG.getProperty(chave, padrao);
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
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "Driver JDBC do MySQL nao encontrado no classpath.", e);
        }
        return DriverManager.getConnection(
                obter("DB_URL", "db.url", URL_PADRAO),
                obter("DB_USER", "db.user", USER_PADRAO),
                obter("DB_PASSWORD", "db.password", PASS_PADRAO));
    }
}

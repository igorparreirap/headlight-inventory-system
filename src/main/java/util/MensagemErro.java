package util;

import java.sql.SQLException;

/**
 * Converte excecoes tecnicas em mensagens amigaveis (i18n) para o usuario.
 * Evita que erros brutos do banco, como "Duplicate entry ...", sejam
 * exibidos diretamente nas telas.
 */
public final class MensagemErro {

    /** Codigo MySQL: violacao de chave unica (registro duplicado). */
    static final int MYSQL_DUPLICADO = 1062;
    /** Codigo MySQL: exclusao bloqueada por chave estrangeira (registro em uso). */
    static final int MYSQL_EM_USO = 1451;
    /** Codigo MySQL: usuario/senha do banco recusados. */
    static final int MYSQL_ACESSO_NEGADO = 1045;
    /** Prefixo SQLState para falhas de conexao. */
    private static final String SQLSTATE_CONEXAO = "08";

    /** Construtor privado: classe puramente utilitaria. */
    private MensagemErro() {
    }

    /**
     * Retorna a chave de mensagem (i18n) correspondente a excecao.
     *
     * @param ex excecao capturada
     * @return chave do arquivo de mensagens
     */
    public static String chave(Throwable ex) {
        for (Throwable t = ex; t != null; t = t.getCause()) {
            if (t instanceof SQLException) {
                SQLException sql = (SQLException) t;
                if (sql.getErrorCode() == MYSQL_DUPLICADO) {
                    return "erro.duplicidade";
                }
                if (sql.getErrorCode() == MYSQL_EM_USO) {
                    return "erro.registro.em.uso";
                }
                if (sql.getErrorCode() == MYSQL_ACESSO_NEGADO
                        || (sql.getSQLState() != null
                        && sql.getSQLState().startsWith(SQLSTATE_CONEXAO))) {
                    return "erro.conexao";
                }
            }
        }
        return "erro.operacao";
    }

    /**
     * Monta o texto final exibido ao usuario no idioma atual. Para erros
     * genericos, anexa a mensagem tecnica para facilitar o diagnostico.
     *
     * @param ex excecao capturada
     * @return mensagem pronta para exibicao
     */
    public static String traduzir(Throwable ex) {
        String chave = chave(ex);
        String texto = GerenciadorIdioma.get(chave);
        if ("erro.operacao".equals(chave) && ex.getMessage() != null) {
            texto += "\n" + ex.getMessage();
        }
        return texto;
    }
}

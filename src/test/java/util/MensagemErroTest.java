package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import org.junit.jupiter.api.Test;

class MensagemErroTest {

    @Test
    void registroDuplicado() {
        SQLException ex = new SQLIntegrityConstraintViolationException(
                "Duplicate entry 'Ford' for key 'uq_marca_nome'", "23000",
                MensagemErro.MYSQL_DUPLICADO);
        assertEquals("erro.duplicidade", MensagemErro.chave(ex));
    }

    @Test
    void registroEmUsoPorChaveEstrangeira() {
        SQLException ex = new SQLIntegrityConstraintViolationException(
                "Cannot delete or update a parent row", "23000",
                MensagemErro.MYSQL_EM_USO);
        assertEquals("erro.registro.em.uso", MensagemErro.chave(ex));
    }

    @Test
    void falhaDeConexao() {
        SQLException ex = new SQLException("Communications link failure", "08S01");
        assertEquals("erro.conexao", MensagemErro.chave(ex));
    }

    @Test
    void encontraCausaAninhada() {
        RuntimeException ex = new RuntimeException("wrapper",
                new SQLException("dup", "23000", MensagemErro.MYSQL_DUPLICADO));
        assertEquals("erro.duplicidade", MensagemErro.chave(ex));
    }

    @Test
    void erroGenericoIncluiDetalheTecnico() {
        IllegalStateException ex = new IllegalStateException("detalhe");
        assertEquals("erro.operacao", MensagemErro.chave(ex));
        assertTrue(MensagemErro.traduzir(ex).endsWith("detalhe"));
    }

    @Test
    void mensagensExistemNosDoisIdiomas() {
        String[] chaves = {"erro.duplicidade", "erro.registro.em.uso",
                "erro.conexao", "erro.operacao"};
        for (String tag : new String[] {"pt-BR", "en-US"}) {
            GerenciadorIdioma.setLocale(java.util.Locale.forLanguageTag(tag));
            for (String chave : chaves) {
                String texto = GerenciadorIdioma.get(chave);
                assertTrue(!texto.startsWith("!"), "Chave ausente (" + tag + "): " + chave);
            }
        }
        GerenciadorIdioma.setLocale(java.util.Locale.forLanguageTag("pt-BR"));
    }
}

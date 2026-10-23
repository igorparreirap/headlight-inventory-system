package util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class HashSenhaTest {

    @Test
    void hashNaoContemSenhaEmTextoPuro() {
        String hash = HashSenha.gerar("minhaSenha123");
        assertTrue(hash.startsWith("pbkdf2$"));
        assertFalse(hash.contains("minhaSenha123"));
    }

    @Test
    void verificaSenhaCorreta() {
        String hash = HashSenha.gerar("admin");
        assertTrue(HashSenha.verificar("admin", hash));
    }

    @Test
    void rejeitaSenhaIncorreta() {
        String hash = HashSenha.gerar("admin");
        assertFalse(HashSenha.verificar("Admin", hash));
        assertFalse(HashSenha.verificar("", hash));
    }

    @Test
    void mesmaSenhaGeraHashesDiferentes() {
        assertNotEquals(HashSenha.gerar("admin"), HashSenha.gerar("admin"));
    }

    @Test
    void rejeitaHashMalformadoOuNulo() {
        assertFalse(HashSenha.verificar("admin", null));
        assertFalse(HashSenha.verificar(null, HashSenha.gerar("admin")));
        assertFalse(HashSenha.verificar("admin", "admin"));
        assertFalse(HashSenha.verificar("admin", "pbkdf2$abc$###$###"));
    }

    @Test
    void validaHashDoUsuarioAdminDoScriptSql() {
        String hashDoSeed =
                "pbkdf2$210000$PWkqgOosEEHEFCI0NWZj6A==$y2GQ0faOGqWXMKfFww2dbjXmamtG/OEeSUiSzA5/9Pk=";
        assertTrue(HashSenha.verificar("admin", hashDoSeed));
    }
}

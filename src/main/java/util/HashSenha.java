package util;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Geracao e verificacao de hash de senhas com PBKDF2-HMAC-SHA256
 * (somente APIs do JDK, sem dependencias externas).
 *
 * <p>Formato armazenado: {@code pbkdf2$<iteracoes>$<salt base64>$<hash base64>}.
 * Cada senha recebe um salt aleatorio, de modo que senhas iguais geram
 * hashes diferentes.</p>
 */
public final class HashSenha {

    private static final String ALGORITMO = "PBKDF2WithHmacSHA256";
    private static final String PREFIXO = "pbkdf2";
    private static final int ITERACOES = 210_000;
    private static final int TAMANHO_SALT = 16;
    private static final int TAMANHO_HASH_BITS = 256;

    private static final SecureRandom RANDOM = new SecureRandom();

    /** Construtor privado: classe puramente utilitaria. */
    private HashSenha() {
    }

    /**
     * Gera o hash de uma senha em texto puro.
     *
     * @param senha senha digitada pelo usuario
     * @return string no formato pbkdf2$iteracoes$salt$hash
     */
    public static String gerar(String senha) {
        byte[] salt = new byte[TAMANHO_SALT];
        RANDOM.nextBytes(salt);
        byte[] hash = derivar(senha.toCharArray(), salt, ITERACOES);
        Base64.Encoder b64 = Base64.getEncoder();
        return PREFIXO + "$" + ITERACOES + "$" + b64.encodeToString(salt)
                + "$" + b64.encodeToString(hash);
    }

    /**
     * Verifica se a senha informada corresponde ao hash armazenado.
     * A comparacao e feita em tempo constante.
     *
     * @param senha       senha digitada
     * @param armazenado  hash salvo no banco
     * @return true se a senha confere
     */
    public static boolean verificar(String senha, String armazenado) {
        if (senha == null || armazenado == null) {
            return false;
        }
        String[] partes = armazenado.split("\\$");
        if (partes.length != 4 || !PREFIXO.equals(partes[0])) {
            return false;
        }
        try {
            int iteracoes = Integer.parseInt(partes[1]);
            byte[] salt = Base64.getDecoder().decode(partes[2]);
            byte[] esperado = Base64.getDecoder().decode(partes[3]);
            byte[] calculado = derivar(senha.toCharArray(), salt, iteracoes);
            return MessageDigest.isEqual(esperado, calculado);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static byte[] derivar(char[] senha, byte[] salt, int iteracoes) {
        PBEKeySpec spec = new PBEKeySpec(senha, salt, iteracoes, TAMANHO_HASH_BITS);
        try {
            return SecretKeyFactory.getInstance(ALGORITMO).generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Algoritmo " + ALGORITMO + " indisponivel", e);
        } finally {
            spec.clearPassword();
        }
    }
}

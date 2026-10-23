package util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Gerencia a internacionalizacao (i18n) da aplicacao.
 * Mantem o Locale atual e o ResourceBundle correspondente, oferecendo o
 * metodo get(chave) para que todas as telas obtenham seus textos de forma
 * uniforme em Portugues (pt_BR) ou Ingles (en_US).
 */
public class GerenciadorIdioma {

    /** Caminho base dos arquivos .properties (pacote util.i18n). */
    private static final String BASE = "util.i18n.mensagens";

    private static Locale localeAtual = new Locale("pt", "BR");
    private static ResourceBundle bundle =
            ResourceBundle.getBundle(BASE, localeAtual);

    /** Construtor privado: classe puramente utilitaria. */
    private GerenciadorIdioma() {
    }

    /**
     * Define o idioma atual e recarrega o ResourceBundle.
     *
     * @param locale novo Locale (ex.: new Locale("en", "US"))
     */
    public static void setLocale(Locale locale) {
        localeAtual = locale;
        bundle = ResourceBundle.getBundle(BASE, localeAtual);
    }

    /** @return o Locale atualmente em uso. */
    public static Locale getLocale() {
        return localeAtual;
    }

    /**
     * Retorna o texto associado a chave no idioma atual.
     *
     * @param chave chave do arquivo de mensagens
     * @return texto traduzido ou a propria chave entre ! se nao encontrada
     */
    public static String get(String chave) {
        try {
            return bundle.getString(chave);
        } catch (MissingResourceException e) {
            return "!" + chave + "!";
        }
    }
}

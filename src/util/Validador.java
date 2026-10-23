package util;

import java.util.Calendar;

/**
 * Utilitario com regras de validacao reutilizaveis pelas telas de cadastro.
 * Cada metodo retorna uma String com a mensagem de erro quando o valor for
 * invalido, ou null quando o valor for valido.
 */
public class Validador {

    /** Construtor privado: classe puramente utilitaria. */
    private Validador() {
    }

    /**
     * Verifica se um campo obrigatorio foi preenchido.
     *
     * @param campo     valor digitado
     * @param nomeCampo nome amigavel do campo (para a mensagem)
     * @return mensagem de erro ou null se valido
     */
    public static String isObrigatorio(String campo, String nomeCampo) {
        if (campo == null || campo.trim().isEmpty()) {
            return "O campo \"" + nomeCampo + "\" e obrigatorio.";
        }
        return null;
    }

    /**
     * Verifica se o valor representa um numero inteiro.
     *
     * @param valor texto a validar
     * @return mensagem de erro ou null se valido
     */
    public static String isInteiro(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return "Informe um numero inteiro.";
        }
        try {
            Integer.parseInt(valor.trim());
            return null;
        } catch (NumberFormatException e) {
            return "Valor invalido: informe apenas numeros inteiros.";
        }
    }

    /**
     * Verifica se o valor representa um numero decimal (double).
     * Aceita virgula ou ponto como separador decimal.
     *
     * @param valor texto a validar
     * @return mensagem de erro ou null se valido
     */
    public static String isDouble(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return "Informe um valor numerico.";
        }
        try {
            Double.parseDouble(valor.trim().replace(",", "."));
            return null;
        } catch (NumberFormatException e) {
            return "Valor invalido: informe um numero (ex.: 199.90).";
        }
    }

    /**
     * Valida o ano: inteiro positivo dentro de uma faixa razoavel
     * (1950 ate o ano atual + 1).
     *
     * @param valor texto a validar
     * @return mensagem de erro ou null se valido
     */
    public static String validarAno(String valor) {
        String erro = isInteiro(valor);
        if (erro != null) {
            return erro;
        }
        int ano = Integer.parseInt(valor.trim());
        int anoAtual = Calendar.getInstance().get(Calendar.YEAR);
        if (ano < 1950 || ano > anoAtual + 1) {
            return "Ano fora da faixa permitida (1950 a " + (anoAtual + 1) + ").";
        }
        return null;
    }

    /**
     * Valida o preco: numero decimal maior ou igual a zero.
     *
     * @param valor texto a validar
     * @return mensagem de erro ou null se valido
     */
    public static String validarPreco(String valor) {
        String erro = isDouble(valor);
        if (erro != null) {
            return erro;
        }
        double preco = Double.parseDouble(valor.trim().replace(",", "."));
        if (preco < 0) {
            return "O preco nao pode ser negativo.";
        }
        return null;
    }
}

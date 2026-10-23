package util;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.Year;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ValidadorTest {

    @Nested
    @DisplayName("isObrigatorio")
    class Obrigatorio {

        @Test
        void aceitaValorPreenchido() {
            assertNull(Validador.isObrigatorio("Ford", "Nome"));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t"})
        void rejeitaVazioOuEmBranco(String valor) {
            assertNotNull(Validador.isObrigatorio(valor, "Nome"));
        }
    }

    @Nested
    @DisplayName("isInteiro")
    class Inteiro {

        @ParameterizedTest
        @ValueSource(strings = {"0", "42", "-7", " 15 "})
        void aceitaInteiros(String valor) {
            assertNull(Validador.isInteiro(valor));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"abc", "1.5", "12a"})
        void rejeitaNaoInteiros(String valor) {
            assertNotNull(Validador.isInteiro(valor));
        }
    }

    @Nested
    @DisplayName("isDouble")
    class Decimal {

        @ParameterizedTest
        @ValueSource(strings = {"10", "199.90", "199,90", "0"})
        void aceitaPontoOuVirgula(String valor) {
            assertNull(Validador.isDouble(valor));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"R$10", "dez", "1,2,3"})
        void rejeitaValoresInvalidos(String valor) {
            assertNotNull(Validador.isDouble(valor));
        }
    }

    @Nested
    @DisplayName("validarAno")
    class Ano {

        @Test
        void aceitaLimites() {
            int proximoAno = Year.now().getValue() + 1;
            assertNull(Validador.validarAno("1950"));
            assertNull(Validador.validarAno(String.valueOf(proximoAno)));
        }

        @Test
        void rejeitaForaDaFaixa() {
            int doisAnosFrente = Year.now().getValue() + 2;
            assertNotNull(Validador.validarAno("1949"));
            assertNotNull(Validador.validarAno(String.valueOf(doisAnosFrente)));
        }

        @Test
        void rejeitaNaoNumerico() {
            assertNotNull(Validador.validarAno("dois mil"));
        }
    }

    @Nested
    @DisplayName("validarPreco")
    class Preco {

        @ParameterizedTest
        @ValueSource(strings = {"0", "0.01", "1499,99"})
        void aceitaPrecosNaoNegativos(String valor) {
            assertNull(Validador.validarPreco(valor));
        }

        @Test
        void rejeitaPrecoNegativo() {
            assertNotNull(Validador.validarPreco("-1"));
        }
    }
}

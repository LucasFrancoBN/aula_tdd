package br.com.fiap.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class MensagemControllerTest {

    @Nested
    class RegistrarMensagem {
        @Test
        void devePermitirRegistrarMensagem() {
            fail("Teste não implementado");
        }
    }

    @Nested
    class BuscarMensagem {
        @Test
        void devePermitirBuscarMensagem() {
            fail("Teste não implementado");
        }

        void deveGerarExcecao_QuandoBuscarMensagem_IdNaoExiste() {
            fail("Teste não implementado");
        }
    }

    @Nested
    class AlterarMensagem {
        @Test
        void devePermitirAlterarMensagem() {
            fail("Teste não implementado");
        }

        @Test
        void deveGerarExcecao_QuandoAlterarMensagem_IdNaoExiste() {
            fail("Teste não implementado");
        }

        @Test
        void deveGerarExcecao_QuandoAlterarMensagem_IdDaMensagemNovaApresentaValorDiferente() {
            fail("Teste não implementado");
        }
    }

    @Nested
    class RemoverMensagem {
        @Test
        void devePermitirRemoverMensagem() {
            fail("Teste não implementado");
        }

        @Test
        void deveGerarExcecao_QuandoRemoverMensagem_IdNaoExiste() {
            fail("Teste não implementado");
        }
    }

    @Nested
    class ListarMensagens {
        @Test
        void devePermitirListarMensagens() {
            fail("Teste não implementado");

        }
    }
}

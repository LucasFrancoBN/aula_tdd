package br.com.fiap.service;

import br.com.fiap.api.model.Mensagem;
import br.com.fiap.api.repository.MensagemRepository;
import br.com.fiap.api.service.MensagemService;
import br.com.fiap.api.service.MensagemServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class MensagemServiceTest {
    private MensagemService mensagemService;

    @Mock
    private MensagemRepository mensagemRepository;

    AutoCloseable mock;

    @BeforeEach
    void setup() {
        // Define que todas as variáveis com a anotação mock sejam carregadas
        mock = MockitoAnnotations.openMocks(this);
        mensagemService = new MensagemServiceImpl(mensagemRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    void devePermitirRegistrarMensagem() {
        // Arrange
        var mensagem = gerarMensagem();
        when(mensagemRepository.save(any(Mensagem.class)))
                .thenAnswer(i -> i.getArgument(0));

        // Act
        var mensagemRegistrada = mensagemService.registrarMensagem(mensagem);

        // Assert
        assertThat(mensagemRegistrada)
                .isInstanceOf(Mensagem.class)
                .isNotNull();

    }

    @Test
    void devePermitirBuscarMensagem() {
        fail("Teste não implementado");
    }

    @Test
    void devePermitirAlterarMensagem() {
        fail("Teste não implementado");
    }

    @Test
    void devePermitirRemoverMensagem() {
        fail("Teste não implementado");
    }

    @Test
    void devePermitirListarMensagens() {
        fail("Teste não implementado");
    }

    private Mensagem gerarMensagem() {
        return Mensagem.builder()
                .usuario("Jose")
                .conteudo("Conteúdo da mensagem")
                .build();
    }
}

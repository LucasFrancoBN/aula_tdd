package br.com.fiap.repository;


import br.com.fiap.api.model.Mensagem;
import br.com.fiap.api.repository.MensagemRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class MensagemRepositoryTest {
  @Mock
  private MensagemRepository mensagemRepository;

  AutoCloseable openMocks;

  @BeforeEach
  void setup() {
    openMocks = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Test
  void devePermitirRegistrarMensagem() {
    // Arrange
    var mensagem = Mensagem.builder()
        .id(UUID.randomUUID())
        .usuario("Jose")
        .conteudo("Conteúdo da mensagem")
        .build();

    // Define o comportamento do mock
    when(mensagemRepository.save(any(Mensagem.class))).thenReturn(mensagem);

    // Act
    var mensagemRegistrada = mensagemRepository.save(mensagem);

    // Assert
    assertThat(mensagemRegistrada)
        .isNotNull()
        .isEqualTo(mensagem);

    // Verifica se o mensagem repository foi chamado ao menos uma vez com o método save com a mensagem de parâmetro
    verify(mensagemRepository, times(1)).save(any(Mensagem.class));
  }

  @Test
  void devePermitirAlterarMensagem() {
    fail("Teste não implementado");
  }

  @Test
  void devePermitirExcluirMensagem() {
    fail("Teste não implementado");
  }

  @Test
  void devePermitirBuscarMensagem() {
    fail("Teste não implementado");
  }

  @Test
  void devePermitirListarMensagem() {
    fail("Teste não implementado");
  }

}

package br.com.fiap.api.repository;

import br.com.fiap.api.api.RestApiApplication;
import br.com.fiap.api.api.model.Mensagem;
import br.com.fiap.api.api.repository.MensagemRepository;
import br.com.fiap.api.utils.MensagemHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = RestApiApplication.class)
@AutoConfigureTestDatabase
@Transactional
@ActiveProfiles("test")
class MensagemRepositoryIT {
  @Autowired
  private MensagemRepository mensagemRepository;

  @Test
  void devePermitirCriarTabela() {
    var totalDeRegistros = mensagemRepository.count();
    assertThat(totalDeRegistros).isPositive();
  }

  @Test
  void devePermitirRegistrarMensagem() {
    // Arrange
    var id = UUID.randomUUID();
    var mensagem = MensagemHelper.gerarMensagem();
    mensagem.setId(id);

    // Act
    var mensagemRecebida = mensagemRepository.save(mensagem);

    // Assert

    assertThat(mensagemRecebida)
        .isInstanceOf(Mensagem.class)
        .isNotNull();

    assertThat(mensagemRecebida.getId()).isEqualTo(id);
    assertThat(mensagemRecebida.getConteudo()).isEqualTo(mensagem.getConteudo());
    assertThat(mensagemRecebida.getUsuario()).isEqualTo(mensagem.getUsuario());


  }

  @Test
  void devePermitirListarMensagem() {
    var mensagensRecebidas = mensagemRepository.findAll();

    // Assert
    Assertions.assertThat(mensagensRecebidas).hasSizeGreaterThan(2);
  }

  @Test
  void devePermitirBuscarMensagem() {
    // Arrange
    var id = UUID.fromString("4c6e0331-b9e8-44ec-96c4-1b40a738dac9");


    // Act
      var mensagemRecebidaOptional = mensagemRepository.findById(id);

    // Assert
    assertThat(mensagemRecebidaOptional).isPresent();

    mensagemRecebidaOptional.ifPresent(mensagemRecebida -> {
      assertThat(mensagemRecebida.getId()).isEqualTo(id);
    });
  }

  @Test
  void devePermitirExcluirMensagem() {
    // Arrange
    var id = UUID.fromString("7e158e54-2baa-4e96-9519-c6278c62ea91");

    // Act
    mensagemRepository.deleteById(id);
    var mensagemRemovida = mensagemRepository.findById(id);

    // Assert
    assertThat(mensagemRemovida).isEmpty();
  }
}

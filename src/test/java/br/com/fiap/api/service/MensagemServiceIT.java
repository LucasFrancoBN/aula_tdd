package br.com.fiap.api.service;

import br.com.fiap.api.api.RestApiApplication;
import br.com.fiap.api.api.service.MensagemService;
import br.com.fiap.api.api.exception.MensagemNotFoundException;
import br.com.fiap.api.api.model.Mensagem;
import br.com.fiap.api.utils.MensagemHelper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = RestApiApplication.class)
@AutoConfigureTestDatabase
@Transactional
@ActiveProfiles("test")
class MensagemServiceIT {
  @Autowired
  private MensagemService mensagemService;

  @Nested
  class RegistrarMensagem {
    @Test
    void devePermitirRegistrarMensagem() {
      var mensagem = MensagemHelper.gerarMensagem();

      var resultadoObtido = mensagemService.registrarMensagem(mensagem);

      assertThat(resultadoObtido)
              .isNotNull()
              .isInstanceOf(Mensagem.class);

      assertThat(mensagem.getId()).isNotNull();
      assertThat(resultadoObtido.getDataCriacao()).isNotNull();
      assertThat(resultadoObtido.getGostei()).isZero();
    }
  }

  @Nested
  class BuscarMensagem {
    @Test
    void devePermitirBuscarMensagem() {
      var id = UUID.fromString("4c6e0331-b9e8-44ec-96c4-1b40a738dac9");
      var resultadoObtido = mensagemService.buscarMensagem(id);

      assertThat(resultadoObtido)
              .isNotNull()
              .isInstanceOf(Mensagem.class);

      assertThat(resultadoObtido.getId())
              .isNotNull()
              .isEqualTo(id);

      assertThat(resultadoObtido.getUsuario())
              .isNotNull()
              .isEqualTo("Adam");

      assertThat(resultadoObtido.getDataCriacao())
              .isNotNull();

      assertThat(resultadoObtido.getGostei()).isZero();

      assertThat(resultadoObtido.getConteudo())
              .isNotNull()
              .isEqualTo("Conteudo da mensagem 01");

    }

    @Test
    void deveGerarExcecao_QuandoBuscarMensagem_IdNaoExiste() {
      var id = UUID.fromString("f3150aae-81e9-4509-b558-18d594fff473");

      assertThatThrownBy(() -> mensagemService.buscarMensagem(id))
              .isInstanceOf(MensagemNotFoundException.class)
              .hasMessage("Mensagem não encontrada");
    }
  }

  @Nested
  class AlterarMensagem {
    @Test
    void devePermitirAlterarMensagem() {
      var id = UUID.fromString("7e158e54-2baa-4e96-9519-c6278c62ea91");
      var mensagemAtualizada = MensagemHelper.gerarMensagem();
      mensagemAtualizada.setId(id);

      var resultadoObtido = mensagemService.alterarMensagem(id, mensagemAtualizada);

      assertThat(resultadoObtido.getId()).isEqualTo(id);
      assertThat(resultadoObtido.getUsuario()).isNotEqualTo(mensagemAtualizada.getUsuario());
      assertThat(resultadoObtido.getConteudo()).isEqualTo(mensagemAtualizada.getConteudo());
    }

    @Test
    void deveGerarExcecao_QuandoAlterarMensagem_IdNaoExiste() {
      var id = UUID.fromString("50308e5f-1f02-4be8-87db-22dc9e187d59");
      var mensagemAtualizada = MensagemHelper.gerarMensagem();
      mensagemAtualizada.setId(id);

      assertThatThrownBy(() -> mensagemService.alterarMensagem(id, mensagemAtualizada))
              .isInstanceOf(MensagemNotFoundException.class)
              .hasMessage("Mensagem não encontrada");

    }

    @Test
    void deveGerarExcecao_QuandoAlterarMensagem_IdDaMensagemNovaApresentaValorDiferente() {
      var id = UUID.fromString("4c6e0331-b9e8-44ec-96c4-1b40a738dac9");
      var mensagemAtualizada = MensagemHelper.gerarMensagem();
      mensagemAtualizada.setId(UUID.fromString("fa90cdb1-9e0c-470d-b6ee-1f148721695e"));

      assertThatThrownBy(() -> mensagemService.alterarMensagem(id, mensagemAtualizada))
              .isInstanceOf(MensagemNotFoundException.class)
              .hasMessage("mensagem atualiza não apresenta ID correto");
    }
  }

  @Nested
  class RemoverMensagem {
    @Test
    void devePermitirRemoverMensagem() {
      var id = UUID.fromString("7dc1766e-1c80-448d-b798-0ad57400dfbc");

      var resultadoObtido = mensagemService.removerMensagem(id);

      assertThat(resultadoObtido).isTrue();

    }

    @Test
    void deveGerarExcecao_QuandoRemoverMensagem_IdNaoExiste() {
      var id = UUID.fromString("091f2ec5-037e-4e35-91c8-8d0a3cb6a980");

      assertThatThrownBy(() -> mensagemService.removerMensagem(id))
              .isInstanceOf(MensagemNotFoundException.class)
              .hasMessage("Mensagem não encontrada");

    }
  }

  @Nested
  class ListarMensagem {
    @Test
    void devePermitirListarMensagens() {
      Page<Mensagem> listaDeMensagemObtida = mensagemService.listarMensagens((Pageable.unpaged()));

      assertThat(listaDeMensagemObtida)
              .hasSize(3)
              .allSatisfy(mensagem -> {
                assertThat(mensagem).isNotNull();
              });
    }

  }
}

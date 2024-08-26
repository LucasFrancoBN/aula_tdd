package br.com.fiap.controller;

import br.com.fiap.api.controller.MensagemController;
import br.com.fiap.api.exception.MensagemNotFoundException;
import br.com.fiap.api.model.Mensagem;
import br.com.fiap.api.service.MensagemService;
import br.com.fiap.utils.MensagemHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MensagemControllerTest {
  private MockMvc mockMvc;

  @Mock
  private MensagemService mensagemService;

  AutoCloseable mock;

  @BeforeEach
  void setup() {
    mock = MockitoAnnotations.openMocks(this);
    MensagemController controller = new MensagemController(mensagemService);
    mockMvc = MockMvcBuilders.standaloneSetup(controller)
        .addFilter((request, response, chain) -> {
          response.setCharacterEncoding("UTF-8");
          chain.doFilter(request, response);
        })
        .build();
  }

  @AfterEach
  void tearDown() throws Exception {
    mock.close();
  }

  @Nested
  class RegistrarMensagem {
    @Test
    void devePermitirRegistrarMensagem() throws Exception {
      //Arrange
      var mensagem = MensagemHelper.gerarMensagem();
      when(mensagemService.registrarMensagem(any(Mensagem.class)))
          .thenAnswer(answer -> answer.getArgument(0));

      // Act && Assert
      mockMvc.perform(
          post("/mensagens")
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(mensagem))
      ).andExpect(status().isCreated());
      verify(mensagemService, times(1)).registrarMensagem(any(Mensagem.class));
    }

    @Test
    void deveGerarExcecao_QuandoRegistrarMensagem_PayloadXML() throws Exception {
      String xmlPayload = "<mensagem><usuario>Ana</usuario><conteudo>Mensagem do Conteudo</conteudo></mensagem>";

      mockMvc.perform(
          post("/mensagens")
              .contentType(MediaType.APPLICATION_XML)
              .content(xmlPayload)
      ).andExpect(status().isUnsupportedMediaType());

      verify(mensagemService, never()).registrarMensagem(any(Mensagem.class));
    }
  }

  @Nested
  class BuscarMensagem {
    @Test
    void devePermitirBuscarMensagem() throws Exception {
      var id = UUID.fromString("ccd734df-8a0f-480f-83de-638c18d972a3");
      var mensagem = MensagemHelper.gerarMensagem();
      mensagem.setId(id);

      when(mensagemService.buscarMensagem(id))
          .thenReturn(mensagem);

      mockMvc.perform(get("/mensagens/{id}", id))
          .andExpect(status().isOk());
      verify(mensagemService, times(1)).buscarMensagem(any(UUID.class));
    }

    @Test
    void deveGerarExcecao_QuandoBuscarMensagem_IdNaoExiste() throws Exception {
      var id = UUID.fromString("ccd734df-8a0f-480f-83de-638c18d972a3");

      when(mensagemService.buscarMensagem(id))
          .thenThrow(MensagemNotFoundException.class);

      mockMvc.perform(get("/mensagens/{id}", id))
          .andExpect(status().isNotFound());

      verify(mensagemService, times(1)).buscarMensagem(any(UUID.class));
    }
  }

  @Nested
  class AlterarMensagem {
    @Test
    void devePermitirAlterarMensagem() throws Exception {
      var id = UUID.fromString("ccd734df-8a0f-480f-83de-638c18d972a3");
      var mensagem = MensagemHelper.gerarMensagem();
      mensagem.setId(id);

      when(mensagemService.alterarMensagem(id, mensagem))
          .thenAnswer(answer -> answer.getArgument(1));

      mockMvc.perform(
          put("/mensagens/{id}", id)
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(mensagem)))
          .andExpect(status().isAccepted());

      verify(mensagemService, times(1)).alterarMensagem(any(UUID.class), any(Mensagem.class));
    }

    @Test
    void deveGerarExcecao_QuandoAlterarMensagem_PayloadXML() throws Exception {
      var id = UUID.fromString("ccd734df-8a0f-480f-83de-638c18d972a3");
      String xmlPayload = "<mensagem><id>" + id + "</id><usuario>Ana</usuario><conteudo>Mensagem do Conteudo</conteudo></mensagem>";

      mockMvc.perform(
          put("/mensagens/{id}", id)
              .contentType(MediaType.APPLICATION_XML)
              .content(xmlPayload)
      ).andExpect(status().isUnsupportedMediaType());

      verify(mensagemService, never()).alterarMensagem(any(UUID.class), any(Mensagem.class));
    }

    @Test
    void deveGerarExcecao_QuandoAlterarMensagem_IdNaoExiste() throws Exception {
      var id = UUID.fromString("ccd734df-8a0f-480f-83de-638c18d972a3");
      var mensagem = MensagemHelper.gerarMensagem();
      var conteudoDaExcecao = "mensagem atualiza não apresenta ID correto";
      mensagem.setId(id);

      when(mensagemService.alterarMensagem(id, mensagem))
          .thenThrow(new MensagemNotFoundException(conteudoDaExcecao));

      mockMvc.perform(
          put("/mensagens/{id}", id)
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(mensagem)))
          // .andDo(print())
          .andExpect(status().isNotFound())
          .andExpect(content().string(conteudoDaExcecao));

      verify(mensagemService, times(1))
          .alterarMensagem(any(UUID.class), any(Mensagem.class));

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

  public static String asJsonString(final Object object) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    return mapper.writeValueAsString(object);
  }
}

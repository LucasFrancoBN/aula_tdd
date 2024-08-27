package br.com.fiap.controller;

import br.com.fiap.api.RestApiApplication;
import br.com.fiap.api.model.Mensagem;
import br.com.fiap.utils.MensagemHelper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = RestApiApplication.class
)
@AutoConfigureTestDatabase
public class MensagemControllerIT {
  @LocalServerPort
  private int port;

  @BeforeEach
  void setup() {
    RestAssured.port = port;
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @Nested
  class RegistrarMensagem {
    @Test
    void devePermitirRegistrarMensagem() {
      var mensagem = MensagemHelper.gerarMensagem();

      // Pré condições
      given()
          .contentType("application/json")
          .body(mensagem)
          .log().all()
      // Quando fizermos a requisição
      .when()
          .post("/mensagens")
      // então
      .then()
          .statusCode(HttpStatus.CREATED.value())
          .body(matchesJsonSchemaInClasspath("schemas/mensagem.schema.json"))
          .log().all();

    }

    @Test
    void deveGerarExcecao_QuandoRegistrarMensagem_PayloadXML() {
      String xmlPayload = "<mensagem><usuario>Ana</usuario><conteudo>Mensagem do Conteudo</conteudo></mensagem>";

      // Pré condições
      given()
          .contentType("application/json")
          .body(xmlPayload)
          .log().all()
          // Quando fizermos a requisição
      .when()
          .post("/mensagens")
          // então
      .then()
          .statusCode(HttpStatus.BAD_REQUEST.value())
          .body(matchesJsonSchemaInClasspath("schemas/error.schema.json"))
          .body("error", equalTo("Bad Request"))
          .body("path", equalTo("/mensagens"))
          .log().all();

    }
  }

  @Nested
  class BuscarMensagem {
    @Test
    void devePermitirBuscarMensagem() {
      var id = "4c6e0331-b9e8-44ec-96c4-1b40a738dac9";

      // Dada a requisição
      when()
          .get("/mensagens/{id}", id)
      // então
      .then()
          .statusCode(HttpStatus.OK.value())
          .log().all();

    }

    @Test
    void deveGerarExcecao_QuandoBuscarMensagem_IdNaoExiste() {
      var id = "4c6e0331-b9e8-44ec-96c4-1b40a738dac1";

      // Dada a requisição
      when()
          .get("/mensagens/{id}", id)
          // então
      .then()
          .statusCode(HttpStatus.NOT_FOUND.value())
          .log().all();
    }
  }

  @Nested
  class AlterarMensagem {
    @Test
    void devePermitirAlterarMensagem() {
      var id = UUID.fromString("7dc1766e-1c80-448d-b798-0ad57400dfbc");
      var mensagem = Mensagem.builder()
              .id(id)
              .usuario("John")
              .conteudo("Conteudo da mensagem 03")
              .build();

      given()
              .contentType("application/json")
              .body(mensagem)
      .when()
              .put("/mensagens/{id}", id)
      .then()
              .statusCode(HttpStatus.ACCEPTED.value())
              .body(matchesJsonSchemaInClasspath("schemas/mensagem.schema.json"))
              .log().all();

    }

    @Test
    void deveGerarExcecao_QuandoAlterarMensagem_ApresentaPayloadXML() {
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
  class ListarMensagem {
    @Test
    void devePermitirListarMensagens() {
      fail("Teste não implementado");
    }
  }
}

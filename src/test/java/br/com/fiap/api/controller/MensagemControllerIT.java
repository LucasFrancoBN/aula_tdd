package br.com.fiap.api.controller;

import br.com.fiap.api.api.RestApiApplication;
import br.com.fiap.api.api.model.Mensagem;
import br.com.fiap.api.utils.MensagemHelper;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = RestApiApplication.class
)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
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
          .filter(new AllureRestAssured())
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
          .filter(new AllureRestAssured())
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
              .filter(new AllureRestAssured())
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
      var id = UUID.fromString("7dc1766e-1c80-448d-b798-0ad57400dfbc");
      String xmlPayload = "<mensagem>" + id + "<usuario>John</usuario><conteudo>Conteudo da mensagem 03</conteudo></mensagem>";

      given()
          .filter(new AllureRestAssured())
          .contentType("application/json")
          .body(xmlPayload)
      .when()
          .put("/mensagens/{id}", id)
      .then()
          .statusCode(HttpStatus.BAD_REQUEST.value())
          .log().all()
          .body(matchesJsonSchemaInClasspath("schemas/error.schema.json"))
          .body("error", equalTo("Bad Request"))
          .body("path", containsString("/mensagens/"))
          .body("path", equalTo("/mensagens/7dc1766e-1c80-448d-b798-0ad57400dfbc"));

    }

    @Test
    void deveGerarExcecao_QuandoAlterarMensagem_IdNaoExiste() {
      var id = UUID.fromString("7dc1766e-1c80-448d-b798-0ad57400dfba");
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
          .statusCode(HttpStatus.NOT_FOUND.value())
          .log().all()
          .body(equalTo("Mensagem não encontrada"));
    }

    @Test
    void deveGerarExcecao_QuandoAlterarMensagem_IdDaMensagemNovaApresentaValorDiferente() {
      var id = UUID.fromString("7dc1766e-1c80-448d-b798-0ad57400dfbc");
      var mensagem = Mensagem.builder()
          .id(UUID.fromString("7dc1766e-1c80-448d-b798-0ad57400dfba"))
          .usuario("John")
          .conteudo("Conteudo da mensagem 03")
          .build();

      given()
          .contentType("application/json")
          .body(mensagem)
      .when()
          .put("/mensagens/{id}", id)
      .then()
          .statusCode(HttpStatus.NOT_FOUND.value())
          .log().all()
          .body(equalTo("mensagem atualiza não apresenta ID correto"));
    }
  }

  @Nested
  class RemoverMensagem {
    @Test
    void devePermitirRemoverMensagem() {
      var id = UUID.fromString("7e158e54-2baa-4e96-9519-c6278c62ea91");

        when()
          .delete("/mensagens/{id}", id)
        .then()
          .log().all()
          .statusCode(HttpStatus.OK.value());
    }

    @Test
    void deveGerarExcecao_QuandoRemoverMensagem_IdNaoExiste() {
      var id = UUID.fromString("7e158e54-2baa-4e96-9519-c6278c62ea9a");

      when()
        .delete("/mensagens/{id}", id)
      .then()
        .log().all()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body(equalTo("Mensagem não encontrada"));
    }
  }

  @Nested
  class ListarMensagem {
    @Test
    void devePermitirListarMensagens() {
      given()
          .queryParam("page", "0")
          .queryParam("size", "10")
      .when()
          .get("/mensagens")
      .then()
          .log().all()
          .statusCode(HttpStatus.OK.value())
          .body(matchesJsonSchemaInClasspath("schemas/mensagem.page.schema.json"));
    }

    @Test
    void devePermitirListarMensagem_QuandoNaoInformadoPagainacao() {
      when()
          .get("/mensagens")
      .then()
          .log().all()
          .statusCode(HttpStatus.OK.value())
          .body(matchesJsonSchemaInClasspath("schemas/mensagem.page.schema.json"));


    }
  }
}

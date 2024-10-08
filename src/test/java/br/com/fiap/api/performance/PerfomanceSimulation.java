package br.com.fiap.api.performance;

import io.gatling.javaapi.core.ActionBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class PerfomanceSimulation extends Simulation {
  private final HttpProtocolBuilder httpProtocol =
      http.baseUrl("http://localhost:8080")
          .header("Content-Type", "application/json");

  ActionBuilder adicionarMensagemRequest = http("adicionar mensagem")
      .post("/mensagens")
      .body(StringBody("{\"usuario\": \"use\", \"conteudo\": \"conteudo da mensagem\"}"))
      .check(status().is(201))
      .check(jsonPath("$.id").saveAs("mensagemId"));

  ActionBuilder buscarMensagemRequest = http("request: buscar mensagem")
      .get("/mensagens/#{mensagemId}")
      .check(status().is(200));

  ActionBuilder deletarMensagemRequest = http("request: deletar mensagem")
      .delete("/mensagens/#{mensagemId}")
      .check(status().is(200));

  ScenarioBuilder cenarioAdicionarMensagem = scenario("adicionar mensagem")
      .exec(adicionarMensagemRequest);

  ScenarioBuilder cenarioBuscarMensagem = scenario("buscar mensagem")
      .exec(adicionarMensagemRequest)
      .exec(buscarMensagemRequest);

  ScenarioBuilder cenarioDeletarMensagem = scenario("deletar mensagem")
      .exec(adicionarMensagemRequest)
      .exec(deletarMensagemRequest);

  {
    setUp(
        cenarioAdicionarMensagem.injectOpen(
            rampUsersPerSec(1)
                .to(2)
                .during(Duration.ofSeconds(10)),
            constantUsersPerSec(2)
                .during(Duration.ofSeconds(20)),
            rampUsersPerSec(2)
                .to(1)
                .during(Duration.ofSeconds(10))

        ),
        cenarioBuscarMensagem.injectOpen(
            rampUsersPerSec(1)
                .to(10)
                .during(Duration.ofSeconds(10)),
            constantUsersPerSec(10)
                .during(Duration.ofSeconds(20)),
            rampUsersPerSec(10)
                .to(1)
                .during(Duration.ofSeconds(10))

        ),
        cenarioDeletarMensagem.injectOpen(
            rampUsersPerSec(1)
                .to(5)
                .during(Duration.ofSeconds(10)),
            constantUsersPerSec(5)
                .during(Duration.ofSeconds(20)),
            rampUsersPerSec(5)
                .to(1)
                .during(Duration.ofSeconds(10))

        )
    )
        .protocols(httpProtocol)
        .assertions(
            global().responseTime().max().lt(50)
        );
  }
}

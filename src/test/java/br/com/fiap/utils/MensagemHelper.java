package br.com.fiap.utils;

import br.com.fiap.api.model.Mensagem;

public abstract  class MensagemHelper {
  public static Mensagem gerarMensagem() {
    return Mensagem.builder()
        .usuario("Jose")
        .conteudo("Conteúdo da mensagem")
        .build();
  }
}

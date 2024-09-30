package br.com.fiap.api.api.exception;

public class MensagemNotFoundException extends RuntimeException {
  public MensagemNotFoundException(String mensagemNotFound) {
    super(mensagemNotFound);
  }
}

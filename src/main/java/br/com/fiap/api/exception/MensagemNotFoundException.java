package br.com.fiap.api.exception;

public class MensagemNotFoundException extends RuntimeException {
  public MensagemNotFoundException(String mensagemNotFound) {
    super(mensagemNotFound);
  }
}

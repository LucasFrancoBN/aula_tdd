package br.com.fiap.api.service;

import br.com.fiap.api.model.Mensagem;

import java.util.List;
import java.util.UUID;

public interface MensagemService {
    Mensagem registrarMensagem(Mensagem mensagem);

    Mensagem buscarMensagem(UUID id);

    Mensagem alterarMensagem(Mensagem mensagemAtual, Mensagem MensagemModificada);

    boolean removerMensagem(UUID id);
}

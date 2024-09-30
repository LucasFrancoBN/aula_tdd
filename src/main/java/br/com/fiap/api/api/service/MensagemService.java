package br.com.fiap.api.api.service;

import br.com.fiap.api.api.model.Mensagem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MensagemService {
    Mensagem registrarMensagem(Mensagem mensagem);

    Mensagem buscarMensagem(UUID id);

    Mensagem alterarMensagem(UUID id, Mensagem mensagemAtualizada);

    boolean removerMensagem(UUID id);

    public Page<Mensagem> listarMensagens(Pageable pageable);
}

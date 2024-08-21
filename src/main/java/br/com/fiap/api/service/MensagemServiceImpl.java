package br.com.fiap.api.service;

import br.com.fiap.api.model.Mensagem;
import br.com.fiap.api.repository.MensagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MensagemServiceImpl implements MensagemService{
    private final MensagemRepository mensagemRepository;

    @Override
    public Mensagem registrarMensagem(Mensagem mensagem) {
        mensagem.setId(UUID.randomUUID());
        return mensagemRepository.save(mensagem);
    }

    @Override
    public Mensagem buscarMensagem(UUID id) {
        return null;
    }

    @Override
    public Mensagem alterarMensagem(Mensagem mensagemAtual, Mensagem MensagemModificada) {
        return null;
    }

    @Override
    public boolean removerMensagem(UUID id) {
        return false;
    }
}

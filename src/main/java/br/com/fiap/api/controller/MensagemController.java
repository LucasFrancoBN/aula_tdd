package br.com.fiap.api.controller;

import br.com.fiap.api.model.Mensagem;
import br.com.fiap.api.service.MensagemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mensagens")
@RequiredArgsConstructor
public class MensagemController {
    @Autowired
    private final MensagemService mensagemService;

    @PostMapping
    public ResponseEntity<Mensagem> registrarMensagem(@RequestBody Mensagem mensagem) {
        var mensagemRegistrada = mensagemService.registrarMensagem(mensagem);
        return new ResponseEntity<>(mensagemRegistrada, HttpStatus.CREATED);
    }
}

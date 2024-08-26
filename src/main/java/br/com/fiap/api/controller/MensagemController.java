package br.com.fiap.api.controller;

import br.com.fiap.api.exception.MensagemNotFoundException;
import br.com.fiap.api.model.Mensagem;
import br.com.fiap.api.service.MensagemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.util.UUID;

@RestController
@RequestMapping("/mensagens")
@RequiredArgsConstructor
public class MensagemController {
    @Autowired
    private final MensagemService mensagemService;

    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Mensagem> registrarMensagem(@RequestBody  Mensagem mensagem) {
        var mensagemRegistrada = mensagemService.registrarMensagem(mensagem);
        return new ResponseEntity<>(mensagemRegistrada, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarMensagem(@PathVariable String id) {
        try{
            var uuid = UUID.fromString(id);
            var mensagemEncontrada = mensagemService.buscarMensagem(uuid);
            return new ResponseEntity<>(mensagemEncontrada, HttpStatus.OK);
        } catch (MensagemNotFoundException e) {
            return new ResponseEntity<>("Id inválido", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(
        value = "/{id}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> alterarMensagem(@PathVariable String id, @RequestBody Mensagem mensagem) {
        try {
          var uuid = UUID.fromString(id);
          var mensagemAtualizada = mensagemService.alterarMensagem(uuid, mensagem);
          return new ResponseEntity<>(mensagemAtualizada, HttpStatus.ACCEPTED);
        } catch (MensagemNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerMensagem(@PathVariable String id) {
        try {
            var uuid = UUID.fromString(id);
            mensagemService.removerMensagem(uuid);
            return new ResponseEntity<>("Mensagem removida", HttpStatus.OK);
        } catch (MensagemNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Page<Mensagem>> listarMensagens(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var pageable = PageRequest.of(page, size);
        var mensagens = mensagemService.listarMensagens(pageable);

        return new ResponseEntity<>(mensagens, HttpStatus.OK);
    }
}

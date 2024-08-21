package br.com.fiap.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Entity
@Table(name = "mensagem")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mensagem {
  @Id
  private UUID id;
  @Column(nullable = false)
  @NotEmpty(message = "Usuário não pode estar vazio")
  private String usuario;
  @Column(nullable = false)
  @NotEmpty(message = "Conteúdo não pode estar vazio")
  private String conteudo;
  @Builder.Default
  private final LocalDateTime dataCriacao = LocalDateTime.now();
  @Builder.Default
  private final int gostei = 0;



}

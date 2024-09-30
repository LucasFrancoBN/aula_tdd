package br.com.fiap.api.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSS")
  private final LocalDateTime dataCriacao = LocalDateTime.now();
  @Builder.Default
  private final int gostei = 0;



}

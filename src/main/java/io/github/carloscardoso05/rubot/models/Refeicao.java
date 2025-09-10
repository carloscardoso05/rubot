package io.github.carloscardoso05.rubot.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record Refeicao(
    @NotNull @NotEmpty List<String> acompanhamentos,
    @NotBlank String principal,
    @NotBlank String vegetariano) {
  public Refeicao {
    if (acompanhamentos == null) acompanhamentos = List.of();
  }
}

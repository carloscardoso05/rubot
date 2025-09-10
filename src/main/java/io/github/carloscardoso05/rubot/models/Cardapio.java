package io.github.carloscardoso05.rubot.models;

import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDate;

public record Cardapio(@NotNull LocalDate dia, @NotNull Refeicao almoco, @NotNull Refeicao janta) {
  public Cardapio {
    var dayOfWeek = dia.getDayOfWeek();
    if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
      String msg = "Dia da semana não pode ser Sábado nem domingo. Valor: %s".formatted(dayOfWeek);
      throw new IllegalArgumentException(msg);
    }
  }

  public DayOfWeek diaSemana() {
    return dia.getDayOfWeek();
  }
}

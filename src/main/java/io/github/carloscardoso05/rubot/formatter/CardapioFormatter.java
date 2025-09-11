package io.github.carloscardoso05.rubot.formatter;

import io.github.carloscardoso05.rubot.models.Cardapio;
import io.github.carloscardoso05.rubot.models.Refeicao;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class CardapioFormatter {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM");

  private static final Map<DayOfWeek, String> DIAS_SEMANA =
      Map.of(
          DayOfWeek.MONDAY, "Segunda-feira",
          DayOfWeek.TUESDAY, "Terça-feira",
          DayOfWeek.WEDNESDAY, "Quarta-feira",
          DayOfWeek.THURSDAY, "Quinta-feira",
          DayOfWeek.FRIDAY, "Sexta-feira");

  public String formatarCardapioDoDia(Cardapio cardapio) {
    if (cardapio == null) {
      return "❌ Cardápio não disponível para hoje.";
    }

    String diaSemana = DIAS_SEMANA.get(cardapio.diaSemana());
    String dataFormatada = cardapio.dia().format(DATE_FORMATTER);
    String almocoFormatado = formatarRefeicao(cardapio.almoco());
    String jantaFormatada = formatarRefeicao(cardapio.janta());

    return """
    🍽️ *CARDÁPIO DE %s (%s)*

    🌅 *ALMOÇO*
    %s
    🌙 *JANTA*
    %s\
    """
        .formatted(diaSemana.toUpperCase(), dataFormatada, almocoFormatado, jantaFormatada);
  }

  public String formatarCardapioDaSemana(Map<DayOfWeek, Cardapio> cardapioSemanal) {
    if (cardapioSemanal == null || cardapioSemanal.isEmpty()) {
      return "❌ Cardápio da semana não disponível.";
    }

    StringBuilder cardapiosFormatados = new StringBuilder();
    for (DayOfWeek dia : DayOfWeek.values()) {
      Cardapio cardapio = cardapioSemanal.get(dia);
      if (cardapio != null) {
        cardapiosFormatados.append(formatarCardapioResumido(cardapio)).append("\n");
      }
    }

    return """
    🍽️ *CARDÁPIO DA SEMANA*

    %s\
    """
        .formatted(cardapiosFormatados.toString());
  }

  private String formatarCardapioResumido(Cardapio cardapio) {
    String diaSemana = DIAS_SEMANA.get(cardapio.diaSemana());
    String dataFormatada = cardapio.dia().format(DATE_FORMATTER);

    return """
    📆 *%s (%s)*
    🌅 *Almoço:*
        *• Principal:* %s
        *• Vegetariano:* %s
    🌙 *Janta:*
        *• Principal:* %s
        *• Vegetariano:* %s

    """
        .formatted(
            diaSemana,
            dataFormatada,
            capitalize(cardapio.almoco().principal()),
            capitalize(cardapio.almoco().vegetariano()),
            capitalize(cardapio.janta().principal()),
            capitalize(cardapio.janta().vegetariano()));
  }

  private String formatarRefeicao(Refeicao refeicao) {
    String acompanhamentosFormatados = "";
    if (!refeicao.acompanhamentos().isEmpty()) {
      StringBuilder acompanhamentos = new StringBuilder("🍚 *Acompanhamentos:*\n");
      for (String acompanhamento : refeicao.acompanhamentos()) {
        acompanhamentos.append("  • ").append(capitalize(acompanhamento)).append("\n");
      }
      acompanhamentosFormatados = acompanhamentos.toString();
    }

    return """
    🍖 *Principal:* %s
    🥬 *Vegetariano:* %s
    %s\
    """
        .formatted(
            capitalize(refeicao.principal()),
            capitalize(refeicao.vegetariano()),
            acompanhamentosFormatados);
  }

  private String capitalize(String str) {
    if (str == null || str.isEmpty()) {
      return str;
    }
    return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
  }
}

package io.github.carloscardoso05.rubot.scraper;

import io.github.carloscardoso05.rubot.models.Cardapio;
import io.github.carloscardoso05.rubot.models.Refeicao;
import jakarta.validation.constraints.NotBlank;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class Scraper {

  private final CardapioFetcher fetcher;
  private static final String LINE_BREAK = "[\\n\\t]+";
  private static final Pattern dataPattern = Pattern.compile("(\\d{2})/(\\d{2})");
  private static final Logger logger = LoggerFactory.getLogger(Scraper.class);

  public Scraper(CardapioFetcher fetcher) {
    this.fetcher = fetcher;
  }

  public Map<DayOfWeek, Cardapio> buscarCardapiosDaSemana() {
    Document doc = fetcher.fetch();
    Map<DayOfWeek, Cardapio> cardapioSemanal = new EnumMap<>(DayOfWeek.class);

    var tbody = doc.selectFirst("#content-section > div > table > tbody:nth-child(1)");

    for (var tr : tbody.select("tr").next()) {
      var tds = tr.select("td");
      LocalDate data = extrairData(tds.eq(0).first().text());
      Refeicao almoco = extrairRefeicao(tds.eq(1).first());
      Refeicao janta = extrairRefeicao(tds.eq(2).first());
      Cardapio cardapio = new Cardapio(data, almoco, janta);
      cardapioSemanal.put(cardapio.diaSemana(), cardapio);
    }

    return cardapioSemanal;
  }

  private Refeicao extrairRefeicao(Element td) {
    return new Refeicao(extrairAcompanhamentos(td), extrairPrincipal(td), extrairVegetariano(td));
  }

  private String extrairPrincipal(Element td) {
    return td.html().split(LINE_BREAK)[0];
  }

  private String extrairVegetariano(Element td) {
    return td.html().split(LINE_BREAK)[2].replace("VEGETARIANO: ", "");
  }

  private List<String> extrairAcompanhamentos(Element td) {
    return List.of(td.select("li").html().split(LINE_BREAK)).stream()
        .map(str -> str.replace(";", "").trim())
        .toList();
  }

  private LocalDate extrairData(@NotBlank String text) {
    var matcher = dataPattern.matcher(text);
    if (matcher.find()) {
      int dia = Integer.parseInt(matcher.group(1));
      int mes = Integer.parseInt(matcher.group(2));
      return LocalDate.now().withMonth(mes).withDayOfMonth(dia);
    }
    var error = new IllegalArgumentException("Data não encontrada em \"%s\"".formatted(text));
    logger.error("Erro ao extrair data", error);
    throw error;
  }
}

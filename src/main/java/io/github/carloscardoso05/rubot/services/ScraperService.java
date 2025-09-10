package io.github.carloscardoso05.rubot.services;

import io.github.carloscardoso05.rubot.models.Cardapio;
import io.github.carloscardoso05.rubot.models.Refeicao;
import jakarta.validation.constraints.NotBlank;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.lang3.NotImplementedException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ScraperService {

  private final CardapioFetcher fetcher;
  private static final Pattern dataPattern = Pattern.compile("(\\d{2})/(\\d{2})");
  private static final Logger logger = LoggerFactory.getLogger(ScraperService.class);

  public ScraperService(CardapioFetcher fetcher) {
    this.fetcher = fetcher;
  }

  public Map<DayOfWeek, Cardapio> getCardapioDaSemana() {
    Document doc = fetcher.fetch();
    Map<DayOfWeek, Cardapio> cardapioSemanal = new EnumMap<>(DayOfWeek.class);

    var tbody = doc.selectFirst("#content-section > div > table > tbody:nth-child(1)");

    for (var tr : tbody.select("tr")) {
      var tds = tr.select("td");
      LocalDate data = extrairData(tds.eq(0).first().text());
      Refeicao almoco = extrairRefeicao(tds.eq(1).first());
      Refeicao janta = extrairRefeicao(tds.eq(2).first());
      Cardapio cardapio = new Cardapio(data, almoco, janta);
      cardapioSemanal.put(cardapio.diaSemana(), cardapio);
    }

    return cardapioSemanal;
  }

  public Cardapio getCardapioDeHoje() {
    Map<DayOfWeek, Cardapio> semana = getCardapioDaSemana();
    return semana.get(LocalDate.now().getDayOfWeek());
  }

  private Refeicao extrairRefeicao(Element td) {
    throw new NotImplementedException();
  }

  private LocalDate extrairData(@NotBlank String text) {
    var match = dataPattern.matcher(text);
    int dia = Integer.parseInt(match.group(0));
    int mes = Integer.parseInt(match.group(1));
    return LocalDate.now().withMonth(mes).withDayOfMonth(dia);
  }
}

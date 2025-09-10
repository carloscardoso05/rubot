package io.github.carloscardoso05.rubot.services;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class DefaultCardapioFetcher implements CardapioFetcher {

  private static final String CARDAPIO_URL =
      "https://saest.ufpa.br/ru/index.php/component/cardapio/";

  @Override
  public Document fetch() {
    try {
      return Jsoup.connect(CARDAPIO_URL).get();
    } catch (IOException e) {
      throw new RUSiteConnectionException(e);
    }
  }
}

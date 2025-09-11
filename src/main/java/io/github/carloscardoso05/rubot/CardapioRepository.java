package io.github.carloscardoso05.rubot;

import io.github.carloscardoso05.rubot.models.Cardapio;
import io.github.carloscardoso05.rubot.scraper.Scraper;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CardapioRepository {
  private final Scraper scraper;
  private Map<DayOfWeek, Cardapio> cardapioCache;
  private LocalDateTime ultimaAtualizacao;
  private final ReentrantLock cacheLock = new ReentrantLock();
  private static final int CACHE_DURATION_MINUTES = 30;
  private static final Logger logger = LoggerFactory.getLogger(CardapioRepository.class);

  public CardapioRepository(Scraper scraper) {
    this.scraper = scraper;
  }

  public Map<DayOfWeek, Cardapio> getCardapioDaSemana() {
    cacheLock.lock();
    try {
      if (cacheEhValido()) {
        logger.debug("Retornando cardápios do cache");
        return cardapioCache;
      }

      logger.info("Cache expirado ou inexistente. Buscando novos cardápios...");
      cardapioCache = scraper.buscarCardapiosDaSemana();
      ultimaAtualizacao = LocalDateTime.now();
      return cardapioCache;
    } finally {
      cacheLock.unlock();
    }
  }

  public Cardapio getCardapioDeHoje() {
    Map<DayOfWeek, Cardapio> semana = getCardapioDaSemana();
    return semana.get(LocalDate.now().getDayOfWeek());
  }

  private boolean cacheEhValido() {
    return cardapioCache != null
        && ultimaAtualizacao != null
        && LocalDateTime.now().isBefore(ultimaAtualizacao.plusMinutes(CACHE_DURATION_MINUTES));
  }
}

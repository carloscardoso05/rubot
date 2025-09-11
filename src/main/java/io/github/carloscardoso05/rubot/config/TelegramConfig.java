package io.github.carloscardoso05.rubot.config;

import io.github.carloscardoso05.rubot.bot.RUBot;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class TelegramConfig {

  private final RUBot ruBot;

  public TelegramConfig(RUBot ruBot) {
    this.ruBot = ruBot;
  }

  @PostConstruct
  public void initBot() throws TelegramApiException {
    TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
    botsApi.registerBot(ruBot);
  }
}

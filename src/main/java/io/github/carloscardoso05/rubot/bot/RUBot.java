package io.github.carloscardoso05.rubot.bot;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

import io.github.carloscardoso05.rubot.CardapioRepository;
import io.github.carloscardoso05.rubot.formatter.CardapioFormatter;
import io.github.carloscardoso05.rubot.models.Cardapio;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

@Component
public class RUBot extends AbilityBot {

  private static final Logger logger = LoggerFactory.getLogger(RUBot.class);

  private static final String HELP_MESSAGE =
      """
      🤖 *RUBot - Comandos Disponíveis:*

      /hoje - Mostra o cardápio de hoje
      /semana - Mostra o cardápio de toda a semana
      /ajuda - Mostra esta mensagem de ajuda

      📍 Este bot fornece informações sobre o cardápio do Restaurante Universitário da UFPA.
      """;

  private static final String WELCOME_MESSAGE =
      """
      🍽️ *Bem-vindo ao RUBot!*

      Olá! Eu sou o bot do Restaurante Universitário da UFPA.
      Posso te ajudar a consultar o cardápio dos dias da semana.

      """;

  private final CardapioRepository cardapioRepository;
  private final CardapioFormatter cardapioFormatter;

  public RUBot(
      @Value("${bot.key}") String botToken,
      CardapioRepository cardapioRepository,
      CardapioFormatter cardapioFormatter) {
    super(botToken, "rub_pa_bot");
    this.cardapioRepository = cardapioRepository;
    this.cardapioFormatter = cardapioFormatter;
  }

  @Override
  public long creatorId() {
    return 1L;
  }

  @Override
  public void onRegister() {
    super.onRegister();

    logger.info("Inicializando bot");
    try {
      var commands =
          List.of(
              new BotCommand("hoje", "Mostra o cardápio de hoje"),
              new BotCommand("semana", "Mostra o cardápio de toda a semana"),
              new BotCommand("ajuda", "Mostra os comandos disponíveis"));

      var setMyCommands = new SetMyCommands();
      setMyCommands.setCommands(commands);
      execute(setMyCommands);
      logger.info("Comandos do bot registrados com sucesso");
    } catch (Exception e) {
      logger.error("Erro ao registrar comandos do bot", e);
    }
  }

  public Ability start() {
    return Ability.builder()
        .name("start")
        .info("Iniciar o bot e ver o cardápio de hoje")
        .locality(ALL)
        .privacy(PUBLIC)
        .action(
            ctx -> {
              logger.debug("Processando /start");
              try {
                String welcomeMessage = WELCOME_MESSAGE + HELP_MESSAGE;
                silent.sendMd(welcomeMessage, ctx.chatId());
              } catch (Exception e) {
                logger.error("Erro ao processar comando /start", e);
                silent.sendMd("❌ Erro ao inicializar o bot.", ctx.chatId());
              }
            })
        .build();
  }

  public Ability cardapioHoje() {
    return Ability.builder()
        .name("hoje")
        .info("Mostra o cardápio de hoje")
        .locality(ALL)
        .privacy(PUBLIC)
        .action(
            ctx -> {
              logger.debug("Processando /hoje");
              try {
                Cardapio cardapio = cardapioRepository.getCardapioDeHoje();
                String response = cardapioFormatter.formatarCardapioDoDia(cardapio);
                silent.sendMd(response, ctx.chatId());
              } catch (Exception e) {
                logger.error("Erro ao processar comando /hoje", e);
                silent.sendMd(
                    "❌ Ocorreu um erro ao processar sua solicitação. Tente novamente mais tarde.",
                    ctx.chatId());
              }
            })
        .build();
  }

  public Ability cardapioSemana() {
    return Ability.builder()
        .name("semana")
        .info("Mostra o cardápio de toda a semana")
        .locality(ALL)
        .privacy(PUBLIC)
        .action(
            ctx -> {
              logger.debug("Processando /semana");
              try {
                Map<DayOfWeek, Cardapio> cardapioSemanal = cardapioRepository.getCardapioDaSemana();
                String response = cardapioFormatter.formatarCardapioDaSemana(cardapioSemanal);
                silent.sendMd(response, ctx.chatId());
              } catch (Exception e) {
                logger.error("Erro ao processar comando /semana", e);
                silent.sendMd(
                    "❌ Ocorreu um erro ao processar sua solicitação. Tente novamente mais tarde.",
                    ctx.chatId());
              }
            })
        .build();
  }

  public Ability ajuda() {
    return Ability.builder()
        .name("ajuda")
        .info("Mostra os comandos disponíveis")
        .locality(ALL)
        .privacy(PUBLIC)
        .action(ctx -> silent.sendMd(HELP_MESSAGE, ctx.chatId()))
        .build();
  }
}

package io.github.carloscardoso05.rubot.scraper;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.carloscardoso05.rubot.models.Cardapio;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ScraperTest {

  private static Scraper scraperService;
  private static final Month CARDAPIO_MONTH = Month.SEPTEMBER;

  @BeforeAll
  static void beforeAll() throws Exception {
    InputStream is = ScraperTest.class.getClassLoader().getResourceAsStream("RU.html");
    assertThat(is).isNotNull();
    String html = new String(is.readAllBytes(), StandardCharsets.UTF_8);

    CardapioFetcher fetcher = () -> Jsoup.parse(html);
    scraperService = new Scraper(fetcher);
  }

  @Test
  void testCardapioDaSemanaIsNotEmptyAndCorrect() {
    Map<DayOfWeek, Cardapio> cardapios = scraperService.getCardapioDaSemana();
    assertThat(cardapios).isNotNull().hasSize(5);
    assertThat(cardapios.keySet())
        .doesNotContainAnyElementsOf(Set.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY));

    assertCardapio(
        cardapios,
        DayOfWeek.MONDAY,
        8,
        "ISCA DE CARNE ACEBOLADA / GUISADO DE CARNE",
        "GUISADINHO DE SOJA COM LEGUMES",
        List.of(
            "ARROZ BRANCO",
            "FEIJÃO COM MACAXEIRA E ESPINAFRE",
            "FAROFA (contém lactose)",
            "BETERRABA AO VINAGRETE",
            "MELÃO"),
        "GUISADO DE CARNE / ISCA DE CARNE / PERNIL AO MOLHO (PROF.)",
        "ESCONDIDINHO DE SOJA",
        List.of(
            "FEIJÃO COM MACAXEIRA E REPOLHO",
            "FAROFA (contém lactose)",
            "BETERRABA AO VINAGRETE",
            "MELÃO"));

    assertCardapio(
        cardapios,
        DayOfWeek.TUESDAY,
        9,
        "FEIJOADA",
        "FEIJOADA VEGETARIANA",
        List.of("ARROZ BRANCO", "FAROFA (contém lactose)", "COUVE E REPOLHO REFOGADOS", "LARANJA"),
        "ARROZ PARAENSE DE FRANGO",
        "ARROZ PARAENSE VEGETARIANO",
        List.of(
            "FEIJÃO COM MACAXEIRA E CARIRU", "FAROFA (contém lactose)", "SALADA CRUA", "LARANJA"));

    assertCardapio(
        cardapios,
        DayOfWeek.WEDNESDAY,
        10,
        "ISCA DE FRANGO / FRANGO AO MOLHO (contém glúten)",
        "ERVILHA SECA À JARDINEIRA",
        List.of(
            "FEIJÃO COM MACAXEIRA E CARIRU",
            "FAROFA (contém lactose)",
            "CENOURA AO VINAGRETE",
            "MAÇÃ"),
        "PICADINHO À JARDINEIRA",
        "JARDINEIRA DE SOJA",
        List.of(
            "ARROZ BRANCO",
            "FEIJÃO COM MACAXEIRA E CARIRU",
            "FAROFA (contém lactose)",
            "MACARRÃO ESPAGUETE (contém glúten)",
            "BANANA"));

    assertCardapio(
        cardapios,
        DayOfWeek.THURSDAY,
        11,
        "ALMÔNDEGAS AO MOLHO (contém glúten)",
        "ALMÔNDEGAS DE SOJA (contém glúten)",
        List.of(
            "ARROZ BRANCO",
            "FEIJÃO COM ABÓBORA",
            "FAROFA (contém lactose)",
            "SALADA CRUA (BET/CEN)",
            "MELÃO"),
        "ISCA DE FRANGO ACEBOLADA",
        "GRÃO DE BICO AO CREME (contém glúten)",
        List.of(
            "ARROZ BRANCO",
            "FEIJÃO COM ABÓBORA E CARIRU",
            "FAROFA (contém lactose)",
            "PEPINO AO VINAGRETE",
            "MELANCIA"));

    assertCardapio(
        cardapios,
        DayOfWeek.FRIDAY,
        12,
        "ISCA DE FRANGO COM LEGUMES",
        "BOLINHO DE LEGUMES (contém glúten)",
        List.of("ARROZ BRANCO", "FEIJÃO COM MAXIXE E CARIRU", "REPOLHO AO VINAGRETE", "MELANCIA"),
        "BIFE DE PANELA (contém glúten)",
        "QUIBE VEGETARIANO (contém glúten)",
        List.of(
            "ARROZ BRANCO",
            "FEIJÃO COM MAXIXE E CARIRU",
            "FAROFA (contém lactose)",
            "SALADA CRUA",
            "LARANJA"));
  }

  private void assertCardapio(
      Map<DayOfWeek, Cardapio> cardapios,
      DayOfWeek diaSemana,
      int diaDoMes,
      String principalAlmoco,
      String vegetarianoAlmoco,
      List<String> acompanhamentosAlmoco,
      String principalJanta,
      String vegetarianoJanta,
      List<String> acompanhamentosJanta) {

    Cardapio cardapio = cardapios.get(diaSemana);
    assertThat(cardapio).isNotNull();

    assertThat(cardapio.diaSemana()).isEqualTo(diaSemana);
    assertThat(cardapio.dia())
        .isEqualTo(LocalDate.now().withMonth(CARDAPIO_MONTH.getValue()).withDayOfMonth(diaDoMes));

    assertThat(cardapio.almoco()).isNotNull();
    assertThat(cardapio.almoco().principal()).isEqualTo(principalAlmoco);
    assertThat(cardapio.almoco().vegetariano()).isEqualTo(vegetarianoAlmoco);
    assertThat(cardapio.almoco().acompanhamentos()).isEqualTo(acompanhamentosAlmoco);

    assertThat(cardapio.janta()).isNotNull();
    assertThat(cardapio.janta().principal()).isEqualTo(principalJanta);
    assertThat(cardapio.janta().vegetariano()).isEqualTo(vegetarianoJanta);
    assertThat(cardapio.janta().acompanhamentos()).isEqualTo(acompanhamentosJanta);
  }
}

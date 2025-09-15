# RUBot - Telegram Bot para Cardápio do Restaurante Universitário da UFPA

[![Java](https://img.shields.io/badge/Java-24-blue.svg)](https://openjdk.org/projects/jdk/24/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Telegram](https://img.shields.io/badge/Telegram-Bot-blue.svg)](https://core.telegram.org/bots)

## Sobre o Projeto

O **RUBot** é um bot para Telegram que fornece informações atualizadas sobre o cardápio semanal do Restaurante Universitário (RU) da Universidade Federal do Pará (UFPA). O site institucional não possui uma interface amigável para dispositivos móveis, portanto, esse bot visa facilitar o acesso ao cardápio do RU diretamente pelo Telegram.

[🔗 Comece a usar o RUBot agora!](https://t.me/rub_pa_bot)

## Como Funciona

O bot utiliza técnicas de **web scraping** para coletar automaticamente os dados do [site oficial do RU](https://saest.ufpa.br/ru/index.php/component/cardapio/) e apresenta as informações de forma organizada e amigável através do Telegram. Os cardápios são cacheados por 30 minutos para melhor performance e reduzir a carga no servidor fonte.

**Principais comandos:**

- `/start` - Inicia o bot e mostra boas-vindas
- `/hoje` - Mostra o cardápio do dia atual
- `/semana` - Mostra o cardápio completo da semana
- `/ajuda` - Exibe os comandos disponíveis

## Tecnologias Utilizadas

- **[Java 24](https://openjdk.org/projects/jdk/24/)** - Linguagem de programação principal
- **[Spring Boot](https://spring.io/projects/spring-boot)** - Framework para aplicação Java
- **[TelegramBots API](https://github.com/rubenlagus/TelegramBots)** - Integração com a API do Telegram
- **[JSoup](https://jsoup.org/)** - Biblioteca para web scraping
- **[Maven](https://maven.apache.org/)** - Gerenciamento de dependências e build
- **[Docker](https://www.docker.com/)** - Containerização da aplicação
- **[Google Cloud Run](https://cloud.google.com/run)** - Plataforma de deploy (produção)

## Como Executar Localmente

### Pré-requisitos

- Java 24 ou superior
- Maven 3.9+
- Conta no Telegram e token de bot ([como obter](https://core.telegram.org/bots#how-do-i-create-a-bot))

### Passos para execução

1. **Clone o repositório**

```bash
git clone https://github.com/carloscardoso05/rubot.git
cd rubot
```

2. **Configure as variáveis de ambiente**

```bash
cp .env.example .env
# Edite o arquivo .env e adicione seu token do bot:
# BOT_KEY=SEU_TOKEN_AQUI
```

3. **Execute com Maven**

```bash
./mvnw spring-boot:run
```

4. **Ou usando Docker**

```bash
docker compose up --build
```

## Deploy

O bot está em produção no **Google Cloud Run** e pode ser acessado através do Telegram:

[🤖 Comece a usar o RUBot agora!](https://t.me/rub_pa_bot)

## Autor

**Carlos Cardoso** - Desenvolvedor Backend Java

[![GitHub](https://img.shields.io/badge/GitHub-carloscardoso05-black.svg)](https://github.com/carloscardoso05)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Carlos%20Cardoso-blue.svg)](https://www.linkedin.com/in/carloscardoso05/)

**Repositório**: [https://github.com/carloscardoso05/rubot](https://github.com/carloscardoso05/rubot)

---

Este projeto demonstra habilidades em:

- Desenvolvimento de bots para Telegram
- Web scraping com Jsoup
- Gerenciamento de cache
- Containerização com Docker
- Deploy em cloud (Google Cloud Run)
- Boas práticas de desenvolvimento Java/Spring Boot

package io.github.carloscardoso05.rubot.services;

public class RUSiteConnectionException extends RuntimeException {
  public RUSiteConnectionException(Throwable cause) {
    super("Não foi possível conectar ao site do RU", cause);
  }
}

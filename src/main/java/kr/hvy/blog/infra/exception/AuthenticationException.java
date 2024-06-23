package kr.hvy.blog.infra.exception;

import java.io.Serial;

public class AuthenticationException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 8507023464710979100L;

  public AuthenticationException(String message, Throwable cause) {
    super(message, cause);
  }

  public AuthenticationException(String message) {
    super(message);
  }
}

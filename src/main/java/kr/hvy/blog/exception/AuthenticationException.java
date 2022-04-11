package kr.hvy.blog.exception;

public class AuthenticationException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8507023464710979100L;

	public AuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthenticationException(String message) {
		super(message);
	}
}

package com.github.gabrielmoreira.propriete;

public class ProprieteException extends RuntimeException {

	private static final long serialVersionUID = 8640634619988032292L;

	public ProprieteException() {
		super();
	}

	public ProprieteException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProprieteException(String message) {
		super(message);
	}

	public ProprieteException(Throwable cause) {
		super(cause);
	}

}

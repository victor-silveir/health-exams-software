package br.com.victor.health.exams.software.services.exceptions;

public class NotEnoughtPixeonCoinsException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public NotEnoughtPixeonCoinsException(String msg) {
		super(msg);
	}
	
	public NotEnoughtPixeonCoinsException(String msg, Throwable cause) {
		super(msg, cause);
	}


}

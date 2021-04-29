package br.com.victor.health.exams.software.services.exceptions;

public class JsonParserError extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public JsonParserError(String msg) {
		super(msg);
	}
	
	public JsonParserError(String msg, Throwable cause) {
		super(msg, cause);
	}


}

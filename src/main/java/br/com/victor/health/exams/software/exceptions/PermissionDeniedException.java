package br.com.victor.health.exams.software.exceptions;

public class PermissionDeniedException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public PermissionDeniedException(String msg) {
		super(msg);
	}
	
	public PermissionDeniedException(String msg, Throwable cause) {
		super(msg, cause);
	}


}

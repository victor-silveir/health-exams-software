package br.com.victor.health.exams.software.services.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {
	private static final long serialVersionUID = 1L;

	private List<String> errors = new ArrayList<String>();

	public ValidationError(Integer status, String msg, String timestamp) {
		super(status, msg, timestamp);

	}

	public List<String> getErrors() {
		return errors;
	}

	public void addError(String messages) {
		errors.add(messages);
	}

}

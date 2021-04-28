package br.com.victor.health.exams.software.services.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {
	private static final long serialVersionUID = 1L;

	private List<FieldMessage> errors = new ArrayList<FieldMessage>();

	public ValidationError(Integer status, String msg, String timestamp) {
		super(status, msg, timestamp);

	}

	public List<FieldMessage> getErrors() {
		return errors;
	}

	public void addError(String fieldName, String messages) {
		errors.add(new FieldMessage(fieldName, messages));
	}

}

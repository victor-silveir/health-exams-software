package br.com.victor.health.exams.software.controllers.exceptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.victor.health.exams.software.services.exceptions.NotEnoughtPixeonCoinsException;
import br.com.victor.health.exams.software.services.exceptions.ObjectAlreadySavedException;
import br.com.victor.health.exams.software.services.exceptions.ObjectNotFoundException;
import br.com.victor.health.exams.software.services.exceptions.PermissionDeniedException;
import br.com.victor.health.exams.software.services.exceptions.StandardError;
import br.com.victor.health.exams.software.services.exceptions.ValidationError;

@ControllerAdvice
public class ExceptionsHandler {
	
	DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
	
	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest req) {
		
		StandardError error = new StandardError(HttpStatus.NOT_FOUND.value(), e.getMessage(), df.format(new Date()));
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	@ExceptionHandler(ObjectAlreadySavedException.class)
	public ResponseEntity<StandardError> objectAlreadySaved(ObjectAlreadySavedException e, HttpServletRequest req) {
		
		StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), df.format(new Date()));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	@ExceptionHandler(NotEnoughtPixeonCoinsException.class)
	public ResponseEntity<StandardError> notEnoughtPixeonCoins(NotEnoughtPixeonCoinsException e, HttpServletRequest req) {
		
		StandardError error = new StandardError(HttpStatus.FORBIDDEN.value(), e.getMessage(), df.format(new Date()));
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
	}
	
	@ExceptionHandler(PermissionDeniedException.class)
	public ResponseEntity<StandardError> permissionDenied(PermissionDeniedException e, HttpServletRequest req) {
		
		StandardError error = new StandardError(HttpStatus.FORBIDDEN.value(), e.getMessage(), df.format(new Date()));
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> validation(MethodArgumentNotValidException e, HttpServletRequest req) {
		
		ValidationError erro = new ValidationError(HttpStatus.BAD_REQUEST.value(), "Erro de Validação", df.format(new Date()));
		
		for (FieldError error : e.getBindingResult().getFieldErrors()) {
			erro.addError(error.getField(), error.getDefaultMessage());
		}
			
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
	}
		
}

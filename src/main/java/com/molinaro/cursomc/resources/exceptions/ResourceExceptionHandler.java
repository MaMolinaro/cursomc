package com.molinaro.cursomc.resources.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.molinaro.cursomc.services.exceptions.AuthorizationException;
import com.molinaro.cursomc.services.exceptions.DataIntegrityException;
import com.molinaro.cursomc.services.exceptions.FileException;
import com.molinaro.cursomc.services.exceptions.ObjectNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {
	
		StandardError err = new StandardError(
				System.currentTimeMillis(), 
				HttpStatus.NOT_FOUND.value(),
				"Not Found",
				e.getMessage(),
				request.getRequestURI());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
	
	@ExceptionHandler(DataIntegrityException.class)
	public ResponseEntity<StandardError> dataIntegrity(DataIntegrityException e, HttpServletRequest request) {
	
		StandardError err = new StandardError(
				System.currentTimeMillis(), 
				HttpStatus.BAD_REQUEST.value(),
				"Data Integrity",
				e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {
	
		ValidationError err = new ValidationError(
				System.currentTimeMillis(), 
				HttpStatus.UNPROCESSABLE_ENTITY.value(),
				"Validation Error",
				e.getMessage(),
				request.getRequestURI());

		for (FieldError x : e.getBindingResult().getFieldErrors()) {
			err.addError(x.getField(), x.getDefaultMessage());
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	@ExceptionHandler(AuthorizationException.class)
	public ResponseEntity<StandardError> authorization(AuthorizationException e, HttpServletRequest request) {
	
		StandardError err = new StandardError(
				System.currentTimeMillis(), 
				HttpStatus.FORBIDDEN.value(),
				"Access Denied",
				e.getMessage(),
				request.getRequestURI());

		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
	}

	@ExceptionHandler(FileException.class)
	public ResponseEntity<StandardError> authorization(FileException e, HttpServletRequest request) {

		StandardError err = new StandardError(
				System.currentTimeMillis(), 
				HttpStatus.BAD_REQUEST.value(),
				"File Error",
				e.getMessage(),
				request.getRequestURI());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	@ExceptionHandler(AmazonServiceException.class)
	public ResponseEntity<StandardError> amazonService(AmazonServiceException e, HttpServletRequest request) {
	
		HttpStatus code = HttpStatus.valueOf(e.getStatusCode());
		
		StandardError err = new StandardError(
				System.currentTimeMillis(), 
				code.value(),
				"Amazon Service Error",
				e.getMessage(),
				request.getRequestURI());

		return ResponseEntity.status(code).body(err);
	}
	
	@ExceptionHandler(AmazonClientException.class)
	public ResponseEntity<StandardError> amazonClient(AmazonClientException e, HttpServletRequest request) {

		StandardError err = new StandardError(
				System.currentTimeMillis(), 
				HttpStatus.BAD_REQUEST.value(),
				"Amazon Client Error",
				e.getMessage(),
				request.getRequestURI());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}

	@ExceptionHandler(AmazonS3Exception.class)
	public ResponseEntity<StandardError> amazonS3(AmazonS3Exception e, HttpServletRequest request) {
	
		StandardError err = new StandardError(
				System.currentTimeMillis(), 
				HttpStatus.BAD_REQUEST.value(),
				"Amazon S3 Error",
				e.getMessage(),
				request.getRequestURI());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
}

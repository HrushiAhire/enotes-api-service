package com.enotes.exceptions;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler 
{
//	@ExceptionHandler(Exception.class)
//	public ResponseEntity<?> handleException(Exception ex)
//	{
//		return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//	}
	
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<?> handleNPE(Exception ex)
	{
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(Exception ex)
	{
		log.error("Controller :: getCategoryById ::", ex.getMessage());
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<?> handleValidationException(ValidationException ex)
	{
		return new ResponseEntity<>(ex.getErrors(), HttpStatus.BAD_REQUEST);
	}
	
//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex)
//	{
//		List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
//		
//		Map<String, Object> error = new LinkedHashMap<>();
//		
//		allErrors.forEach(er ->{ 
//		String message = er.getDefaultMessage();
//		String field = ((FieldError)(er)).getField();
//		error.put(field, message);
//		});
//		
//		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//	}
	
}

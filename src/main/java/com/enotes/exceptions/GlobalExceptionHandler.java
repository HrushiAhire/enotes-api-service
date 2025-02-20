package com.enotes.exceptions;

import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.enotes.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler 
{	
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<?> handleNPE(Exception ex)
	{
		return CommonUtil.createErrorResponseMessage(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(Exception ex)
	{
		return CommonUtil.createErrorResponseMessage(ex.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<?> handleValidationException(ValidationException ex)
	{
		return CommonUtil.createErrorResponse(ex.getErrors(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ExistDataException.class)
	public ResponseEntity<?> handleDataExistenceException(ExistDataException ex)
	{
		return CommonUtil.createErrorResponseMessage(ex.getMessage(), HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex)
	{
		return CommonUtil.createErrorResponseMessage(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(FileNotFoundException.class)
	public ResponseEntity<?> handleFileNotFoundException(FileNotFoundException ex)
	{
		return CommonUtil.createErrorResponseMessage(ex.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	
}

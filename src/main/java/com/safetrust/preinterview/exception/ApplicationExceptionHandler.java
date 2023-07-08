package com.safetrust.preinterview.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationExceptionHandler {
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException ex){
		Map<String, String> errorMap = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error ->{
			errorMap.put(error.getField(), error.getDefaultMessage());
		});
		System.err.println("!!!!!!!!!!!!!!==> Handle error: " + ex.getMessage());
		return errorMap;
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex){
		LocalDateTime curDate = LocalDateTime.now();
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		ErrorDetail errorDetail = new ErrorDetail(dateFormat.format(curDate),ex.getMessage());
		System.err.println("!!!!!!!!!!!!!!==> Handle error: " + ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetail);
	}
	
	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<?> handleServiceException(ServiceException ex){
		LocalDateTime curDate = LocalDateTime.now();
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		ErrorDetail errorDetail = new ErrorDetail(dateFormat.format(curDate),ex.getMessage());
		System.err.println("!!!!!!!!!!!!!!==> Handle error: " + ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetail);
	}
}

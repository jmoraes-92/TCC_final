package com.orcamentos.kaspper.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
		Map<String, String> errorResponse = new HashMap<>();
		errorResponse.put("error", "Recurso não encontrado");
		errorResponse.put("message", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
		Map<String, Object> response = new HashMap<>();
		response.put("error", "Argumento inválido");
		response.put("message", ex.getMessage());
		response.put("timestamp", LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(
			DataIntegrityViolationException ex) {
		Map<String, String> errorResponse = new HashMap<>();
		errorResponse.put("error", "Violação de integridade referencial");
		errorResponse.put("message", "Não é possível excluir a demanda porque há tarefas associadas.");
		return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
	}

}

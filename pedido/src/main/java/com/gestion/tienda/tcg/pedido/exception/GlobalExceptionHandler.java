package com.gestion.tienda.tcg.pedido.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(PedidoNotFoundException.class)
        public ResponseEntity<Object> handleNotFound(PedidoNotFoundException ex) {
                return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        }

        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<Object> handleBadRequest(BadRequestException ex) {
                return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        private ResponseEntity<Object> buildResponse(HttpStatus status, String message) {
                Map<String, Object> body = new LinkedHashMap<>();
                body.put("timestamp", LocalDateTime.now());
                body.put("status", status.value());
                body.put("error", status.getReasonPhrase());
                body.put("message", message);
                return new ResponseEntity<>(body, status);
        }
}
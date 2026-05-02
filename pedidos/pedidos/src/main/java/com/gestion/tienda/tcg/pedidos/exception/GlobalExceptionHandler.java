package com.gestion.tienda.tcg.pedidos.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // PEDIDO NO ENCONTRADO
    @ExceptionHandler(PedidoNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePedidoNotFound(
            PedidoNotFoundException ex) {

        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // DETALLE NO ENCONTRADO
    @ExceptionHandler(DetallePedidoNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleDetalleNotFound(
            DetallePedidoNotFoundException ex) {

        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // ENVIO NO ENCONTRADO
    @ExceptionHandler(EnvioNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEnvioNotFound(
            EnvioNotFoundException ex) {

        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // BAD REQUEST
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(
            BadRequestException ex) {

        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // ERROR GENERAL
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(
            Exception ex) {

        log.error("Error interno: {}", ex.getMessage());

        return buildResponse(
                "Error interno del servidor",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // METODO GENERAL
    private ResponseEntity<Map<String, Object>> buildResponse(
            String mensaje,
            HttpStatus status) {

        Map<String, Object> body = new HashMap<>();

        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", mensaje);

        return new ResponseEntity<>(body, status);
    }
}
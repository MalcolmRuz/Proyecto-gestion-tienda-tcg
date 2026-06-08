package com.gestion.tienda.tcg.registro.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j

public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> recursoNoEncontrado(RecursoNoEncontradoException ex){

        log.error("Recurso no Encontrado, manejo error (404) {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND,
                        "Recurso no encontrado",
                        ex.getMessage()
                )
        );

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> errorGenerico(Exception ex){

        log.error("Error no controlado en GlobalExceptionHandler ", ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Error interno del servidor",
                        ex.getMessage()
                )
        );

    }

}
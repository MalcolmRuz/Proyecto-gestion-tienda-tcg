package com.gestion_tienda_tcg.productos.exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductoInvalidoException.class)
    public ResponseEntity<ErrorResponse> manejarProductoInvalido(ProductoInvalidoException ex) {
        log.error("Respondiendo con 400 Bad Request: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),

                HttpStatus.BAD_REQUEST.value(),
                "BAD REQUEST",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    @ExceptionHandler(CategoriaInvalidaException.class)
    public ResponseEntity<ErrorResponse> manejarCategoriaInvalida(ProductoInvalidoException ex) {
        log.error("Respondiendo con 400 Bad Request: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),

                HttpStatus.BAD_REQUEST.value(),
                "BAD REQUEST",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarErroresGenerales(Exception ex) {
        log.error("Error no controlado: ", ex);

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),

                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "ERROR SERVIDOR",
                "Ocurrió un error inesperado"

        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
package com.gestion_tienda_tcg.productos.exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

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
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarValidaciones(MethodArgumentNotValidException ex) {
        log.warn("Error de validación en request: {} campo(s) inválido(s)",
                ex.getBindingResult().getFieldErrorCount());

        // Junta todos los errores de campos en un solo string
        String mensajes = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "BAD REQUEST",
                mensajes
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
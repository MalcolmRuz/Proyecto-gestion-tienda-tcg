package com.gestion.tienda.tcg.registro.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

// DTO utilizado para retornar información
// detallada de errores en la API
@Getter
@AllArgsConstructor

public class ErrorResponse{

    private LocalDateTime timestamp;
    private HttpStatus status;
    private String error;
    private String message;

}

package com.gestion.tienda.tcg.pago.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor

public class ErrorResponse{

    private LocalDateTime timestamp;
    private HttpStatus status;
    private String error;
    private String message;

}


package com.gestion_tienda_tcg.productos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProveedorNoEncontradoException extends RuntimeException {
    public ProveedorNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}

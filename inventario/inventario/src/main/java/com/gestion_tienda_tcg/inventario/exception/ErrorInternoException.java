package com.gestion_tienda_tcg.inventario.exception;

public class ErrorInternoException  extends RuntimeException{
    public ErrorInternoException(String mensaje) {
        super(mensaje);
    }
}

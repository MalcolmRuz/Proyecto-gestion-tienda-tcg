package com.gestion_tienda_tcg.inventario.exception;

public class InventarioInvalidoException extends RuntimeException {

    public InventarioInvalidoException(String mensaje){
    super (mensaje);
    }
}

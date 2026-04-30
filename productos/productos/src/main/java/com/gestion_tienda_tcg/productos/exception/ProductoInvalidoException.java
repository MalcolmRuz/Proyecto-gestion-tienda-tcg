package com.gestion_tienda_tcg.productos.exception;

public class ProductoInvalidoException extends  RuntimeException {
    public ProductoInvalidoException(String mensaje){
            super (mensaje);
    }
}

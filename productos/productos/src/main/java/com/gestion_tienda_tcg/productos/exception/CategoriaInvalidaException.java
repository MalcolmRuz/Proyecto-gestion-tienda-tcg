package com.gestion_tienda_tcg.productos.exception;

public class CategoriaInvalidaException extends  RuntimeException{
    public CategoriaInvalidaException(String mensaje){
        super(mensaje);
    }
}

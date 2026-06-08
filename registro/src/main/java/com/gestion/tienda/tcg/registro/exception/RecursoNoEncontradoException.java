package com.gestion.tienda.tcg.registro.exception;

// Excepción personalizada utilizada cuando
// un recurso no existe en la base de datos.

public class RecursoNoEncontradoException extends RuntimeException{

    public RecursoNoEncontradoException(String mensaje){

        // Envía mensaje personalizado al RuntimeException
        super(mensaje);

    }
}

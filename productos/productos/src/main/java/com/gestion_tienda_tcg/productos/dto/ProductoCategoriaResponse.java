package com.gestion_tienda_tcg.productos.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProductoCategoriaResponse {
private final Long idProductoCategoria;
private final Long idProducto;
private final String nombreProducto;
private final Long idCategoria;
private final String nombreCategoria;
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
private final LocalDateTime fechaAsignacion;



}

package com.gestion.tienda.tcg.pedido.dto;

import java.util.List;

import lombok.Data;

@Data
public class CarritoDto {
    private Long id;
    private String estadoCarrito;
    private Double totalCarrito;
    private List<ItemDto> items;

    @Data
    public static class ItemDto {
        private Long productoId;
        private String descripcionProducto;
        private Integer cantidad;
        private Double precioUnitario;
    }
}
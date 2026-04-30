package com.gestion_tienda_tcg.productos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PRODUCTO_CATEGORIA")
public class ProductoCategoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProductoCategoria;
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto productoId;
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoriaId;

}

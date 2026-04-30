package com.gestion_tienda_tcg.productos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private Producto producto;
    @Column(name = "nombre_producto")
    private String nombreProducto;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @Column(name = "nombre_categoria")
    private String nombreCategoria;

    @Column(name = "fecha_asignacion")
    private LocalDateTime fechaAsignacion;

    @PrePersist
    protected void onCreate() {
        this.fechaAsignacion = LocalDateTime.now();
    }

}

package com.gestion_tienda_tcg.productos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;

    @Column(name = "NOMBRE_PRODUCTO")
    private String nombreProducto;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Column(name = "ESTADO_ACTIVO")
    private boolean estadoActivo;

    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

    @Column
    private Double precioUnitario;

}




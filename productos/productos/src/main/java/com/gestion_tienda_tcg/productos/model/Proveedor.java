package com.gestion_tienda_tcg.productos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Proveedor")
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProveedor;

    @Column(name = "NOMBRE PROVEEDOR")
    private String nombreProveedor;

    @Column(name = "contacto")
    private String contactoProveedor;
}
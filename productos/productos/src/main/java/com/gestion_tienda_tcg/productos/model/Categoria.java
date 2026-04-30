package com.gestion_tienda_tcg.productos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CATEGORIA")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategoria;

    @Column(name = "NOMBRE TCG")
    private String categoriaTcg;

    @Column(name = "TIPO PRODUCTO")
    private String tipoProducto;
}

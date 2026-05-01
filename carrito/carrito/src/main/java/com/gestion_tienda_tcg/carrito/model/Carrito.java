package com.gestion_tienda_tcg.carrito.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "carrito")
public class Carrito {

    @Id
    private Long idCarrito;

}

package com.gestion.tienda.tcg.registro.model;

import com.gestion.tienda.tcg.registro.enums.NombreRol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Define esta clase como Entidad asociada a la tabla rol
@Entity
@Table(name = "rol")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRol;

    //Nombre del rol selecionado de enums/NombreRol
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private NombreRol nombreRol; // ADMIN/CLIENTE
}

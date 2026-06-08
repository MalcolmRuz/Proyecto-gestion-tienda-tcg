package com.gestion.tienda.tcg.registro.repository;

import com.gestion.tienda.tcg.registro.enums.NombreRol;
import com.gestion.tienda.tcg.registro.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Repositorio encargado de acceder y manipular
// datos de la entidad Rol

public interface RolRepository extends JpaRepository<Rol,Long> {

    //Busca un rol mediante su nombre
    Optional<Rol>findByNombreRol(NombreRol nombreRol);

}


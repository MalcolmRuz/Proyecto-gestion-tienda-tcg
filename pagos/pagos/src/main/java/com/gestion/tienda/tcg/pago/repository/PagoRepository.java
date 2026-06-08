package com.gestion.tienda.tcg.pago.repository;

import com.gestion.tienda.tcg.pago.enums.EstadoPago;
import com.gestion.tienda.tcg.pago.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByIdCarrito(Long idCarrito);

    List<Pago> findByEstado(EstadoPago estado);

    boolean existsByIdCarritoAndEstado(Long idCarrito, EstadoPago estado);
}

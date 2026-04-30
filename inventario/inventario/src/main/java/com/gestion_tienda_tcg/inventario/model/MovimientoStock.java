package com.gestion_tienda_tcg.inventario.model;

import com.gestion_tienda_tcg.inventario.enums.TipoMovimiento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovimientoStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long idMovimientoStock;

    @Column(name = "cantidad_movimiento",nullable = false)
    private Integer cantidadMovimiento;

    @ManyToOne
    @JoinColumn(name = "id_inventario",nullable = false)
    private Inventario inventario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento",nullable = false)
    private TipoMovimiento tipo;

    @Column(name = "fecha_movimiento",nullable = false)
    private LocalDateTime fechaMovimiento;

    @PrePersist
    protected void onCreate() {
        this.fechaMovimiento = LocalDateTime.now();
    }

}

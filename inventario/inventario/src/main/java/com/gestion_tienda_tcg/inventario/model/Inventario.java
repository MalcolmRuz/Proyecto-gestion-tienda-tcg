package com.gestion_tienda_tcg.inventario.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventario {
   @Id
   @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long idInventario;
   @Column(name = "stock_actual", nullable = false)
   private int stockActual;
   @Column(name = "fecha_inventario")
    private LocalDate fechaInventario;

   //@OneToMany(mappedBy = "inventario",cascade = CascadeType.ALL)
   //private List<MovimientoStock> movimientos;
    //por implementar

   //inicializa fecha
    @PrePersist
    protected  void onCreate(){
        fechaInventario = LocalDate.now();
   }

}

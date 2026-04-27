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
   @Column(name = "id_producto")
   private Long idProducto;
   @Column(name = "stock_actual", nullable = false)
   private int stockActual;
   @Column(name = "fecha_inventario")
    private LocalDate fechaInventario;

   //inicializa fecha
    @PrePersist
    protected  void onCreate(){
        fechaInventario = LocalDate.now();
   }

}

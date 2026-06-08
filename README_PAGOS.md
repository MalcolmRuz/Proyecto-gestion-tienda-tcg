
API - [PAGOS]

Este módulo gestiona el registro y procesamiento de pagos asociados a pedidos, validando montos, estado del pedido y comunicándose con el microservicio de pedidos.

## Tecnologías
* Java 17
* Spring Boot (Web, Data JPA)
* Spring Cloud OpenFeign
* Oracle SQL

---

##  Endpoints principales

POST /api/v1/pagos  
→ Realiza un pago asociado a un pedido
- Obtiene pedido desde microservicio de pedidos (Feign)
- Valida existencia del pedido
- Evita doble pago (APROBADO)
- Si monto < total → RECHAZADO y pedido vuelve a PENDIENTE_PAGO
- Si monto ≥ total → APROBADO
- Si hay exceso → calcula vuelto
- Actualiza estado del pedido (PAGADO o PENDIENTE_PAGO)

---

GET /api/v1/pagos  
→ Lista todos los pagos registrados

---

## Estados de pago

- PENDIENTE → pago creado pero no procesado
- APROBADO → pago exitoso
- RECHAZADO → monto insuficiente
- CANCELADO → anulación manual

---

## Reglas de negocio

- Un pedido solo puede tener un pago APROBADO
- El pago depende del total del pedido (microservicio externo)
- Si el monto es menor al total → RECHAZADO
- Si el monto es mayor → se genera vuelto
- Pago exitoso → pedido cambia a PAGADO
- Pago rechazado → pedido vuelve a PENDIENTE_PAGO  
```
   ____     ____      _   _   ____    U  ___ u             U  ___ u  __  __     ____   
U /"___|uU |  _"\ uU |"|u| |U|  _"\ u  \/"_ \/              \/"_ \/U|' \/ '|uU /"___|u 
\| |  _ / \| |_) |/ \| |\| |\| |_) |/  | | | |     U  u     | | | |\| |\/| |/\| |  _ / 
 | |_| |   |  _ <    | |_| | |  __/.-,_| |_| |     /___\.-,_| |_| | | |  | |  | |_| |  
  \____|   |_| \_\  <<\___/  |_|    \_)-\___/     |__"__|\_)-\___/  |_|  |_|   \____|  
  _)(|_    //   \\_(__) )(   ||>>_       \\                   \\   <<,-,,-.    _)(|_   
 (__)__)  (__)  (__)   (__) (__)__)     (__)                 (__)   (./  \.)  (__)__)  
```
**💳 API - [Pagos]**

Procesa y registra los pagos asociados a pedidos. Valida el monto, aplica reglas de negocio (vuelto, rechazo por monto insuficiente) y actualiza el estado del pedido en consecuencia.

---

## Tecnologías

- Java 17
- Spring Boot (Web, Data JPA)
- Spring Cloud OpenFeign
- Oracle SQL
- Spring Cloud Eureka Client

---

## Configuración

| Propiedad | Valor |
|---|---|
| Nombre en Eureka | `pago` |
| Puerto | `8081` |
| Eureka | `http://localhost:8761/eureka/` |

---

## Dependencias con otros microservicios

| Microservicio | Uso |
|---|---|
| `PEDIDO-SERVICE` | Obtiene el total del pedido y actualiza su estado tras el pago |

La comunicación se realiza mediante **Feign** con propagación del token JWT.

---

## Endpoints

Base URL: `http://localhost:8081/api/v1`

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/pagos` | Realizar un pago asociado a un pedido |
| `GET` | `/pagos` | Listar todos los pagos registrados |

#### Body: realizar un pago
```json
{
  "idPedido": 3,
  "monto": 25000
}
```

#### Lógica del pago
- Si el pedido ya tiene un pago `APROBADO` → se rechaza (no se puede pagar dos veces).
- Si el monto es **menor** al total del pedido → pago `RECHAZADO`, pedido vuelve a `PENDIENTE_PAGO`.
- Si el monto es **igual o mayor** al total → pago `APROBADO`, pedido pasa a `PAGADO`.
- Si hay exceso de monto → se calcula y registra el vuelto.

---

## Estados del pago

| Estado | Descripción |
|---|---|
| `PENDIENTE` | Pago creado pero aún no procesado |
| `APROBADO` | Pago exitoso |
| `RECHAZADO` | Monto insuficiente |
| `CANCELADO` | Anulado manualmente |

---

## Swagger UI

Con el servicio corriendo, puedes explorar y probar los endpoints en:

```
http://localhost:8081/swagger-ui.html
```

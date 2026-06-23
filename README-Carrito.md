```
   ____     ____      _   _   ____    U  ___ u             U  ___ u  __  __     ____   
U /"___|uU |  _"\ uU |"|u| |U|  _"\ u  \/"_ \/              \/"_ \/U|' \/ '|uU /"___|u 
\| |  _ / \| |_) |/ \| |\| |\| |_) |/  | | | |     U  u     | | | |\| |\/| |/\| |  _ / 
 | |_| |   |  _ <    | |_| | |  __/.-,_| |_| |     /___\.-,_| |_| | | |  | |  | |_| |  
  \____|   |_| \_\  <<\___/  |_|    \_)-\___/     |__"__|\_)-\___/  |_|  |_|   \____|  
  _)(|_    //   \\_(__) )(   ||>>_       \\                   \\   <<,-,,-.    _)(|_   
 (__)__)  (__)  (__)   (__) (__)__)     (__)                 (__)   (./  \.)  (__)__)  
```
**🛒 API - [Carrito]**

Gestiona el carrito de compras de los usuarios. Permite agregar o quitar productos, verificar stock en tiempo real y procesar el pago, generando automáticamente el pedido al confirmar.

---

## Tecnologías

- Java 17
- Spring Boot 3.5.14 (Web, Data JPA, Security)
- JWT
- Spring Cloud OpenFeign
- Spring Cloud Netflix Eureka Client
- MySQL
- Lombok
- Maven

---

## Configuración

| Propiedad | Valor |
|---|---|
| Nombre en Eureka | `CARRITO` |
| Puerto | `8083` |
| Base de datos | MySQL (`carrito`) |
| Eureka | `http://localhost:8761/eureka/` |

---

## Dependencias con otros microservicios

| Microservicio | Uso |
|---|---|
| `INVENTARIO-SERVICE` | Consultar stock disponible y descontar unidades al pagar |
| `PRODUCTO-SERVICE` | Obtener precio y descripción del producto al agregar un item |
| `PEDIDO-SERVICE` | Crear el pedido automáticamente tras confirmar el pago |

La comunicación entre servicios se realiza mediante **Feign** con propagación del token JWT.

---

## Autenticación

Todos los endpoints requieren token JWT en el header:
```
Authorization: Bearer <token>
```

---

## Estados del carrito

```
ACTIVO ──► PAGADO
ACTIVO ──► CANCELADO
ACTIVO ──► PENDIENTE   (stock insuficiente al intentar pagar)
PENDIENTE ──► ACTIVO   (reactivación manual)
PENDIENTE ──► CANCELADO
```

| Estado | Descripción |
|---|---|
| `ACTIVO` | Operativo, se pueden agregar y quitar items |
| `PENDIENTE` | Pago fallido por stock insuficiente, esperando reactivación |
| `PAGADO` | Pago confirmado, no se puede modificar |
| `CANCELADO` | Anulado manualmente |

---

## Endpoints

Base URL: `http://localhost:8083/api/v1`

### Carritos

| Método | Endpoint | Descripción | Body |
|---|---|---|---|
| `POST` | `/carritos` | Crear un nuevo carrito | `{}` |
| `GET` | `/carritos` | Listar todos los carritos | — |
| `GET` | `/carritos/{id}` | Obtener carrito por ID | — |
| `POST` | `/carritos/{id}/pagar` | Pagar el carrito y generar pedido | `PagarCarritoRequest` |
| `POST` | `/carritos/{id}/cancelar` | Cancelar el carrito | — |
| `POST` | `/carritos/{id}/reactivar` | Reactivar un carrito en estado PENDIENTE | — |
| `DELETE` | `/carritos/{id}` | Eliminar el carrito físicamente | — |

#### Body: `PagarCarritoRequest`
```json
{
  "usuarioId": 1,
  "direccionEnvio": "Av. Siempre Viva 742"
}
```

---

### Items del carrito

| Método | Endpoint | Descripción | Body |
|---|---|---|---|
| `POST` | `/carritos/{idCarrito}/items` | Agregar un producto al carrito | `CarritoItemRequest` |
| `GET` | `/carritos/{idCarrito}/items` | Listar todos los items del carrito | — |
| `GET` | `/carritos/{idCarrito}/items/{idItem}` | Ver un item específico | — |
| `PATCH` | `/carritos/items/{idItem}` | Actualizar cantidad de un item | `ActualizarCantidadRequest` |
| `DELETE` | `/carritos/items/{idItem}` | Eliminar un item del carrito | — |

#### Body: `CarritoItemRequest`
```json
{
  "productoId": 5,
  "cantidad": 2
}
```

#### Body: `ActualizarCantidadRequest`
```json
{
  "cantidad": 3
}
```

---

### Historial del carrito

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/carritos/historial/{idCarrito}` | Ver el historial de cambios de un carrito |

---

## Flujo de pago

1. El cliente llama a `POST /carritos/{id}/pagar` con `usuarioId` y `direccionEnvio`.
2. Se valida que el carrito esté `ACTIVO` y tenga items.
3. Se consulta el stock de cada item en `INVENTARIO-SERVICE`.
   - Si algún item no tiene stock suficiente → carrito pasa a `PENDIENTE` y se lanza error.
4. Se descuenta el stock de todos los items.
5. El carrito pasa a estado `PAGADO`.
6. Se crea automáticamente el pedido en `PEDIDO-SERVICE`.
   - Si la creación del pedido falla, el carrito igual queda `PAGADO` y se registra una advertencia en el historial.

---

## Swagger UI

Con el servicio corriendo, puedes explorar y probar los endpoints en:

```
http://localhost:8083/swagger-ui.html
```

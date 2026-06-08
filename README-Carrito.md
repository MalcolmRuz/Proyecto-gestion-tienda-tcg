# Microservicio Carrito

Microservicio encargado de gestionar el carrito de compras de los usuarios dentro del sistema de gestión de tienda TCG. Permite crear y administrar carritos, agregar o quitar productos, controlar el stock con el microservicio de inventario y procesar el pago, generando automáticamente el pedido correspondiente.

---

## Tecnologías

- Java 17
- Spring Boot 3.5.14
- Spring Data JPA
- Spring Security + JWT
- Spring Cloud OpenFeign
- Spring Cloud Netflix Eureka Client
- Oracle Database
- Lombok
- Maven

---

## Configuración

| Propiedad | Valor |
|---|---|
| Nombre en Eureka | `carrito` |
| Puerto | `8083` |
| Base de datos | Oracle (carritobd_low) |

El servicio se registra automáticamente en Eureka al iniciar. Requiere que el servidor Eureka esté corriendo en `http://localhost:8761`.

---

## Dependencias con otros microservicios

| Microservicio | Uso |
|---|---|
| `INVENTARIO-SERVICE` | Consultar stock y descontar unidades al pagar |
| `PRODUCTO-SERVICE` | Obtener precio y descripción del producto al agregar un item |
| `PEDIDO-SERVICE` | Crear el pedido automáticamente tras confirmar el pago |

La comunicación entre servicios se realiza mediante **Feign** con propagación del token JWT a través de `FeingInterceptor`.

---

## Estados del carrito

```
ACTIVO ──► PAGADO
ACTIVO ──► CANCELADO
ACTIVO ──► PENDIENTE  (stock insuficiente al intentar pagar)
PENDIENTE ──► ACTIVO  (reactivación manual)
PENDIENTE ──► CANCELADO
```

| Estado | Descripción |
|---|---|
| `ACTIVO` | Carrito operativo, se pueden agregar y quitar items |
| `PENDIENTE` | Pago fallido por stock insuficiente, a la espera de reactivación |
| `PAGADO` | Pago confirmado, no se puede modificar ni eliminar |
| `CANCELADO` | Carrito anulado manualmente |

---

## Endpoints

Todos los endpoints requieren autenticación mediante token JWT en el header `Authorization: Bearer <token>`.

Base URL: `http://localhost:8083/api/v1`

### Carritos

| Método | Endpoint | Descripción | Body | Respuesta |
|---|---|---|---|---|
| `POST` | `/carritos` | Crear un nuevo carrito | `{}` (vacío) | `201 CarritoResponse` |
| `GET` | `/carritos` | Listar todos los carritos | — | `200 List<CarritoResponse>` |
| `GET` | `/carritos/{id}` | Obtener carrito por ID | — | `200 CarritoResponse` |
| `POST` | `/carritos/{id}/pagar` | Pagar el carrito y generar pedido | `PagarCarritoRequest` | `200 CarritoResponse` |
| `POST` | `/carritos/{id}/cancelar` | Cancelar el carrito | — | `200 CarritoResponse` |
| `POST` | `/carritos/{id}/reactivar` | Reactivar un carrito en estado PENDIENTE | — | `200 CarritoResponse` |
| `DELETE` | `/carritos/{id}` | Eliminar físicamente el carrito | — | `204 No Content` |

#### Body: `PagarCarritoRequest`
```json
{
  "usuarioId": 1,
  "direccionEnvio": "Av. Siempre Viva 742"
}
```

---

### Items del carrito

| Método | Endpoint | Descripción | Body | Respuesta |
|---|---|---|---|---|
| `POST` | `/carritos/{idCarrito}/items` | Agregar un producto al carrito | `CarritoItemRequest` | `200 CarritoItemResponse` |
| `GET` | `/carritos/{idCarrito}/items` | Listar todos los items del carrito | — | `200 List<CarritoItemResponse>` |
| `GET` | `/carritos/{idCarrito}/items/{idItem}` | Obtener un item específico del carrito | — | `200 CarritoItemResponse` |
| `PATCH` | `/carritos/items/{idItem}` | Actualizar la cantidad de un item | `ActualizarCantidadRequest` | `200 CarritoItemResponse` |
| `DELETE` | `/carritos/items/{idItem}` | Eliminar un item del carrito | — | `204 No Content` |

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

| Método | Endpoint | Descripción | Respuesta |
|---|---|---|---|
| `GET` | `/carritos/historial/{idCarrito}` | Obtener el historial de cambios de un carrito | `200 List<CarritoHistorialResponse>` |

---

## Flujo principal: pagar un carrito

1. El cliente llama a `POST /carritos/{id}/pagar` con `usuarioId` y `direccionEnvio`.
2. Se valida que el carrito esté en estado `ACTIVO` y tenga items.
3. Se consulta el stock de cada item en `INVENTARIO-SERVICE`.
   - Si algún item no tiene stock suficiente → el carrito pasa a `PENDIENTE` y se lanza error.
4. Se descuenta el stock de todos los items en `INVENTARIO-SERVICE`.
5. El carrito pasa a estado `PAGADO`.
6. Se llama automáticamente a `PEDIDO-SERVICE` para crear el pedido con los datos del carrito, `usuarioId` y `direccionEnvio`.
7. Si la creación del pedido falla, el carrito igual queda `PAGADO` y se registra una advertencia en el historial.

---

## Estructura del proyecto

```
carrito/
└── src/main/java/com/gestion/tienda/tcg/carrito/
    ├── CarritoApplication.java
    ├── Client/
    │   ├── InventarioClient.java
    │   ├── ProductoClient.java
    │   └── PedidoClient.java
    ├── config/
    │   └── FeingInterceptor.java
    ├── controller/
    │   ├── CarritoController.java
    │   ├── CarritoItemController.java
    │   └── CarritoHistorialController.java
    ├── dto/
    ├── enums/
    ├── exception/
    ├── mapper/
    ├── model/
    ├── repository/
    ├── security/
    └── service/
        ├── CarritoService.java
        ├── CarritoItemService.java
        └── CarritoHistorialService.java
```

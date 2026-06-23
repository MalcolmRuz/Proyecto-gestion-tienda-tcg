```
   ____     ____      _   _   ____    U  ___ u             U  ___ u  __  __     ____   
U /"___|uU |  _"\ uU |"|u| |U|  _"\ u  \/"_ \/              \/"_ \/U|' \/ '|uU /"___|u 
\| |  _ / \| |_) |/ \| |\| |\| |_) |/  | | | |     U  u     | | | |\| |\/| |/\| |  _ / 
 | |_| |   |  _ <    | |_| | |  __/.-,_| |_| |     /___\.-,_| |_| | | |  | |  | |_| |  
  \____|   |_| \_\  <<\___/  |_|    \_)-\___/     |__"__|\_)-\___/  |_|  |_|   \____|  
  _)(|_    //   \\_(__) )(   ||>>_       \\                   \\   <<,-,,-.    _)(|_   
 (__)__)  (__)  (__)   (__) (__)__)     (__)                 (__)   (./  \.)  (__)__)  
```
**📦 API - [Producto]**

Gestiona el catálogo de productos, categorías y proveedores de la tienda TCG. Al registrar un producto, se crea automáticamente un inventario en el microservicio de inventario.

---

## Tecnologías

- Java 17
- Spring Boot (Web, Data JPA)
- Oracle SQL
- Spring Cloud Eureka Client

---

## Configuración

| Propiedad | Valor |
|---|---|
| Nombre en Eureka | `PRODUCTO-SERVICE` |
| Puerto | `8086` |
| Eureka | `http://localhost:8761/eureka/` |

Asegúrate de tener estas propiedades en tu `application.properties` antes de iniciar el servicio.

---

## Dependencias con otros microservicios

| Microservicio | Uso |
|---|---|
| `INVENTARIO-SERVICE` | Se crea un inventario automáticamente al registrar un nuevo producto |

---

## ⚠️ Orden de registro importante

> Antes de crear un producto, **primero debes registrar un proveedor** en la base de datos.

---

## Endpoints

Base URL: `http://localhost:8086/api/v1`

### Productos

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/productos` | Crear un nuevo producto (también crea su inventario automáticamente) |
| `PUT` | `/productos/{id}` | Actualizar datos de un producto existente |
| `PATCH` | `/productos/{id}/desactivar` | Desactivar un producto (no se elimina, se conserva el historial) |
| `GET` | `/productos/proveedor/{idProveedor}` | Listar todos los productos de un proveedor |

#### Body: crear o actualizar producto
```json
{
  "nombre": "Mazo Pokémon",
  "precio": 15000,
  "idProveedor": 1
}
```

---

### Categorías

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/categoria` | Crear una nueva categoría (ej: "Singles", "Cajas", "Sobres") |
| `GET` | `/categoria` | Listar todas las categorías |
| `PUT` | `/categoria/{id}` | Editar una categoría existente |

---

### Relación Producto–Categoría

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/productoCategoria/asignar` | Asociar un producto con una categoría |
| `DELETE` | `/productoCategoria/quitar/{idRelacion}` | Quitar la asociación entre un producto y su categoría |
| `GET` | `/productoCategoria/categoria/{idCategoria}` | Listar todos los productos de una categoría |

#### Body: asignar categoría a producto
```json
{
  "idProducto": 3,
  "idCategoria": 2
}
```

---

### Proveedores

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/proveedor` | Registrar un nuevo proveedor |
| `GET` | `/proveedor` | Listar todos los proveedores |
| `PATCH` | `/proveedor/{id}/contacto?nuevoContacto=...` | Actualizar el dato de contacto de un proveedor |

#### Body: crear proveedor
```json
{
  "nombre": "Distribuidora TCG Chile",
  "contacto": "contacto@tcgchile.cl"
}
```

---

## Swagger UI

Con el servicio corriendo, puedes explorar y probar los endpoints en:

```
http://localhost:8086/swagger-ui.html
```

---



#
   ____     ____      _   _   ____    U  ___ u             U  ___ u  __  __     ____   
U /"___|uU |  _"\ uU |"|u| |U|  _"\ u  \/"_ \/              \/"_ \/U|' \/ '|uU /"___|u 
\| |  _ / \| |_) |/ \| |\| |\| |_) |/  | | | |     U  u     | | | |\| |\/| |/\| |  _ / 
 | |_| |   |  _ <    | |_| | |  __/.-,_| |_| |     /___\.-,_| |_| | | |  | |  | |_| |  
  \____|   |_| \_\  <<\___/  |_|    \_)-\___/     |__"__|\_)-\___/  |_|  |_|   \____|  
  _)(|_    //   \\_(__) )(   ||>>_       \\                   \\   <<,-,,-.    _)(|_   
 (__)__)  (__)  (__)   (__) (__)__)     (__)                 (__)   (./  \.)  (__)__)  
  API - [Productos]

Este módulo se encarga de manera independiente de toda la lógica relacionada con el manejo de stock y registro de movimientos en el sistema.

## 🛠️ Tecnologías Utilizadas
* **Lenguaje:** Java 17
* **Framework:** Spring Boot (Web, Data JPA)
* **Base de Datos:** SQL

---

##  Configuración y Variables de Entorno

Para que este servicio funcione correctamente, debes configurar las siguientes propiedades en tu archivo `application.properties`:

* `spring.application.name=PRODUCTO-SERVICE`
* `server.port=8086`
* `eureka.client.service-url.defaultZone=http://localhost:8761/eureka/`


---

## 🚀 Funciones principales
IMPORTANTE!!! PRIMERO SE DEBE REGISTRAR UN PROVEEDOR EN LA BASE DE DATOS ANTES DE INGRESAR UN PRODUCTO.
```bash
POST /api/v1/productos
```

Crea y guarda un nuevo producto en el sistema. Recibe un JSON con la información del producto. Esto CREA un inventario automaticamente en la API Inventario

```bash
PUT /api/v1/productos/{id}
```
Obtiene el detalle completo de un producto específico según su ID.

```bash
PATCH /api/v1/productos/{id}/desactivar
```

Desactiva un producto del sistema por su ID. No se elimina para conservar historial
```bash
GET /api/v1/productos/proveedor/{idProveedor}
```
Lista todos los productos que pertenecen a un proveedor específico según su ID.
```bash
POST /api/v1/categoria
```
Crea una nueva categoría para clasificar los productos (ej: "Singles","Cajas", "Sobres"). Recibe un JSON con los datos.
```bash
GET /api/v1/categoria
```
Lista todas las categorías que están registradas en el sistema.
```bash
PUT /api/v1/categoria/{id}
```
Edita el nombre o datos de una categoría existente mediante su ID.
```bash
POST /api/v1/productoCategoria/asignar
```
Asocia un producto con una categoría específica (ej: asignar el producto "Mazo Pokémon" a la categoría "Cartas"). Recibe un JSON con los IDs del producto y de la categoría.
```bash
DELETE /api/v1/productoCategoria/quitar/{idRelacion}
```
Elimina la asociación entre un producto y su categoría utilizando el ID de esa relación.
```bash
GET /api/v1/productoCategoria/categoria/{idCategoria}
```
Busca y lista todas las relaciones de productos que pertenecen a una categoría específica utilizando el ID de la categoría.
```bash
POST /api/v1/proveedor
```
Registra un nuevo proveedor en el sistema (ej: el distribuidor que les vende los productos de TCG). Recibe un JSON con la información.
```bash
GET /api/v1/proveedor
```
Lista todos los proveedores registrados en la base de datos.
```bash
PATCH /api/v1/proveedor/{id}/contacto
```
Actualiza de forma rápida el dato de contacto (teléfono o correo) de un proveedor específico mediante su ID. Envía el nuevo dato como un parámetro en la URL (?nuevoContacto=...).


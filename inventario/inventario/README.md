---



#
   ____     ____      _   _   ____    U  ___ u             U  ___ u  __  __     ____   
U /"___|uU |  _"\ uU |"|u| |U|  _"\ u  \/"_ \/              \/"_ \/U|' \/ '|uU /"___|u 
\| |  _ / \| |_) |/ \| |\| |\| |_) |/  | | | |     U  u     | | | |\| |\/| |/\| |  _ / 
 | |_| |   |  _ <    | |_| | |  __/.-,_| |_| |     /___\.-,_| |_| | | |  | |  | |_| |  
  \____|   |_| \_\  <<\___/  |_|    \_)-\___/     |__"__|\_)-\___/  |_|  |_|   \____|  
  _)(|_    //   \\_(__) )(   ||>>_       \\                   \\   <<,-,,-.    _)(|_   
 (__)__)  (__)  (__)   (__) (__)__)     (__)                 (__)   (./  \.)  (__)__)  
 📦 API - [Inventario]

Este módulo se encarga de manera independiente de toda la lógica relacionada con el manejo de stock y registro de movimientos en el sistema.

## 🛠️ Tecnologías Utilizadas
* **Lenguaje:** Java 17
* **Framework:** Spring Boot (Web, Data JPA)
* **Base de Datos:** Oracle SQL

---

##  Configuración y Variables de Entorno

Para que este servicio funcione correctamente, debes configurar las siguientes propiedades en tu archivo `application.properties`:

* `spring.application.name=INVENTARIO-SERVICE`
* `server.port=8085`
* `eureka.client.service-url.defaultZone=http://localhost:8761/eureka/`


---

## 🚀 Funciones principales
```bash
POST /api/v1/inventarios
```
Crea un inventario. NO SE RECOMIENDA CREARLO DESDE AQUI. Al crear un producto desde API PRODUCTO se genera automaticamente un inventario

```bash
GET /api/v1/inventarios/producto/{idProducto}
```
Busca por id de producto y devuelve inventario asociado. 

IMPORTANTE para buscar Stock
```bash
PATCH /api/v1/inventarios/{idProducto}/aumentar/{cantidad}

PATCH /api/v1/inventarios/{idProducto}/reducir/{cantidad}
```
Enpoints para aumentar o reducir stock de algun producto segun su ID

Aumentar requiere permisos de administrador
Disminuir requiere autenticacion y se utiliza al momento de confirmar pedido

```bash
PUT /api/v1/inventarios
```
Actualiza inventario. requiere un json con la informacion.

Solo utilizar en caso de requerir corregir un error de inventario.

```bash
GET /api/v1/movimientosStock
```
Cada movimiento stock queda registrado en un historial. Aqui se lista todos los movimientos

```bash
GET /api/v1/movimientosStock/reportePorfecha
```
Indicando por parametros fecha INICIO y fecha FIN se filtra el historial que correspondan entre las fechas indicadas.



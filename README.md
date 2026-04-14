# Marketplace Camisetas API

Backend REST para el TPO de Aplicaciones Interactivas. El proyecto modela un e-commerce de camisetas titulares y suplentes de selecciones del Mundial 2026.

## Stack

- Java 8
- Spring Boot 2.7.18
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- H2 Database
- Swagger / OpenAPI
- Maven

## Estructura

```text
src/main/java/com/uade/tpo/marketplace
|- config
|- controller
|- dto
|- exception
|- model
|- repository
|- security
|- service
```

## Base de Datos

La app usa H2 en modo archivo:

```text
jdbc:h2:file:./data/marketplace
```

Consola:

```text
http://localhost:8080/h2-console
```

Credenciales:

```text
User Name: sa
Password:
```

## Swagger

Con la app levantada, la documentacion interactiva de endpoints esta disponible en:

```text
http://localhost:8080/swagger-ui/index.html
```

El JSON OpenAPI se puede consultar en:

```text
http://localhost:8080/v3/api-docs
```

Para probar endpoints protegidos desde Swagger, usar el boton `Authorize` y pegar el token JWT. En Insomnia/Postman, enviar el header:

```text
Authorization: Bearer TOKEN
```

## Datos

La base no se carga automaticamente. Todos los datos deben cargarse mediante Insomnia usando los endpoints REST.

Orden recomendado:

1. Crear el primer admin con `/api/auth/bootstrap-admin`.
2. Crear generos.
3. Crear talles.
4. Crear tipos de camiseta.
5. Crear paises.
6. Crear camisetas.

## Endpoints Actuales

```text
POST /api/auth/register
POST /api/auth/login
POST /api/auth/bootstrap-admin

GET    /api/carrito
POST   /api/carrito/items
PATCH  /api/carrito/items/{id}
DELETE /api/carrito/items/{id}
DELETE /api/carrito

GET  /api/catalogo/generos
GET  /api/catalogo/generos/{id}
POST /api/catalogo/generos
PUT  /api/catalogo/generos/{id}
DELETE /api/catalogo/generos/{id}

GET  /api/catalogo/talles
GET  /api/catalogo/talles/{id}
POST /api/catalogo/talles
PUT  /api/catalogo/talles/{id}
DELETE /api/catalogo/talles/{id}

GET  /api/catalogo/tipos-camiseta
GET  /api/catalogo/tipos-camiseta/{id}
POST /api/catalogo/tipos-camiseta
PUT  /api/catalogo/tipos-camiseta/{id}
DELETE /api/catalogo/tipos-camiseta/{id}

GET  /api/catalogo/paises
GET  /api/catalogo/paises/{id}
POST /api/catalogo/paises
PUT  /api/catalogo/paises/{id}
DELETE /api/catalogo/paises/{id}

GET  /api/camisetas
GET  /api/camisetas/{id}
POST /api/camisetas
PUT  /api/camisetas/{id}
DELETE /api/camisetas/{id}

GET  /api/camisetas/{id}/variantes
POST /api/camisetas/{id}/variantes
GET  /api/camisetas/variantes/{id}
PUT  /api/camisetas/variantes/{id}
PATCH /api/camisetas/variantes/{id}/stock
DELETE /api/camisetas/variantes/{id}

POST /api/camisetas/{id}/descuento
PUT  /api/camisetas/{id}/descuento
DELETE /api/camisetas/{id}/descuento
```

## Autenticacion

Primer admin:

```http
POST /api/auth/bootstrap-admin
Content-Type: application/json

{
  "nombre": "Admin",
  "apellido": "Marketplace",
  "email": "admin@uade.edu.ar",
  "password": "123456",
  "direccion": "UADE",
  "telefono": "1100000000"
}
```

Este endpoint solo funciona si todavia no existe ningun usuario con rol `ADMIN`.

Registro:

```http
POST /api/auth/register
Content-Type: application/json

{
  "nombre": "Cristian",
  "apellido": "Kim",
  "email": "ckim@uade.edu.ar",
  "password": "123456",
  "direccion": "Av. Gaona 3545",
  "telefono": "1122334455"
}
```

Login:

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "ckim@uade.edu.ar",
  "password": "123456"
}
```

Quedan publicos los endpoints de lectura de camisetas/catalogo, H2, Swagger y auth. Los endpoints de escritura de camisetas/catalogo requieren token de un usuario con rol `ADMIN`.

## Carrito

Los endpoints de carrito requieren token de usuario autenticado.

Agregar item:

```http
POST /api/carrito/items
Authorization: Bearer TOKEN
Content-Type: application/json

{
  "varianteId": 1,
  "cantidad": 2
}
```

Actualizar cantidad:

```http
PATCH /api/carrito/items/1
Authorization: Bearer TOKEN
Content-Type: application/json

{
  "cantidad": 3
}
```

Si se agrega dos veces la misma variante, se suma la cantidad. No se permite superar el stock disponible.

## Filtros de camisetas

El listado de camisetas acepta filtros opcionales por query params:

```text
GET /api/camisetas?paisId=1
GET /api/camisetas?tipoCamisetaId=1
GET /api/camisetas?generoId=1
GET /api/camisetas?minPrecio=50000&maxPrecio=120000
GET /api/camisetas?search=argentina
GET /api/camisetas?paisId=1&tipoCamisetaId=1&generoId=1&minPrecio=50000&maxPrecio=120000&search=argentina
```

Por defecto solo devuelve camisetas activas.

## Ejecucion

```powershell
.\mvnw.cmd -s .mvn\settings.xml spring-boot:run
```

## Verificacion

```powershell
.\mvnw.cmd -s .mvn\settings.xml "-Dmaven.compiler.fork=true" test
```

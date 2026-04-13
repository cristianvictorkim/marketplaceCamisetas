# Marketplace Camisetas API

Backend REST para el TPO de Aplicaciones Interactivas. El proyecto modela un e-commerce de camisetas titulares y suplentes de selecciones del Mundial 2026.

## Stack

- Java 8
- Spring Boot 2.7.18
- Spring Web
- Spring Data JPA
- H2 Database
- Swagger / OpenAPI
- Maven

## Estructura

```text
src/main/java/com/uade/tpo/marketplace
|- controller
|- dto
|- exception
|- model
|- repository
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

## Datos

La base no se carga automaticamente. Todos los datos deben cargarse mediante Insomnia usando los endpoints REST.

Orden recomendado:

1. Crear generos.
2. Crear talles.
3. Crear tipos de camiseta.
4. Crear paises.
5. Crear camisetas.

## Endpoints Actuales

```text
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

### Filtros de camisetas

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

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
POST /api/auth/register
POST /api/auth/login
POST /api/auth/bootstrap-admin

GET  /api/catalogo/generos
POST /api/catalogo/generos

GET  /api/catalogo/talles
POST /api/catalogo/talles

GET  /api/catalogo/tipos-camiseta
POST /api/catalogo/tipos-camiseta

GET  /api/catalogo/paises
POST /api/catalogo/paises

GET  /api/camisetas
GET  /api/camisetas/{id}
POST /api/camisetas
```

## Autenticacion

Primer admin:

```http
POST /api/auth/bootstrap-admin
Content-Type: application/json

{
  "nombre": "Admin",
  "apellido": "Marketplace",
  "email": "admin@mail.com",
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
  "email": "cris@mail.com",
  "password": "123456",
  "direccion": "Av. Siempre Viva 123",
  "telefono": "1122334455"
}
```

Login:

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "cris@mail.com",
  "password": "123456"
}
```

La respuesta devuelve un token JWT. Para consumir endpoints protegidos, enviar:

```text
Authorization: Bearer TOKEN
```

Quedan publicos los endpoints de lectura de camisetas/catalogo, H2 y auth. Los endpoints de escritura de camisetas/catalogo requieren token de un usuario con rol `ADMIN`.

## Ejecucion

```powershell
.\mvnw.cmd -s .mvn\settings.xml spring-boot:run
```

## Verificacion

```powershell
.\mvnw.cmd -s .mvn\settings.xml "-Dmaven.compiler.fork=true" test
```

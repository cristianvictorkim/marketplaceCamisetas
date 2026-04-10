# E-commerce API Base

Proyecto base en Spring Boot organizado en 3 capas:

- `controller`: expone endpoints REST.
- `service`: contiene la logica de negocio.
- `repository`: encapsula el acceso a datos.

## Estructura inicial

```text
src/main/java/com/uade/ecommerceapi
|- controller
|- dto
|- exception
|- model
|- repository
|- service
```

## Recurso de ejemplo

Se incluye una vertical simple de `Product` para mostrar el flujo completo:

`ProductController -> ProductService -> ProductRepository`

Por ahora la persistencia es en memoria mediante `InMemoryProductRepository`. Esto permite:

- trabajar la arquitectura por capas desde el inicio;
- probar endpoints sin base de datos;
- reemplazar luego el repositorio por JPA sin cambiar el contrato de la capa de servicio.

## Siguiente etapa recomendada

1. Agregar `spring-boot-starter-data-jpa` y el driver de la base elegida.
2. Mover `model` hacia entidades JPA reales en un paquete `entity`.
3. Reemplazar `InMemoryProductRepository` por interfaces que extiendan `JpaRepository`.
4. Incorporar Spring Security + JWT.
5. Separar modulos de autenticacion, usuarios, productos, pedidos y pagos segun la consigna.

## Nota de compatibilidad

El entorno actual tiene Java 8 instalado, por eso el proyecto queda preparado con Spring Boot `2.7.18`.
Si mas adelante subis a Java 17 o superior, conviene migrar a Spring Boot 3.x.

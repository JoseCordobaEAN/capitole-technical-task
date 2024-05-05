# Servicio de Precios de Comercio Electrónico

Este README proporciona información sobre un servicio de precios de comercio electrónico desarrollado utilizando Spring Boot. El servicio ofrece un punto final REST para consultar información de precios basada en parámetros de entrada.

## Tabla de Contenidos

- [Introducción](#introducción)
- [Uso](#uso)
- [Pruebas](#pruebas)
- [Criterios de Evaluación](#criterios-de-evaluación)

## Introducción

En la base de datos de comercio electrónico de la compañía, existe una tabla `PRICES` que refleja el precio final (PVP) y la tarifa aplicada a un producto de una cadena dentro de fechas específicas. A continuación se muestra un ejemplo de la tabla con los campos relevantes:

### Tabla PRICES

| BRAND_ID | START_DATE           | END_DATE             | PRICE_LIST | PRODUCT_ID | PRIORITY | PRICE | CURR |
|----------|----------------------|----------------------|------------|------------|----------|-------|------|
| 1        | 2020-06-14-00.00.00  | 2020-12-31-23.59.59  | 1          | 35455      | 0        | 35.50 | EUR  |
| 1        | 2020-06-14-15.00.00  | 2020-06-14-18.30.00  | 2          | 35455      | 1        | 25.45 | EUR  |
| 1        | 2020-06-15-00.00.00  | 2020-06-15-11.00.00  | 3          | 35455      | 1        | 30.50 | EUR  |
| 1        | 2020-06-15-16.00.00  | 2020-12-31-23.59.59  | 4          | 35455      | 1        | 38.95 | EUR  |

#### Campos:

- `BRAND_ID`: Clave foránea que representa el grupo de cadenas (1 = ZARA).
- `START_DATE`, `END_DATE`: Rango de fechas durante el cual se aplica el precio de tarifa indicado.
- `PRICE_LIST`: Identificador de la tarifa de precios aplicable.
- `PRODUCT_ID`: Identificador del código del producto.
- `PRIORITY`: Desambiguador de aplicación de precios. Si dos tarifas coinciden dentro de un rango de fechas, se aplica la de mayor prioridad (mayor valor numérico).
- `PRICE`: Precio final de venta.
- `CURR`: Código de moneda ISO.

## Uso

La aplicación es un servicio Spring Boot que proporciona un end-point REST para consultar información de precios. El end-point acepta los siguientes parámetros de entrada:

- `applicationDate`: Fecha de aplicación.
- `productId`: Identificador del producto.
- `brandId`: Identificador de la cadena.

El end-point devuelve los siguientes datos de salida:

- `productId`: Identificador del producto.
- `brandId`: Identificador de la cadena.
- `applicableTariff`: Tarifa a aplicar.
- `applicationDates`: Fechas de aplicación.
- `finalPrice`: Precio final a aplicar.

## Pruebas

El servicio incluye pruebas para validar las siguientes solicitudes con los datos de ejemplo:

- **Prueba 1**: Solicitud a las 10:00 del día 14 para el producto 35455 para la marca 1 (ZARA).
- **Prueba 2**: Solicitud a las 16:00 del día 14 para el producto 35455 para la marca 1 (ZARA).
- **Prueba 3**: Solicitud a las 21:00 del día 14 para el producto 35455 para la marca 1 (ZARA).
- **Prueba 4**: Solicitud a las 10:00 del día 15 para el producto 35455 para la marca 1 (ZARA).
- **Prueba 5**: Solicitud a las 21:00 del día 16 para el producto 35455 para la marca 1 (ZARA).

## Criterios de Evaluación

Se considerarán los siguientes criterios durante la evaluación:

- Diseño y construcción del servicio.
- Calidad del código.
- Corrección de resultados en las pruebas.

---
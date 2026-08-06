# SistemaFacturacion API

API REST de facturacion desarrollada con Spring Boot y Maven.

El proyecto trabaja el tema de recibos de factura con una relacion maestro-detalle. La factura funciona como registro principal y sus detalles representan los conceptos cobrados. Cada detalle tambien puede incluir impuestos, para acercar el modelo a una factura real.

## Modelo principal

- `Cliente`: guarda los datos basicos del cliente, como nombre, RFC, correo, telefono y codigo postal.
- `Factura`: guarda los datos generales del comprobante, como folio, cliente, fecha, subtotal, total e importes de impuestos.
- `DetalleFactura`: representa cada concepto de la factura, con descripcion, cantidad, precio unitario, importe y objeto de impuesto.
- `ImpuestoDetalleFactura`: registra los impuestos aplicados a un detalle, como IVA, ISR o IEPS, indicando si son traslados o retenciones.

La relacion entre cliente y facturas permite asociar comprobantes a un cliente registrado. La relacion entre factura y detalles usa cascade para que, al guardar una factura, tambien se guarden sus conceptos. La relacion entre detalle e impuestos sigue la misma idea para mantener juntos los datos que pertenecen al mismo registro.

El proyecto tambien incluye clases de dominio separadas de las entidades JPA y mapeadores MapStruct para convertir entre el modelo de base de datos y el modelo usado por la logica de la aplicacion.

## Base de datos

La base configurada para desarrollo es H2 en memoria. Se eligio porque permite ejecutar y probar el proyecto sin instalar un servidor externo de base de datos.

No se instalo una base de datos aparte. H2 se descarga como dependencia Maven del proyecto y se ejecuta junto con la aplicacion.

Datos de conexion local:

- URL JDBC: `jdbc:h2:mem:facturaciondb`
- Usuario: `sa`
- Password: vacio
- Consola H2: `http://localhost:8080/h2-console`

Como la base esta en memoria, la informacion se borra cuando se detiene la aplicacion. Esto es practico para pruebas rapidas durante el desarrollo.

La base se puebla automaticamente al iniciar la aplicacion con datos de prueba de clientes, facturas, detalles e impuestos.

## Ejecucion local

Para correr el proyecto desde terminal:

```bash
./mvnw spring-boot:run
```

Cuando la aplicacion este activa, se puede consultar:

- `http://localhost:8080/api/clientes`
- `http://localhost:8080/api/facturas`
- `http://localhost:8080/h2-console`
- `http://localhost:8080/swagger-ui/index.html`

## Endpoints principales

Clientes:

- `GET /api/clientes/all`
- `POST /api/clientes/save`
- `DELETE /api/clientes/delete/{id}`

Facturas:

- `GET /api/facturas/all`
- `GET /api/facturas/cliente/{clienteId}`
- `POST /api/facturas/save`
- `PATCH /api/facturas/{facturaId}/impuestos/{impuestoId}`
- `DELETE /api/facturas/delete/{id}`

Detalles e impuestos:

- `GET /api/detalles-factura/all`
- `GET /api/impuestos-detalle/all`
- `PUT /api/impuestos-detalle/update/{id}`

Los detalles e impuestos se crean y eliminan junto con la factura por cascade. Los impuestos tambien pueden actualizarse cuando se necesita corregir el calculo fiscal.
El endpoint `PATCH` de facturas permite modificar parcialmente un impuesto que pertenezca a una factura especifica y recalcula los totales de impuestos de esa factura.

## Funciones actuales

- Estructura inicial de Spring Boot con Maven.
- Dependencias para WebMVC, JPA, validaciones, H2, MySQL Driver y DevTools.
- Entidades JPA para clientes, facturas, detalles e impuestos.
- Repositorios Spring Data para acceder a la informacion de cada entidad.
- Clases de dominio y mapeadores MapStruct.
- Datos de prueba cargados automaticamente al iniciar.
- Servicios para clientes, facturas, detalles e impuestos.
- Controladores REST con rutas similares al proyecto market.
- Documentacion Swagger/OpenAPI con ejemplos JSON para crear clientes y facturas.
- Manejo global de errores HTTP.
- Validaciones basicas para evitar datos incompletos o valores invalidos.

## Estado

El proyecto todavia esta en etapa de construccion. Ya cuenta con persistencia, dominio, mapeadores, datos de prueba, controladores, Swagger y manejo global de errores. Falta agregar actualizaciones `PUT` si se requiere un CRUD completo.

## Pruebas

```bash
./mvnw test
```

## Seguridad y configuracion

- La configuracion local usa H2 en memoria y no contiene credenciales productivas.
- No subir archivos `.env`, bases locales, `target/` ni configuraciones personales del IDE.
- Para una base real, usar variables de entorno o un perfil local ignorado por Git.

## Derechos

Codigo publicado para revision profesional. Sin licencia de reutilizacion; todos los derechos reservados.

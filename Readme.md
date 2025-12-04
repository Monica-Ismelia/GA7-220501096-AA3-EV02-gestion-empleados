# Sistema de Gestión de Recursos Humanos (SGRH)

### Módulos de Software Codificados y Probados

**Evidencia:** GA7-220501096-AA3-EV02
**Aprendiz:** Mónica Ismelia Cañas Reyes
**Programa:** Tecnólogo en Análisis y Desarrollo de Software
**Instructora:** Eliana Chacón Loaiza
**Fecha:** Diciembre 2025

---

## Contenido

* Configuración del entorno
* Script SQL de la base de datos
* Tecnologías utilizadas
* Pruebas por historia de usuario (CRUD)
* Ejecución del proyecto

---

## Configuración

Antes de ejecutar el proyecto, actualice las credenciales de MySQL en el archivo:

```
src/main/resources/application.properties
```

**Configuración recomendada:**

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/empresa
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Asegúrese de tener la base de datos creada y accesible.

---

## Script SQL de Base de Datos (MySQL)

```sql
CREATE DATABASE IF NOT EXISTS empresa;
USE empresa;

CREATE TABLE empleados (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    correo VARCHAR(120) UNIQUE NOT NULL,
    salario DECIMAL(10,2) NOT NULL,
    fecha_ingreso DATE NOT NULL
);
```

---

## Tecnologías Utilizadas

| Tecnología      | Descripción             |
| --------------- | ----------------------- |
| Java 17         | Lenguaje backend        |
| Spring Boot 3   | Framework para API REST |
| Spring Data JPA | Persistencia de datos   |
| MySQL           | Motor de base de datos  |
| Postman         | Pruebas de endpoints    |
| Maven           | Gestión del proyecto    |

---

# Pruebas por Historia de Usuario

## HU-01 — Registrar empleado

**Endpoint:** `POST /api/empleados`

**Ejemplo de JSON:**

```json
{
  "nombre": "Diego Cañas",
  "correo": "diego@example.com",
  "fechaIngreso": "2025-10-01",
  "salario": 3500000
}
```

**Pruebas ejecutadas:**

| Caso  | Entrada      | Resultado Esperado  | Estado |
| ----- | ------------ | ------------------- | ------ |
| CP-01 | JSON válido  | Empleado creado     | ✔      |
| CP-02 | Falta correo | Error de validación | ✔      |

---

## HU-02 — Listar empleados

**Endpoint:** `GET /api/empleados`

| Caso  | Entrada       | Resultado Esperado | Estado |
| ----- | ------------- | ------------------ | ------ |
| CP-03 | Consulta      | Lista de empleados | ✔      |
| CP-04 | Sin registros | Lista vacía        | ✔      |

---

## HU-03 — Actualizar empleado

**Endpoint:** `PUT /api/empleados/{id}`

**Ejemplo JSON:**

```json
{
  "nombre": "Diego Cañas",
  "correo": "diego@example.com",
  "salario": 4500000,
  "fechaIngreso": "2023-10-20"
}
```

| Caso  | Entrada        | Resultado              | Estado |
| ----- | -------------- | ---------------------- | ------ |
| CP-05 | Datos válidos  | Actualización correcta | ✔      |
| CP-06 | ID inexistente | Error 404              | ✔      |

---

## HU-04 — Eliminar empleado

**Endpoint:** `DELETE /api/empleados/{id}`

| Caso  | Acción         | Resultado | Estado |
| ----- | -------------- | --------- | ------ |
| CP-07 | ID existente   | Eliminado | ✔      |
| CP-08 | ID inexistente | Error 404 | ✔      |

---

## Ejecución del proyecto

Para iniciar la aplicación:

### 1. Limpiar y compilar

```
mvn clean package
```

### 2. Ejecutar el archivo JAR

```
java -jar target/gestion-empleados-1.0-SNAPSHOT.jar
```

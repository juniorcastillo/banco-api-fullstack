# Sistema Bancario - Backend

API REST desarrollada con Spring Boot para la gestión de clientes, cuentas y movimientos bancarios. Incluye validaciones de negocio y generación de reportes.

---

## Tecnologías

- Java 21
- Spring Boot 3
- Spring Data JPA
- PostgreSQL
- Maven
- Docker

---

## Funcionalidades

### Clientes
- Crear cliente
- Editar cliente
- Eliminar cliente
- Activar / desactivar cliente

### Cuentas
- Crear cuenta asociada a cliente
- Validación de cliente activo
- Número de cuenta único

### Movimientos
- Crédito (depósito)
- Débito (retiro)

### Validaciones de negocio
- ¡ No permitir saldo negativo
-  Cliente inactivo no puede operar
-  Límite diario de retiro
-  Número de cuenta único

### Reportes
- Movimientos por cliente y rango de fechas
- Exportación de reportes en PDF

---

## Arquitectura

- Controller → Endpoints REST
- Service → Lógica de negocio
- Repository → Acceso a datos
- Entity → Modelo de datos
- DTO → Respuestas optimizadas

---

## 🐳 Instalación y ejecución con Docker

### 1. Crear red de Docker
```bash
docker network create banco-net
```

### 2. Levantar PostgreSQL
```bash
docker run -d --name banco-db --network banco-net -e POSTGRES_DB=postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres:15
```
### 2.5. Compilar el proyecto
```bash
mvn clean package -DskipTests
```
### 3. Construir imagen del backend
```bash
docker build -t banco-api .
```

### 4. Levantar contenedor del backend
```bash
docker run -d --name banco-api --network banco-net -p 8080:8080 banco-api
```

### 5. Verificar ejecución
```bash
docker ps
```

Probar en navegador:
http://localhost:8080/clientes

---

##  Autor

Junior Castillo  
Desarrollador Full Stack (Java + Angular)
# 🏦 Banco API Fullstack

Aplicación Full Stack para la gestión de un sistema bancario, desarrollada con **Spring Boot (backend)** y **Angular (frontend)**, con soporte completo para **Docker**.

---

## 📌 Descripción

Este sistema permite simular operaciones bancarias reales mediante una arquitectura moderna y desacoplada.

### Funcionalidades principales:

* Gestión de clientes
* Gestión de cuentas bancarias
* Registro de movimientos (crédito / débito)
* Validaciones de negocio
* Consumo de API REST desde frontend Angular
* Ejecución completa mediante Docker

---

## 🧱 Arquitectura

```text
Frontend (Angular)
        ↓
Backend (Spring Boot REST API)
        ↓
Base de datos (PostgreSQL)
```

---

## ⚙️ Tecnologías utilizadas

### 🖥 Backend

* Java 21
* Spring Boot 3
* Spring Data JPA
* Hibernate
* PostgreSQL
* Maven
* Jakarta Validation

### 🌐 Frontend

* Angular 21
* TypeScript
* RxJS
* Standalone Components

### 🐳 DevOps

* Docker
* Docker Compose
* Nginx (para servir Angular en producción)

---

## 📁 Estructura del proyecto

```text
banco-api-fullstack/
│
├── backend/              # API Spring Boot
├── frontend/             # Aplicación Angular
├── postman/              # Colección Postman
├── docker-compose.yml
└── README.md
```

---

# 🚀 Instalación desde cero

## 1️⃣ Clonar repositorio

```bash
git clone https://github.com/juniorcastillo/banco-api-fullstack.git
cd banco-api-fullstack
```

---

# 🐳 Ejecución con Docker (RECOMENDADO)

Levanta todo el sistema con un solo comando:

```bash
docker-compose up --build
```

---

## 🌐 Servicios disponibles

| Servicio   | URL                   |
| ---------- | --------------------- |
| Frontend   | http://localhost:4200 |
| Backend    | http://localhost:8080 |
| PostgreSQL | localhost:5433        |

---

## 🔄 Reiniciar desde cero

```bash
docker-compose down -v
docker-compose up --build
```

---

## 🛑 Detener servicios

```bash
docker-compose down
```

---

## 🧠 Funcionamiento en Docker

* PostgreSQL se ejecuta en un contenedor
* Backend se conecta mediante red interna (`db`)
* Frontend se sirve mediante Nginx
* No se requieren instalaciones locales adicionales

---

# ⚙️ Ejecución manual (sin Docker)

## 🖥 Backend

### Requisitos:

* Java 21
* Maven
* PostgreSQL

### Configuración BD:

```text
DB: banco
USER: postgres
PASSWORD: postgres
```

### Ejecutar:

```bash
cd backend
mvn clean package -DskipTests
java -jar target/banco-api-0.0.1-SNAPSHOT.jar
```

Disponible en:

```text
http://localhost:8080
```

---

## 🌐 Frontend

### Requisitos:

* Node.js 18+
* Angular CLI

### Ejecutar:

```bash
cd frontend
npm install
ng serve
```

Disponible en:

```text
http://localhost:4200
```

---

## 🏗 Build de producción Frontend

```bash
npm run build
```

Esto genera:

```text
dist/
```

---

# 🐳 Docker Frontend (Producción)

El frontend se construye y se sirve mediante **Nginx** dentro de Docker.

Flujo:

```text
Angular build → Nginx → navegador
```

---

# 🧠 Reglas de negocio implementadas

* ❌ No se permite saldo negativo
* ❌ Cliente inactivo no puede operar
* ❌ Cuenta inactiva no puede operar
* 🔒 Límite diario de débito: 1000

---

# 📦 Endpoints principales

## 👤 Clientes

* GET `/clientes`
* POST `/clientes`

## 💳 Cuentas

* GET `/cuentas`
* POST `/cuentas/cliente/{id}`

## 💰 Movimientos

* GET `/movimientos`
* POST `/movimientos/cuenta/{cuentaId}`

---

# 🔄 Ejemplo de respuesta

```json
{
  "id": 1,
  "tipoMovimiento": "CREDITO",
  "valor": 100,
  "saldo": 1100,
  "fecha": "2026-04-26",
  "cuentaId": 1,
  "numeroCuenta": "123456",
  "clienteId": 1,
  "nombreCliente": "Juan Pérez"
}
```

---

# 🧪 Postman

Ubicación:

```text
postman/banco-api.postman_collection.json
```

### Importar:

1. Abrir Postman
2. Click en "Import"
3. Seleccionar el archivo JSON

---

# 🔐 CORS

El backend permite peticiones desde:

```text
http://localhost:4200
```

---

# ⚙️ Configuración Docker (Backend)

```properties
spring.datasource.url=jdbc:postgresql://db:5432/banco
spring.datasource.username=postgres
spring.datasource.password=postgres
```

---

# 👨‍💻 Autor

**Junior Castillo**
Full Stack Developer
Java | Spring Boot | Angular

---

# 🚀 Conclusión

Este proyecto demuestra:

* Desarrollo Full Stack completo
* Arquitectura limpia (Controller → Service → Repository → DTO)
* Integración frontend-backend
* Uso de Docker para despliegue reproducible
* Buenas prácticas de desarrollo

---

# 🏦 Banco Frontend

Aplicación web desarrollada en Angular para la gestión de clientes, cuentas y movimientos bancarios.

---

## Tecnologías

- Angular 21
- TypeScript
- RxJS
- Tailwind CSS
- Nginx (despliegue)

---

## Funcionalidades

- Gestión de clientes
- Gestión de cuentas
- Registro de movimientos
- Visualización de reportes
- Descarga de PDF

---

##  Ejecución en local

### 1. Instalar dependencias
npm install

### 2. Levantar aplicación
ng serve

### 3. Acceder
http://localhost:4200

---

## Configuración API

Asegúrate de que el frontend apunte al backend:
http://localhost:8080

---

## Despliegue con Docker

### 1. Construir imagen
docker build -t banco-frontend .

### 2. Ejecutar contenedor
docker run -d --name banco-frontend -p 4200:80 banco-frontend

### 3. Acceder
Hay que abrirlo en ventana de incognito y lipiar la cache del navegador
http://localhost:4200

---

## Comandos útiles

Ver contenedores:
docker ps

Ver logs:
docker logs banco-frontend

Eliminar contenedor:
docker rm -f banco-frontend

Eliminar imagen:
docker rmi banco-frontend

---

## Consideraciones

- El backend debe estar corriendo en http://localhost:8080
- Angular se compila en modo producción dentro del contenedor
- Nginx sirve la aplicación estática

---

##  Autor

Junior Castillo  
Desarrollador Full Stack (Java + Angular)  
Santo Domingo, República Dominicana
# Backend API – Práctica 2: Aplicación móvil con REST y Autenticación

Repositorio para el backend del proyecto de la **Práctica 2** de la materia *Desarrollo de Aplicaciones Móviles Nativas (IPN - ESCOM)*.

Esta API REST está desarrollada en **Kotlin + Spring Boot**, con enfoque en autenticación segura, gestión por roles y operaciones CRUD.

---

## Implementar un servicio REST

- Autenticación de usuarios (login y registro)
- Contraseñas encriptadas con BCrypt
- Manejo de sesiones seguras con JWT
- Control de acceso por roles (`USER` y `ADMIN`)
- Operaciones CRUD sobre usuarios
- Gestión de fotos de perfil por usuario y administrador

---

## 🧩 Funcionalidades principales

### 🔐 Autenticación
- **POST** `/api/auth/register` – Registro de nuevo usuario
- **POST** `/api/auth/login` – Inicio de sesión con generación de JWT
- Contraseñas encriptadas con `BCryptPasswordEncoder`

### 👥 Roles
- `ADMIN`: Acceso total al CRUD y gestión de usuarios
- `USER`: Acceso limitado a su propio perfil

### 📦 Endpoints CRUD

#### Usuario autenticado:
- **GET** `/api/user/me` – Ver su perfil
- **PUT** `/api/user/me` – Editar su perfil
- **DELETE** `/api/user/me` – Eliminar su cuenta

#### Administrador:
- **GET** `/api/user/all` – Listar todos los usuarios

---

## 🖼️ Gestión de Fotos de Perfil

### Subida y modificación
- **PUT** `/api/user/{id}/profile-picture`
  - Permite subir/modificar la foto (propio usuario o administrador)
  - Responde con la URL de la imagen servida

### Consulta de imagen
- **GET** `/api/user/profile-picture/{filename}`  
  - Sirve directamente la imagen para renderizarla en frontend

---

## 🔐 Seguridad

- **JWT Tokens** generados al iniciar sesión.
- Filtro de autenticación `JwtAuthFilter.kt` y configuración de seguridad en `SecurityConfig.kt`.
- Roles validados con anotaciones `@PreAuthorize` y lógica de negocio.

---

## 📁 Estructura del Proyecto

```
src/main/kotlin/com/aria/backendapi/
├── config/            → Configuración de seguridad y filtros
├── controller/        → Controladores REST
├── dto/               → Data Transfer Objects
├── model/             → Entidades JPA
├── repository/        → Interfaces para acceso a datos
├── service/           → Lógica de negocio
├── util/              → Utilidades (JWT, etc.)
```

---

## 🧪 Pruebas y uso

Puedes probar los endpoints con:
- **Postman**
- **Insomnia**
- **Curl**

Ejemplo:
```
curl -X PUT http://localhost:8080/api/user/1/profile-picture \
 -H "Authorization: Bearer {token}" \
 -F "file=@/ruta/a/imagen.jpg"
```

---

## ⚙️ Dependencias principales

| Librería                    | Propósito                               |
|----------------------------|------------------------------------------|
| Spring Boot Web            | Creación de servicios REST               |
| Spring Security + JWT      | Autenticación y protección de rutas      |
| Spring Data JPA + H2       | Persistencia con base de datos           |
| BCrypt                     | Encriptación de contraseñas              |
| MultipartFile              | Subida de imágenes                       |

---

## 🧾 Requisitos cumplidos

✅ CRUD sobre servicio REST  
✅ Registro/Login con contraseñas encriptadas  
✅ JWT y manejo de sesión segura  
✅ Roles ADMIN y USER  
✅ Gestión de perfil con imagen  
✅ Estructura modular por paquetes  
✅ Pruebas funcionales vía herramientas externas  

---

## 📚 Bibliografía

- Spring Boot Documentation – https://spring.io/projects/spring-boot  
- JWT Intro – https://jwt.io/introduction  
- Kotlin + Spring Boot – https://kotlinlang.org/docs/spring-boot.html

---

## 👤 Autor

**Nombre:** _Roman Ochoa Oliva_  
**Boleta:** _2022630419_  

---

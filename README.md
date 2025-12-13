# Movies API

API REST para la gestión de películas, directores, actores, productoras, usuarios y reseñas. Proyecto desarrollado con Spring Boot para la asignatura de Acceso a Datos.

## Tabla de Contenidos

- [Descripción](#descripción)
- [Tecnologías](#tecnologías)
- [Requisitos Previos](#requisitos-previos)
- [Instalación y Configuración](#instalación-y-configuración)
- [Ejecución](#ejecución)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Endpoints de la API](#endpoints-de-la-api)
- [Pruebas con Postman](#pruebas-con-postman)
- [Postman Runner Parameterization](#postman-runner-parameterization)
- [Base de Datos](#base-de-datos)

## Descripción

Sistema completo de gestión de películas que permite:
- Gestionar estudios cinematográficos
- Administrar información de directores y actores
- Catalogar películas con sus relaciones
- Gestionar usuarios del sistema
- Crear y consultar reseñas de películas

El proyecto implementa una arquitectura en capas (Domain, Repository, Service, Controller) con DTOs para optimizar las respuestas de la API.

## Tecnologías

- **Java 21**
- **Spring Boot**
- **MariaDB** 
- **Lombok**
- **ModelMapper** 
- **Docker** y **Docker Compose** 
- **Maven** 

## Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

- **Java JDK 21** o superior
    - Verifica con: `java -version`
- **Maven 3.9+**
    - Verifica con: `mvn -version`
- **Docker Desktop**
    - Descarga desde: https://www.docker.com/products/docker-desktop/
- **Git**
    - Verifica con: `git --version`
- **Postman** 
    - Descarga desde: https://www.postman.com/downloads/

## Instalación y Configuración

### 1. Clonar el repositorio
```bash
git clone https://github.com/cristinasevi/acceso-datos-aa1.git
cd acceso-datos-aa1
```

### 2. Configurar variables de entorno

Crea un archivo `.env` en la raíz del proyecto:
```env
MARIADB_USER=root
MARIADB_PASSWORD=password
MARIADB_DATABASE=movies
MARIADB_ROOT_PASSWORD=rootpassword
```

### 3. Iniciar la base de datos
```bash
# Iniciar contenedor de MariaDB
docker-compose -f docker-compose.dev.yaml up -d

# Verificar que el contenedor está corriendo
docker ps
```
Deberías ver un contenedor llamado `movies-db` corriendo en el puerto 3307.

## Ejecución

```bash
mvn spring-boot:run
```
La aplicación estará disponible en: **http://localhost:8080**

## Estructura del Proyecto
```
movies/
├── src/
│   ├── main/
│   │   ├── java/acceso/datos/aa1/movies/
│   │   │   ├── config/          # Configuración de la aplicación
│   │   │   ├── controller/      # Controladores REST
│   │   │   ├── domain/          # Entidades JPA
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── exception/       # Excepciones personalizadas
│   │   │   ├── repository/      # Repositorios JPA
│   │   │   ├── service/         # Lógica de negocio
│   │   │   └── util/            # Utilidades
│   │   └── resources/
│   │       ├── application.properties  # Configuración de Spring y BD
│   │       ├── logback-spring.xml      # Configuración de logs
│   │       └── wiremock/
│   │           └── mappings/    # Archivos de mock para WireMock
│   └── test/                    # Tests unitarios
├── postman/                     # Colección de Postman parametrizada
├── docker-compose.dev.yaml      # Configuración de Docker
├── movies.yaml                  # OpenAPI 3.0
├── .env                         # Variables de entorno
├── pom.xml                      # Dependencias Maven
└── README.md
```

## Endpoints de la API

### Studios

| Método | Endpoint | Descripción                  |
|--------|----------|------------------------------|
| GET | `/studios` | Obtener todas las productoras |
| GET | `/studios?country={country}` | Filtrar por país             |
| GET | `/studios/{id}` | Obtener productora por ID    |
| POST | `/studios` | Crear nueva productora       |
| PUT | `/studios/{id}` | Actualizar productora        |
| DELETE | `/studios/{id}` | Eliminar productora             |

### Directors

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/directors` | Obtener todos los directores |
| GET | `/directors?nationality={nationality}` | Filtrar por nacionalidad |
| GET | `/directors/{id}` | Obtener director por ID |
| POST | `/directors` | Crear nuevo director |
| PUT | `/directors/{id}` | Actualizar director |
| DELETE | `/directors/{id}` | Eliminar director |

### Actors

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/actors` | Obtener todos los actores |
| GET | `/actors?nationality={nationality}` | Filtrar por nacionalidad |
| GET | `/actors/{id}` | Obtener actor por ID |
| POST | `/actors` | Crear nuevo actor |
| PUT | `/actors/{id}` | Actualizar actor |
| DELETE | `/actors/{id}` | Eliminar actor |

### Movies

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/movies` | Obtener todas las películas |
| GET | `/movies?genre={genre}` | Filtrar por género |
| GET | `/movies/{id}` | Obtener película por ID |
| POST | `/movies` | Crear nueva película |
| PUT | `/movies/{id}` | Actualizar película |
| DELETE | `/movies/{id}` | Eliminar película |

**Géneros válidos:** Action, Drama, Comedy, Horror, Science Fiction, Romance, Thriller, Animation, Documentary, Adventure

### Users

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/users` | Obtener todos los usuarios |
| GET | `/users/{id}` | Obtener usuario por ID |
| POST | `/users` | Crear nuevo usuario |
| PUT | `/users/{id}` | Actualizar usuario |
| DELETE | `/users/{id}` | Eliminar usuario |

### Reviews

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/reviews` | Obtener todas las reseñas |
| GET | `/movies/{movieId}/reviews` | Obtener reseñas de una película |
| GET | `/users/{userId}/reviews` | Obtener reseñas de un usuario |
| GET | `/reviews/{id}` | Obtener reseña por ID |
| POST | `/movies/{movieId}/reviews` | Crear reseña para una película |
| PUT | `/reviews/{id}` | Actualizar reseña |
| DELETE | `/reviews/{id}` | Eliminar reseña |

## Pruebas con Postman

### Importar la colección

1. Abre Postman
2. Click en **"Import"**
3. Selecciona el archivo `Movies_postman_collection.json` o accede desde esta URL: https://www.postman.com/cristinasevi/workspace/gs-dam/collection/35157639-d60878d9-bb48-4cf0-a400-46d74a50d98b?action=share&creator=35157639
4. La colección aparecerá con 36 requests organizados en 6 carpetas

### Orden de ejecución recomendado

Para crear datos de prueba completos:

1. **POST** `/studios` - Crear estudio
2. **POST** `/directors` - Crear director
3. **POST** `/actors` - Crear actor
4. **POST** `/movies` - Crear película (necesita IDs de studio y director)
5. **POST** `/users` - Crear usuario
6. **POST** `/movies/{movieId}/reviews` - Crear reseña (necesita IDs de movie y user)

### Ejemplos de peticiones

#### Crear un estudio
```bash
POST http://localhost:8080/studios
Content-Type: application/json

{
  "name": "Warner Bros",
  "country": "USA",
  "foundationYear": 1923,
  "email": "contact@warnerbros.com",
  "phone": "+1234567890",
  "active": true
}
```

#### Crear una película
```bash
POST http://localhost:8080/movies
Content-Type: application/json

{
  "title": "Inception",
  "synopsis": "A thief who steals corporate secrets through dream-sharing technology",
  "releaseDate": "2010-07-16",
  "duration": 148,
  "genre": "Science Fiction",
  "averageRating": 8.8,
  "imageUrl": "https://example.com/image.jpg",
  "studio": {"id": 1},
  "director": {"id": 1}
}
```

## Postman Runner Parameterization

1. Abre **Collection Runner** (botón "Run" en la colección)
2. Selecciona el request **POST** correspondiente
3. Click en **"Select File"** y carga el archivo CSV
4. Click en **"Run"**
5. Los datos del CSV se insertarán automáticamente

## Base de Datos

### Modelo de datos

El sistema utiliza 6 entidades principales con las siguientes relaciones:

- **Studio** ← (1:N) → **Movie**
- **Director** ← (1:N) → **Movie**
- **Actor** ← (N:M) → **Movie**
- **Movie** ← (1:N) → **Review**
- **User** ← (1:N) → **Review**

### Consultas en MariaDB
```bash
# Ver tablas
SHOW TABLES FROM MOVIES;

# Ver campos de tabla movies
DESC movies.movies;

# Ver datos de tabla movies
SELECT * FROM movies.movies;
```

## Detener la aplicación

### Detener Spring Boot

Presiona `Ctrl + C` en la terminal donde está corriendo.

### Detener la base de datos
```bash
# Detener el contenedor
docker-compose -f docker-compose.dev.yaml down

# Detener Y eliminar datos
docker-compose -f docker-compose.dev.yaml down -v
```

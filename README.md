# Lomedra Task API

API REST para la gestión de tareas desarrollada con Java 21 y Spring Boot 4.1.1.  
La aplicación permite crear, consultar, actualizar y eliminar tareas mediante endpoints REST. Incluye validación de datos, manejo de errores con respuestas JSON, persistencia con H2 como base de datos en memoria, pruebas automatizadas y documentación interactiva con OpenAPI y Swagger UI.

## Características

- Operaciones CRUD para la gestión de tareas.
- Validación del campo `title`, que es obligatorio y no puede estar vacío.
- Respuestas HTTP adecuadas para cada operación.
- Manejo centralizado de errores con respuestas JSON.
- Persistencia de datos utilizando H2.
- Separación por capas entre controller, service y repository.
- Pruebas automatizadas con Spring Boot y MockMvc.
- Documentación interactiva mediante OpenAPI y Swagger UI.
- Ejecución mediante Docker. 
- Integración continua con GitHub Actions.

## Tecnologías utilizadas

| Tecnología | Uso en el proyecto |
| --- | --- |
| Java 21 | Lenguaje principal de la aplicación |
| Spring Boot 4.1.1 | Framework para el desarrollo de la API REST |
| Spring Web MVC | Manejo de endpoints y solicitudes HTTP |
| Spring Data JPA | Acceso y persistencia de datos |
| H2 Database | Base de datos ligera para la aplicación |
| Jakarta Bean Validation | Validación de los datos recibidos |
| Maven Wrapper | Gestión de dependencias y construcción del proyecto |
| JUnit 5 | Ejecución de pruebas automatizadas |
| MockMvc | Pruebas de los endpoints REST |
| OpenAPI / Swagger UI | Documentación y prueba interactiva de la API |
| Git / GitHub | Control de versiones y gestión del repositorio |
| Docker | Construcción y ejecución de la API en un contenedor |
| GitHub Actions | Ejecución automática de pruebas en cada push y pull request |


## Decisiones técnicas

Se utilizó **Java 21** por ser una versión LTS que ofrece estabilidad y soporte a largo plazo, además de permitir trabajar con una versión moderna del lenguaje.

**Spring Boot 4.1.1** fue elegido porque facilita la creación de APIs REST y proporciona integración directa con herramientas utilizadas en el proyecto, como Spring Web MVC, Spring Data JPA, Bean Validation y el soporte de pruebas de Spring.

Para la persistencia se utiliza **H2**, una base de datos ligera que no requiere instalar ni configurar un servidor de base de datos externo, por lo que permite ejecutar el proyecto con una configuración mínima.

El proyecto utiliza **Maven Wrapper**, por lo que no es necesario tener Maven instalado globalmente. Este wrapper permite utilizar la configuración de Maven incluida en el proyecto y ayuda a mantener un proceso de construcción reproducible.

La aplicación sigue una estructura por capas para separar las responsabilidades relacionadas con las solicitudes HTTP, la lógica de negocio y el acceso a datos.

## Arquitectura

La aplicación utiliza una arquitectura por capas para mantener separadas las responsabilidades principales: `HTTP Request -> Controller -> Service -> Repository -> H2 Database` 

- **Controller:** recibe las solicitudes HTTP y genera las respuestas de la API.
- **Service:** contiene la lógica relacionada con las operaciones sobre las tareas.
- **Repository:** gestiona el acceso a datos mediante Spring Data JPA.
- **Entity:** representa el modelo persistido en la base de datos.
- **DTO:** define los datos utilizados en las solicitudes y respuestas de la API.
- **Mapper:** realiza la conversión entre los DTOs y la entidad `Task`.
- **Exception Handler:** centraliza el manejo de errores y genera respuestas JSON consistentes.

## Estructura del proyecto

El código está organizado por responsabilidad, de manera que la lógica HTTP, la lógica de negocio, la persistencia, los modelos de datos y que el manejo de errores permanezcan separados.
```text
src/
├── main/
│   ├── java/com/lomedra/taskapi/
│   │   ├── controller/
│   │   │   └── TaskController.java
│   │   ├── dto/
│   │   │   ├── TaskRequest.java
│   │   │   └── TaskResponse.java
│   │   ├── entity/
│   │   │   └── Task.java
│   │   ├── exception/
│   │   │   ├── ApiErrorResponse.java
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   └── TaskNotFoundException.java
│   │   ├── mapper/
│   │   │   └── TaskMapper.java
│   │   ├── repository/
│   │   │   └── TaskRepository.java
│   │   ├── service/
│   │   │   └── TaskService.java
│   │   └── LomedraTaskApiApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/lomedra/taskapi/
        ├── controller/
        │   └── TaskControllerTest.java
        └── LomedraTaskApiApplicationTests.java
```

Esta organización permite mantener el controller enfocado en HTTP, delegar las operaciones de negocio al service y concentrar el acceso a datos en el repository. Los DTOs mantienen separado el contrato de la API de la entidad utilizada para persistencia.

## Modelo de tarea

Cada tarea contiene los siguientes campos:

| Campo | Tipo | Descripción |
| --- | --- | --- |
| `id` | Long | Identificador único generado automáticamente |
| `title` | String | Título de la tarea. Es obligatorio y no puede estar vacío |
| `description` | String | Descripción opcional de la tarea |
| `isCompleted` | boolean | Indica si la tarea está completada |

El `id` es generado por la capa de persistencia, por lo que no es necesario enviarlo al crear o actualizar una tarea.

### Ejemplo de solicitud

```json
{
  "title": "Finish REST API",
  "description": "Complete the project documentation",
  "isCompleted": false
}
```

### Ejemplo de respuesta

```json
{
  "id": 1,
  "title": "Finish REST API",
  "description": "Complete the project documentation",
  "isCompleted": false
}
```

## Endpoints

La API expone los siguientes endpoints bajo la ruta base `/api/tasks`:

| Método | Endpoint | Descripción | Respuesta exitosa |
| --- | --- | --- | --- |
| `GET` | `/api/tasks` | Lista todas las tareas | `200 OK` |
| `GET` | `/api/tasks/{id}` | Obtiene una tarea por su identificador | `200 OK` |
| `POST` | `/api/tasks` | Crea una nueva tarea | `201 Created` |
| `PUT` | `/api/tasks/{id}` | Actualiza una tarea existente | `200 OK` |
| `DELETE` | `/api/tasks/{id}` | Elimina una tarea existente | `204 No Content` |

Cuando una operación intenta acceder a una tarea que no existe, la API responde con `404 Not Found`.

Las solicitudes con datos inválidos responden con `400 Bad Request`.

## Validación y manejo de errores

El campo `title` es obligatorio y no puede contener únicamente espacios en blanco. Esta validación se realiza con Jakarta Bean Validation mediante `@NotBlank`.

Por ejemplo, una solicitud como:

```json
{
  "title": "",
  "description": "Invalid task",
  "isCompleted": false
}
```

produce una respuesta `400 Bad Request`:

```json
{
  "timestamp": "2026-08-29T00:00:00",
  "status": 400,
  "message": "Validation failed",
  "errors": {
    "title": "Title is required and cannot be empty"
  }
}
```

También se devuelve `400 Bad Request` cuando el cuerpo de la solicitud contiene JSON mal formado o no puede ser interpretado correctamente.

Si el identificador solicitado no corresponde a una tarea existente, la API devuelve `404 Not Found`:

```json
{
  "timestamp": "2026-08-29T00:00:00",
  "status": 404,
  "message": "Task with id 123 was not found",
  "errors": {}
}
```

El manejo de estas excepciones se encuentra centralizado para mantener un formato de error consistente en toda la API.

## Persistencia

La aplicación utiliza H2 como base de datos ligera, evitando depender de un servidor de base de datos externo para ejecutar el proyecto.

Con la configuración actual, los datos se mantienen en memoria durante la ejecución de la aplicación y se reinician al detenerla. Esto permite comenzar con un entorno limpio cada vez que se inicia el proyecto.

## Ejecución local

### Requisitos

Para ejecutar el proyecto localmente se necesita:

- Java 21.
- Git, en caso de clonar el repositorio.

No es necesario instalar Maven globalmente, ya que el proyecto incluye Maven Wrapper.

### Ejecutar en Windows

Desde la raíz del proyecto:

```powershell
.\mvnw.cmd spring-boot:run
```

### Ejecutar en Linux o macOS

```bash
./mvnw spring-boot:run
```

Cuando la aplicación haya iniciado correctamente estará disponible en:

```text
http://localhost:8080
```

Por ejemplo, para consultar todas las tareas:

```text
http://localhost:8080/api/tasks
```


## Ejecución con Docker

El proyecto incluye un `Dockerfile` para construir y ejecutar la API dentro de un contenedor. Para utilizar esta opción es necesario tener Docker instalado y en ejecución.

### Construir la imagen

Desde la raíz del proyecto:

```bash
docker build -t lomedra-task-api .
```

### Ejecutar el contenedor

```bash
docker run --name lomedra-task-api-container -p 8080:8080 lomedra-task-api
```

La API estará disponible en:

```text
http://localhost:8080
```

Y Swagger UI en:

```text
http://localhost:8080/swagger-ui.html
```

### Detener y eliminar el contenedor

Para detenerlo:

```bash
docker stop lomedra-task-api-container
```

Para eliminar el contenedor después de detenerlo:

```bash
docker rm lomedra-task-api-container
```

La imagen `lomedra-task-api` permanece disponible y puede utilizarse nuevamente para crear otro contenedor.

## Pruebas automatizadas

El proyecto incluye pruebas automatizadas para verificar comportamientos importantes de la API, entre ellos:

- Creación exitosa de una tarea.
- Listado de tareas.
- Actualización de una tarea.
- Eliminación de una tarea.
- Rechazo de solicitudes con `title` vacío.
- Respuesta `404 Not Found` cuando una tarea no existe.

Las pruebas utilizan Spring Boot, JUnit 5, MockMvc y H2.

En Windows pueden ejecutarse con:

```powershell
.\mvnw.cmd clean test
```

En Linux o macOS:

```bash
./mvnw clean test
```

Una ejecución correcta debe finalizar con `BUILD SUCCESS`.

## OpenAPI y Swagger UI

La API incluye documentación interactiva generada con OpenAPI y Swagger UI.

Con la aplicación en ejecución, Swagger UI está disponible en:

```text
http://localhost:8080/swagger-ui.html
```

Desde esta interfaz es posible consultar la documentación y ejecutar solicitudes directamente sobre los endpoints.

La especificación OpenAPI en formato JSON está disponible en:

```text
http://localhost:8080/v3/api-docs
```

Un flujo sencillo para probar el CRUD desde Swagger UI es:

```text
POST /api/tasks
GET /api/tasks
GET /api/tasks/{id}
PUT /api/tasks/{id}
DELETE /api/tasks/{id}
```

Los endpoints no dependen obligatoriamente de este orden; el flujo anterior únicamente facilita una prueba completa de las operaciones disponibles.

## Integración continua

El proyecto utiliza GitHub Actions para ejecutar automáticamente las pruebas en cada `push` y `pull_request`.

El workflow utiliza Java 21 y ejecuta:

```bash
./mvnw -B clean test
```

Esto permite verificar automáticamente que el proyecto compile correctamente y que las pruebas continúen pasando al realizar nuevos cambios.
## Orientacion Vocacional para Estudiantes


El objetivo de este proyecto es desarrollar una aplicación web que facilite la orientación
vocacional para estudiantes de nivel secundario y universitario. La plataforma conectará a
los estudiantes con expertos en diversas áreas profesionales, proporcionando
herramientas para explorar diferentes careers, evaluar habilidades e intereses, y recibir
asesoramiento personalizado. Los estudiantes podrán crear perfiles, realizar pruebas de
orientación vocacional, y acceder a recursos que los ayudarán a tomar decisiones
informadas sobre su futuro académico y profesional.


### Revisa el Progreso del Proyecto

| **Columna**       | **Descripción**                                                                                                                                    |
|-------------------|----------------------------------------------------------------------------------------------------------------------------------------------------|
| **Backlog**       | Contiene todas las historias de usuario, tareas y características que deben desarrollarse. Es el listado de todo el trabajo pendiente.              |
| **En Progreso**   | Incluye las tareas que están actualmente en desarrollo. Visualiza el trabajo en curso para asegurar el flujo continuo de trabajo.                   |
| **Revisión**      | Después de completar una tarea, se mueve aquí para una revisión de código y revisión por pares (peer review). Esta fase incluye la creación de **pull requests** para asegurar que el código cumpla con los estándares de calidad antes de integrarse al proyecto principal. |
| **En Pruebas**    | Contiene las tareas que han pasado la revisión de código y necesitan pruebas exhaustivas (unitarias, de integración y de aceptación) para garantizar su calidad. |
| **Hecho**         | Las tareas completamente desarrolladas, revisadas y probadas se mueven aquí, indicando que están listas y finalizadas.                               |

Mira cómo va avanzando nuestro trabajo visitando el siguiente enlace: [Jira](https://insog2.atlassian.net/jira/software/projects/OVT/boards/34/backlog?atlOrigin=eyJpIjoiMzQ5OWMwN2JmZGY2NGNkN2IyMjEwMjc0MTlkMGUwMTciLCJwIjoiaiJ9).


### Funcionalidades de la Aplicación

#### **Módulo de Gestión de Usuarios:**

- **Registro y gestión de perfiles:**
    - Los usuarios podrán registrarse y crear
      perfiles donde podrán almacenar su información personal, intereses, y
      resultados de pruebas de orientación vocacional.

#### **Módulo de Evaluación Vocacional:**

- **Realización de pruebas:**
    - Los estudiantes podrán realizar pruebas de
      orientación vocacional que evalúen sus habilidades, intereses y valores.
      Estas pruebas generarán informes que ayudarán a los estudiantes a
      identificar posibles careers y campos de estudio.

- **Resultados Personalizados:**
    - Los informes de las pruebas ofrecerán
      recomendaciones de careers y sugerencias de estudios adicionales
      basados en los resultados obtenidos.

#### **Módulo de Conexión con Expertos:**

- **Busqueda de Mentores:**
    - Mentores: Los estudiantes podrán buscar y conectarse con
      profesionales en diversos campos que estén dispuestos a ofrecer
      orientación y asesoramiento.

- **Sesiones de Asesoría:**
    - Funcionalidad para que los estudiantes puedan
      agendar sesiones de asesoría con expertos. Los expertos pueden
      proporcionar orientación sobre la elección de carrera, preparación
      académica, y otros temas relevantes.

- **Beneficio para los expertos:**
    - Los expertos podrán ganar visibilidad y
      construir una red profesional, además de recibir una compensación
      económica si ofrecen asesorías pagadas.

#### **Módulo de Recursos Educativos**

- **Acceso a contenido:**
    - Integración de artículos, videos, y otros recursos
      educativos que proporcionen información sobre diferentes careers,
      habilidades necesarias, y tendencias del mercado laboral.
- **Recomendacios Personalizadas:**
    - Ofrecimiento de contenido
      educativo basado en los resultados de las pruebas vocacionales y los
      intereses del usuario.

#### **Módulo de pagos en línea**

- **Compra de Servicios de Asesoría:**
    - Integración de un sistema de pagos
      que permita a los estudiantes pagar por sesiones de asesoría
      personalizada con expertos o por acceso a recursos exclusivos.

## Diagramas de la Aplicación

Para entender mejor la estructura y diseño de la aplicación "Orientación Vocacional de Estudiantes", revisa los siguientes diagramas:

### Diagrama de Clases

![Diagrama de Clases]()


### Diagrama de Base de Datos

![Diagrama de Base de Datos]()

Este diagrama ilustra el esquema de la base de datos utilizada por la aplicación, mostrando las tablas, columnas, y relaciones entre las entidades.

### Descripción de Capas del Proyecto

| capa        | descripción                                                                                  |
|-------------|----------------------------------------------------------------------------------------------|
| api         | Contiene los controladores REST que manejan las solicitudes HTTP y las respuestas.            |
| entity      | Define las entidades del modelo de datos que se mapean a las tablas de la base de datos.      |
| repository  | Proporciona la interfaz para las operaciones CRUD y la interacción con la base de datos.      |
| service     | Declara la lógica de negocio y las operaciones que se realizarán sobre las entidades.         |
| service impl| Implementa la lógica de negocio definida en los servicios, utilizando los repositorios necesarios. |

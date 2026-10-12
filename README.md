# # 📚 BibliotecaEscolar

### Sistema de Gestión de Biblioteca Escolar en Java

EFT **Desarrollo Orientado a Objetos II**, orientado a la administración de una biblioteca escolar mediante una aplicación de escritorio desarrollada en Java Swing y conectada a una base de datos Oracle Cloud.

El sistema permite gestionar libros, categorías, estudiantes, préstamos y devoluciones, además de consultar reportes y controlar el acceso según el rol del usuario.

---

## 🛠️ Tecnologías utilizadas

- **Java JDK 25:** lenguaje de programación.
- **Apache NetBeans:** entorno de desarrollo.
- **Apache Maven:** gestión de dependencias y compilación.
- **Java Swing:** interfaz gráfica de escritorio.
- **JDBC:** conexión y operaciones con la base de datos.
- **Oracle Autonomous Database:** almacenamiento de información en Oracle Cloud.
- **Oracle SQL Developer:** creación y administración de las tablas.
- **Oracle Wallet:** conexión segura a la base de datos.

## 📁 Estructura del proyecto

```text
BibliotecaEscolar/
│
├── src/main/java/
│   │
│   ├── conexion/
│   │   └── DatabaseConnection.java
│   │
│   ├── modelo/
│   │   ├── Usuario.java
│   │   ├── Estudiante.java
│   │   ├── Libro.java
│   │   ├── Categoria.java
│   │   └── Prestamo.java
│   │
│   ├── dao/
│   │   ├── UsuarioDAO.java
│   │   ├── EstudianteDAO.java
│   │   ├── LibroDAO.java
│   │   ├── CategoriaDAO.java
│   │   └── PrestamoDAO.java
│   │
│   ├── controlador/
│   │   ├── ControladorUsuario.java
│   │   ├── ControladorEstudiante.java
│   │   ├── ControladorLibro.java
│   │   ├── ControladorCategoria.java
│   │   └── ControladorPrestamo.java
│   │
│   ├── vista/
│   │   ├── VentanaLogin.java
│   │   ├── VentanaPrincipal.java
│   │   ├── VentanaLibros.java
│   │   ├── VentanaCategorias.java
│   │   ├── VentanaEstudiantes.java
│   │   ├── VentanaPrestamos.java
│   │   └── VentanaReportes.java
│   │
│   └── principal/
│       ├── CrearAdmin.java
│       └── Main.java
│
├── database
│        ├──Jorge_Munoz_EFT_Semana9_POO.sql
│
├── pom.xml
└── README.md
```

## 🧩 Arquitectura y funcionamiento

El proyecto utiliza una organización basada en el patrón **Modelo–Vista–Controlador (MVC)**, complementada con clases DAO para gestionar la persistencia de datos.

| Paquete | Función |
|---|---|
| `modelo` | Representa las entidades y sus atributos mediante clases Java. |
| `vista` | Contiene las ventanas gráficas desarrolladas con Swing. |
| `controlador` | Comunica la interfaz gráfica con las operaciones del sistema. |
| `dao` | Ejecuta las operaciones SQL utilizando JDBC, `PreparedStatement` y `ResultSet`. |
| `conexion` | Administra el acceso a Oracle Cloud mediante el patrón Singleton. |
| `principal` | Contiene las clases encargadas de iniciar el sistema y crear el bibliotecario inicial. |

### Clases principales

- **DatabaseConnection:** establece conexiones JDBC con Oracle Cloud utilizando Wallet y Singleton.
- **Usuario:** representa los datos y el rol de cada usuario.
- **Estudiante:** representa a los estudiantes y utiliza herencia desde Usuario.
- **Libro:** almacena los datos de los libros y su stock.
- **Categoria:** representa la clasificación de los libros.
- **Prestamo:** representa los préstamos y devoluciones.
- **Clases DAO:** permiten registrar, consultar, actualizar y eliminar información según las reglas de cada entidad.
- **Clases Controlador:** conectan las operaciones de los DAO con las ventanas.
- **VentanaLogin:** permite iniciar sesión.
- **VentanaPrincipal:** muestra las funcionalidades disponibles según el rol.
- **Ventanas de gestión:** permiten administrar libros, categorías, estudiantes y préstamos.
- **VentanaReportes:** permite consultar historiales, préstamos activos y libros más solicitados.
- **CrearAdmin:** registra el bibliotecario inicial en la base de datos.
- **Main:** inicia la aplicación gráfica.

## 🗄️ Base de datos Oracle

La aplicación utiliza **Oracle Autonomous Database**, alojada en Oracle Cloud.

El modelo relacional contiene cinco tablas:

| Tabla | Descripción |
|---|---|
| `USUARIOS` | Almacena cuentas, credenciales y roles. |
| `ESTUDIANTES` | Registra la información académica de los estudiantes y su relación con los usuarios. |
| `CATEGORIAS` | Almacena las categorías de los libros. |
| `LIBROS` | Registra los libros disponibles y su stock. |
| `PRESTAMOS` | Almacena los préstamos, fechas y estado de devolución. |

Las tablas utilizan claves primarias (PK), claves foráneas (FK), restricciones `UNIQUE`, `CHECK` y columnas `IDENTITY` para generar identificadores automáticamente.

### Relaciones principales

- Un usuario puede estar asociado a un registro de estudiante.
- Una categoría puede contener varios libros.
- Un estudiante puede realizar múltiples préstamos.
- Un libro puede participar en diferentes préstamos.

El proyecto utiliza **transacciones SQL** para registrar préstamos, actualizar existencias y procesar devoluciones, manteniendo la consistencia de la información.

## ⚙️ Requisitos previos

Antes de ejecutar el proyecto, se necesita:

1. Tener instalado **JDK 25**.
2. Contar con **Apache NetBeans** y Maven.
3. Disponer de una base de datos Oracle Autonomous Database.
4. Descargar y descomprimir el **Wallet** correspondiente a la base de datos.
5. Contar con acceso a Oracle SQL Developer.
6. Tener configuradas las dependencias Oracle JDBC en `pom.xml`.

## 🚀 Instalación y ejecución

### Paso 1. Abrir el proyecto

Clonar el repositorio:

```bash
git clone URL_DEL_REPOSITORIO
```

Abrir la carpeta `BibliotecaEscolar` como proyecto Maven en Apache NetBeans.

### Paso 2. Crear las tablas en Oracle

Conectarse a Oracle Cloud desde SQL Developer y ejecutar el script SQL del proyecto.

Las tablas deben crearse en este orden:

1. `USUARIOS`
2. `CATEGORIAS`
3. `ESTUDIANTES`
4. `LIBROS`
5. `PRESTAMOS`

Es importante crear primero las tablas independientes para evitar errores relacionados con las claves foráneas.

### Paso 3. Configurar Oracle Wallet

Descargar el Wallet desde Oracle Cloud y descomprimirlo en una carpeta local.

En `conexion/DatabaseConnection.java`, configurar la ruta correspondiente:

```java
private static final String WALLET_PATH =
    "/ruta/local/Wallet_Biblioteca";
```

Verificar también el alias TNS de la base de datos:

```java
private static final String URL =
    "jdbc:oracle:thin:@evsumativa3_high?TNS_ADMIN="
    + WALLET_PATH;
```

El alias debe coincidir con el servicio definido en el archivo `tnsnames.ora` del Wallet.

Configurar las credenciales de Oracle mediante las variables de entorno:

```text
ORACLE_DB_USER
ORACLE_DB_PASSWORD
```

**Por seguridad, no se deben publicar contraseñas ni archivos privados del Wallet en GitHub.** Cada persona que ejecute el proyecto deberá utilizar sus propias credenciales y configuración de conexión.

### Paso 4. Compilar el proyecto

Desde NetBeans, hacer clic derecho sobre `BibliotecaEscolar` y seleccionar:

**Clean and Build**

Maven descargará las dependencias configuradas en `pom.xml` y compilará las clases Java.

### Paso 5. Ejecutar primero `CrearAdmin.java`

**Este paso debe realizarse antes de iniciar la aplicación por primera vez.**

Abrir la clase:

```text
principal/CrearAdmin.java
```

Configurar los datos del bibliotecario inicial mediante las variables de entorno:

```text
ADMIN_INICIAL
CLAVE_ADMIN_INICIAL
RUT_ADMIN_INICIAL
```

Ejecutar la clase con **Run File** en NetBeans.

Si la operación es exitosa, aparecerá:

```text
Bibliotecario creado correctamente.
```

Este procedimiento registra el primer usuario con rol `BIBLIOTECARIO`.

**Importante:** ejecutar `CrearAdmin.java` una sola vez por cuenta, ya que Oracle no permite repetir el RUT o correo de un usuario registrado.

### Paso 6. Ejecutar `Main.java`

Después de crear el bibliotecario, ejecutar:

```text
principal/Main.java
```

Esta clase abre la ventana de inicio de sesión de BibliotecaEscolar.

Ingresar el correo y la contraseña configurados al crear el bibliotecario.

Una vez iniciada la sesión, se mostrará el menú principal con las funcionalidades correspondientes al rol.

**Orden correcto de ejecución:**

```text
1. Crear las tablas en Oracle SQL Developer
             ↓
2. Configurar Oracle Wallet y JDBC
             ↓
3. Compilar el proyecto con Maven
             ↓
4. Ejecutar CrearAdmin.java (solo inicialmente)
             ↓
5. Ejecutar Main.java
             ↓
6. Iniciar sesión y utilizar BibliotecaEscolar
```

## 👥 Roles del sistema

**Bibliotecario**

Puede administrar los libros, categorías, estudiantes, préstamos y devoluciones, además de consultar reportes generales.

**Estudiante**

Puede acceder a las funcionalidades habilitadas para su rol, consultar su historial de préstamos y realizar las operaciones permitidas por la aplicación.

## 📖 Gestión de préstamos

Al registrar un préstamo:

1. Se verifica que el libro tenga stock disponible.
2. Se registra el préstamo asociado al estudiante.
3. Se descuenta una unidad del stock.
4. Se almacena la fecha del préstamo y el estado pendiente.

Al devolver un libro:

1. Se actualiza el estado del préstamo a devuelto.
2. Se registra la fecha de devolución.
3. Se incrementa nuevamente el stock del libro.

El sistema considera un plazo de siete días para identificar préstamos atrasados.

## 🧵 Programación orientada a objetos

El proyecto aplica los siguientes conceptos:

- **Encapsulamiento:** atributos privados con métodos getters y setters.
- **Herencia:** la clase Estudiante extiende de Usuario.
- **Abstracción:** separación de responsabilidades mediante modelos, controladores y DAO.
- **Singleton:** instancia única del administrador de conexión.
- **Manejo de excepciones:** control de errores mediante `try`, `catch` y `SQLException`.
- **Colecciones:** utilización de `ArrayList` para almacenar resultados.
- **Multihilos:** utilización de `Thread` y `Runnable` en operaciones de préstamos.
- **Sincronización:** utilización de `synchronized` y transacciones JDBC para coordinar operaciones sobre préstamos.
---

**Desarrollado por:** Jorge Muñoz León  
**Lenguaje:** Java 25  
**Base de datos:** Oracle Autonomous Database  
**IDE:** Apache NetBeans  
**Proyecto:** BibliotecaEscolar

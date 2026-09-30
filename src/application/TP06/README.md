# TP06 - Sistema de recomendación musical

## Materia
Algoritmos y Estructuras de Datos II

## Consigna del ejercicio
Desarrollar una aplicación de consola que administre canciones y usuarios. Cada canción pertenece a un género y cada usuario puede indicar sus géneros favoritos. El sistema debe recomendar aleatoriamente canciones de esos géneros que todavía no haya escuchado el usuario.

La aplicación permite:
- agregar géneros, canciones y usuarios;
- iniciar y cerrar sesión;
- agregar o quitar géneros favoritos del usuario logueado;
- solicitar una recomendación;
- registrar canciones como escuchadas para excluirlas de recomendaciones futuras.

### Observaciones
- La aplicación debe manejar entradas inválidas sin caerse. El modelo y las implementaciones de los TDA rechazan datos inválidos mediante excepciones; la interfaz valida las entradas antes de llamar al modelo.
- Se incluye una base de datos precargada para facilitar la prueba.
- Las decisiones de interacción y experiencia de usuario se documentan para poder explicarlas durante el parcial.

## Qué implementa este proyecto en TP06
La aplicación recomienda canciones que cumplen simultáneamente estos criterios:
1. La canción pertenece a un género favorito del usuario logueado.
2. El usuario todavía no registró la canción como escuchada.

Si hay varias canciones candidatas, se selecciona una al azar. Si no hay candidatas, se informa que no quedan canciones nuevas que coincidan con sus preferencias.

## Arquitectura de TP06
La arquitectura separa entidades, reglas del sistema e interacción por consola:

1. **Modelo de dominio**
   - `Genero`: representa un género musical.
   - `Cancion`: representa título, artista y género.
   - `Usuario`: representa al usuario, sus géneros favoritos y su historial.

2. **Lógica de recomendación**
   - `SistemaRecomendacion`: administra los catálogos y valida las operaciones del dominio. Construye el conjunto de canciones candidatas y elige una de manera aleatoria.

3. **Interfaz de consola y orquestación**
   - `RecomendacionMusicalExercise`: carga los datos iniciales, presenta el menú, valida entradas y delega las operaciones al sistema.

TP06 se integra con la infraestructura general del proyecto:
- `MainProgram` permite elegir `TP06 - RecomendacionMusicalExercise`.
- `Exercise` proporciona el ciclo de ejecución de la consola.
- `SimpleSet` / `SimpleArraySet` se utilizan como estructura principal para catálogos, preferencias e historial.

## Uso del TDA setModule
Los siguientes datos se modelan con `SimpleSet`:
- el catálogo de géneros;
- el catálogo de canciones;
- el catálogo de usuarios;
- los géneros favoritos de cada usuario;
- las canciones escuchadas por cada usuario;
- el conjunto temporal de canciones candidatas a recomendación.

El conjunto evita duplicados y ofrece las operaciones `add`, `contains`, `toArray` e `isEmpty` utilizadas por el ejercicio. Para recomendar, `SistemaRecomendacion` recorre el catálogo de canciones, conserva aquellas cuyo género está en los favoritos y descarta las que ya aparecen en el historial del usuario. Luego selecciona una canción del conjunto resultante.

La igualdad de géneros y usuarios no distingue mayúsculas de minúsculas. Una canción se identifica por la combinación de título y artista, también sin distinguir mayúsculas; el género no forma parte de su identidad para detectar duplicados.

## Base de datos inicial
Cada vez que se inicia TP06 se cargan cinco géneros, nueve canciones y cuatro usuarios. Los datos son de prueba y viven en memoria durante la ejecución del ejercicio.

### Géneros
- Rock
- Pop
- Jazz
- Folklore
- Electronica

### Canciones
| # | Canción | Artista | Género |
|---|---|---|---|
| 1 | Muchacha (Ojos de papel) | Almendra | Rock |
| 2 | Rastros | Lali | Pop |
| 3 | Invierno | Drexler | Jazz |
| 4 | Zamba para olvidar | Mercedes Sosa | Folklore |
| 5 | Musica ligera | Soda Stereo | Rock |
| 6 | Ciudad magica | Tan Bionica | Pop |
| 7 | Be Strong | Korolova | Electronica |
| 8 | Milonga del moro judio | Jorge Drexler | Folklore |
| 9 | Instant Crush | Daft Punk | Electronica |

### Usuarios, preferencias e historial inicial
| Usuario | Géneros favoritos | Canciones ya escuchadas |
|---|---|---|
| Diego | Rock, Pop | Muchacha (Ojos de papel) |
| Juliana | Folklore, Jazz | Zamba para olvidar |
| Betsabe | Electronica | Ninguna |
| Trinidad | Pop, Jazz | Rastros |

Con estos datos, Betsabe puede recibir una recomendación de Electronica desde el inicio. Juliana puede recibir una canción de Folklore o Jazz, excepto `Zamba para olvidar`, que ya está en su historial. Las recomendaciones concretas de entre las candidatas pueden variar porque la selección es aleatoria.

## Flujo general
1. `MainProgram` instancia `RecomendacionMusicalExercise` al seleccionar TP06.
2. El constructor crea el `SistemaRecomendacion` y carga géneros, canciones, usuarios, preferencias e historial de prueba.
3. El menú muestra el usuario logueado y sus géneros preferidos, si hay una sesión activa.
4. El usuario elige una operación. La interfaz valida los datos ingresados y utiliza el sistema para efectuarla.
5. Para recomendar, el sistema obtiene las canciones que coinciden con los favoritos y excluye las escuchadas.
6. La recomendación se muestra sin modificar el historial. Si el usuario escuchó la canción, debe registrarla explícitamente desde la opción correspondiente.
7. `mm` termina TP06 y devuelve el control al menú principal.

## Opciones del menú
| Opción | Acción |
|---|---|
| 1 | Agregar un género al catálogo. |
| 2 | Agregar una canción indicando título, artista y género registrado. |
| 3 | Agregar un usuario. |
| 4 | Iniciar sesión seleccionando un usuario de la lista. |
| 5 | Agregar o quitar un género favorito del usuario logueado. |
| 6 | Recomendar una canción según preferencias e historial. |
| 7 | Registrar una canción del catálogo como escuchada por el usuario logueado. |
| 8 | Cerrar la sesión actual. |
| `mm` | Volver al menú principal del proyecto. |

## Rol de cada clase y métodos

### `Genero`
**Rol:** entidad de dominio que representa un género musical.

**Atributos principales:**
- `nombre`: se almacena sin espacios al inicio o al final.

**Métodos:**
- `Genero(String nombre)`: crea el género; rechaza valores nulos o vacíos con `IllegalArgumentException`.
- `getNombre()`: devuelve el nombre.
- `equals(Object other)` / `hashCode()`: implementan identidad sin distinguir mayúsculas y minúsculas.
- `toString()`: devuelve el nombre para mostrarlo en consola.

### `Cancion`
**Rol:** entidad de dominio que representa una canción y su género.

**Atributos principales:**
- `titulo`
- `artista`
- `genero`

**Métodos:**
- `Cancion(String titulo, String artista, Genero genero)`: valida que el título, el artista y el género sean válidos y normaliza los espacios exteriores de los textos.
- `getTitulo()`, `getArtista()` y `getGenero()`: devuelven los datos de la canción.
- `equals(Object other)` / `hashCode()`: identifican la canción por título y artista, ignorando mayúsculas y minúsculas.
- `toString()`: muestra título, artista y género.

### `Usuario`
**Rol:** entidad que conserva las preferencias y el historial de un usuario.

**Atributos principales:**
- `nombre`
- `generosFavoritos` (`SimpleSet<Genero>`)
- `cancionesEscuchadas` (`SimpleSet<Cancion>`)

**Métodos:**
- `Usuario(String nombre)`: valida que el nombre no sea nulo ni vacío.
- `getNombre()`, `getGenerosFavoritos()` y `getCancionesEscuchadas()`: exponen los datos del usuario.
- `agregarGeneroFavorito(Genero genero)`: agrega un favorito y devuelve `false` si ya existía.
- `quitarGeneroFavorito(Genero genero)`: quita un favorito y devuelve `false` si no estaba asignado.
- `registrarEscuchada(Cancion cancion)`: agrega una canción al historial y devuelve `false` si ya estaba registrada.
- `equals(Object other)` / `hashCode()`: identifican al usuario por nombre, sin distinguir mayúsculas.
- `toString()`: devuelve el nombre.

### `SistemaRecomendacion`
**Rol:** servicio de dominio que administra los catálogos y aplica las reglas de recomendación.

**Atributos principales:**
- `generos` (`SimpleSet<Genero>`)
- `canciones` (`SimpleSet<Cancion>`)
- `usuarios` (`SimpleSet<Usuario>`)
- `random`: generador de selección aleatoria.

**Métodos:**
- `agregarGenero(Genero genero)`: incorpora el género y devuelve `false` si ya existía.
- `agregarCancion(Cancion cancion)`: incorpora la canción si su género está registrado; rechaza con `IllegalArgumentException` canciones inválidas o géneros inexistentes.
- `agregarUsuario(Usuario usuario)`: incorpora el usuario y evita duplicados según su identidad.
- `buscarGenero(String nombre)` / `buscarUsuario(String nombre)`: buscan por nombre sin distinguir mayúsculas; devuelven `null` si no encuentran coincidencia o el texto no es válido.
- `buscarCancion(int posicion)`: devuelve la canción en la posición numerada, o `null` si la posición queda fuera de rango.
- `registrarGeneroFavorito(Usuario usuario, Genero genero)`: valida que el usuario y género pertenezcan al sistema y asigna la preferencia.
- `quitarGeneroFavorito(Usuario usuario, Genero genero)`: valida el usuario y género y quita la preferencia.
- `registrarEscuchada(Usuario usuario, Cancion cancion)`: valida que tanto el usuario como la canción pertenezcan al sistema y actualiza su historial.
- `recomendar(Usuario usuario)`: valida al usuario y devuelve un `Optional<Cancion>` con una candidata aleatoria, o vacío si no existe ninguna.
- `getGeneros()`, `getCanciones()` y `getUsuarios()`: dan acceso a los conjuntos administrados por el sistema.
- `validarUsuario(Usuario usuario)`: comprobación interna que impide operar con usuarios ajenos al sistema.

### `RecomendacionMusicalExercise`
**Rol:** controlador de TP06; coordina el menú de consola, valida entradas y presenta los resultados.

**Atributos principales:**
- `sistema`: instancia del servicio de recomendación.
- `usuarioLogueado`: usuario activo, o `null` si no hay sesión.
- `firstTime`: controla el mensaje de bienvenida y resumen de datos iniciales.
- `CANCELAR`: palabra clave para interrumpir el ingreso de texto o números.

**Métodos principales:**
- `RecomendacionMusicalExercise(Scanner scanner)`: inicializa la consola y carga los datos de prueba.
- `cargarBaseDeDatos()`: crea los géneros, canciones, usuarios y sus preferencias e historial iniciales.
- `exerciseLogic()` / `mostrarMenu()`: ejecutan el ciclo del ejercicio, muestran usuario y favoritos, presentan opciones y despachan la acción elegida.
- `agregarGenero()`: solicita y agrega un género.
- `agregarCancion()`: solicita título y artista, permite elegir un género del catálogo y registra la canción.
- `agregarUsuario()`: solicita y registra un usuario.
- `iniciarSesion()`: muestra y permite elegir un usuario; el menú muestra sus favoritos bajo la leyenda `Usuario logueado`.
- `gestionarGenerosFavoritos()`: asigna o quita un favorito al usuario activo.
- `recomendarCancion()`: solicita al sistema una recomendación y muestra el resultado o informa que no hay candidatas.
- `registrarCancionEscuchada()`: muestra el catálogo y agrega la canción elegida al historial del usuario.
- `cerrarSesion()` / `requiereSesion()`: cierra la sesión o impide operaciones que requieren usuario activo.
- `mostrarGeneros()`, `mostrarUsuarios()` y `mostrarCanciones()`: presentan catálogos numerados.
- `formatearGenerosFavoritos(Usuario usuario)`: prepara la lista de favoritos del usuario para el encabezado del menú.
- `mostrarConNumeracion(SimpleSet<T> elementos)`: imprime un conjunto con posiciones comenzando en 1.
- `obtenerGenero(int posicion)` / `obtenerUsuario(int posicion)`: resuelven la opción numérica validada.
- `pedirTexto(String mensaje)`: solicita texto no vacío o permite cancelar.
- `pedirEntero(String mensaje, int minimo, int maximo)`: parsea y valida un número dentro de un rango o permite cancelar.

## Manejo de errores e inputs inválidos
La interfaz de consola reduce la posibilidad de enviar argumentos inválidos al modelo:
- los textos se recortan y no se aceptan vacíos;
- los ingresos numéricos se parsean con `try/catch` para manejar texto que no sea un entero;
- las opciones numéricas se validan contra el rango mostrado antes de acceder a una posición;
- `cancelar` abandona el ingreso en curso sin alterar los datos;
- se evita agregar canciones si no hay géneros registrados;
- se comprueba que existan usuarios, canciones o géneros antes de solicitar una selección;
- las operaciones que dependen de un usuario activo informan si no hay sesión iniciada;
- la opción de recomendar distingue el caso de no tener favoritos del caso en que no quedan canciones candidatas.

El modelo también valida invariantes: por ejemplo, no acepta canciones cuyo género no esté registrado, ni operaciones de preferencias o historial para usuarios o canciones ajenos al sistema. La consola evita esas excepciones mediante sus validaciones y seleccionando elementos de los catálogos existentes.

## Decisiones de UX visibles en TP06
- El menú muestra quién está logueado; debajo se muestran sus géneros preferidos para dar contexto a la recomendación y hacer visibles los cambios de preferencias.
- Los catálogos se presentan numerados y se seleccionan por posición. Así se evitan problemas de escritura y diferencias de mayúsculas en nombres.
- La sesión se mantiene hasta que se cierra explícitamente o se sale del ejercicio. La gestión de favoritos, el historial y las recomendaciones requieren una sesión.
- La acción de recomendar no agrega automáticamente la canción al historial. Una recomendación no garantiza que se haya escuchado; el usuario registra la escucha de forma explícita.
- Los duplicados se evitan con conjuntos y reglas de igualdad por entidad. Esto evita repetir géneros, nombres de usuario o canciones del mismo título y artista.
- Si no hay canciones que cumplan ambos criterios de recomendación, el sistema informa el resultado en lugar de seleccionar una canción que no corresponda.
- Las altas y los cambios informan si se realizaron o si el elemento ya existía/no estaba asignado.

## Pruebas manuales sugeridas
1. Iniciar sesión como Betsabe y comprobar que el menú muestre `Electronica` bajo `Usuario logueado`.
2. Pedir una recomendación para Betsabe; debe ser `Be Strong` o `Instant Crush`.
3. Registrar una de esas canciones como escuchada y volver a pedir una recomendación; la registrada ya no debe aparecer.
4. Iniciar sesión como Juliana y comprobar que puede recibir canciones de Folklore o Jazz, pero no `Zamba para olvidar`.
5. Iniciar sesión como Diego o Trinidad y comprobar que sus favoritos aparecen debajo de su nombre en el menú.
6. Asignar y quitar un género favorito; comprobar que el encabezado del menú se actualice.
7. Intentar agregar un género, usuario o canción duplicados; el sistema debe informar que ya existen.
8. Probar entradas vacías, texto en campos numéricos, posiciones fuera de rango y `cancelar`; la aplicación debe seguir funcionando sin terminar inesperadamente.

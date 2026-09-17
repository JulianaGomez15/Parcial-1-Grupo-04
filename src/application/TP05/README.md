# TP05 - Sistema de impresión

## Materia
Algoritmos y Estructuras de Datos II

## Consigna del ejercicio
Desarrollar una aplicación de consola en Java de entre las siguientes posibles:

- **Sistema de impresión:** se imprimen documentos, cada uno con varias páginas y la opción de imprimir a color o en blanco y negro. El usuario puede crear documentos, enviarlos a la cola de impresión, cancelar la cola e imprimir. Cuando se imprime, se deben mostrar en orden las páginas de todos los documentos en la cola, empezando por todos los documentos en blanco y negro y siguiendo con todos los de color.
- **Sistema de gestión de mesas de restaurante:** en un restaurante, hay una mesa para 2 y 4 personas, y los comensales pueden registrarse para esperar su turno escribiendo su nombre y cantidad de personas. La aplicación permite anotar nuevos clientes, y hacer pasar a nuevos clientes una vez que se libera la mesa correspondiente. Grupos de 1 y 2 personas van a la mesa de 2 personas, 3 y 4 a la de 4, y más de 4 no están permitidos en el establecimiento.

### Observaciones
- La aplicación desarrollada debe manejar inputs inválidos del usuario en todas las opciones, evitando crashes. Las implementaciones del TDA en sí deben generar excepciones si reciben comandos o datos inválidos, es la misma aplicación la que debe asegurar que eso no ocurra.
- Se debe agregar una base de datos pre-programada, a modo de facilitar el testeo de la aplicación (no aplica para la primera opción).
- Las decisiones sobre experiencia de usuario, interacciones y soluciones a problemas deberán ser explicadas y defendidas en el parcial, por lo cual se recomienda documentarlas.

---

## Qué implementa este proyecto en TP05
En este repositorio, TP05 implementa un sistema de impresión con:
- creación de documentos;
- envío a cola de impresión;
- cancelación de toda la cola;
- impresión por páginas;
- prioridad de salida: primero documentos en blanco y negro y luego color.

## Arquitectura de TP05
La arquitectura está separada en tres partes:

1. **Modelo de dominio**
   - `Documento`: representa los datos del documento a imprimir.

2. **Servicio de colas de impresión**
   - `ColaImpresion`: encapsula la lógica de encolado, prioridad, cancelación y snapshot.

3. **Interfaz de consola y orquestación**
   - `ImpresionExercise`: controla menú, validaciones, flujo por fases y ejecución de impresión.

Además, TP05 se apoya en infraestructura general del proyecto:
- `MainProgram` para seleccionar `TP05 - ImpresionExercise`.
- `Exercise` como clase base con loop principal.
- `SimpleList` / `SimpleArrayList` para listado de documentos.
- `SimpleQueue` / `SimpleArrayQueue` para colas internas.

## Flujo general
1. `MainProgram` crea `ImpresionExercise`.
2. El constructor carga una base inicial de documentos.
3. El menú permite crear, enviar, cancelar e imprimir.
4. Al enviar, `ColaImpresion` decide cola destino según tipo (`color` o `blanco y negro`).
5. Al imprimir, se consumen primero documentos de blanco y negro y luego de color.

## Rol de cada clase y métodos

### `ImpresionExercise`
**Rol:** controlador principal del TP05 (UI, validaciones y flujo de operaciones).

**Atributos principales:**
- `documentos`: catálogo de documentos creados.
- `colaImpresion`: servicio de colas de impresión.
- `CANCELAR`: palabra clave para abortar operaciones.
- `firstTime`: controla bienvenida.

**Métodos:**
- `ImpresionExercise(Scanner scanner)`
  - Constructor; inicializa estado y carga base de datos.
- `cargarBaseDeDatos()`
  - Agrega documentos iniciales para pruebas.
- `exerciseLogic()`
  - Dispatcher por fase (`menu`, `crear`, `enviar`, `cancelar`, `imprimir`).
- `menuLogic()`
  - Muestra documentos, cola y opciones; interpreta comando de usuario.
- `crearDocumentoLogic()`
  - Pide datos, valida y crea un documento nuevo.
- `enviarAImprimirLogic()`
  - Permite elegir un documento del catálogo y encolarlo.
- `cancelarColaLogic()`
  - Vacía ambas colas de impresión.
- `imprimirLogic()`
  - Procesa la cola hasta vaciarla, imprimiendo página por página.
- `mostrarDocumentosCreados()`
  - Lista el catálogo de documentos.
- `mostrarCola()`
  - Lista cola pendiente en orden real de impresión.
- `imprimirPaginas(Documento documento)`
  - Imprime por consola cada página del documento.
- `pedirSiEsColor()`
  - Solicita tipo de impresión (B&N o color) con validación y cancelación.
- `volverAlMenu()`
  - Resetea fase al menú.
- `pedirTexto(...)`
  - Input de texto no vacío con opción de cancelar.
- `pedirEntero(...)`
  - Input numérico en rango con opción de cancelar.

### `Documento`
**Rol:** entidad de dominio que representa un trabajo de impresión.

**Atributos principales:**
- `nombre`
- `paginas`
- `color`

**Métodos:**
- `Documento(String nombre, int paginas, boolean color)`
  - Constructor; normaliza nombre (`trim`) y guarda datos.
- `getNombre()`
  - Devuelve nombre.
- `getPaginas()`
  - Devuelve cantidad de páginas.
- `esColor()`
  - Informa si el documento es a color.
- `toString()`
  - Genera texto legible con nombre, páginas y tipo.

### `ColaImpresion`
**Rol:** servicio de cola dual con política de prioridad.

**Atributos principales:**
- `colaBlancoYNegro`
- `colaColor`

**Métodos:**
- `enviar(Documento documento)`
  - Encola el documento en su cola correspondiente.
- `cancelar()`
  - Limpia ambas colas.
- `isEmpty()`
  - Indica si no hay documentos pendientes en ninguna cola.
- `sacarSiguiente()`
  - Devuelve y quita el siguiente documento a imprimir (prioriza B&N).
- `snapshot()`
  - Construye una vista de la cola en orden de impresión sin perder el contenido.
- `copiarSinPerder(SimpleQueue<Documento> cola, SimpleList<Documento> destino)`
  - Copia temporalmente una cola en lista destino y restaura su estado original.

## Manejo de errores e inputs inválidos
TP05 valida entradas para evitar errores de ejecución:
- texto no vacío;
- números enteros con parseo seguro (`try/catch`);
- control de rangos;
- prevención de operaciones inválidas sobre cola vacía;
- cancelación explícita con `cancelar`.

## Decisiones de UX visibles en TP05
- El sistema muestra siempre el catálogo y la cola antes de pedir acción.
- Se ofrece feedback explícito en cada operación (creado, encolado, cancelado, impresión finalizada).
- La cola se visualiza en el mismo orden en que realmente se imprimirá.
- La prioridad B&N antes de color está implementada de forma consistente en extracción y visualización.

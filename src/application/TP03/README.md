# TP03 - Lista de tareas

## Materia
Algoritmos y Estructuras de Datos II

## Consigna del ejercicio
Desarrollar una aplicación de consola en Java de entre las siguientes posibles:

### Opción 1: Sistema de playlist musical
- En la cual tenemos una playlist a la que podemos agregar y remover canciones, reproducir y detener la reproducción, y pasar a la anterior o siguiente.
- La consola debería mostrar en pantalla toda la playlist, e indicar cuál es la canción que se está reproduciendo actualmente.
- Podemos agregar canciones escribiendo el nombre y remover por nombre o por número dentro de la playlist.

**Bonus:** agregar una opción de reproducir la siguiente canción aleatoriamente o en orden, y de loopear la siguiente canción o detener la reproducción después de la última canción.

### Opción 2: Lista de tareas
- En la cual podemos agregar y remover tareas, y marcarlas como realizadas o no.
- Podemos agregar escribiendo el nombre de la tarea, y remover por nombre o por número dentro de la lista.
- La consola debería mostrar en pantalla todas las tareas, indicando cuáles están completas.

**Bonus:** simular un sistema de fecha y hora con una opción de avanzar el tiempo por 1h, y agregar a las tareas una fecha y hora límite. La consola debería tener un apartado especial para las tareas atrasadas, o indicar cuáles están atrasadas de alguna otra forma clara.

### Observaciones
- La aplicación desarrollada debe manejar inputs inválidos del usuario en todas las opciones, evitando crashes. Las implementaciones del TDA en sí deben generar excepciones si reciben comandos o datos inválidos, de la misma aplicación la que debe asegurar que eso no ocurra.
- Se debe agregar una base de datos pre-programada, a modo de facilitar el testeo de la aplicación.
- Las decisiones sobre experiencia de usuario, interacciones y soluciones a problemas deberán ser explicadas y defendidas en el parcial, por lo cual se recomienda documentarlas.

---

## Qué implementa este proyecto en TP03
En este repositorio, TP03 implementa la **Opción 2 (Lista de tareas)** con:
- alta de tareas,
- visualización,
- marcado de completadas,
- eliminación por título o por posición,
- y bonus de **tiempo simulado** para detectar tareas atrasadas.

## Arquitectura de TP03
La arquitectura está separada en tres partes:

1. **Modelo de dominio**
   - `Tarea`: representa los datos y reglas de una tarea.

2. **Simulación de tiempo**
   - `SimuladorTiempo`: encapsula el reloj del ejercicio (no usa `LocalDateTime.now()` como fuente principal).

3. **Interfaz de consola y orquestación**
   - `TareaExercise`: maneja menú, validaciones de entrada, flujo por fases y operaciones sobre la lista.

Además, TP03 se apoya en la infraestructura general del proyecto:
- `MainProgram` para elegir el ejercicio desde menú.
- `Exercise` como base abstracta con el loop de ejecución.
- `SimpleList<Tarea>` / `SimpleArrayList<Tarea>` como estructura de almacenamiento.

## Flujo general
1. `MainProgram` permite elegir `TP03 - TareaExercise`.
2. `Exercise.run()` ejecuta un bucle mientras `running` sea `true`.
3. `TareaExercise.exerciseLogic()` deriva la ejecución según `currentPhase`.
4. Cada fase resuelve una acción del menú y vuelve a fase `0` (menú).
5. El estado de tareas vive en `SimpleArrayList<Tarea>`.
6. El estado de “atrasada” se evalúa contra el tiempo del `SimuladorTiempo`.

## Rol de cada clase y métodos

### `Tarea`
**Rol:** entidad de dominio que modela una tarea con título, fechas y estado.

**Atributos principales:**
- `titulo`
- `fechaCreacion`
- `fechaCompletada`
- `completada`
- `fechaLimite`

**Métodos:**
- `Tarea(String tit, LocalDateTime fCreacion, LocalDateTime fCompletada, LocalDateTime fLimite)`
  - Constructor que inicializa todos los campos principales.
- `setCompletada(LocalDateTime ahora)`
  - Marca la tarea como completada y guarda la fecha de completado.
- `estaAtrasada(LocalDateTime ahora)`
  - Devuelve `true` si no está completada, tiene fecha límite y el tiempo actual ya superó ese límite.
- `getTitulo()`
  - Devuelve el título.
- `toString()`
  - Genera representación textual mostrando título, fecha de creación, estado y fecha límite (si existe).

### `SimuladorTiempo`
**Rol:** proveer un “ahora” controlado y reproducible para el ejercicio.

**Métodos:**
- `SimuladorTiempo(LocalDateTime tiempoInicial)`
  - Inicializa el reloj simulado.
- `avanzarUnaHora()`
  - Incrementa una hora al tiempo actual.
- `getTiempoActual()`
  - Devuelve el `LocalDateTime` actual del simulador.
- `getTiempoFormateado()`
  - Devuelve el tiempo actual en formato `dd/MM/yyyy HH:mm`.

### `TareaExercise`
**Rol:** controlador de TP03. Administra menú, estados, entradas y operaciones.

**Atributos principales:**
- `tareas`: lista de tareas (`SimpleList<Tarea>`)
- `reloj`: simulador de tiempo
- `firstTime`: control de mensaje de bienvenida
- `mostrarSiempre`: define si la lista se imprime automáticamente en cada vuelta
- `CANCELAR`: palabra clave para abortar operaciones

**Métodos de ciclo/fases:**
- `TareaExercise(Scanner scanner)`
  - Constructor que inicializa datos base y carga tareas predefinidas (base pre-programada para testeo).
- `exerciseLogic()`
  - Dispatcher por `currentPhase` (menú/agregar/mostrar/completar/eliminar/avanzar).

**Métodos de interacción principal:**
- `menuLogic()`
  - Muestra hora, opciones y resuelve comandos del usuario.
- `agregarLogic()`
  - Pide título y horas hasta vencimiento; crea y agrega tarea.
- `mostrarLogic()`
  - Imprime lista actual de tareas.
- `completarLogic()`
  - Pide título, busca tarea y la marca completada.
- `eliminarPorTituloLogic()`
  - Pide título, busca y elimina.
- `eliminarPorPosicionLogic()`
  - Muestra lista, pide número de posición y elimina.
- `avanzarTiempoLogic()`
  - Avanza una hora el reloj simulado.

**Métodos auxiliares:**
- `mostrarTareas()`
  - Recorre e imprime todas las tareas; agrega marca `[ATRASADA]` cuando corresponde.
- `buscarPorTitulo(String titulo)`
  - Búsqueda case-insensitive por título.
- `listaVacia()`
  - Corte defensivo para evitar operaciones inválidas cuando no hay tareas.
- `volverAlMenu()`
  - Resetea fase a menú principal interno del ejercicio.
- `pedirTexto(...)`
  - Input robusto de texto no vacío con opción de cancelar.
- `pedirEntero(...)`
  - Input robusto de entero en rango con opción de cancelar.

## Manejo de errores e inputs inválidos
TP03 prioriza que la consola no crashee frente a entradas inválidas:
- se valida texto vacío,
- se controla parseo de enteros con `try/catch`,
- se validan rangos numéricos,
- se permite cancelar operaciones con la palabra `cancelar`,
- y se evita operar sobre lista vacía.

## Decisión de UX visible en TP03
Existe una opción `auto` para alternar si la lista se muestra automáticamente en cada vuelta del menú.  
Esto ayuda tanto al uso interactivo como al testeo manual de estados.

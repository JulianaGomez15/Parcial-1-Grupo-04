# Parcial 1 - Grupo 04

Proyecto de Algoritmos y Estructuras de Datos II. Aplicación de consola en Java, con implementación propia del TDA List (`SimpleArrayList`) y los ejercicios de cada TP.

## Cómo correr

Desde la raíz del proyecto:

```
find scr -name "*.java" > sources.txt
javac -d out @sources.txt
java -cp out application.MainProgram
```

## Menú principal

Al ejecutar `MainProgram`, se elige un ejercicio por número:

- **0: TestExercise** — ejercicio de prueba inicial.
- **1: ListExercise** — ejercicio de uso de listas.
- **2: TP03 - Lista de tareas** — consigna de TP03, con el bonus incluido (fecha límite, reloj simulado y tareas atrasadas).
- **3: Salir**

## TP03 - Lista de tareas

Implementa la consigna de "Lista de tareas": agregar, remover, marcar como completada y listar todas las tareas indicando su estado, más el bonus completo (reloj simulado, fecha límite y tareas atrasadas). Arranca con tareas pre-cargadas (base de datos pre-programada) para facilitar el testeo, en vez de arrancar vacía.

## Manejo de errores

Las implementaciones del TDA (`SimpleArrayList`) lanzan excepciones ante índices o datos inválidos; los ejercicios capturan esas excepciones y validan la entrada del usuario (opciones de menú, números, títulos vacíos) para evitar que la aplicación se cierre de forma inesperada.

## Clases y funciones

### Base del proyecto (`application`)

**`MainProgram`** — punto de entrada (`main`). Muestra el menú de ejercicios en un loop y, según lo que elige el usuario, instancia el `Exercise` correspondiente y lo corre.
- `run()`: loop principal, llama a `selectExercise` y luego a `exercise.run()`.
- `selectExercise(Scanner)`: lee la opción del usuario y crea el `Exercise` elegido (o corta el loop con `running = false`).

**`Exercise`** (abstracta) — clase base de todos los ejercicios, incluidos los que no son de TP03. Define el patrón que siguen: un loop que llama a `exerciseLogic()` hasta que `running` pasa a `false`, navegando entre "pantallas" con el campo `currentPhase`.
- `run()`: `while(running) exerciseLogic();`
- `exerciseLogic()`: abstracta, cada ejercicio decide qué hacer en cada `currentPhase`.
- `repeatOperationCheck(String)`: helper para preguntas de sí/no (`s`/`n`), usado por otros ejercicios del proyecto.

### TDA List (`application.listModule`)

**`SimpleList<E>`** — interfaz del TDA List: `add`, `remove` (por objeto y por índice), `get`, `set`, `clear`, `size`, `isEmpty`, `contains`, `indexOf`.

**`SimpleArrayList<E>`** — implementación del TDA List sobre un array interno que se redimensiona (`resize()`, duplica el tamaño) cuando se llena. Es la estructura que usa `TareaExercise` para guardar sus tareas. Lanza `IndexOutOfBoundsException` en `get`/`set`/`remove(int)` con índices inválidos (validado en `validateIndex`).

### TP03 (`application.TP03`)

**`Tarea`** — modela una tarea: `titulo`, `fechaCreacion`, `fechaCompletada`, `completada` y `fechaLimite` (opcional, `null` si la tarea no tiene vencimiento).
- `setCompletada(LocalDateTime ahora)`: marca la tarea como completada, usando el tiempo simulado que se le pasa (no la hora real de la máquina).
- `estaAtrasada(LocalDateTime ahora)`: `true` si la tarea no está completada, tiene fecha límite, y ya pasó respecto al tiempo simulado.
- `getTitulo()`, `toString()`: getter y representación en texto con el estado (PENDIENTE/COMPLETADO) y la fecha límite si tiene.

**`TareaExercise`** — ejercicio de consola de TP03: agregar, remover (por título o posición), marcar como completada y listar tareas, con el bonus completo (reloj simulado + fecha límite + indicador de atrasadas). Guarda sus tareas en un `SimpleArrayList<Tarea>` y reutiliza `SimuladorTiempo` (`application.TP03.simuladorTiempo`) como reloj simulado (arranca el 01/01/2026 08:00). El constructor precarga 3 tareas de ejemplo: una pendiente, una ya atrasada y una completada, para poder probar todo sin cargar nada a mano.
- `exerciseLogic()` / `menuLogic()`: despachan según `currentPhase` a los métodos de abajo.
- `addLogic()`: pide título y en cuántas horas vence (`0` = sin fecha límite); calcula la fecha límite con el reloj simulado.
- `showLogic()` / `printList()`: imprime todas las tareas numeradas, agregando `[ATRASADA]` a las que corresponda según `estaAtrasada`.
- `completeLogic()`: busca una tarea por título y la marca como completada usando la hora del reloj simulado.
- `removeByNameLogic()` / `removeByIndexLogic()`: eliminan por título o por posición, capturando `IndexOutOfBoundsException` si la posición no existe.
- `advanceTimeLogic()`: avanza el reloj simulado 1 hora.

### Reloj simulado (`application.TP03.simuladorTiempo`)

**`SimuladorTiempo`** — reloj simulado, independiente de la hora real de la máquina. Lo usa `TareaExercise` para que la fecha límite y el estado de "atrasada" de las tareas dependan de un tiempo controlable, no del reloj del sistema.
- `avanzarUnaHora()`: suma 1 hora al tiempo interno.
- `getTiempoActual()` / `getTiempoFormateado()`: devuelven el tiempo simulado actual, como `LocalDateTime` o ya formateado.

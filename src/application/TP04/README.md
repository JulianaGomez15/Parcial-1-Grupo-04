# TP04 - Aventura narrativa

## Materia
Algoritmos y Estructuras de Datos II

## Consigna del ejercicio
Desarrollar una aplicación de consola en Java de entre las siguientes posibles:

- **Historial de navegador web:** el usuario tiene opciones para ir hacia una página (escribe por consola el nombre de la página), volver atrás y volver adelante. A cada paso, el programa muestra la página actual y las opciones. Al volver atrás, se deberá cargar la última página visitada, al volver adelante se carga la siguiente, y si se visita una nueva página se elimina sólo el historial hacia adelante.
- **Menú de configuración para app o dispositivo:** el menú desarrollado deberá tener varios submenús, cada uno de ellos con la posibilidad de volver hacia atrás, elegir otro submenú, o hacer algún ajuste. Los datos configurados por el usuario deben mostrarse en alguna pantalla.
  - Ejemplo:
    - Ajustes
      - Pantalla
        - Resolución
        - Brillo
      - Sonido
        - Volumen
        - Mono / Stereo
- **Aventura narrativa:** contar una historia que, a cada paso, permita al usuario elegir entre varios caminos y volver atrás.
  - Ejemplo:
    - “Abrís los ojos y estás en un bosque oscuro. Hay dos caminos: hacia tu izquierda y derecha”
      - Armar una fogata para acampar
      - Ir hacia la izquierda
      - Ir hacia la derecha
      - Atrás (No disponible en la primera pantalla)

### Observaciones
- La aplicación desarrollada debe manejar inputs inválidos del usuario en todas las opciones, evitando crashes. Las implementaciones del TDA en sí deben generar excepciones si reciben comandos o datos inválidos, es la misma aplicación la que debe asegurar que eso no ocurra.
- Se debe agregar una base de datos pre-programada, a modo de facilitar el testeo de la aplicación (no aplica para la primera opción).
- Las decisiones sobre experiencia de usuario, interacciones y soluciones a problemas deberán ser explicadas y defendidas en el parcial, por lo cual se recomienda documentarlas.

---

## Qué implementa este proyecto en TP04
En este repositorio, TP04 implementa una aventura de bosque con múltiples caminos y finales:
- escenas con texto descriptivo;
- opciones que navegan a otra escena;
- opciones locales que no cambian de escena (ej. “armar una fogata”);
- botón lógico de **“Atrás”** para deshacer el último movimiento;
- salida al menú principal con `mm`.

## Arquitectura de TP04
La arquitectura está separada en tres partes:

1. **Modelo narrativo**
   - `Escena`: modela una escena, sus opciones y sus destinos.
   - `HistoriaAventura`: almacena y resuelve escenas por id.

2. **Control de navegación**
   - `SimpleStack<Integer>` (`SimpleLinkedStack`): guarda el recorrido del usuario.

3. **Interfaz de consola y orquestación**
   - `AventuraExercise`: muestra texto, toma decisiones del usuario, aplica reglas de navegación y maneja validaciones.

Además, TP04 se apoya en la infraestructura general del proyecto:
- `MainProgram` para seleccionar `TP04 - AventuraExercise`.
- `Exercise` como clase base con el loop principal.
- `SimpleList` / `SimpleArrayList` para guardar escenas y opciones.

## Flujo general
1. `MainProgram` crea `AventuraExercise`.
2. El constructor crea las escenas y opciones de la historia.
3. Se apila la escena inicial en `recorrido`.
4. En cada vuelta, se toma `recorrido.peek()` como escena actual.
5. El usuario elige opción numérica, `Atrás` o `mm`.
6. Si la opción tiene destino válido, se hace `push(destino)`.
7. Si elige `Atrás`, se hace `pop()` y se vuelve a la escena anterior.

## Rol de cada clase y métodos

### `AventuraExercise`
**Rol:** controlador principal del TP04 (UI, flujo y reglas de interacción).

**Atributos principales:**
- constantes de ids (`INICIO`, `RIO`, `CUEVA_SENDERO`, etc.);
- `historia`: instancia de `HistoriaAventura`;
- `recorrido`: pila de ids de escena;
- `firstTime`: controla mensaje de bienvenida.

**Métodos:**
- `AventuraExercise(Scanner scanner)`
  - Constructor que crea toda la historia (escenas + opciones + finales) y apila la escena inicial.
- `exerciseLogic()`
  - Ejecuta la lógica del ejercicio llamando al menú.
- `menuLogic()`
  - Muestra escena actual y opciones, valida input, procesa `mm`, opción `Atrás`, navegación y acciones locales.

### `Escena`
**Rol:** entidad de dominio narrativa que representa un nodo de la historia.

**Atributos principales:**
- `id`
- `descripcion`
- `opciones` (`SimpleList<String>`)
- `destinos` (`SimpleList<Integer>`) donde `-1` representa opción sin navegación.

**Métodos:**
- `Escena(int id, String descripcion)`
  - Constructor base de escena.
- `agregarOpcion(String texto, int escenaDestino)`
  - Agrega una opción que sí navega a otra escena.
- `agregarOpcion(String texto)`
  - Sobrecarga para opción local (sin cambiar escena), guardando destino `-1`.
- `getId()`
  - Devuelve id de escena.
- `getDescripcion()`
  - Devuelve descripción.
- `cantidadOpciones()`
  - Devuelve cantidad de opciones disponibles.
- `getTextoOpcion(int index)`
  - Devuelve texto de opción por índice.
- `getDestino(int index)`
  - Devuelve destino asociado a una opción.

### `HistoriaAventura`
**Rol:** contenedor de escenas y resolver de búsqueda por id.

**Atributos principales:**
- `escenas` (`SimpleList<Escena>`)

**Métodos:**
- `HistoriaAventura()`
  - Inicializa la colección de escenas.
- `agregarEscena(Escena escena)`
  - Registra una escena en la historia.
- `obtenerEscena(int id)`
  - Busca una escena por id y la devuelve (o `null` si no existe).

## Manejo de errores e inputs inválidos
TP04 contempla entradas inválidas:
- parseo numérico protegido con `try/catch`;
- validación de rango de opción;
- control explícito de comando `mm`;
- opción `Atrás` disponible solo si hay escena previa en la pila (`size > 1`).

## Decisiones de UX visibles en TP04
- La escena actual siempre se muestra al inicio de cada vuelta para mantener contexto.
- Las opciones se numeran dinámicamente según la escena.
- `Atrás` se oculta en el estado inicial para evitar operaciones inválidas.
- Las acciones locales (como fogata) dan feedback sin romper el flujo narrativo.

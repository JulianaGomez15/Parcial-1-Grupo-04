# TP11 - GPS (sedes UADE)

## Materia
Algoritmos y Estructuras de Datos II

## Consigna del ejercicio
Desarrollar una aplicación de consola en Java de entre las siguientes posibles:

- **GPS:** dado un mapa representado como grafo ponderado, un punto origen y un punto destino, calcula el camino más corto.
- **Simulador estratégico:** dado un grafo que representa acciones, cada una con un costo, y un objetivo, calcula el plan de acción menos costoso para lograr el objetivo. Por ejemplo, para tener la cena lista, se puede pedir un delivery con un costo alto, o preparar los ingredientes y cocinar, cada paso con un costo bajo pero la suma de pasos puede llegar a dar un costo más o menos alto que pocos pasos costosos. El objetivo y los pasos pueden ser cualquier cosa que resulte coherente.

### Observaciones
- Se debe incluir un grafo ya hecho, y cargarlo al iniciar la app. Editarlo es opcional y se valora positivamente.
- Al inicio (o después de cada edición) se debe imprimir todo el grafo, arista por arista. Ej: `A → B: 3`.
- Se debe contemplar el ingreso de inputs no esperados sin que genere errores.

---

## Qué implementa este proyecto en TP11
En este repositorio, TP11 implementa la opción **GPS**, usando como mapa las sedes de UADE:
- grafo pre-cargado con 4 sedes (Belgrano, Recoleta, Monserrat, Pinamar) y las distancias entre ellas;
- impresión del grafo completo al iniciar, arista por arista;
- cálculo del camino más corto entre un origen y un destino fijos (UADE Belgrano → UADE Monserrat), usando el algoritmo de Dijkstra.

No se implementó edición del grafo: la consigna la marca como opcional, y se decidió mantener el alcance acotado a lo pedido.

El cálculo del camino se hace con el **`DijkstraSolver` del propio `graphModule`** (no una reimplementación propia) — ver más abajo el bug que tuvo que arreglarse para que funcione.

## Por qué el camino es fijo (y no se pide por teclado)
La consigna pide "un punto origen y un punto destino", pero no exige que se ingresen interactivamente. Para mantener el ejercicio simple y acotado se fijaron ambos puntos en el código (`UADE Belgrano` → `UADE Monserrat`), en vez de agregar un menú de selección de sede. El algoritmo de Dijkstra sigue siendo genérico (recibe cualquier origen) y se aplica igual sobre todo el grafo.

## Arquitectura de TP11
A diferencia de los TPs anteriores, TP11 es **una sola clase**:

- **`GPSExercise`**: carga el grafo, lo imprime, usa `DijkstraSolver` para calcular el camino y expone el menú de consola.

No hay una clase de dominio separada (como `Escena` o `Usuario` en otros TPs) porque el TDA relevante (`Graph`/`Edge`/`DijkstraSolver`) ya lo provee `graphModule`, y no hace falta modelar nada adicional por fuera de esos vértices (`String`, el nombre de la sede).

TP11 se apoya en la infraestructura general del proyecto:
- `MainProgram` para seleccionar `GPSExercise`.
- `Exercise` como clase base con el loop principal.
- `graphModule` (`Graph<T>`, `Edge<T>`, `ListGraph<T>`, `DijkstraSolver<T>`) para el grafo y el algoritmo.
- `priorityQueueModule` (`SimpleLinkedPriorityQueue`), con el fix descrito más abajo — usado internamente por `DijkstraSolver`.
- `SimpleList` / `SimpleArrayList` para armar el camino reconstruido.
- `SimpleStack` / `SimpleLinkedStack` para invertir el camino al reconstruirlo.

## Bug encontrado y arreglado en `priorityQueueModule`
Al usar `DijkstraSolver` contra nuestro grafo real (sedes con más de un vecino cada una) explotaba con `NullPointerException`, reproducible incluso sin ningún código propio de por medio, solo instanciando `ListGraph` + `DijkstraSolver` tal como estaban en el repo.

La causa: en `SimpleLinkedPriorityQueue.enqueue()`, la rama `else` (inserción en el medio o al final de la lista) nunca actualizaba el campo `last` cuando el nodo nuevo terminaba siendo el último de la cola. Con un solo vecino por nodo el bug no se disparaba (por eso había pasado desapercibido), pero con cualquier grafo donde un nodo tenga 2+ conexiones, sí.

El fix (una sola línea agregada, documentada con comentario en el propio archivo) en `SimpleLinkedPriorityQueue.java`:

```java
current.next = addedNode;

// Fix: si el nodo nuevo quedo al final de la lista (no se inserto en
// el medio), "last" tiene que pasar a apuntar a el. Antes de este
// cambio "last" quedaba desactualizado, lo que corrompia la lista
// enlazada en los siguientes enqueue/dequeue (NullPointerException
// en escenarios con mas de un vecino por nodo, como en TP11 - GPS).
if (addedNode.next == null)
    last = addedNode;
```

No se tocó ningún otro archivo ni lógica. Con este fix aplicado, `DijkstraSolver.dijkstraAllNodes(...)` funciona correctamente y es lo que usa `GPSExercise` para calcular el camino más corto.

(Nota aparte, que no llegó a afectar a este TP: `DijkstraSolver` también usa `SimpleArrayDictionary` por dentro, que tiene un bug de resize distinto — el array interno nunca crece más allá de la capacidad por defecto de 4. Con nuestro grafo de exactamente 4 sedes no se dispara, pero si el grafo creciera a 5+ vértices, explotaría igual. Queda pendiente para quien lo necesite en otro TP.)

## Flujo general
1. `MainProgram` crea `GPSExercise`.
2. El constructor carga las 4 sedes y sus conexiones (ida y vuelta) en el `Graph<String>`.
3. Al iniciar se imprime el grafo completo, arista por arista.
4. El menú ofrece calcular el camino más corto (fijo, Belgrano → Monserrat), volver a mostrar el grafo, o salir.
5. Al calcular, se corre Dijkstra desde el origen sobre todo el grafo, se valida que el destino sea alcanzable, y se reconstruye el camino recorriendo el mapa de "anteriores" con una pila (para invertir el orden).

## Rol de cada método

### `GPSExercise`
**Rol:** única clase del TP11; carga el grafo, calcula caminos y maneja el menú de consola.

**Atributos principales:**
- `ORIGEN`, `DESTINO`: constantes con las sedes fijas del cálculo.
- `grafo`: instancia de `Graph<String>` (`ListGraph`).
- `firstTime`: controla el mensaje de bienvenida y la primera impresión del grafo.

**Métodos:**
- `GPSExercise(Scanner scanner)`: carga las 4 sedes y sus conexiones.
- `agregarConexion(String, String, int)`: agrega una arista en ambos sentidos (el mapa se trata como no dirigido).
- `exerciseLogic()` / `menuLogic()`: loop del ejercicio; muestra el menú y despacha segun el comando.
- `calcularLogic()`: llama a `dijkstra.dijkstraAllNodes(ORIGEN, grafo)`, valida que `DESTINO` sea alcanzable (`weight != Integer.MAX_VALUE`), imprime el camino y la distancia total.
- `reconstruirCamino(...)`: recorre la tabla que devuelve `DijkstraSolver` desde el destino hacia el origen (siguiendo el campo `destination` de cada `Edge`, que guarda el nodo anterior), apilando cada paso, y lo vuelca a una lista en el orden correcto.
- `mostrarGrafo()`: imprime cada arista del grafo (`origen -> destino: peso`).

## Manejo de errores e inputs inválidos
- Cualquier texto que no sea `calcular`, `mostrar` o `mm` en el menú se rechaza con un mensaje, sin cortar la ejecución.
- El cálculo del camino no depende de ningún input del usuario (origen y destino son fijos), por lo que no hay parseo de texto ni riesgo de datos inválidos en esa parte.
- Si el destino fuera inalcanzable, se informa explícitamente en vez de intentar reconstruir un camino inexistente (no aplica con el grafo actual, que está totalmente conectado, pero queda contemplado).

## Decisiones de UX visibles en TP11
- El grafo se imprime completo apenas arranca el ejercicio, para que quede claro qué mapa se está usando antes de pedir cualquier cálculo.
- El menú deja explícito, en el propio texto de la opción, cuál es el camino que se va a calcular (`Belgrano a Monserrat`), ya que no se pide por teclado.
- Se usa `->` en vez de la flecha Unicode del enunciado para evitar problemas de encoding en consolas que no manejen UTF-8.

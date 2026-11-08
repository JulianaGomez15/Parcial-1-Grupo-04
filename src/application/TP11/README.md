# TP11 - GPS (la misión del mago)

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
En este repositorio, TP11 implementa la opción **GPS**, con una ambientación de aventura en vez de un mapa real: un mago tiene la misión de llegar desde la **Torre del Mago** hasta el **Castillo del Dragón**, y para eso cuenta con varios caminos posibles entre distintos lugares del reino.

- grafo pre-cargado con 4 lugares (Torre del Mago, Bosque Encantado, Castillo del Dragón, Pantano Oscuro) y el esfuerzo de cada tramo;
- impresión del mapa completo al iniciar, arista por arista;
- al calcular, se listan **todos los caminos posibles** entre origen y destino (no solo el resultado final), cada uno con su esfuerzo total, y se marca cuál es el más conveniente según Dijkstra — así se ve la comparación, no solo la respuesta.

No se implementó edición del grafo: la consigna la marca como opcional, y se decidió mantener el alcance acotado a lo pedido.

El cálculo se apoya en el **`DijkstraSolver` del propio `graphModule`** (no una reimplementación propia) para determinar cuál de los caminos encontrados es el de menor esfuerzo — ver más abajo el bug que tuvo que arreglarse para que funcione.

## Por qué el origen y destino son fijos (y no se piden por teclado)
La consigna pide "un punto origen y un punto destino", pero no exige que se ingresen interactivamente. Para mantener el ejercicio simple y acotado se fijaron ambos puntos en el código (Torre del Mago → Castillo del Dragón), en vez de agregar un menú de selección de lugar. El algoritmo de Dijkstra sigue siendo genérico (recibe cualquier origen) y se aplica igual sobre todo el grafo.

## Por qué se muestran todos los caminos, no solo el resultado
Mostrar únicamente "el camino más corto es X" no deja ver *por qué* lo es. Por eso `calcularLogic()` enumera todas las alternativas reales entre origen y destino (caminos simples, sin repetir lugares) con su esfuerzo total, y recién ahí marca cuál es el más conveniente — a la manera de una aventura con varias rutas posibles, donde cada una tiene un costo distinto.

## Arquitectura de TP11
A diferencia de los TPs anteriores, TP11 es **una sola clase**:

- **`GPSExercise`**: carga el grafo, lo imprime, busca y compara los caminos posibles usando `DijkstraSolver`, y expone el menú de consola.

No hay una clase de dominio separada (como `Escena` o `Usuario` en otros TPs) porque el TDA relevante (`Graph`/`Edge`/`DijkstraSolver`) ya lo provee `graphModule`, y no hace falta modelar nada adicional por fuera de esos vértices (`String`, el nombre del lugar).

TP11 se apoya en la infraestructura general del proyecto:
- `MainProgram` para seleccionar `GPSExercise`.
- `Exercise` como clase base con el loop principal.
- `graphModule` (`Graph<T>`, `Edge<T>`, `ListGraph<T>`, `DijkstraSolver<T>`) para el grafo y el algoritmo.
- `priorityQueueModule` (`SimpleLinkedPriorityQueue`), con el fix descrito más abajo — usado internamente por `DijkstraSolver`.
- `SimpleList` / `SimpleArrayList` para armar los caminos encontrados.
- `SimpleSet` / `SimpleArraySet` para llevar el registro de lugares visitados durante la búsqueda (evita ciclos).

## Bug encontrado y arreglado en `priorityQueueModule`
Al usar `DijkstraSolver` contra un grafo real (lugares con más de un vecino cada uno) explotaba con `NullPointerException`, reproducible incluso sin ningún código propio de por medio, solo instanciando `ListGraph` + `DijkstraSolver` tal como estaban en el repo.

La causa: en `SimpleLinkedPriorityQueue.enqueue()`, la rama `else` (inserción en el medio o al final de la lista) nunca actualizaba el campo `last` cuando el nodo nuevo terminaba siendo el último de la cola. Con un solo vecino por nodo el bug no se disparaba (por eso había pasado desapercibido), pero con cualquier grafo donde un nodo tenga 2+ conexiones, sí.

El fix (una línea agregada, documentada con comentario en el propio archivo) en `SimpleLinkedPriorityQueue.java`:

```java
current.next = addedNode;

// Fix: si el nodo nuevo termino siendo el ultimo, "last" tenia que
// actualizarse y no lo hacia, lo que rompia la lista mas adelante.
if (addedNode.next == null)
    last = addedNode;
```

No se tocó ningún otro archivo ni lógica. Con este fix aplicado, `DijkstraSolver.dijkstraAllNodes(...)` funciona correctamente y es lo que usa `GPSExercise` para determinar el esfuerzo mínimo real, con el que se compara cada camino encontrado.

(Nota aparte, que no llegó a afectar a este TP: `DijkstraSolver` también usa `SimpleArrayDictionary` por dentro, que tiene un bug de resize distinto — el array interno nunca crece más allá de la capacidad por defecto de 4. Con nuestro grafo de exactamente 4 lugares no se dispara, pero si el grafo creciera a 5+ vértices, explotaría igual. Queda pendiente para quien lo necesite en otro TP.)

## Flujo general
1. `MainProgram` crea `GPSExercise`.
2. El constructor carga los 4 lugares y sus conexiones (ida y vuelta) en el `Graph<String>`.
3. Al iniciar se imprime el mapa completo, arista por arista.
4. El menú ofrece calcular los caminos posibles (origen y destino fijos), volver a mostrar el mapa, o salir.
5. Al calcular: se corre Dijkstra desde el origen para saber el esfuerzo mínimo real; se buscan por separado, con backtracking, todos los caminos simples entre origen y destino; se imprime cada uno con su esfuerzo, marcando el que coincide con el mínimo de Dijkstra.

## Rol de cada método

### `GPSExercise`
**Rol:** única clase del TP11; carga el grafo, busca y compara caminos, y maneja el menú de consola.

**Atributos principales:**
- `ORIGEN`, `DESTINO`: constantes con los lugares fijos del cálculo (`"Torre del Mago"`, `"Castillo del Dragon"`).
- `grafo`: instancia de `Graph<String>` (`ListGraph`).
- `firstTime`: controla el mensaje de bienvenida y la primera impresión del mapa.

**Métodos:**
- `GPSExercise(Scanner scanner)`: carga los 4 lugares y sus conexiones.
- `agregarConexion(String, String, int)`: agrega una arista en ambos sentidos (el mapa se trata como no dirigido).
- `exerciseLogic()` / `menuLogic()`: loop del ejercicio; muestra el menú y despacha según el comando.
- `calcularLogic()`: obtiene el esfuerzo mínimo real vía `DijkstraSolver`, dispara `buscarCaminos(...)` para encontrar todas las alternativas, e imprime cada una marcando la de menor esfuerzo.
- `buscarCaminos(...)`: búsqueda en profundidad (DFS) con backtracking sobre `Graph`, que encuentra todos los caminos simples (sin repetir lugares) entre `ORIGEN` y `DESTINO`, acumulando el esfuerzo de cada uno.
- `copiarCamino(...)`: clona el camino acumulado en el momento de llegar al destino, para que el backtracking no lo pise después.
- `imprimirCamino(...)`: imprime un camino numerado, con su esfuerzo, y marca si es el más conveniente.
- `mostrarGrafoCompacto()`: versión corta para la bienvenida — un lugar por línea con todos sus vecinos, sin esfuerzo.
- `mostrarGrafoCompleto()`: versión arista por arista con su esfuerzo (formato que pide la consigna), se muestra con el comando `mostrar`.

## Manejo de errores e inputs inválidos
- Cualquier texto que no sea `calcular`, `mostrar` o `mm` en el menú se rechaza con un mensaje, sin cortar la ejecución.
- El cálculo no depende de ningún input del usuario (origen y destino son fijos), por lo que no hay parseo de texto ni riesgo de datos inválidos en esa parte.
- Si el destino fuera inalcanzable, se informa explícitamente en vez de intentar buscar caminos inexistentes (no aplica con el grafo actual, que está totalmente conectado, pero queda contemplado).

## Decisiones de UX visibles en TP11
- El mapa se imprime en versión compacta (sin esfuerzo, un lugar por línea) apenas arranca el ejercicio, para que quede claro qué grafo se está usando sin saturar la pantalla de números. La versión completa, arista por arista con su esfuerzo — el formato que pide la consigna — está disponible con el comando `mostrar`.
- Al calcular, se muestran **todas** las alternativas de camino, no solo la ganadora, para que se entienda por qué Dijkstra eligió la que eligió (por ejemplo, un camino directo de esfuerzo 9 le gana a uno de dos tramos que suma 10, aunque ambos sean válidos).
- El menú deja explícito, en el propio texto de la opción, cuál es la misión que se va a resolver (origen → destino), ya que no se pide por teclado.
- Se usa `->` en vez de la flecha Unicode del enunciado para evitar problemas de encoding en consolas que no manejen UTF-8.

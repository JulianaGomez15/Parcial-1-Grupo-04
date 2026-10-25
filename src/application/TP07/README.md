# TP07 - Organizador de e-mail

## Materia
Algoritmos y Estructuras de Datos II

## Consigna del ejercicio
Desarrollar una aplicación de consola en Java que utilice el TDA PriorityQueue. Se eligió la opción **Sistema de organización de e-mail**: los mails recibidos se organizan en 3 pestañas (Principal, Notificaciones y Spam). Cada mail contiene un asunto, la pestaña a la que se asigna y un nivel de prioridad "Alto" o "Bajo".

Los mails los genera el mismo usuario de la app, que simula ser quien los escribe para probar la correcta asignación de categoría y orden. El usuario completa los 3 datos.

### Observaciones
- La aplicación debe manejar inputs inválidos del usuario (prioridades o categorías inválidas) con `if`, avisando y volviendo a pedir el dato. No se usa `throw` para esos casos.
- Antes de sacar un mail se pregunta si la pestaña está vacía. Así no se llama a `dequeue` sobre una cola vacía.
- Las decisiones de experiencia de usuario se documentan para poder explicarlas en el parcial.

## Qué implementa este proyecto en TP07
- Escribir un mail indicando asunto, pestaña y prioridad.
- Ver el contenido de una pestaña en el orden en que se leerían los mails.
- Abrir el siguiente mail de una pestaña (el más prioritario), que queda marcado como leído y sale de la bandeja.
- Resumen permanente con la cantidad de mails sin leer por pestaña.

## Arquitectura de TP07
1. **Modelo de dominio**
   - `Pestania`: enum con las 3 pestañas posibles.
   - `Prioridad`: enum con las prioridades Alta y Baja, y su valor numérico para la cola.
   - `Mail`: asunto, pestaña y prioridad.

2. **Servicio**
   - `BandejaEntrada`: administra una `SimplePriorityQueue<Mail>` por pestaña.

3. **Interfaz de consola y orquestación**
   - `EmailExercise`: menú, validaciones, flujo por fases y salida por consola.

TP07 se integra con la infraestructura general del proyecto:
- `MainProgram` permite elegir `TP07 - EmailExercise` (opción 6).
- `Exercise` proporciona el ciclo de ejecución y el manejo de fases.
- `SimplePriorityQueue` / `SimpleArrayPriorityQueue` son la estructura principal de cada pestaña.
- `SimpleList` / `SimpleArrayList` se usan para mostrar el contenido de una pestaña sin perderlo.

## Uso del TDA priorityQueueModule
Cada pestaña es una cola de prioridad independiente. Al recibir un mail, `BandejaEntrada` lo encola en la cola de su pestaña con la prioridad `Prioridad.getValor()`:

| Prioridad | Valor en la cola |
|---|---|
| Alta | 0 |
| Baja | 1 |

En el TDA el número más chico sale primero, por eso Alta vale 0.

**¿Por qué una cola por pestaña y no una sola cola?** Las pestañas son independientes: el usuario lee Principal sin que le aparezcan mails de Spam. Con una sola cola habría que recorrerla y filtrar, perdiendo la ventaja de que el próximo mail ya esté al frente. Con tres colas, `dequeue` sobre la cola de la pestaña devuelve directamente el siguiente mail correcto.

**¿Por qué `SimpleArrayPriorityQueue`?** Al insertar, solo desplaza los elementos con prioridad estrictamente mayor (`priority < priorities[i - 1]`). Así, entre mails de la misma prioridad se respeta el orden de llegada (FIFO), que es lo esperable en una bandeja de entrada.

**Mostrar una pestaña sin perder los mails.** La cola solo permite ver el primero (`peek`). Para listar todos, `snapshot` los saca uno por uno guardándolos en una lista y después los vuelve a encolar en el mismo orden. Como la cola respeta el orden de llegada entre iguales, queda exactamente como estaba. Es la misma técnica que `ColaImpresion.copiarSinPerder` del TP05.

## Base de datos inicial
Para facilitar las pruebas, al iniciar TP07 se cargan estos mails:

| Asunto | Pestaña | Prioridad |
|---|---|---|
| Reunion de grupo del parcial | Principal | Baja |
| Entrega TP07 manana | Principal | Alta |
| Alguien comento tu publicacion | Notificaciones | Baja |
| Nuevo inicio de sesion en tu cuenta | Notificaciones | Alta |
| Ganaste un premio!!! | Spam | Baja |

En Principal y Notificaciones el de prioridad Baja se cargó primero a propósito: al ver la pestaña, el de prioridad Alta aparece adelante aunque haya llegado después.

## Opciones del menú
| Opción | Acción |
|---|---|
| 1 | Escribir mail: pide asunto, pestaña y prioridad. |
| 2 | Ver pestaña: lista los mails en el orden en que se leerán. |
| 3 | Abrir siguiente mail de una pestaña: muestra el más prioritario y lo saca de la bandeja. |
| `mm` | Volver al menú principal del proyecto. |

## Rol de cada clase y métodos

### `Pestania`
**Rol:** enum con las pestañas válidas (`PRINCIPAL`, `NOTIFICACIONES`, `SPAM`).
- `getNombre()` / `toString()`: nombre legible para la consola.

### `Prioridad`
**Rol:** enum con las prioridades válidas (`ALTA`, `BAJA`).
- `getNombre()` / `toString()`: nombre legible.
- `getValor()`: número que se usa como prioridad en la cola (Alta = 0, Baja = 1).

### `Mail`
**Rol:** entidad de dominio.
- `Mail(String asunto, Pestania pestania, Prioridad prioridad)`: guarda los datos ya validados por la consola y recorta el asunto. `esValido` indica si el asunto, la pestaña y la prioridad sirven antes de construirlo.
- `getAsunto()`, `getPestania()`, `getPrioridad()`: devuelven los datos.
- `toString()`: `[Prioridad] Asunto`.

### `BandejaEntrada`
**Rol:** servicio que organiza los mails en una cola de prioridad por pestaña.

**Atributos:** `principal`, `notificaciones`, `spam` (`SimplePriorityQueue<Mail>`).

**Métodos:**
- `recibir(Mail mail)`: encola el mail en la cola de su pestaña según su prioridad.
- `abrirSiguiente(Pestania pestania)`: si la pestaña está vacía o no existe, devuelve `null` y no llama a `dequeue`. Si hay mails, saca y devuelve el más prioritario.
- `isEmpty(Pestania pestania)` / `cantidad(Pestania pestania)`: estado de la pestaña.
- `snapshot(Pestania pestania)`: devuelve una lista con los mails en orden de lectura, sin modificar la cola.
- `colaDe(Pestania pestania)`: devuelve la cola que corresponde a la pestaña.

### `EmailExercise`
**Rol:** controlador de TP07; menú, validaciones y presentación.

**Atributos:** `bandeja`, `firstTime` (mensaje de bienvenida), `CANCELAR` (palabra clave para abortar).

**Métodos:**
- `EmailExercise(Scanner scanner)`: inicializa y carga la base de datos.
- `cargarBaseDeDatos()`: precarga mails de prueba.
- `exerciseLogic()`: despacha según la fase (menú, escribir, ver, abrir).
- `menuLogic()`: muestra el resumen y las opciones e interpreta el comando.
- `escribirMailLogic()`: pide los 3 datos, crea el mail, lo envía a la bandeja e informa en qué posición quedó.
- `verPestaniaLogic()`: pide una pestaña y la muestra.
- `abrirMailLogic()`: pide una pestaña, verifica que no esté vacía y abre el siguiente mail.
- `mostrarResumen()`: cantidad de mails sin leer por pestaña.
- `mostrarPestania(Pestania pestania)`: lista numerada de los mails de la pestaña.
- `posicionEnPestania(Mail mail)`: busca la posición en que quedó un mail recién recibido.
- `pedirPestania(String mensaje)` / `pedirPrioridad()`: piden una opción válida por número o por nombre, con opción de cancelar.
- `pedirTexto(String mensaje)`: texto no vacío con opción de cancelar.
- `volverAlMenu()`: vuelve a la fase 0.

## Manejo de errores e inputs inválidos
- **Pestañas y prioridades inválidas:** se vuelven a pedir con un mensaje que indica las opciones válidas. Como son `enum`, es imposible crear un mail con una categoría inexistente.
- **Asunto vacío:** no se acepta.
- **`cancelar`:** en cualquier dato abandona la operación sin crear nada.
- **Pestaña vacía:** antes de abrir un mail se verifica `isEmpty`. Si no hay mails, se avisa y no se llama a `dequeue`.
- **Opciones del menú inválidas:** se informa y se vuelve a mostrar el menú.

La consola valida el asunto, la pestaña y la prioridad antes de crear el mail. `BandejaEntrada` devuelve `false` o `null` si la operación no se puede hacer, y no usa `throw` para un mail nulo o una pestaña vacía.

## Decisiones de UX visibles en TP07
- **Resumen siempre visible:** el menú muestra cuántos mails sin leer hay en cada pestaña, como el contador de una bandeja real.
- **Selección por número o por nombre:** se puede escribir `1` o `Principal` (sin importar mayúsculas). Los números evitan errores de tipeo y los nombres resultan naturales.
- **Confirmación de posición:** al escribir un mail se informa en qué posición quedó dentro de su pestaña. Así el usuario comprueba en el momento que la asignación de categoría y orden es correcta, que es el objetivo de la consigna.
- **"Ver" no modifica, "abrir" sí:** ver una pestaña no saca mails. Abrir uno lo marca como leído y lo quita, igual que un `dequeue`.
- **Mismo orden al ver y al abrir:** la lista de "Ver pestaña" es exactamente el orden en que "Abrir siguiente" los va a entregar.

## Pruebas manuales sugeridas
1. Ver Principal: `Entrega TP07 manana` (Alta) debe aparecer antes que `Reunion de grupo del parcial` (Baja) aunque se cargó después.
2. Escribir un mail Alta en Principal: debe quedar en la posición 2 (después del otro Alta y antes de los Baja).
3. Escribir un mail Baja en Principal: debe quedar último.
4. Abrir el siguiente mail de Principal: debe salir el Alta más antiguo y el contador debe bajar.
5. Abrir el mail de Spam dos veces: la segunda vez debe avisar que no hay mails, sin crashear.
6. Ingresar pestañas y prioridades inválidas (`4`, `urgente`, texto vacío) y comprobar que se vuelven a pedir.
7. Escribir `cancelar` en cualquier paso y comprobar que no se crea el mail.

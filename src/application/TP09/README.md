# TP09 - Agenda de contactos

## Materia
Algoritmos y Estructuras de Datos II

## Consigna del ejercicio
Desarrollar una aplicación de consola en Java que utilice el TDA Binary Search Tree. Se eligió la opción **Aplicación de contactos**: se pueden crear contactos con nombre, número y mail, editar cualquiera de esos datos o borrar el contacto. No puede haber dos contactos con el mismo nombre.

### Observaciones
- Tiene que haber una opción que muestre todos los contactos.
- Al iniciar, la estructura arranca vacía. Hay una opción del menú para cargar una base de datos pre-programada y facilitar el testeo.
- La aplicación maneja inputs inválidos del usuario con `if`, le muestra el problema y lo deja corregirlo. No se usa `throw` para una validación de negocio.
- El único `try/catch` es el de `NumberFormatException` al convertir un texto a número.
- Las decisiones de experiencia de usuario se documentan para poder explicarlas en el parcial.

## Qué implementa este proyecto en TP09
- Crear un contacto con nombre, teléfono y mail.
- Ver todos los contactos en orden alfabético.
- Editar el nombre, el teléfono o el mail de un contacto existente.
- Borrar un contacto.
- Cargar cinco contactos de prueba, sin pisar un nombre que ya esté cargado.

## Arquitectura de TP09
1. **Modelo de dominio**
   - `Contacto`: nombre, teléfono y mail. El nombre es la clave de comparación.

2. **Servicio**
   - `AgendaContactos`: administra un `BST<Contacto>`.

3. **Interfaz de consola y orquestación**
   - `ContactosExercise`: menú, validaciones, flujo por fases y salida por consola.

TP09 se integra con la infraestructura general del proyecto:
- `MainProgram` permite elegir `TP09 - Contactos Exercise` (opción 9).
- `Exercise` proporciona el ciclo de ejecución y el manejo de fases.
- `BST` es la estructura de la agenda.
- `SimpleList` / `SimpleArrayList` guardan los mismos contactos del árbol, en el orden alfabético del in-order, para poder numerarlos. `BST.inOrder` solo los imprime.

## Uso del TDA treeModule
Cada contacto es un nodo del árbol. `Contacto.compareTo` compara el nombre sin distinguir mayúsculas, así que el BST queda ordenado alfabéticamente y dos nombres iguales (por ejemplo `Ana` y `ana`) ocupan el mismo lugar.

| Operación de la agenda | Operación del BST |
|---|---|
| Crear | `insert`, y el mismo objeto entra en la lista ordenada |
| Saber si el nombre ya existe | `contains` con un contacto de búsqueda |
| Recuperar el contacto guardado | la lista, porque el `BST` no devuelve el nodo |
| Ver todos | la lista ordenada. Sigue el orden del in-order |
| Borrar | `remove`, y se saca de la lista |
| Cambiar el nombre | `remove` + `insert`, y se reubica en la lista |
| Cantidad | `listado.size()`, porque el `BST` no expone el tamaño |

**¿Por qué el nombre es la clave?** La consigna prohíbe dos contactos con el mismo nombre. El teléfono y el mail se pueden repetir (dos personas pueden compartir un número) y no definen el orden.

**¿Por qué in-order y no BFS?** `levelOrder` recorre por niveles. En una agenda el orden útil es el alfabético, y eso es exactamente el in-order de un BST. Este `BST` no devuelve ese recorrido: `inOrder` solo lo imprime. La agenda guarda los mismos objetos en una `SimpleList` e inserta cada uno según `compareTo`, así que la lista queda en el mismo orden y el menú puede numerarla.

**¿Por qué cambiar el nombre saca el nodo y lo vuelve a insertar?** El árbol está ordenado por nombre. Si solo se cambiara el texto, el nodo quedaría en una rama que ya no corresponde. El teléfono y el mail no participan de `compareTo`, así que editarlos modifica el mismo objeto que está guardado en el nodo.

**Búsqueda sin inventar un teléfono.** `contains` compara con `compareTo`. `Contacto.paraBuscar` arma un contacto que solo tiene nombre, para preguntarle al árbol si ese nombre está. Ese objeto no se inserta. El contacto de verdad, con teléfono y mail, se recupera de la lista.

## Validaciones, sin `throw`
Un dato incorrecto, un nombre repetido o un contacto que no está no se resuelven con `throw`. Se preguntan con un `if` antes de tocar el árbol. Si la operación no corresponde, el método devuelve `false` o no hace nada, y la consola le explica al usuario qué pasó. El programa sigue en el menú.

| Situación | Qué pasa |
|---|---|
| `insert` de `null` o de un valor que ya está | El BST no lo agrega. |
| `remove` de `null` o de un valor que no está | El BST queda igual. |
| `contains` con `null` | No se llama: la agenda valida el nombre antes. |
| Nombre vacío, teléfono o mail inválido, nombre repetido, contacto que no está | `AgendaContactos` devuelve `false` y no modifica el árbol. |

## Base de datos inicial
La agenda arranca vacía. La opción 5 carga estos contactos, que son los mails del equipo:

| Nombre | Teléfono | Mail |
|---|---|---|
| Luka Ruiz Diaz | 11 5555-0101 | luka.ruizdiaz@uade.edu.ar |
| Betsabe Rodriguez | 11 5555-0102 | betsabe.rodriguez@uade.edu.ar |
| Trinidad Garcia Valicente | 11 5555-0103 | trinidad.garciavalicente@uade.edu.ar |
| Juliana Gomez | 11 5555-0104 | juliana.gomez@uade.edu.ar |
| Diego Cinquino | 11 5555-0105 | diego.cinquino@uade.edu.ar |

Si alguno de esos nombres ya está, no se vuelve a insertar. Al ver la agenda el orden es alfabético: Betsabe, Diego, Juliana, Luka, Trinidad. No es el orden en que se cargaron, y eso muestra que el BST ordena al insertar.

## Opciones del menú
| Opción | Acción |
|---|---|
| 1 | Crear contacto: pide nombre, teléfono y mail. |
| 2 | Ver todos los contactos, numerados y en orden alfabético. |
| 3 | Editar el nombre, el teléfono o el mail de un contacto de la lista. |
| 4 | Borrar un contacto de la lista, con confirmación. |
| 5 | Cargar los contactos de prueba que todavía no están. |
| `mm` | Volver al menú principal del proyecto. |

## Rol de cada clase y métodos

### `Contacto`
**Rol:** entidad de dominio y clave del árbol.
- `Contacto(String nombre, String telefono, String mail)`: lo usa la agenda cuando los tres datos ya son válidos. Recorta espacios con `trim`.
- `paraBuscar(String nombre)`: contacto auxiliar, solo con nombre, para `contains`.
- `esNombreValido(String nombre)`: no nulo y no vacío después de `trim`.
- `esTelefonoValido(String telefono)`: solo dígitos, espacios, `+`, `-` y paréntesis, con al menos 6 dígitos.
- `esMailValido(String mail)`: una sola `@`, texto antes y un dominio con un punto que no quede al borde. No se exige un dominio en particular, porque un contacto puede ser de cualquier mail.
- `getNombre()`, `getTelefono()`, `getMail()`: devuelven los datos.
- `setNombre(String nombre)`, `setTelefono(String telefono)`, `setMail(String mail)`: si el dato sirve, lo reemplazan y devuelven `true`. Si no, dejan el anterior y devuelven `false`.
- `compareTo(Contacto other)`: ordena por nombre sin distinguir mayúsculas.
- `equals(Object other)` / `hashCode()`: misma identidad que `compareTo`.
- `toString()`: `Nombre | teléfono | mail`.

### `AgendaContactos`
**Rol:** servicio que guarda los contactos en un BST.

**Atributos:** `contactos` (`BST<Contacto>`), `listado` (los mismos contactos en orden alfabético), `DATOS_DE_PRUEBA`.

**Métodos:**
- `agregar(String nombre, String telefono, String mail)`: devuelve `false` si el nombre está vacío, el teléfono o el mail no sirven, o el nombre ya existe. Si no, inserta y devuelve `true`.
- `existe(String nombre)`: consulta el árbol por nombre. Con un nombre vacío devuelve `false`.
- `buscar(String nombre)`: si el nombre está en el árbol, devuelve el contacto de la lista. Si no, devuelve `null`.
- `editarNombre(Contacto contacto, String nuevoNombre)`: devuelve `false` si el contacto no está, el nombre está vacío o ya lo tiene otro. Si solo cambia la capitalización, actualiza el texto. Si el nombre es otro, saca el nodo y lo inserta de nuevo.
- `editarTelefono(Contacto contacto, String telefono)` / `editarMail(Contacto contacto, String mail)`: devuelven `false` si el contacto no está o el dato no sirve. Si sirve, cambian el objeto que ya está en el nodo.
- `borrar(Contacto contacto)`: devuelve `false` y no llama a `remove` si el contacto no está. Si está, lo saca y devuelve `true`.
- `cargarDatosDePrueba()`: inserta los contactos de la tabla que todavía no existen y devuelve cuántos agregó.
- `cantidadDatosDePrueba()`: tamaño de esa tabla.
- `listar()`: copia de la lista, en el orden alfabético del in-order.
- `size()`: cantidad de contactos de la lista. `isEmpty()`: pregunta al árbol.
- `estaEnAgenda(Contacto contacto)`: `true` solo si el contacto no es nulo y está en el árbol.

### `ContactosExercise`
**Rol:** controlador de TP09; menú, validaciones y presentación.

**Atributos:** `agenda`, `firstTime` (mensaje de bienvenida), `CANCELAR`.

**Métodos:**
- `ContactosExercise(Scanner scanner)`: crea la agenda vacía. No carga los datos de prueba.
- `exerciseLogic()`: despacha según la fase (menú, crear, ver, editar, borrar, cargar).
- `menuLogic()`: muestra la cantidad de contactos y las opciones.
- `crearLogic()`: pide los tres datos, rechaza el nombre repetido y crea el contacto.
- `verLogic()`: lista la agenda.
- `editarLogic()`: elige un contacto por número y después el campo a cambiar.
- `editarNombre(Contacto contacto)`, `editarTelefono(Contacto contacto)`, `editarMail(Contacto contacto)`: piden el valor nuevo y delegan en la agenda.
- `borrarLogic()`: elige un contacto, pide confirmación y lo borra.
- `cargarDatosLogic()`: carga la base y, si entró alguno, muestra la agenda ordenada.
- `elegirContacto(String accion)`: si la agenda está vacía avisa y no llama a `remove`. Si no, pide un número de la lista.
- `sigueEnAgenda(Contacto contacto)`: antes de editar o borrar, confirma con `existe` que el nombre sigue en el árbol.
- `mostrarContactos()`: lista numerada en orden alfabético.
- `pedirTexto(String mensaje)`, `pedirTelefono()`, `pedirMail()`, `pedirEntero(...)`: entradas con reintento y `cancelar`.
- `confirmar(String mensaje)`: acepta solo `s` o `n`.
- `volverAlMenu()`: vuelve a la fase 0.

## Manejo de errores e inputs inválidos
No se usa `throw` para un dato mal ingresado, un nombre repetido, una agenda vacía o una operación que no se puede hacer. Esos casos se anticipan con `if` / `else`. El usuario ve el mensaje y el programa sigue en el menú, para que pueda corregir.

El único `try/catch` está en `pedirEntero`: `Integer.parseInt` puede lanzar `NumberFormatException` cuando el texto no es un número. Después de convertirlo, el rango se controla con un `if`, no con otra excepción.

- Nombre vacío, teléfono inválido o mail inválido: se vuelven a pedir. No se crea el contacto.
- Nombre repetido: se avisa y no se inserta. `Diego Cinquino` y `diego cinquino` cuentan como el mismo nombre.
- Agenda vacía o número fuera de la lista: se avisa y no se llama a `remove` ni a `get` con un índice inválido.
- Antes de editar o borrar, `sigueEnAgenda` pregunta si ese nombre sigue estando.
- `cancelar` abandona la operación. Borrar pide `s` o `n`. Una opción de menú inválida vuelve a mostrar el menú.
- La carga de prueba usa `agregar`, que devuelve `false` si el nombre ya existe, y sigue con el siguiente.

`Contacto` y `AgendaContactos` no imprimen ni lanzan. Devuelven `true` o `false`. La consola es la que muestra el mensaje. `BST.insert` y `BST.remove` tampoco lanzan: si el valor no corresponde, no modifican el árbol.

## Decisiones de UX visibles en TP09
- **Arranque vacío y visible:** el primer mensaje dice que la agenda está vacía y que la opción 5 carga los datos de prueba. El menú siempre muestra cuántos contactos hay.
- **Lista numerada:** para editar o borrar se elige un número de la lista, no se reescribe el nombre. Así no falla por un espacio o una mayúscula de más. El nombre igual se pide al crear, porque ahí es un dato nuevo.
- **El nombre repetido se corta temprano:** al crear, si el nombre ya existe no se piden teléfono ni mail.
- **Ver después de cargar:** si la carga agregó contactos, se muestra la lista. El orden alfabético (y no el de la tabla) se ve en el momento.
- **Confirmación solo al borrar:** crear y editar se pueden abandonar con `cancelar`. Borrar además pide `s`, porque no se puede deshacer.
- **Misma persona, otra capitalización:** cambiar `luka ruiz diaz` por `Luka Ruiz Diaz` actualiza cómo se muestra, sin tratarlo como otro contacto.

## Pruebas manuales sugeridas
1. Entrar a TP09 y ver la agenda: tiene que decir 0 contactos.
2. Elegir 2: tiene que avisar que está vacía, sin tirar una excepción.
3. Elegir 5: tienen que aparecer 5 contactos, con Betsabe primero y Trinidad último.
4. Volver a elegir 5: no tiene que duplicar a nadie.
5. Crear `Diego Cinquino` después de cargar: tiene que rechazarlo. Crear `Ana Lopez` con teléfono `11 4444-0000` y mail `ana@mail.com`: tiene que quedar entre Betsabe y Diego.
6. Editar el teléfono de Ana y ver la lista: el nombre sigue en el mismo lugar y el teléfono cambió.
7. Renombrar a Ana como `Zoe Lopez` y ver la lista: tiene que pasar al final.
8. Intentar renombrar a Zoe como `Luka Ruiz Diaz`: tiene que rechazarlo y Zoe tiene que seguir estando.
9. Borrar un contacto y responder `n`: tiene que seguir en la lista. Repetirlo con `s`: tiene que desaparecer y el contador bajar.
10. Probar nombre vacío, teléfono `abc`, mail `ana`, un número fuera de la lista, texto en vez de número, `cancelar` y una opción de menú inexistente. La aplicación tiene que seguir en el menú.

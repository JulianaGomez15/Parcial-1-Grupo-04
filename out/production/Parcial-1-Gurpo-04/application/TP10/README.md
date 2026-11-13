# TP10 - Agenda de contactos con AVL

## Materia
Algoritmos y Estructuras de Datos II

## Consigna del ejercicio
Crear una aplicación de consola de agenda de contactos basada en un árbol AVL. Se pueden crear contactos con nombre, teléfono y mail, editar cualquiera de esos datos y borrar contactos. No puede haber dos contactos con el mismo nombre.

### Observaciones
- Tiene que haber una opción para mostrar todos los contactos.
- La agenda arranca vacía y ofrece una opción para cargar datos de prueba.
- La aplicación valida los datos ingresados y muestra mensajes si no son válidos, sin terminar inesperadamente.
- Las decisiones de interacción y experiencia de usuario se documentan para poder explicarlas durante el parcial.

## Qué implementa este proyecto en TP10
- Crear contactos con nombre, teléfono y mail.
- Mostrar los contactos numerados y en orden alfabético.
- Editar el nombre, el teléfono o el mail de un contacto existente.
- Borrar un contacto con confirmación.
- Cargar cinco contactos de prueba, sin duplicar nombres existentes.
- Usar un AVL para conservar el balance durante inserciones y remociones.

## Arquitectura de TP10
1. **Modelo de dominio**
   - `ContactoAVL`: contiene nombre, teléfono y mail; el nombre es la clave de comparación.

2. **Servicio de agenda**
   - `AgendaContactosAVL`: valida operaciones y administra el árbol AVL y la lista ordenada de contactos.

3. **Interfaz de consola y orquestación**
   - `ContactosAVLExercise`: presenta el menú, valida entradas y coordina las operaciones.

TP10 se integra con la infraestructura general del proyecto:
- `MainProgram` permite seleccionar `TP10 - Contactos AVL Exercise` con la opción 10.
- `Exercise` proporciona el ciclo de ejecución y el manejo de fases.
- `application.treeModule.AVL` es la estructura arbórea compartida. TP10 la utiliza sin modificarla y adapta localmente la remoción para rebalancear de forma segura.
- `SimpleList` / `SimpleArrayList` mantienen los contactos ordenados alfabéticamente para poder listarlos y seleccionarlos por posición.

## Uso del TDA AVL
Cada contacto se almacena como un nodo del AVL. `ContactoAVL.compareTo` compara los nombres sin distinguir mayúsculas y minúsculas, por lo que nombres como `Ana Lopez` y `ana lopez` se consideran iguales.

| Operación de la agenda | Operación utilizada |
|---|---|
| Crear | Inserta el contacto en el AVL y en la lista ordenada. |
| Saber si un nombre existe | `contains` con un contacto auxiliar construido por `ContactoAVL.paraBuscar`. |
| Recuperar un contacto | Búsqueda en la lista ordenada; el AVL compartido no devuelve el nodo. |
| Ver todos | Recorrido de la lista en orden alfabético. |
| Borrar | `remove` del AVL y eliminación del contacto de la lista. |
| Cambiar el nombre | Elimina el contacto del árbol, cambia la clave y lo inserta de nuevo; también actualiza la lista. |
| Cantidad | `listado.size()`. |

**¿Por qué el nombre es la clave?** La consigna no permite contactos con el mismo nombre. El teléfono y el mail pueden repetirse y no determinan el orden de la agenda.

**¿Por qué mantener una lista además del árbol?** El AVL compartido ofrece recorridos que imprimen o devuelven un orden por niveles, pero la interfaz necesita mostrar y numerar todos los contactos alfabéticamente. La lista conserva los mismos objetos en ese orden para listar, editar y borrar por posición.

**¿Por qué se quita y se reinserta al cambiar el nombre?** El nombre determina la ubicación del contacto en el árbol. Si se cambiara directamente, el nodo podría quedar en una rama incorrecta. Editar el teléfono o mail no afecta la clave y solo modifica el objeto almacenado.

**Remoción y rebalanceo.** Las inserciones usan el AVL de `application.treeModule`. La agenda define un subtipo interno del AVL que sobrescribe la remoción y reequilibra los ancestros modificados; así puede manejar una rama vacía sin llamar al cálculo de balance sobre un nodo inexistente. Las rotaciones necesarias se implementan dentro de TP10, sin cambiar el AVL compartido.

**Búsqueda sin crear datos ficticios.** `ContactoAVL.paraBuscar` crea un objeto auxiliar que contiene únicamente el nombre. Se usa con `contains` para comprobar si existe una clave; el contacto completo se obtiene de la lista.

## Validaciones e inputs inválidos
La aplicación valida antes de cambiar la agenda y muestra los errores desde la consola:

| Situación | Resultado |
|---|---|
| Nombre vacío | Se informa y no se crea ni modifica el contacto. |
| Nombre repetido, incluso con distintas mayúsculas | Se rechaza para mantener la unicidad de la clave. |
| Teléfono vacío o con caracteres no permitidos | Se vuelve a pedir el dato. Se aceptan dígitos, espacios, `+`, `-` y paréntesis, con al menos seis dígitos. |
| Mail sin una sola `@` o sin dominio con punto | Se vuelve a pedir el dato. |
| Agenda vacía al intentar editar o borrar | Se informa y no se opera sobre el árbol. |
| Número no entero o fuera del rango | Se informa y se vuelve a pedir la selección. |
| Operación cancelada | `cancelar` abandona la operación sin aplicar cambios. |
| Nombre, teléfono o mail no válido al usar el servicio | El método devuelve `false` y la agenda no se modifica. |

El único `try/catch` de la interfaz está en la conversión de texto a entero, para manejar `NumberFormatException`. Las validaciones de formato y rango se resuelven con condiciones antes de acceder a la lista o al árbol.

## Base de datos inicial
La agenda comienza vacía. La opción 5 agrega los contactos de prueba que aún no estén cargados:

| Nombre | Teléfono | Mail |
|---|---|---|
| Luka Ruiz Diaz | 11 5555-0101 | luka.ruizdiaz@uade.edu.ar |
| Betsabe Rodriguez | 11 5555-0102 | betsabe.rodriguez@uade.edu.ar |
| Trinidad Garcia Valicente | 11 5555-0103 | trinidad.garciavalicente@uade.edu.ar |
| Juliana Gomez | 11 5555-0104 | juliana.gomez@uade.edu.ar |
| Diego Cinquino | 11 5555-0105 | diego.cinquino@uade.edu.ar |

Al listar, el orden alfabético es: Betsabe, Diego, Juliana, Luka y Trinidad. Si se vuelve a cargar la base, los contactos existentes se omiten.

## Opciones del menú
| Opción | Acción |
|---|---|
| 1 | Crear contacto: solicita nombre, teléfono y mail. |
| 2 | Ver todos los contactos, numerados y ordenados alfabéticamente. |
| 3 | Editar el nombre, teléfono o mail del contacto seleccionado. |
| 4 | Borrar el contacto seleccionado, con confirmación `s/n`. |
| 5 | Cargar contactos de prueba que todavía no estén en la agenda. |
| `mm` | Volver al menú principal del proyecto. |

## Rol de cada clase y métodos

### `ContactoAVL`
**Rol:** entidad de dominio y clave del árbol.
- `ContactoAVL(String nombre, String telefono, String mail)`: construye el contacto con los datos validados y recorta espacios exteriores.
- `paraBuscar(String nombre)`: crea una clave auxiliar que contiene solamente el nombre.
- `esNombreValido(String nombre)`: verifica que el nombre no sea nulo ni vacío.
- `esTelefonoValido(String telefono)`: valida los caracteres permitidos y exige al menos seis dígitos.
- `esMailValido(String mail)`: exige una sola `@` y un dominio con punto.
- `getNombre()`, `getTelefono()`, `getMail()`: devuelven los datos del contacto.
- `setNombre(...)`, `setTelefono(...)`, `setMail(...)`: validan y actualizan el campo, o devuelven `false` si el valor no sirve.
- `compareTo(ContactoAVL other)`: compara nombres sin distinguir mayúsculas.
- `equals(Object other)` / `hashCode()`: definen identidad por nombre, sin distinguir mayúsculas.
- `toString()`: muestra `Nombre | teléfono | mail`.

### `AgendaContactosAVL`
**Rol:** servicio que administra contactos mediante el AVL compartido y una lista ordenada para la interfaz.

**Atributos:** `contactos` (subtipo local de `application.treeModule.AVL<ContactoAVL>`), `listado` y `DATOS_DE_PRUEBA`.

**Métodos:**
- `agregar(String nombre, String telefono, String mail)`: valida los campos, evita nombres duplicados e inserta el contacto.
- `existe(String nombre)`: busca el nombre en el árbol; devuelve `false` si el texto no es válido.
- `buscar(String nombre)`: devuelve el contacto completo o `null` si no existe.
- `editarNombre(ContactoAVL contacto, String nuevoNombre)`: valida la clave nueva, evita duplicados y reinserta el contacto para conservar el orden del árbol.
- `editarTelefono(...)` / `editarMail(...)`: valida y actualiza datos que no afectan la clave.
- `borrar(ContactoAVL contacto)`: elimina el contacto del AVL y de la lista, si pertenece a la agenda.
- `cargarDatosDePrueba()`: agrega los contactos de prueba todavía ausentes y devuelve la cantidad agregada.
- `cantidadDatosDePrueba()`: devuelve la cantidad de filas de la base precargada.
- `listar()`: devuelve una copia de la lista de contactos en orden alfabético.
- `size()` / `isEmpty()`: informan el estado de la agenda.
- `ContactTree.removeRecursive(...)`: remueve nodos y reequilibra los ancestros dentro del package TP10, sin modificar `treeModule.AVL`.

### `ContactosAVLExercise`
**Rol:** controlador de TP10; maneja el menú, la interacción y la presentación en consola.

**Atributos:** `agenda`, `firstTime` y `CANCELAR`.

**Métodos:**
- `ContactosAVLExercise(Scanner scanner)`: inicializa el ejercicio con una agenda vacía.
- `exerciseLogic()`: deriva a la fase correspondiente del menú.
- `menuLogic()`: muestra cantidad de contactos y opciones disponibles.
- `crearLogic()`: solicita y valida los datos para crear un contacto.
- `verLogic()` / `mostrarContactos()`: muestra la agenda numerada en orden alfabético.
- `editarLogic()`: permite elegir un contacto y el campo que se va a editar.
- `editarNombre(...)`, `editarTelefono(...)`, `editarMail(...)`: solicitan el valor nuevo y delegan la actualización en la agenda.
- `borrarLogic()`: selecciona el contacto, solicita confirmación y lo elimina.
- `cargarDatosLogic()`: carga los datos de prueba e informa los contactos agregados u omitidos.
- `elegirContacto(String accion)`: selecciona un contacto por número después de comprobar que la agenda no esté vacía.
- `pedirTexto(...)`, `pedirTelefono()`, `pedirMail()`, `pedirEntero(...)`: solicitan datos con validación y opción de cancelar.
- `confirmar(String mensaje)`: solicita confirmación `s/n` antes de borrar.
- `volverAlMenu()`: restablece la fase del menú principal del ejercicio.

## Decisiones de UX
- **Arranque vacío:** el usuario ve el estado de la agenda y puede cargar los datos de prueba desde el menú.
- **Selección numerada:** editar y borrar usan posiciones de la lista en lugar de volver a escribir nombres.
- **Orden alfabético:** la agenda se muestra ordenada por nombre, independientemente del orden de inserción o de las rotaciones del AVL.
- **Prevención de duplicados:** el nombre se valida antes de pedir teléfono y mail al crear.
- **Confirmación al borrar:** evita eliminar un contacto por una selección accidental.
- **Cancelación:** ingresar `cancelar` interrumpe el ingreso en curso sin guardar cambios.
- **Edición segura de la clave:** al cambiar el nombre se quita y se vuelve a insertar el contacto para preservar el orden y balance del árbol.

## Pruebas manuales sugeridas
1. Elegir TP10 desde la opción 10 del menú principal y verificar que la agenda arranque con cero contactos.
2. Elegir la opción 2 y comprobar que la agenda vacía se informa sin causar errores.
3. Cargar los datos de prueba y verificar que aparezcan cinco contactos ordenados alfabéticamente.
4. Volver a cargar los datos y comprobar que no se dupliquen.
5. Intentar crear `Diego Cinquino` y comprobar que se rechaza antes de solicitar teléfono y mail.
6. Crear `Ana Lopez`, con teléfono `11 4444-0000` y mail `ana@mail.com`, y comprobar que aparece en el lugar alfabético correcto.
7. Editar el teléfono o mail de Ana y verificar que el contacto permanezca en su posición.
8. Cambiar el nombre de Ana a `Zoe Lopez` y comprobar que la lista la muestre en el lugar correspondiente.
9. Intentar cambiar un nombre por uno ya existente y comprobar que el contacto conserve sus datos anteriores.
10. Borrar un contacto y responder `n`; verificar que siga en la agenda. Repetir y responder `s`; debe desaparecer y reducirse el contador.
11. Probar teléfono `abc`, mail `ana`, nombre vacío, selección no numérica, número fuera del rango, `cancelar` y una opción inexistente. La aplicación debe seguir funcionando.

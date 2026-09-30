# TP06 - Sistema de recomendacion musical

## Consigna

La aplicacion recomienda canciones de los generos favoritos de un usuario que todavia no haya escuchado. Permite cargar generos, canciones y usuarios, iniciar y cerrar sesion, gestionar generos favoritos y registrar canciones escuchadas.

## Arquitectura

- `Genero`, `Cancion` y `Usuario` modelan los datos y rechazan valores invalidos con `IllegalArgumentException`.
- `SistemaRecomendacion` administra catalogos, valida relaciones entre entidades y obtiene recomendaciones.
- `RecomendacionMusicalExercise` gestiona el menu de consola, valida todas las entradas antes de invocar el modelo y carga los datos iniciales.
- `MainProgram` permite iniciar TP06 desde el menu principal.

Los catalogos, los generos favoritos y las canciones escuchadas usan `SimpleSet` (`SimpleArraySet`). Los generos y usuarios no distinguen mayusculas/minusculas; una cancion se identifica por la combinacion de titulo y artista.

## Base de datos inicial

Al iniciar el ejercicio se cargan cinco generos, nueve canciones y cuatro usuarios (`Diego`, `Juliana`, `Betsabe` y `Trinidad`), todos con preferencias iniciales. Diego prefiere Rock y Pop; Juliana, Folklore y Jazz; Betsabe, Electronica; Trinidad, Pop y Jazz. El historial inicial permite probar la exclusion de canciones ya escuchadas. Por ejemplo, Betsabe puede obtener recomendaciones de Electronica inmediatamente. El listado incluye dos canciones de Electronica: `Be Strong` (Korolova) e `Instant Crush` (Daft Punk).

## Uso y decisiones de experiencia

- Las operaciones de favoritos, historial y recomendaciones requieren una sesion activa. Cerrar sesion permite iniciar con otro usuario.
- En el menu, los generos favoritos se muestran en la linea inmediatamente posterior a `Usuario logueado`; la lista se actualiza al iniciar sesion o modificar preferencias. Si no hay sesion activa, solo se muestra que no hay usuario logueado.
- Cada listado se numera para seleccionar elementos sin tener que volver a escribir nombres y para evitar errores de coincidencia.
- Se puede cancelar cualquier ingreso de texto o numero escribiendo `cancelar`; `mm` vuelve al menu principal del proyecto.
- Los nombres vacios, numeros no validos o fuera de rango y las opciones desconocidas se informan y se vuelven a solicitar o se descartan sin terminar el programa.
- Una recomendacion no se registra automaticamente como escuchada: recomendar no implica que el usuario haya reproducido la cancion. La opcion de registrar una cancion escuchada actualiza el historial y evita que vuelva a recomendarse.
- Si no quedan canciones ineditas de los generos favoritos, la aplicacion lo informa sin producir una recomendacion incorrecta.
- Entre las canciones elegibles se elige una al azar. Canciones con el mismo titulo y artista se consideran duplicadas, sin importar mayusculas/minusculas.

## Pruebas manuales sugeridas

1. Iniciar sesion como Betsabe y pedir una recomendacion; comprobar que sea de Electronica.
2. Registrar la cancion recomendada como escuchada y volver a pedir otra; no debe repetirse.
3. Iniciar sesion como Juliana y comprobar que una recomendacion pertenezca a Folklore o Jazz, y no sea `Zamba para olvidar`.
4. Intentar crear elementos duplicados, seleccionar numeros invalidos e ingresar texto vacio; el menu debe mantenerse operativo.

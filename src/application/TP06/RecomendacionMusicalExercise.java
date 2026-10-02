package application.TP06;

import application.Exercise;
import application.setModule.SimpleSet;

import java.util.Optional;
import java.util.Scanner;

public class RecomendacionMusicalExercise extends Exercise {
    private static final String CANCELAR = "cancelar";

    private final SistemaRecomendacion sistema = new SistemaRecomendacion();
    private Usuario usuarioLogueado;
    private boolean firstTime = true;

    public RecomendacionMusicalExercise(Scanner scanner) {
        super(scanner);
        cargarBaseDeDatos();
    }

    private void cargarBaseDeDatos() {
        Genero rock = new Genero("Rock");
        Genero pop = new Genero("Pop");
        Genero jazz = new Genero("Jazz");
        Genero folklore = new Genero("Folklore");
        Genero electronica = new Genero("Electronica");
        sistema.agregarGenero(rock);
        sistema.agregarGenero(pop);
        sistema.agregarGenero(jazz);
        sistema.agregarGenero(folklore);
        sistema.agregarGenero(electronica);

        Cancion viento = new Cancion("Muchacha (Ojos de papel)", "Almendra", rock);
        Cancion rastros = new Cancion("Rastros", "Lali", pop);
        Cancion invierno = new Cancion("Invierno", "Drexler", jazz);
        Cancion zamba = new Cancion("Zamba para olvidar", "Mercedes Sosa", folklore);
        Cancion musica = new Cancion("Musica ligera", "Soda Stereo", rock);
        Cancion ciudad = new Cancion("Ciudad magica", "Tan Bionica", pop);
        Cancion noche = new Cancion("Be Strong", "Korolova", electronica);
        Cancion milonga = new Cancion("Milonga del moro judio", "Jorge Drexler", folklore);
        Cancion instantCrush = new Cancion("Instant Crush", "Daft Punk", electronica);
        sistema.agregarCancion(viento);
        sistema.agregarCancion(rastros);
        sistema.agregarCancion(invierno);
        sistema.agregarCancion(zamba);
        sistema.agregarCancion(musica);
        sistema.agregarCancion(ciudad);
        sistema.agregarCancion(noche);
        sistema.agregarCancion(milonga);
        sistema.agregarCancion(instantCrush);

        Usuario diego = new Usuario("Diego");
        Usuario juliana = new Usuario("Juliana");
        Usuario betsabe = new Usuario("Betsabe");
        Usuario trinidad = new Usuario("Trinidad");
        sistema.agregarUsuario(diego);
        sistema.agregarUsuario(juliana);
        sistema.agregarUsuario(betsabe);
        sistema.agregarUsuario(trinidad);
        sistema.registrarGeneroFavorito(diego, rock);
        sistema.registrarGeneroFavorito(diego, pop);
        sistema.registrarEscuchada(diego, viento);
        sistema.registrarGeneroFavorito(juliana, folklore);
        sistema.registrarGeneroFavorito(juliana, jazz);
        sistema.registrarEscuchada(juliana, zamba);
        sistema.registrarGeneroFavorito(betsabe, electronica);
        sistema.registrarGeneroFavorito(trinidad, pop);
        sistema.registrarGeneroFavorito(trinidad, jazz);
        sistema.registrarEscuchada(trinidad, rastros);
    }

    @Override
    protected void exerciseLogic() {
        mostrarMenu();
    }

    private void mostrarMenu() {
        if (firstTime) {
            System.out.println("\nBienvenido al Sistema de Recomendacion Musical.");
            System.out.println("Base de prueba cargada: 5 generos, 9 canciones y 4 usuarios.");
            firstTime = false;
        }

        System.out.println("\nUsuario logueado: "
                + (usuarioLogueado == null ? "ninguno" : usuarioLogueado.getNombre()));
        if (usuarioLogueado == null) {
            mostrarMenuInicio();
        } else {
            System.out.println("Generos preferidos: " + formatearGenerosFavoritos(usuarioLogueado));
            mostrarMenuUsuario();
        }
    }

    private void mostrarMenuInicio() {
        System.out.println("\n1: Agregar usuario"
                + "\n2: Iniciar sesion"
                + "\nmm: Volver al menu principal");
        String opcion = scanner.nextLine().trim().toLowerCase();

        switch (opcion) {
            case "1":
                agregarUsuario();
                break;
            case "2":
                iniciarSesion();
                break;
            case "mm":
                running = false;
                break;
            default:
                System.out.println("\nOpcion invalida.");
        }
    }

    private void mostrarMenuUsuario() {
        System.out.println("\n1: Agregar genero"
                + "\n2: Agregar cancion"
                + "\n3: Asignar o quitar genero favorito"
                + "\n4: Recomendar una cancion"
                + "\n5: Registrar una cancion escuchada"
                + "\n6: Cerrar sesion"
                + "\nmm: Volver al menu principal");
        String opcion = scanner.nextLine().trim().toLowerCase();

        switch (opcion) {
            case "1":
                agregarGenero();
                break;
            case "2":
                agregarCancion();
                break;
            case "3":
                gestionarGenerosFavoritos();
                break;
            case "4":
                recomendarCancion();
                break;
            case "5":
                registrarCancionEscuchada();
                break;
            case "6":
                cerrarSesion();
                break;
            case "mm":
                running = false;
                break;
            default:
                System.out.println("\nOpcion invalida.");
        }
    }

    private void agregarGenero() {
        String nombre = pedirTexto("Ingrese el nombre del genero");
        if (nombre == null) return;
        boolean agregado = sistema.agregarGenero(new Genero(nombre));
        System.out.println(agregado ? "\nGenero agregado." : "\nEse genero ya existe.");
    }

    private void agregarCancion() {
        String titulo = pedirTexto("Ingrese el titulo de la cancion");
        if (titulo == null) return;
        String artista = pedirTexto("Ingrese el nombre del artista");
        if (artista == null) return;
        mostrarGeneros();
        if (sistema.getGeneros().isEmpty()) {
            System.out.println("\nPrimero debe agregar un genero.");
            return;
        }
        Integer posicion = pedirEntero("Seleccione el numero del genero", 1, sistema.getGeneros().size());
        if (posicion == null) return;
        Genero genero = obtenerGenero(posicion);
        boolean agregada = sistema.agregarCancion(new Cancion(titulo, artista, genero));
        System.out.println(agregada ? "\nCancion agregada." : "\nEsa cancion ya existe para ese artista.");
    }

    private void agregarUsuario() {
        String nombre = pedirTexto("Ingrese el nombre del usuario");
        if (nombre == null) return;
        boolean agregado = sistema.agregarUsuario(new Usuario(nombre));
        System.out.println(agregado ? "\nUsuario agregado." : "\nEse usuario ya existe.");
    }

    private void iniciarSesion() {
        mostrarUsuarios();
        if (sistema.getUsuarios().isEmpty()) {
            System.out.println("\nNo hay usuarios registrados.");
            return;
        }
        Integer posicion = pedirEntero("Seleccione el numero del usuario", 1, sistema.getUsuarios().size());
        if (posicion == null) return;
        usuarioLogueado = obtenerUsuario(posicion);
        System.out.println("\nSesion iniciada como " + usuarioLogueado.getNombre() + ".");
    }

    private void gestionarGenerosFavoritos() {
        if (!requiereSesion()) return;
        System.out.println("\n1: Agregar genero favorito\n2: Quitar genero favorito");
        Integer accion = pedirEntero("Seleccione una opcion", 1, 2);
        if (accion == null) return;
        mostrarGeneros();
        if (sistema.getGeneros().isEmpty()) {
            System.out.println("\nNo hay generos para asignar.");
            return;
        }
        Integer posicion = pedirEntero("Seleccione el numero del genero", 1, sistema.getGeneros().size());
        if (posicion == null) return;
        Genero genero = obtenerGenero(posicion);
        boolean cambiado = accion == 1
                ? sistema.registrarGeneroFavorito(usuarioLogueado, genero)
                : sistema.quitarGeneroFavorito(usuarioLogueado, genero);
        if (cambiado) {
            System.out.println(accion == 1
                    ? "\nGenero favorito asignado."
                    : "\nGenero favorito quitado.");
        } else {
            System.out.println(accion == 1
                    ? "\nEse genero ya era favorito."
                    : "\nEse genero no estaba entre sus favoritos.");
        }
    }

    private void recomendarCancion() {
        if (!requiereSesion()) return;
        if (usuarioLogueado.getGenerosFavoritos().isEmpty()) {
            System.out.println("\nPrimero asigne al menos un genero favorito.");
            return;
        }
        Optional<Cancion> recomendacion = sistema.recomendar(usuarioLogueado);
        if (recomendacion.isPresent()) {
            System.out.println("\nRecomendacion: " + recomendacion.get());
            System.out.println("La recomendacion no se marca como escuchada automaticamente.");
        } else {
            System.out.println("\nNo hay canciones nuevas de sus generos favoritos.");
        }
    }

    private void registrarCancionEscuchada() {
        if (!requiereSesion()) return;
        mostrarCanciones();
        if (sistema.getCanciones().isEmpty()) {
            System.out.println("\nNo hay canciones para registrar.");
            return;
        }
        Integer posicion = pedirEntero("Seleccione el numero de la cancion", 1, sistema.getCanciones().size());
        if (posicion == null) return;
        boolean registrada = sistema.registrarEscuchada(usuarioLogueado, sistema.buscarCancion(posicion));
        System.out.println(registrada
                ? "\nCancion registrada como escuchada."
                : "\nEsa cancion ya estaba registrada como escuchada.");
    }

    private void cerrarSesion() {
        if (!requiereSesion()) return;
        System.out.println("\nSesion cerrada para " + usuarioLogueado.getNombre() + ".");
        usuarioLogueado = null;
    }

    private boolean requiereSesion() {
        if (usuarioLogueado != null) return true;
        System.out.println("\nInicie sesion primero para usar esta opcion.");
        return false;
    }

    private void mostrarGeneros() {
        System.out.println("\nGENEROS:");
        mostrarConNumeracion(sistema.getGeneros());
    }

    private void mostrarUsuarios() {
        System.out.println("\nUSUARIOS:");
        mostrarConNumeracion(sistema.getUsuarios());
    }

    private void mostrarCanciones() {
        System.out.println("\nCANCIONES:");
        mostrarConNumeracion(sistema.getCanciones());
    }

    private String formatearGenerosFavoritos(Usuario usuario) {
        Genero[] generosFavoritos = usuario.getGenerosFavoritos().toArray(new Genero[0]);
        if (generosFavoritos.length == 0) return "ninguno";

        StringBuilder resultado = new StringBuilder();
        for (Genero genero : generosFavoritos) {
            if (resultado.length() > 0) resultado.append(", ");
            resultado.append(genero);
        }
        return resultado.toString();
    }

    private <T> void mostrarConNumeracion(SimpleSet<T> elementos) {
        if (elementos.isEmpty()) {
            System.out.println("No hay elementos cargados.");
            return;
        }
        T[] listado = elementos.toArray((T[]) new Object[0]);
        for (int i = 0; i < listado.length; i++) {
            System.out.println((i + 1) + ". " + listado[i]);
        }
    }

    private Genero obtenerGenero(int posicion) {
        return sistema.getGeneros().toArray(new Genero[0])[posicion - 1];
    }

    private Usuario obtenerUsuario(int posicion) {
        return sistema.getUsuarios().toArray(new Usuario[0])[posicion - 1];
    }

    private String pedirTexto(String mensaje) {
        while (true) {
            System.out.println("\n" + mensaje + " (\"" + CANCELAR + "\" para cancelar):");
            String entrada = scanner.nextLine().trim();
            if (entrada.equalsIgnoreCase(CANCELAR)) return null;
            if (!entrada.isEmpty()) return entrada;
            System.out.println("El texto no puede estar vacio.");
        }
    }

    private Integer pedirEntero(String mensaje, int minimo, int maximo) {
        while (true) {
            System.out.println("\n" + mensaje + " (\"" + CANCELAR + "\" para cancelar):");
            String entrada = scanner.nextLine().trim();
            if (entrada.equalsIgnoreCase(CANCELAR)) return null;

            int numero;
            try {
                numero = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Respuesta invalida, ingrese un numero.");
                continue;
            }
            if (numero < minimo || numero > maximo) {
                System.out.println("Ingrese un numero entre " + minimo + " y " + maximo + ".");
                continue;
            }
            return numero;
        }
    }
}

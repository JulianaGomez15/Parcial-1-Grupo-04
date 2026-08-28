package TP03;

import java.time.LocalDateTime;

public class Menu {

    private ListaTarea tareas;
    private RelojSimulado reloj;
    private ValidadorEntrada validador;
    private boolean running = true;

    public Menu(ListaTarea tareas, RelojSimulado reloj, ValidadorEntrada validador) {
        if (tareas == null || reloj == null || validador == null) {
            throw new IllegalArgumentException("Menu necesita lista, reloj y validador.");
        }
        this.tareas = tareas;
        this.reloj = reloj;
        this.validador = validador;
    }

    public void run() {
        while (running) {
            mostrarMenu();
            int userInput = validador.leerEnteroEntre("Opcion: ", 0, 6);
            ejecutarOpcion(userInput);
        }
    }

    private void mostrarMenu() {
        System.out.println("\nHora actual: " + reloj);
        System.out.println("Selecciona una opcion:"
                + "\n1: Agregar Tarea"
                + "\n2: Mostrar Tareas"
                + "\n3: Marcar/Desmarcar como realizada"
                + "\n4: Eliminar tarea"
                + "\n5: Avanzar una hora"
                + "\n6: Mostrar tareas atrasadas"
                + "\n0: Salir");
    }

    private void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                agregarTarea();
                break;
            case 2:
                mostrarTareas();
                break;
            case 3:
                marcarDesmarcarTarea();
                break;
            case 4:
                eliminarTarea();
                break;
            case 5:
                avanzarUnaHora();
                break;
            case 6:
                mostrarTareasAtrasadas();
                break;
            case 0:
                running = false;
                System.out.println("Hasta luego!");
                break;
            default:
                System.out.println("Opcion invalida. Elegi un numero del menu.");
                break;
        }
    }

    private void agregarTarea() {
        try {
            String titulo = validador.leerTextoNoVacio("Ingresa el titulo de la tarea: ");
            if (tareas.existeTitulo(titulo)) {
                System.out.println("Ya existe una tarea con ese titulo.");
                return;
            }

            LocalDateTime fechaLimite = validador.leerFechaHora("Ingresa fecha y hora limite (dd/MM/yyyy HH:mm): ");
            while (!fechaLimite.isAfter(reloj.obtenerFechaHoraActual())) {
                System.out.println("La fecha limite debe ser posterior a la hora actual del reloj (" + reloj + ").");
                fechaLimite = validador.leerFechaHora("Ingresa fecha y hora limite (dd/MM/yyyy HH:mm): ");
            }

            Tarea nueva = new Tarea(titulo, reloj.obtenerFechaHoraActual(), null, fechaLimite);
            tareas.addTarea(nueva);
            System.out.println("Tarea agregada.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void mostrarTareas() {
        System.out.println("\nHora actual: " + reloj);
        if (tareas.getSize() == 0) {
            System.out.println("No hay tareas.");
            return;
        }

        LocalDateTime ahora = reloj.obtenerFechaHoraActual();
        for (int i = 0; i < tareas.getSize(); i++) {
            Tarea tarea = tareas.getTarea(i);
            String linea = (i + 1) + ". " + tarea.toString();
            if (tarea.estaAtrasada(ahora)) {
                linea += " [ATRASADA]";
            }
            System.out.println(linea);
        }
    }

    private void marcarDesmarcarTarea() {
        if (tareas.getSize() == 0) {
            System.out.println("No hay tareas.");
            return;
        }

        mostrarTareas();
        int posicion = validador.leerEnteroEntre(
                "Ingresa el numero de la tarea que quiere marcar/desmarcar: ",
                1,
                tareas.getSize());

        try {
            Tarea tarea = tareas.getTarea(posicion - 1);
            if (tarea.estaRealizada()) {
                tarea.marcarComoPendiente();
                System.out.println("Tarea marcada como pendiente.");
            } else {
                tarea.marcarComoRealizada(reloj.obtenerFechaHoraActual());
                System.out.println("Tarea marcada como realizada.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void eliminarTarea() {
        if (tareas.getSize() == 0) {
            System.out.println("No hay tareas.");
            return;
        }

        mostrarTareas();
        int posicion = validador.leerEnteroEntre(
                "Ingresa el numero de la tarea que quiere eliminar: ",
                1,
                tareas.getSize());
        try {
            tareas.eliminarPorIndex(posicion - 1); // el usuario cuenta desde 1
            System.out.println("Eliminado correctamente.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void avanzarUnaHora() {
        reloj.avanzarUnaHora();
        System.out.println("Tiempo avanzado 1 hora. Hora actual: " + reloj);
    }

    private void mostrarTareasAtrasadas() {
        System.out.println("\n--- Tareas atrasadas ---");
        System.out.println("Hora actual: " + reloj);

        if (tareas.getSize() == 0) {
            System.out.println("No hay tareas.");
            return;
        }

        LocalDateTime ahora = reloj.obtenerFechaHoraActual();
        boolean hayAtrasadas = false;
        for (int i = 0; i < tareas.getSize(); i++) {
            Tarea tarea = tareas.getTarea(i);
            if (tarea.estaAtrasada(ahora)) {
                System.out.println((i + 1) + ". " + tarea.toString() + " [ATRASADA]");
                hayAtrasadas = true;
            }
        }

        if (!hayAtrasadas) {
            System.out.println("No hay tareas atrasadas.");
        }
    }

}

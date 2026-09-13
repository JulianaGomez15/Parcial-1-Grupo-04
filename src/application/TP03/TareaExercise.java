package application.TP03;

import java.time.LocalDateTime;
import java.util.Scanner;

import application.Exercise;
import application.listModule.SimpleArrayList;
import application.listModule.SimpleList;

public class TareaExercise extends Exercise {

    // Palabra clave para abortar una operación y volver al menú
    private static final String CANCELAR = "cancelar";

    private final SimpleList<Tarea> tareas = new SimpleArrayList<Tarea>();
    private final SimuladorTiempo reloj = new SimuladorTiempo(LocalDateTime.of(2026, 9, 4, 16, 0));
    private boolean firstTime = true;

    // Si está en true, la lista se imprime sola en cada vuelta del menú
    private boolean mostrarSiempre = true;

    public TareaExercise(Scanner scanner) {
        super(scanner);
        LocalDateTime ahora = reloj.getTiempoActual(); // viernes 4/9/2026 16:00
        tareas.add(new Tarea("Entregar TP03", ahora.withHour(9).withMinute(0), null, ahora.plusHours(2)));
        tareas.add(new Tarea("Repasar para el parcial", ahora.withHour(10).withMinute(30), null, ahora.minusHours(1)));
        Tarea comprarApuntes = new Tarea("Comprar apuntes", ahora.withHour(11).withMinute(0), null, ahora.plusHours(5));
        comprarApuntes.setCompletada(ahora.withHour(12).withMinute(0));
        tareas.add(comprarApuntes);
    }

    @Override
    protected void exerciseLogic() {
        switch (currentPhase) {
            case 0:
                menuLogic();
                break;
            case 1:
                agregarLogic();
                break;
            case 2:
                mostrarLogic();
                break;
            case 3:
                completarLogic();
                break;
            case 4:
                eliminarPorTituloLogic();
                break;
            case 5:
                eliminarPorPosicionLogic();
                break;
            case 6:
                avanzarTiempoLogic();
                break;
        }
    }

    private void menuLogic() {
        if (firstTime) {
            System.out.println("\n¡Bienvenido a la Lista de Tareas!");
            firstTime = false;
        }

        System.out.println("\nHora actual: " + reloj.getTiempoFormateado());

        // La lista arranca visible, salvo que el usuario la apague con "auto"
        if (mostrarSiempre) {
            mostrarTareas();
        }

        System.out.println("\nSeleccione una opción:"
                + "\nagregar: Agregar tarea"
                + "\nmostrar: Mostrar tareas"
                + "\ncompletar: Marcar tarea como completada"
                + "\neliminar titulo: Eliminar por título"
                + "\neliminar posicion: Eliminar por posición"
                + "\navanzar: Avanzar una hora"
                + "\nauto: Mostrar la lista en cada paso (actualmente: " + (mostrarSiempre ? "sí" : "no") + ")"
                + "\nmm: Volver al menú principal");

        String userInput = scanner.nextLine().trim().toLowerCase();

        switch (userInput) {
            case "agregar":
                currentPhase = 1;
                break;
            case "mostrar":
                currentPhase = 2;
                break;
            case "completar":
                currentPhase = 3;
                break;
            case "eliminar titulo":
                currentPhase = 4;
                break;
            case "eliminar posicion":
                currentPhase = 5;
                break;
            case "avanzar":
                currentPhase = 6;
                break;
            case "auto":
                mostrarSiempre = !mostrarSiempre;
                System.out.println(mostrarSiempre
                        ? "\nLa lista se va a mostrar en cada paso."
                        : "\nLa lista solo se va a mostrar con la opción \"mostrar\".");
                break;
            case "mm":
                running = false;
                break;
            default:
                System.out.println("\nRespuesta inválida.");
                break;
        }
    }

    private void agregarLogic() {
        String titulo = pedirTexto("\nIngrese el título de la tarea");
        if (titulo == null) {
            volverAlMenu();
            return;
        }

        Integer horas = pedirEntero("\n¿En cuántas horas vence esta tarea? (0 = sin fecha límite)", 0, Integer.MAX_VALUE);
        if (horas == null) {
            volverAlMenu();
            return;
        }

        LocalDateTime fechaLimite = horas > 0 ? reloj.getTiempoActual().plusHours(horas) : null;
        tareas.add(new Tarea(titulo, reloj.getTiempoActual(), null, fechaLimite));
        System.out.println("\nTarea agregada correctamente.");

        volverAlMenu();
    }

    private void mostrarLogic() {
        mostrarTareas();
        volverAlMenu();
    }

    private void completarLogic() {
        if (listaVacia()) {
            return;
        }

        // Si el título no existe, se vuelve a pedir en vez de salir al menú
        while (true) {
            String titulo = pedirTexto("\nIngrese el título de la tarea a completar");
            if (titulo == null) {
                volverAlMenu();
                return;
            }

            Tarea tarea = buscarPorTitulo(titulo);
            if (tarea == null) {
                System.out.println("\nNo se encontró esa tarea.");
                continue;
            }

            tarea.setCompletada(reloj.getTiempoActual());
            System.out.println("\nTarea completada.");
            volverAlMenu();
            return;
        }
    }

    private void eliminarPorTituloLogic() {
        if (listaVacia()) {
            return;
        }

        while (true) {
            String titulo = pedirTexto("\nIngrese el título de la tarea a eliminar");
            if (titulo == null) {
                volverAlMenu();
                return;
            }

            Tarea tarea = buscarPorTitulo(titulo);
            if (tarea == null) {
                System.out.println("\nNo se encontró esa tarea.");
                continue;
            }

            tareas.remove(tarea);
            System.out.println("\nTarea eliminada.");
            volverAlMenu();
            return;
        }
    }

    private void eliminarPorPosicionLogic() {
        if (listaVacia()) {
            return;
        }

        mostrarTareas();

        Integer posicion = pedirEntero("\nIngrese la posición a eliminar", 1, tareas.size());
        if (posicion == null) {
            volverAlMenu();
            return;
        }

        tareas.remove(posicion - 1);
        System.out.println("\nTarea eliminada.");

        volverAlMenu();
    }

    private void avanzarTiempoLogic() {
        reloj.avanzarUnaHora();
        System.out.println("\nTiempo avanzado. Hora actual: " + reloj.getTiempoFormateado());
        volverAlMenu();
    }

    private void mostrarTareas() {
        if (tareas.isEmpty()) {
            System.out.println("\nNo hay tareas cargadas.");
            return;
        }

        LocalDateTime ahora = reloj.getTiempoActual();
        for (int i = 0; i < tareas.size(); i++) {
            Tarea tarea = tareas.get(i);
            String linea = (i + 1) + ". " + tarea;
            if (tarea.estaAtrasada(ahora)) {
                linea += " [ATRASADA]";
            }
            System.out.println(linea);
        }
    }

    private Tarea buscarPorTitulo(String titulo) {
        for (int i = 0; i < tareas.size(); i++) {
            if (tareas.get(i).getTitulo().equalsIgnoreCase(titulo)) {
                return tareas.get(i);
            }
        }
        return null;
    }

    // Corta la operación si no hay nada con que trabajar
    private boolean listaVacia() {
        if (tareas.isEmpty()) {
            System.out.println("\nNo hay tareas cargadas.");
            volverAlMenu();
            return true;
        }
        return false;
    }

    private void volverAlMenu() {
        currentPhase = 0;
    }

    // Pide texto hasta que sea válido; devuelve null si el usuario cancela
    private String pedirTexto(String mensaje) {
        while (true) {
            System.out.println(mensaje + " (\"" + CANCELAR + "\" para volver al menú):");

            String userInput = scanner.nextLine().trim();

            if (userInput.equalsIgnoreCase(CANCELAR)) {
                return null;
            }

            if (!userInput.isEmpty()) {
                return userInput;
            }

            System.out.println("\nEl texto no puede estar vacío.");
        }
    }

    // Pide un número entre minimo y maximo; devuelve null si el usuario cancela
    private Integer pedirEntero(String mensaje, int minimo, int maximo) {
        while (true) {
            System.out.println(mensaje + " (\"" + CANCELAR + "\" para volver al menú):");

            String userInput = scanner.nextLine().trim();

            if (userInput.equalsIgnoreCase(CANCELAR)) {
                return null;
            }

            int numero;
            try {
                numero = Integer.parseInt(userInput);
            } catch (NumberFormatException e) {
                System.out.println("\nRespuesta inválida, ingrese un número.");
                continue;
            }

            if (numero < minimo || numero > maximo) {
                System.out.println("\nRespuesta inválida, ingrese un número entre " + minimo + " y " + maximo + ".");
                continue;
            }

            return numero;
        }
    }
}

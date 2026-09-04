package application.TP03;

import java.time.LocalDateTime;
import java.util.Scanner;

import application.Exercise;
import application.TP03.simuladorTiempo.SimuladorTiempo;

public class TareaExercise extends Exercise {

    private final ListaTarea tareas = new ListaTarea();
    private final SimuladorTiempo reloj = new SimuladorTiempo(LocalDateTime.of(2026, 9, 4, 16, 0));
    private boolean firstTime = true;

    public TareaExercise(Scanner scanner) {
        super(scanner);
        LocalDateTime ahora = reloj.getTiempoActual(); // viernes 4/9/2026 16:00
        tareas.addTarea(new Tarea("Entregar TP03", ahora.withHour(9).withMinute(0), null, ahora.plusHours(2)));
        tareas.addTarea(new Tarea("Repasar para el parcial", ahora.withHour(10).withMinute(30), null, ahora.minusHours(1)));
        Tarea comprarApuntes = new Tarea("Comprar apuntes", ahora.withHour(11).withMinute(0), null, ahora.plusHours(5));
        comprarApuntes.setCompletada(ahora.withHour(12).withMinute(0));
        tareas.addTarea(comprarApuntes);
    }

    @Override
    protected void exerciseLogic() {
        switch (currentPhase) {
            case 0:
                menuLogic();
                break;
            case 1:
                addLogic();
                break;
            case 2:
                showLogic();
                break;
            case 3:
                completeLogic();
                break;
            case 4:
                removeByNameLogic();
                break;
            case 5:
                removeByIndexLogic();
                break;
            case 6:
                advanceTimeLogic();
                break;
        }
    }

    private void menuLogic() {
        if (firstTime) {
            System.out.println("\n¡Bienvenido a la Lista de Tareas!");
            firstTime = false;
        }

        System.out.println("\nHora actual: " + reloj.getTiempoFormateado());
        System.out.println("\nSeleccione una opción:"
                + "\nadd: Agregar tarea"
                + "\nshow: Mostrar tareas"
                + "\ncomplete: Marcar tarea como completada"
                + "\nremove name: Eliminar por título"
                + "\nremove index: Eliminar por posición"
                + "\nadvance: Avanzar 1 hora"
                + "\nmm: Volver al menú");

        String userInput = scanner.nextLine().toLowerCase();

        switch (userInput) {
            case "add":
                currentPhase = 1;
                break;
            case "show":
                currentPhase = 2;
                break;
            case "complete":
                currentPhase = 3;
                break;
            case "remove name":
                currentPhase = 4;
                break;
            case "remove index":
                currentPhase = 5;
                break;
            case "advance":
                currentPhase = 6;
                break;
            case "mm":
                running = false;
                break;
            default:
                System.out.println("\nRespuesta inválida.");
                break;
        }
    }

    private void addLogic() {
        System.out.println("\nIngrese el título de la tarea:");
        String titulo = scanner.nextLine();

        if (titulo == null || titulo.trim().isEmpty()) {
            System.out.println("\nEl título no puede estar vacío.");
            currentPhase = 0;
            return;
        }

        System.out.println("\n¿En cuántas horas vence esta tarea? (0 = sin fecha límite):");
        while (!scanner.hasNextInt()) {
            System.out.println("\nRespuesta inválida, ingrese un número.");
            scanner.nextLine();
        }
        int horas = scanner.nextInt();
        scanner.nextLine();

        if (horas < 0) {
            System.out.println("\nLas horas no pueden ser negativas.");
            currentPhase = 0;
            return;
        }

        LocalDateTime fechaLimite = horas > 0 ? reloj.getTiempoActual().plusHours(horas) : null;
        tareas.addTarea(new Tarea(titulo, reloj.getTiempoActual(), null, fechaLimite));
        System.out.println("\nTarea agregada correctamente.");

        currentPhase = 0;
    }

    private void showLogic() {
        printList();
        currentPhase = 0;
    }

    private void printList() {
        if (tareas.isEmpty()) {
            System.out.println("\nNo hay tareas cargadas.");
            return;
        }
        tareas.mostrarLista(reloj.getTiempoActual());
    }

    private void completeLogic() {
        if (tareas.isEmpty()) {
            System.out.println("\nNo hay tareas cargadas.");
            currentPhase = 0;
            return;
        }

        System.out.println("\nIngrese el título de la tarea a completar:");
        String titulo = scanner.nextLine();

        boolean encontrada = tareas.buscarTareaParaCompletar(titulo, reloj.getTiempoActual());

        System.out.println(encontrada ? "\nTarea completada." : "\nNo se encontró esa tarea.");
        currentPhase = 0;
    }

    private void removeByNameLogic() {
        if (tareas.isEmpty()) {
            System.out.println("\nNo hay tareas cargadas.");
            currentPhase = 0;
            return;
        }

        System.out.println("\nIngrese el título de la tarea a eliminar:");
        String titulo = scanner.nextLine();

        boolean eliminada = tareas.eliminarPorTitulo(titulo);

        System.out.println(eliminada ? "\nTarea eliminada." : "\nNo se encontró esa tarea.");
        currentPhase = 0;
    }

    private void removeByIndexLogic() {
        if (tareas.isEmpty()) {
            System.out.println("\nNo hay tareas cargadas.");
            currentPhase = 0;
            return;
        }

        printList(); // no toca currentPhase, solo imprime
        System.out.println("\nIngrese la posición a eliminar:");

        while (!scanner.hasNextInt()) {
            System.out.println("\nRespuesta inválida, ingrese un número.");
            scanner.nextLine();
        }

        int index = scanner.nextInt();
        scanner.nextLine();

        // El TDA lanza excepcion si el indice es invalido; aca lo evitamos
        if (index - 1 < 0 || index - 1 >= tareas.size()) {
            System.out.println("\nÍndice inválido.");
            currentPhase = 0;
            return;
        }

        tareas.eliminarPorIndex(index - 1);
        System.out.println("\nTarea eliminada.");

        currentPhase = 0;
    }

    private void advanceTimeLogic() {
        reloj.avanzarUnaHora();
        System.out.println("\nTiempo avanzado. Hora actual: " + reloj.getTiempoFormateado());
        currentPhase = 0;
    }
}

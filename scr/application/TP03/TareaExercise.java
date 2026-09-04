package application.TP03;

import java.time.LocalDateTime;
import java.util.Scanner;

import application.listModule.SimpleArrayList;
import application.Exercise;
import application.TP03.simuladorTiempo.SimuladorTiempo;

public class TareaExercise extends Exercise {

    private final SimpleArrayList<Tarea> tareas = new SimpleArrayList<>();
    private final SimuladorTiempo reloj = new SimuladorTiempo(LocalDateTime.of(2026, 1, 1, 8, 0));
    private boolean firstTime = true;

    public TareaExercise(Scanner scanner) {
        super(scanner);
        tareas.add(new Tarea("Entregar TP03", reloj.getTiempoActual(), null, reloj.getTiempoActual().plusHours(2)));
        tareas.add(new Tarea("Repasar para el parcial", reloj.getTiempoActual(), null, reloj.getTiempoActual().minusHours(1)));
        Tarea comprarApuntes = new Tarea("Comprar apuntes", reloj.getTiempoActual(), null, reloj.getTiempoActual().plusHours(5));
        comprarApuntes.setCompletada(reloj.getTiempoActual());
        tareas.add(comprarApuntes);
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

        System.out.println("\n¿En cuántas horas vence esta tarea? (0 = sin fecha límite):");
        while (!scanner.hasNextInt()) {
            System.out.println("\nRespuesta inválida, ingrese un número.");
            scanner.nextLine();
        }
        int horas = scanner.nextInt();
        scanner.nextLine();

        LocalDateTime fechaLimite = horas > 0 ? reloj.getTiempoActual().plusHours(horas) : null;

        tareas.add(new Tarea(titulo, reloj.getTiempoActual(), null, fechaLimite));
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

    private void completeLogic() {
        if (tareas.isEmpty()) {
            System.out.println("\nNo hay tareas cargadas.");
            currentPhase = 0;
            return;
        }

        System.out.println("\nIngrese el título de la tarea a completar:");
        String titulo = scanner.nextLine();

        boolean encontrada = false;
        for (int i = 0; i < tareas.size() && !encontrada; i++) {
            if (tareas.get(i).getTitulo().equals(titulo)) {
                tareas.get(i).setCompletada(reloj.getTiempoActual());
                encontrada = true;
            }
        }

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

        boolean eliminada = false;
        for (int i = 0; i < tareas.size() && !eliminada; i++) {
            if (tareas.get(i).getTitulo().equals(titulo)) {
                tareas.remove(i);
                eliminada = true;
            }
        }

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

        try {
            tareas.remove(index - 1);
            System.out.println("\nTarea eliminada.");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("\nÍndice inválido.");
        }

        currentPhase = 0;
    }

    private void advanceTimeLogic() {
        reloj.avanzarUnaHora();
        System.out.println("\nTiempo avanzado. Hora actual: " + reloj.getTiempoFormateado());
        currentPhase = 0;
    }
}

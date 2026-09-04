package application.TP03;

import java.time.LocalDateTime;
import java.util.Scanner;

import application.Exercise;

public class TareaExercise extends Exercise {

    private final ListaTarea tareas = new ListaTarea();
    private boolean firstTime = true;

    public TareaExercise(Scanner scanner) {
        super(scanner);
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
        }
    }

    private void menuLogic() {
        if (firstTime) {
            System.out.println("\n¡Bienvenido a la Lista de Tareas!");
            firstTime = false;
        }

        System.out.println("\nSeleccione una opción:"
                + "\nadd: Agregar tarea"
                + "\nshow: Mostrar tareas"
                + "\ncomplete: Marcar tarea como completada"
                + "\nremove name: Eliminar por título"
                + "\nremove index: Eliminar por posición"
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

        tareas.addTarea(new Tarea(titulo, LocalDateTime.now(), null, null));
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
        tareas.mostrarLista();
    }

    private void completeLogic() {
        if (tareas.isEmpty()) {
            System.out.println("\nNo hay tareas cargadas.");
            currentPhase = 0;
            return;
        }

        System.out.println("\nIngrese el título de la tarea a completar:");
        String titulo = scanner.nextLine();

        boolean encontrada = tareas.buscarTareaParaCompletar(titulo);

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

        boolean eliminada = tareas.eliminarPorIndex(index - 1);
        System.out.println(eliminada ? "\nTarea eliminada." : "\nÍndice inválido.");

        currentPhase = 0;
    }
}

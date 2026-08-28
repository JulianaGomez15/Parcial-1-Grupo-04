package TP03;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Menu {

    private ListaTarea tareas;
    private Scanner scanner;
    private boolean running = true;

    public Menu(ListaTarea tareas) {
        this.tareas = tareas;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        while (running) {
            System.out.println("Selecciona una opción:"
                    + "\n1: Agregar Tarea"
                    + "\n2: Mostrar Tareas"
                    + "\n3: Marcar/Desmarcar como realizada"
                    + "\n4: Eliminar por nombre"
                    + "\n5: Eliminar por posición"
                    + "\n0: Salir");

            if (scanner.hasNextInt()) {
                int userInput = scanner.nextInt();
                ejecutarOpcion(userInput);
            } else {
                System.out.println("Eso no es un número válido. Probá de nuevo.");
                scanner.next();
            }
        }
    }

    private void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                scanner.nextLine();
                System.out.print("Ingresá el título de la tarea: ");
                String titulo = scanner.nextLine();
                Tarea nueva = new Tarea(titulo, LocalDateTime.now(), null, null);
                tareas.addTarea(nueva);
                break;
            case 2:
                tareas.mostrarLista();
                break;
            case 3:
                scanner.nextLine();
                System.out.print("Ingresá el título de la tarea que quiere completar: ");
                String t = scanner.nextLine();
                boolean completado = tareas.buscarTareaParaCompletar(t);
                if (completado) {
                    System.out.println("Tarea completada");
                } else {
                    System.out.println("No se encontro tarea con ese titulo");
                }
                break;
            case 4:
                scanner.nextLine();
                System.out.print("Ingresá el título de la tarea que quiere eliminar: ");
                String tituloEliminar = scanner.nextLine();
                boolean eliminado = tareas.eliminarPorTitulo(tituloEliminar);
                if (eliminado) {
                    System.out.println("Eliminado correctamente");
                } else {
                    System.out.println("No se pudo eliminar");
                }
                break;
            case 5:
                tareas.mostrarLista();
                System.out.print("Ingresá el número de la tarea que quiere eliminar: ");
                if (!scanner.hasNextInt()) {
                    System.out.println("Eso no es un número válido.");
                    scanner.next(); // descarta la entrada inválida
                    break;
                }
                int indiceEliminar = scanner.nextInt();
                boolean res = tareas.eliminarPorIndex(indiceEliminar - 1); // el usuario cuenta desde 1
                if (res) {
                    System.out.println("Eliminado correctamente");
                } else {
                    System.out.println("No se pudo eliminar");
                }
                break;
            case 0:
                running = false;
                System.out.println("¡Hasta luego!");
                break;
            default:
                System.out.println("Opción inválida. Elegí un número del menú.");
                break;
        }
    }
}
package application.TP05;

import java.util.Scanner;

import application.Exercise;
import application.listModule.SimpleArrayList;
import application.listModule.SimpleList;

public class ImpresionExercise extends Exercise {

    private static final String CANCELAR = "cancelar";

    private final SimpleList<Documento> documentos = new SimpleArrayList<>();
    private final ColaImpresion colaImpresion = new ColaImpresion();
    private boolean firstTime = true;

    public ImpresionExercise(Scanner scanner) {
        super(scanner);
        cargarBaseDeDatos();
    }

    private void cargarBaseDeDatos() {
        documentos.add(new Documento("Documento1", 3, false));
        documentos.add(new Documento("Documento2", 2, true));
        documentos.add(new Documento("Documento3", 4, false));
    }

    @Override
    protected void exerciseLogic() {
        switch (currentPhase) {
            case 0:
                menuLogic();
                break;
            case 1:
                crearDocumentoLogic();
                break;
            case 2:
                enviarAImprimirLogic();
                break;
            case 3:
                cancelarColaLogic();
                break;
            case 4:
                imprimirLogic();
                break;
        }
    }

    private void menuLogic() {
        if (firstTime) {
            System.out.println("\n¡Bienvenido al Sistema de Impresión!");
            firstTime = false;
        }

        mostrarDocumentosCreados();
        mostrarCola();

        System.out.println("\nSeleccione una opción:"
                + "\n1: Crear documento"
                + "\n2: Enviar documento a imprimir"
                + "\n3: Cancelar cola de impresión"
                + "\n4: Imprimir"
                + "\nmm: Volver al menú");

        String userInput = scanner.nextLine().trim().toLowerCase();

        switch (userInput) {
            case "1":
                currentPhase = 1;
                break;
            case "2":
                currentPhase = 2;
                break;
            case "3":
                currentPhase = 3;
                break;
            case "4":
                currentPhase = 4;
                break;
            case "mm":
                running = false;
                break;
            default:
                System.out.println("\nRespuesta inválida.");
                break;
        }
    }

    private void crearDocumentoLogic() {
        String nombre = pedirTexto("\nIngrese el nombre del documento");
        if (nombre == null) {
            volverAlMenu();
            return;
        }

        Integer paginas = pedirEntero("\nIngrese la cantidad de páginas", 1, 100);
        if (paginas == null) {
            volverAlMenu();
            return;
        }

        Boolean color = pedirSiEsColor();
        if (color == null) {
            volverAlMenu();
            return;
        }

        documentos.add(new Documento(nombre, paginas, color));
        System.out.println("\nDocumento creado correctamente.");
        volverAlMenu();
    }

    private void enviarAImprimirLogic() {
        if (documentos.isEmpty()) {
            System.out.println("\nNo hay documentos creados. Cree un documento primero.");
            volverAlMenu();
            return;
        }

        mostrarDocumentosCreados();

        Integer posicion = pedirEntero("\nIngrese el número del documento a enviar a imprimir", 1, documentos.size());
        if (posicion == null) {
            volverAlMenu();
            return;
        }

        Documento documento = documentos.get(posicion - 1);
        colaImpresion.enviar(documento);
        System.out.println("\n\"" + documento.getNombre() + "\" fue enviado a la cola de impresión.");
        volverAlMenu();
    }

    private void cancelarColaLogic() {
        if (colaImpresion.isEmpty()) {
            System.out.println("\nNo se puede cancelar: la cola de impresión ya está vacía.");
            volverAlMenu();
            return;
        }

        colaImpresion.cancelar();
        System.out.println("\nCola de impresión cancelada. Se eliminaron todos los documentos pendientes.");
        volverAlMenu();
    }

    private void imprimirLogic() {
        if (colaImpresion.isEmpty()) {
            System.out.println("\nNo se puede imprimir: la cola de impresión está vacía.");
            volverAlMenu();
            return;
        }

        System.out.println("\n--- Impresión ---");
        while (!colaImpresion.isEmpty()) {
            imprimirPaginas(colaImpresion.sacarSiguiente());
        }
        System.out.println("\nImpresión finalizada.");
        volverAlMenu();
    }

    private void mostrarDocumentosCreados() {
        System.out.println("\nDOCUMENTOS CREADOS:");
        if (documentos.isEmpty()) {
            System.out.println("No hay documentos creados.");
            return;
        }

        for (int i = 0; i < documentos.size(); i++) {
            System.out.println((i + 1) + ". " + documentos.get(i));
        }
    }

    private void mostrarCola() {
        System.out.println("\nCOLA DE IMPRESIÓN:");
        if (colaImpresion.isEmpty()) {
            System.out.println("La cola está vacía.");
            return;
        }

        SimpleList<Documento> documentosEnCola = colaImpresion.snapshot();
        for (int i = 0; i < documentosEnCola.size(); i++) {
            System.out.println((i + 1) + ". " + documentosEnCola.get(i));
        }
    }

    private void imprimirPaginas(Documento documento) {
        for (int pagina = 1; pagina <= documento.getPaginas(); pagina++) {
            System.out.println("Imprimiendo " + documento.getNombre() + " - Página " + pagina);
        }
    }

    private Boolean pedirSiEsColor() {
        while (true) {
            System.out.println("\nSeleccione el tipo de impresión (\"" + CANCELAR + "\" para volver al menú):"
                    + "\n1: Blanco y negro"
                    + "\n2: Color");

            String userInput = scanner.nextLine().trim();

            if (userInput.equalsIgnoreCase(CANCELAR)) {
                return null;
            }

            switch (userInput.toLowerCase()) {
                case "1":
                    return false;
                case "2":
                    return true;
                default:
                    System.out.println("\nSelección incorrecta. Ingrese 1 (Blanco y negro) o 2 (Color).");
                    break;
            }
        }
    }

    private void volverAlMenu() {
        currentPhase = 0;
    }

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

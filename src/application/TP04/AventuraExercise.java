package application.TP04;

import java.util.Scanner;

import application.Exercise;
import application.stackModule.SimpleLinkedStack;
import application.stackModule.SimpleStack;

public class AventuraExercise extends Exercise {

    // Ids de las escenas
    private static final int INICIO = 0;
    private static final int RIO = 1;
    private static final int CUEVA_SENDERO = 2;
    private static final int FINAL_RIO = 3;
    private static final int PUEBLO = 4;
    private static final int FINAL_OSO = 5;
    private static final int FINAL_MIRADOR = 6;
    private static final int FINAL_PUEBLO = 7;

    private final HistoriaAventura historia = new HistoriaAventura();
    private final SimpleStack<Integer> historial = new SimpleLinkedStack<>(); // para poder volver atras
    private int escenaActualId = INICIO;
    private boolean firstTime = true;

    public AventuraExercise(Scanner scanner) {
        super(scanner);

        Escena inicio = new Escena(INICIO,
                "Abris los ojos y estas en un bosque oscuro. Hay dos caminos: uno hacia tu izquierda y otro hacia tu derecha.");
        inicio.agregarOpcion("Armar una fogata para acampar");
        inicio.agregarOpcion("Ir hacia la izquierda", RIO);
        inicio.agregarOpcion("Ir hacia la derecha", CUEVA_SENDERO);
        historia.agregarEscena(inicio);

        Escena rio = new Escena(RIO,
                "Caminando hacia la izquierda encontras un rio caudaloso. Podes cruzarlo nadando o seguir su curso.");
        rio.agregarOpcion("Armar una fogata para acampar");
        rio.agregarOpcion("Cruzar el rio nadando", FINAL_RIO);
        rio.agregarOpcion("Seguir el curso del rio", PUEBLO);
        historia.agregarEscena(rio);

        Escena cuevaSendero = new Escena(CUEVA_SENDERO,
                "Hacia la derecha encontras una cueva oscura y, un poco mas lejos, un sendero de piedras.");
        cuevaSendero.agregarOpcion("Armar una fogata para acampar");
        cuevaSendero.agregarOpcion("Entrar a la cueva", FINAL_OSO);
        cuevaSendero.agregarOpcion("Seguir el sendero de piedras", FINAL_MIRADOR);
        historia.agregarEscena(cuevaSendero);

        historia.agregarEscena(new Escena(FINAL_RIO,
                "Luchas contra la corriente y, exhausto, llegas a la otra orilla. Lograste cruzar el rio. FIN."));

        Escena pueblo = new Escena(PUEBLO,
                "Siguiendo el curso del rio llegas a un pequeno pueblo. Te ofrecen quedarte a descansar o volver al bosque.");
        pueblo.agregarOpcion("Quedarte a descansar en el pueblo", FINAL_PUEBLO);
        pueblo.agregarOpcion("Volver al bosque", INICIO);
        historia.agregarEscena(pueblo);

        historia.agregarEscena(new Escena(FINAL_OSO,
                "Dentro de la cueva te encontras con un oso durmiendo la siesta... y lo despertaste. FIN."));

        historia.agregarEscena(new Escena(FINAL_MIRADOR,
                "El sendero de piedras te lleva a un mirador con una vista hermosa del bosque. FIN."));

        historia.agregarEscena(new Escena(FINAL_PUEBLO,
                "Decidis quedarte a vivir en el pueblo. FIN."));
    }

    @Override
    protected void exerciseLogic() {
        menuLogic();
    }

    private void menuLogic() {
        if (firstTime) {
            System.out.println("\nBienvenido a la Aventura Narrativa!");
            firstTime = false;
        }

        Escena actual = historia.obtenerEscena(escenaActualId);
        System.out.println("\n" + actual.getDescripcion());

        int cantidadOpciones = actual.cantidadOpciones();
        // imprime la lista numerada de opciones de la escena actual para que el usuario elija.
        for (int i = 0; i < cantidadOpciones; i++) {
            System.out.println((i + 1) + ": " + actual.getTextoOpcion(i));
        }

        // Atras solo se ofrece si hay historial, o sea nunca en la primera pantalla
        boolean hayHistorial = !historial.isEmpty();
        int opcionAtras = cantidadOpciones + 1;
        if (hayHistorial) {
            System.out.println(opcionAtras + ": Atras");
        }
        System.out.println("mm: Volver al menu principal");

        String userInput = scanner.nextLine().trim().toLowerCase();

        if (userInput.equals("mm")) {
            running = false;
            return;
        }

        int opcionElegida;
        try {
            opcionElegida = Integer.parseInt(userInput);
        } catch (NumberFormatException e) {
            System.out.println("\nRespuesta invalida, ingrese un numero de opcion valido.");
            return;
        }

        if (hayHistorial && opcionElegida == opcionAtras) {
            escenaActualId = historial.pop();
            System.out.println("\nVolves sobre tus pasos...");
            return;
        }

        if (opcionElegida < 1 || opcionElegida > cantidadOpciones) {
            System.out.println("\nOpcion invalida.");
            return;
        }

        int destino = actual.getDestino(opcionElegida - 1); // busca a qué escena lleva la opción que el usuario acaba de elegir

        if (destino == -1) {
            // Opcion de accion (ej: armar fogata), no cambia de escena
            System.out.println("\nDecidis: " + actual.getTextoOpcion(opcionElegida - 1) + ". Descansas un rato y recuperas energias.");
            return;
        }

        historial.push(escenaActualId);
        escenaActualId = destino;
    }
}

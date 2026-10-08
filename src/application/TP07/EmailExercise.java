package application.TP07;

import java.util.Scanner;

import application.Exercise;
import application.listModule.SimpleList;

public class EmailExercise extends Exercise {

    private static final String CANCELAR = "cancelar";

    private final BandejaEntrada bandeja = new BandejaEntrada();
    private boolean firstTime = true;

    public EmailExercise(Scanner scanner) {
        super(scanner);
        cargarBaseDeDatos();
    }

    // Mails precargados para poder probar el orden sin escribir todo a mano
    private void cargarBaseDeDatos() {
        bandeja.recibir(new Mail("Reunion de grupo del parcial", Pestania.PRINCIPAL, Prioridad.BAJA));
        bandeja.recibir(new Mail("Entrega TP07 manana", Pestania.PRINCIPAL, Prioridad.ALTA));
        bandeja.recibir(new Mail("Alguien comento tu publicacion", Pestania.NOTIFICACIONES, Prioridad.BAJA));
        bandeja.recibir(new Mail("Nuevo inicio de sesion en tu cuenta", Pestania.NOTIFICACIONES, Prioridad.ALTA));
        bandeja.recibir(new Mail("Ganaste un premio!!!", Pestania.SPAM, Prioridad.BAJA));
    }

    @Override
    protected void exerciseLogic() {
        switch (currentPhase) {
            case 0:
                menuLogic();
                break;
            case 1:
                escribirMailLogic();
                break;
            case 2:
                verPestaniaLogic();
                break;
            case 3:
                abrirMailLogic();
                break;
        }
    }

    private void menuLogic() {
        if (firstTime) {
            System.out.println("\n¡Bienvenido al Organizador de E-mail!");
            firstTime = false;
        }

        mostrarResumen();

        System.out.println("\nSeleccione una opción:"
                + "\n1: Escribir mail"
                + "\n2: Ver pestaña"
                + "\n3: Abrir siguiente mail de una pestaña"
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
            case "mm":
                running = false;
                break;
            default:
                System.out.println("\nRespuesta inválida.");
                break;
        }
    }

    private void escribirMailLogic() {
        String asunto = pedirTexto("\nIngrese el asunto del mail");
        if (asunto == null) {
            volverAlMenu();
            return;
        }

        Pestania pestania = pedirPestania("\nSeleccione la pestaña a la que se asigna el mail");
        if (pestania == null) {
            volverAlMenu();
            return;
        }

        Prioridad prioridad = pedirPrioridad();
        if (prioridad == null) {
            volverAlMenu();
            return;
        }

        Mail mail = new Mail(asunto, pestania, prioridad);
        bandeja.recibir(mail);

        int posicion = posicionEnPestania(mail);
        System.out.println("\nMail recibido en " + pestania + " con prioridad " + prioridad
                + ". Quedó en la posición " + posicion + " de " + bandeja.cantidad(pestania) + ".");
        volverAlMenu();
    }

    private void verPestaniaLogic() {
        Pestania pestania = pedirPestania("\nSeleccione la pestaña que quiere ver");
        if (pestania == null) {
            volverAlMenu();
            return;
        }

        mostrarPestania(pestania);
        volverAlMenu();
    }

    private void abrirMailLogic() {
        Pestania pestania = pedirPestania("\nSeleccione la pestaña de la que quiere abrir el siguiente mail");
        if (pestania == null) {
            volverAlMenu();
            return;
        }

        // Validamos antes de llamar al TDA para que no lance la excepcion
        if (bandeja.isEmpty(pestania)) {
            System.out.println("\nNo hay mails en " + pestania + ".");
            volverAlMenu();
            return;
        }

        Mail mail = bandeja.abrirSiguiente(pestania);
        System.out.println("\n--- Mail abierto ---"
                + "\nAsunto: " + mail.getAsunto()
                + "\nPestaña: " + mail.getPestania()
                + "\nPrioridad: " + mail.getPrioridad()
                + "\n--------------------"
                + "\nEl mail se marcó como leído y salió de la bandeja.");
        volverAlMenu();
    }

    private void mostrarResumen() {
        System.out.println("\nBANDEJA DE ENTRADA:");
        for (Pestania pestania : Pestania.values()) {
            System.out.println("- " + pestania + ": " + bandeja.cantidad(pestania) + " sin leer");
        }
    }

    private void mostrarPestania(Pestania pestania) {
        System.out.println("\n" + pestania.getNombre().toUpperCase() + ":");
        if (bandeja.isEmpty(pestania)) {
            System.out.println("No hay mails en esta pestaña.");
            return;
        }

        SimpleList<Mail> mails = bandeja.snapshot(pestania);
        for (int i = 0; i < mails.size(); i++) {
            System.out.println((i + 1) + ". " + mails.get(i));
        }
    }

    // Busca en que posicion quedo el mail recien recibido,
    // para mostrarle al usuario que el orden es el correcto
    private int posicionEnPestania(Mail mail) {
        SimpleList<Mail> mails = bandeja.snapshot(mail.getPestania());
        for (int i = 0; i < mails.size(); i++) {
            if (mails.get(i) == mail) return i + 1;
        }
        return -1;
    }

    // Acepta el numero de la opcion o el nombre de la pestaña
    private Pestania pedirPestania(String mensaje) {
        Pestania[] pestanias = Pestania.values();

        while (true) {
            System.out.println(mensaje + " (\"" + CANCELAR + "\" para volver al menú):");
            for (int i = 0; i < pestanias.length; i++) {
                System.out.println((i + 1) + ": " + pestanias[i]);
            }

            String userInput = scanner.nextLine().trim();

            if (userInput.equalsIgnoreCase(CANCELAR)) {
                return null;
            }

            for (int i = 0; i < pestanias.length; i++) {
                if (userInput.equals(String.valueOf(i + 1))
                        || userInput.equalsIgnoreCase(pestanias[i].getNombre())) {
                    return pestanias[i];
                }
            }

            System.out.println("\nPestaña inválida. Ingrese 1 (Principal), 2 (Notificaciones) o 3 (Spam).");
        }
    }

    // Acepta el numero de la opcion o el nombre de la prioridad
    private Prioridad pedirPrioridad() {
        Prioridad[] prioridades = Prioridad.values();

        while (true) {
            System.out.println("\nSeleccione la prioridad del mail (\"" + CANCELAR + "\" para volver al menú):");
            for (int i = 0; i < prioridades.length; i++) {
                System.out.println((i + 1) + ": " + prioridades[i]);
            }

            String userInput = scanner.nextLine().trim();

            if (userInput.equalsIgnoreCase(CANCELAR)) {
                return null;
            }

            for (int i = 0; i < prioridades.length; i++) {
                if (userInput.equals(String.valueOf(i + 1))
                        || userInput.equalsIgnoreCase(prioridades[i].getNombre())) {
                    return prioridades[i];
                }
            }

            System.out.println("\nPrioridad inválida. Ingrese 1 (Alta) o 2 (Baja).");
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
}

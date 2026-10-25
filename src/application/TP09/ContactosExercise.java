package application.TP09;

import java.util.Scanner;

import application.Exercise;
import application.listModule.SimpleList;

public class ContactosExercise extends Exercise {

    private static final String CANCELAR = "cancelar";

    private final AgendaContactos agenda = new AgendaContactos();
    private boolean firstTime = true;

    public ContactosExercise(Scanner scanner) {
        super(scanner);
    }

    @Override
    protected void exerciseLogic() {
        switch (currentPhase) {
            case 0:
                menuLogic();
                break;
            case 1:
                crearLogic();
                break;
            case 2:
                verLogic();
                break;
            case 3:
                editarLogic();
                break;
            case 4:
                borrarLogic();
                break;
            case 5:
                cargarDatosLogic();
                break;
        }
    }

    private void menuLogic() {
        if (firstTime) {
            System.out.println("\nBienvenido a la agenda de contactos.");
            System.out.println("La agenda arranca vacia. La opcion 5 carga contactos de prueba.");
            firstTime = false;
        }

        System.out.println("\nAGENDA: " + agenda.size() + (agenda.size() == 1 ? " contacto" : " contactos"));

        System.out.println("\nSeleccione una opcion:"
                + "\n1: Crear contacto"
                + "\n2: Ver todos los contactos"
                + "\n3: Editar contacto"
                + "\n4: Borrar contacto"
                + "\n5: Cargar datos de prueba"
                + "\nmm: Volver al menu");

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
            case "5":
                currentPhase = 5;
                break;
            case "mm":
                running = false;
                break;
            default:
                System.out.println("\nRespuesta invalida.");
                break;
        }
    }

    private void crearLogic() {
        String nombre = pedirTexto("\nIngrese el nombre");
        if (nombre == null) {
            volverAlMenu();
            return;
        }

        // El nombre es la clave del arbol: se rechaza antes de pedir el resto.
        if (agenda.existe(nombre)) {
            System.out.println("\nYa existe un contacto con ese nombre.");
            volverAlMenu();
            return;
        }

        String telefono = pedirTelefono();
        if (telefono == null) {
            volverAlMenu();
            return;
        }

        String mail = pedirMail();
        if (mail == null) {
            volverAlMenu();
            return;
        }

        // Nombre, telefono y mail ya fueron validados, y el nombre no esta repetido.
        if (agenda.agregar(nombre, telefono, mail)) {
            System.out.println("\nContacto creado. Va a aparecer en orden alfabetico al ver la agenda.");
        } else {
            System.out.println("\nNo se pudo crear el contacto.");
        }
        volverAlMenu();
    }

    private void verLogic() {
        mostrarContactos();
        volverAlMenu();
    }

    private void editarLogic() {
        Contacto contacto = elegirContacto("editar");
        if (contacto == null) {
            volverAlMenu();
            return;
        }

        System.out.println("\nContacto seleccionado: " + contacto);
        Integer campo = pedirEntero("Que dato quiere cambiar?\n1: Nombre\n2: Telefono\n3: Mail\nSeleccione una opcion", 1, 3);
        if (campo == null) {
            volverAlMenu();
            return;
        }

        if (campo == 1) {
            editarNombre(contacto);
        } else if (campo == 2) {
            editarTelefono(contacto);
        } else {
            editarMail(contacto);
        }
        volverAlMenu();
    }

    private void editarNombre(Contacto contacto) {
        String nombre = pedirTexto("\nIngrese el nuevo nombre");
        if (nombre == null) return;

        if (!sigueEnAgenda(contacto)) return;

        if (agenda.existe(nombre) && !nombre.equalsIgnoreCase(contacto.getNombre())) {
            System.out.println("\nYa existe un contacto con ese nombre.");
            return;
        }

        if (agenda.editarNombre(contacto, nombre)) {
            System.out.println("\nNombre actualizado: " + contacto.getNombre());
        } else {
            System.out.println("\nNo se pudo actualizar el nombre.");
        }
    }

    private void editarTelefono(Contacto contacto) {
        String telefono = pedirTelefono();
        if (telefono == null) return;
        if (!sigueEnAgenda(contacto)) return;

        if (agenda.editarTelefono(contacto, telefono)) {
            System.out.println("\nTelefono actualizado: " + contacto.getTelefono());
        } else {
            System.out.println("\nNo se pudo actualizar el telefono.");
        }
    }

    private void editarMail(Contacto contacto) {
        String mail = pedirMail();
        if (mail == null) return;
        if (!sigueEnAgenda(contacto)) return;

        if (agenda.editarMail(contacto, mail)) {
            System.out.println("\nMail actualizado: " + contacto.getMail());
        } else {
            System.out.println("\nNo se pudo actualizar el mail.");
        }
    }

    private void borrarLogic() {
        Contacto contacto = elegirContacto("borrar");
        if (contacto == null) {
            volverAlMenu();
            return;
        }

        if (!confirmar("\nBorrar a " + contacto.getNombre() + "?")) {
            System.out.println("\nNo se borro el contacto.");
            volverAlMenu();
            return;
        }

        if (!sigueEnAgenda(contacto)) {
            volverAlMenu();
            return;
        }

        if (agenda.borrar(contacto)) {
            System.out.println("\nContacto borrado. Quedan " + agenda.size() + ".");
        } else {
            System.out.println("\nNo se pudo borrar el contacto.");
        }
        volverAlMenu();
    }

    private void cargarDatosLogic() {
        int agregados = agenda.cargarDatosDePrueba();
        int omitidos = agenda.cantidadDatosDePrueba() - agregados;

        if (agregados == 0) {
            System.out.println("\nNo se cargo ningun contacto nuevo: esos nombres ya estan en la agenda.");
        } else {
            System.out.println("\nSe cargaron " + agregados + " contactos de prueba.");
            if (omitidos > 0) {
                System.out.println("Se omitieron " + omitidos + " porque ya habia un contacto con ese nombre.");
            }
            mostrarContactos();
        }
        volverAlMenu();
    }

    private Contacto elegirContacto(String accion) {
        if (agenda.isEmpty()) {
            System.out.println("\nNo hay contactos en la agenda.");
            return null;
        }

        SimpleList<Contacto> lista = mostrarContactos();
        Integer posicion = pedirEntero("Seleccione el numero del contacto que quiere " + accion, 1, lista.size());
        if (posicion == null) return null;
        return lista.get(posicion - 1);
    }

    private SimpleList<Contacto> mostrarContactos() {
        SimpleList<Contacto> lista = agenda.listar();
        System.out.println("\nCONTACTOS:");
        if (lista.isEmpty()) {
            System.out.println("La agenda esta vacia. Use la opcion 5 para cargar datos de prueba.");
            return lista;
        }

        for (int i = 0; i < lista.size(); i++) {
            System.out.println((i + 1) + ". " + lista.get(i));
        }
        return lista;
    }

    private String pedirTelefono() {
        while (true) {
            System.out.println("\nIngrese el telefono (\"" + CANCELAR + "\" para volver al menu):");
            System.out.println("Use numeros, espacios, +, - o parentesis. Minimo 6 digitos.");

            String userInput = scanner.nextLine().trim();
            if (userInput.equalsIgnoreCase(CANCELAR)) return null;
            if (Contacto.esTelefonoValido(userInput)) return userInput;

            System.out.println("\nTelefono invalido.");
        }
    }

    private String pedirMail() {
        while (true) {
            System.out.println("\nIngrese el mail (\"" + CANCELAR + "\" para volver al menu):");

            String userInput = scanner.nextLine().trim();
            if (userInput.equalsIgnoreCase(CANCELAR)) return null;
            if (Contacto.esMailValido(userInput)) return userInput;

            System.out.println("\nMail invalido. Tiene que tener un @ y un dominio, por ejemplo ana@mail.com.");
        }
    }

    private boolean confirmar(String mensaje) {
        while (true) {
            System.out.println(mensaje + " (s/n)");
            String userInput = scanner.nextLine().trim().toLowerCase();
            if (userInput.equals("s")) return true;
            if (userInput.equals("n")) return false;
            System.out.println("\nRespuesta invalida. Ingrese s o n.");
        }
    }

    private boolean sigueEnAgenda(Contacto contacto) {
        if (agenda.existe(contacto.getNombre())) return true;
        System.out.println("\nEse contacto ya no esta en la agenda.");
        return false;
    }

    private void volverAlMenu() {
        currentPhase = 0;
    }

    private String pedirTexto(String mensaje) {
        while (true) {
            System.out.println(mensaje + " (\"" + CANCELAR + "\" para volver al menu):");

            String userInput = scanner.nextLine().trim();
            if (userInput.equalsIgnoreCase(CANCELAR)) return null;
            if (!userInput.isEmpty()) return userInput;

            System.out.println("\nEl texto no puede estar vacio.");
        }
    }

    private Integer pedirEntero(String mensaje, int minimo, int maximo) {
        while (true) {
            System.out.println("\n" + mensaje + " (\"" + CANCELAR + "\" para volver al menu):");
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

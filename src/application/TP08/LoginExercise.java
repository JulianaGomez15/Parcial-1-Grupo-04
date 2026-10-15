package application.TP08;

import java.util.Scanner;

import application.Exercise;

public class LoginExercise extends Exercise {

    private final SistemaLogin sistema = new SistemaLogin();
    private boolean firstTime = true;

    public LoginExercise(Scanner scanner) {
        super(scanner);

        // Base de datos pre-programada con los mails del equipo, para poder testear
        sistema.registrar("luka.ruizdiaz@uade.edu.ar", "1234");
        sistema.registrar("betsabe.rodriguez@uade.edu.ar", "1234");
        sistema.registrar("trinidad.garciavalicente@uade.edu.ar", "1234");
        sistema.registrar("juliana.gomez@uade.edu.ar", "1234");
        sistema.registrar("diego.cinquino@uade.edu.ar", "1234");
    }

    @Override
    protected void exerciseLogic() {
        menuLogic();
    }

    private void menuLogic() {
        if (firstTime) {
            System.out.println("\nBienvenido al portal de UADE!");
            firstTime = false;
        }

        System.out.println("\nSeleccione una opcion:"
                + "\nregistrar: Crear una cuenta nueva"
                + "\ningresar: Iniciar sesion"
                + "\nmm: Volver al menu principal");

        String userInput = scanner.nextLine().trim().toLowerCase();

        switch (userInput) {
            case "registrar":
                registrarLogic();
                break;
            case "ingresar":
                ingresarLogic();
                break;
            case "mm":
                running = false;
                break;
            default:
                System.out.println("\nRespuesta invalida.");
                break;
        }
    }

    private void registrarLogic() {
        System.out.println("\nIngrese su mail de UADE:");
        String nombreUsuario = scanner.nextLine().trim();

        if (nombreUsuario.isEmpty()) {
            System.out.println("\nEl nombre de usuario no puede estar vacio.");
            return;
        }

        if (!sistema.esMailValido(nombreUsuario)) {
            System.out.println("\nSolo se pueden registrar mails que terminen en @uade.edu.ar.");
            return;
        }

        if (sistema.buscar(nombreUsuario) != null) {
            System.out.println("\nYa existe una cuenta con ese mail.");
            return;
        }

        System.out.println("\nIngrese una contraseña:");
        String contrasenia = scanner.nextLine();

        if (contrasenia.isEmpty()) {
            System.out.println("\nLa contraseña no puede estar vacia.");
            return;
        }

        // nombreUsuario y contrasenia ya fueron validados arriba, asi que
        // sistema.registrar nunca recibe datos invalidos en uso normal.
        sistema.registrar(nombreUsuario, contrasenia);
        System.out.println("\nCuenta creada con exito.");
    }

    private void ingresarLogic() {
        System.out.println("\nIngrese su mail de UADE:");
        String nombreUsuario = scanner.nextLine().trim();

        Usuario usuario = sistema.buscar(nombreUsuario);
        if (usuario == null) {
            System.out.println("\nNo existe una cuenta con ese mail.");
            return;
        }

        if (usuario.isBloqueado()) {
            System.out.println("\nLa cuenta esta bloqueada por demasiados intentos fallidos.");
            return;
        }

        boolean correcta = false;
        while (!correcta && !usuario.isBloqueado()) {
            System.out.println("\nIngrese su contraseña:");
            String contrasenia = scanner.nextLine();

            // Ya chequeamos que no este bloqueado antes de entrar al while, asi que
            // validarContrasenia nunca explota en uso normal.
            correcta = usuario.validarContrasenia(contrasenia);

            if (correcta) {
                System.out.println("\nBienvenido/a, " + usuario.getNombreUsuario() + "!");
            } else if (usuario.isBloqueado()) {
                System.out.println("\nContraseña incorrecta. Se alcanzo el maximo de intentos: la cuenta fue bloqueada.");
            } else {
                System.out.println("\nContraseña incorrecta, intente nuevamente.");
            }
        }
    }
}

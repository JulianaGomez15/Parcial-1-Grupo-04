package TP03;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ValidadorEntrada {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private Scanner scanner;

    public ValidadorEntrada(Scanner scanner) {
        if (scanner == null) {
            throw new IllegalArgumentException("El scanner no puede ser nulo.");
        }
        this.scanner = scanner;
    }

    public int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            if (scanner.hasNextInt()) {
                int valor = scanner.nextInt();
                scanner.nextLine();
                return valor;
            }
            System.out.println("Eso no es un numero valido. Proba de nuevo.");
            scanner.nextLine();
        }
    }

    public int leerEnteroEntre(String mensaje, int minimo, int maximo) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor >= minimo && valor <= maximo) {
                return valor;
            }
            System.out.println("El numero debe estar entre " + minimo + " y " + maximo + ".");
        }
    }

    public String leerTextoNoVacio(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("El texto no puede estar vacio.");
        }
    }

    public LocalDateTime leerFechaHora(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String input = scanner.nextLine().trim();
            try {
                return LocalDateTime.parse(input, FORMATO);
            } catch (DateTimeParseException e) {
                System.out.println("Formato invalido. Use dd/MM/yyyy HH:mm (ejemplo: 28/08/2026 21:30).");
            }
        }
    }

}

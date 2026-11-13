package application.recursion;

import application.Exercise;

import java.util.Scanner;

public class RecursionExercise extends Exercise {
    // Límites para evitar overflow
    private static final int MAX_FIBONACCI = 40;
    private static final int MAX_EXCLUSIVE_SUM = 10000;
    private static final int MAX_PYRAMID = 30;
    private static final int MAX_FACTORIAL = 20;


    public RecursionExercise(Scanner scanner) {
        super(scanner);
    }

    @Override
    protected void exerciseLogic() {
        switch (currentPhase) {
            case 1: menuLogic(); break;
            case 2: factorialLogic(); break;
            case 3: fibonacciLogic(); break;
            case 4: exclusiveSumLogic(); break;
            case 5: pyramidLogic(); break;
            case 6: isPalindromeLogic(); break;
            default: currentPhase = 1;
        }
    }
    private void menuLogic() {
        System.out.println();
        System.out.println("=== Ejercicios de Recursión ===");
        System.out.println("1. Factorial");
        System.out.println("2. Fibonacci");
        System.out.println("3. Suma exclusiva");
        System.out.println("4. Pirámide");
        System.out.println("5. Palíndromo");
        System.out.println("0. Volver al menú principal");

        System.out.print("Elegí una opción: ");
        String userInput = scanner.nextLine().trim();

        switch (userInput) {
            case "1":
                currentPhase = 2;
                break;
            case "2":
                currentPhase = 3;
                break;
            case "3":
                currentPhase = 4;
                break;
            case "4":
                currentPhase = 5;
                break;
            case "5":
                currentPhase = 6;
                break;
            case "0":
                running = false;
                break;
            default:
                System.out.println("Opción inválida, probá de nuevo.");
                break;
        }
    }

    private void isPalindromeLogic() {
        System.out.println("\nIngrese una palabra:");
        String palabra = scanner.nextLine().trim().toLowerCase();

        if (isPalindrome(palabra, 0)) {
            System.out.println("La palabra \"" + palabra + "\" es un palíndromo.");
        } else {
            System.out.println("La palabra \"" + palabra + "\" no es un palíndromo.");
        }
        currentPhase = 0;
        return;
    }

    private boolean isPalindrome(String palabra, int indice) {
        if (palabra.length() == 1) {
            return true;
        }

        if (indice == (palabra.length() / 2))
            return true;

        if (palabra.charAt(indice) == palabra.charAt(palabra.length() - 1-indice))
            return isPalindrome(palabra, indice +1);

        return false;
    }

    private void pyramidLogic() {
        int number = pedirEntero();
        if (number == -1) {
            currentPhase = 0;
            return;
        }

        pyramid(number);
        currentPhase = 0;
    }

    private void pyramid(int number) {
        pyramidRecursive(number, 1);
    }

    private void pyramidRecursive(int total, int level) {
        if (level > total) {
            return;
        }

        // Imprimir espacios
        for (int i = 0; i < total - level; i++) {
            System.out.print(" ");
        }

        // Imprimir X
        for (int i = 0; i < level; i++) {
            System.out.print("X");
        }

        System.out.println();

        // Llamada recursiva
        pyramidRecursive(total, level + 1);
    }

    private void exclusiveSumLogic() {
        int number = pedirEntero();
        if (number == -1) {
            currentPhase = 0;
            return;
        }

        System.out.println("La suma exclusiva de " + number + " es: " + exclusiveSum(number));
        currentPhase = 0;
    }

    private int exclusiveSum(int number) {
        if (number <= 0) {
            return 0;
        }

        if (number % 2 != 0) {
            return exclusiveSum(number - 1);
        }

        return number + exclusiveSum(number - 1);
    }

    private void fibonacciLogic() {
        int number = pedirEntero();
        if (number == -1) {
            currentPhase = 0;
            return;
        }

        System.out.println("El numero Fibonacci en la posicion " + number + " es: " + fibonacci(number));
        currentPhase = 0;
    }

    private int fibonacci(int number) {
        if (number <= 1) {
            return number;
        }

        return fibonacci(number - 1) + fibonacci(number - 2);
    }

    private void factorialLogic() {
        int number = pedirEntero();
        if (number == -1) {
            currentPhase = 0;
            return;
        }

        System.out.println("El factorial de " + number + " es: " + factorial(number));
        currentPhase = 0;
    }

    private int factorial(int number) {
        if (number <= 1) {
            return 1;
        }

        return number * factorial(number - 1);
    }

    private int pedirEntero (){
        System.out.println("\nIngrese un numero entero no negativo:");
        int number;
        try {
            number = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Entrada invalida. Volviendo al menu.");
            currentPhase = 0;
            return -1;
        }

        if (number < 0) {
            System.out.println("El numero debe ser no negativo. Volviendo al menu.");
            currentPhase = 0;
            return-1 ;
        }
        return number;
    }
}

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
            case 6: palindromeLogic(); break;
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


    // --- Palindrome ---
    private void palindromeLogic() {
        System.out.print("Ingresá una palabra o frase: ");
        String userInput = scanner.nextLine().trim();

        if (userInput.isEmpty()) {
            System.out.println("No ingresaste nada.");
        } else {
            if (isPalindrome(userInput)) {
                System.out.println("\"" + userInput + "\" ES un palíndromo.");
            } else {
                System.out.println("\"" + userInput + "\" NO es un palíndromo.");
            }
            currentPhase = 1;
        }
    }

    boolean isPalindrome(String text) {
        // Este metodo se encarga de acomodar el texto para mostrarlo en pantalla
        return checkPalindrome(text.toLowerCase().replace(" ", ""));
    }

    private boolean checkPalindrome(String text) {
        //Caso base: con 0 o 1 caracteres siempre va a ser palíndromo
        if(text.length() <= 1) return true;

        String first = text.substring(0, 1);
        String last = text.substring(text.length()-1);
        String middle = text.substring(1, text.length()-1);

        //Caso base: si los extremos son distintos, no es palíndromo
        if(!first.equals(last)) return false;

        //Paso recursivo: prueba con el medio
        else return checkPalindrome(middle);
    }


    // --- Pirámide ---
    private void pyramidLogic() {
    }


    // --- Exclusive Sum ---
    private void exclusiveSumLogic() {
        System.out.print("Ingresá un número (1 a " + MAX_EXCLUSIVE_SUM + "): ");
        String userInput = scanner.nextLine().trim();

        try {
            int n = Integer.parseInt(userInput);
            if (n < 1 || n > MAX_EXCLUSIVE_SUM) {
                System.out.println("El número tiene que estar entre 1 y " + MAX_EXCLUSIVE_SUM + ".");
            } else {
                System.out.println("Suma de enteros positivos menores a " + n + " = " + exclusiveSum(n));
                currentPhase = 1;
            }
        } catch (NumberFormatException e) {
            System.out.println("Eso no es un número entero válido.");
        }
    }

    long exclusiveSum(int n) {
        //Caso base
        if(n <= 1) return 0;
        //Paso recursivo
        else return (n-1) + exclusiveSum(n-1);
    }


    // --- Fibonacci ---
    private void fibonacciLogic() {
        System.out.print("Ingresá una posición (0 a " + MAX_FIBONACCI + "): ");
        String userInput = scanner.nextLine().trim();

        try {
            int position = Integer.parseInt(userInput);
            if (position < 0 || position > MAX_FIBONACCI) {
                System.out.println("La posición tiene que estar entre 0 y " + MAX_FIBONACCI + ".");
            } else {
                System.out.println("Fibonacci(" + position + ") = " + fibonacci(position));
                currentPhase = 1;
            }
        } catch (NumberFormatException e) {
            System.out.println("No es un número válido.");
        }
    }

    long fibonacci(int position){
        //Caso base
        if(position == 0 || position == 1) return position;

        //Paso recursivo
        else return fibonacci(position-1) + fibonacci(position-2);
    }


    // --- Factorial ---
    private void factorialLogic() {
        System.out.print("Ingresá un número (0 a " + MAX_FACTORIAL + "): ");
        String userInput = scanner.nextLine().trim();

        try {
            int n = Integer.parseInt(userInput);
            if (n < 0 || n > MAX_FACTORIAL) {
                System.out.println("El número tiene que estar entre 0 y " + MAX_FACTORIAL + ".");
            } else {
                System.out.println(n + "! = " + factorial(n));
                currentPhase = 1;
            }
        } catch (NumberFormatException e) {
            System.out.println("Eso no es un número entero válido.");
        }

    }

    long factorial(int n) {
        //Caso base
        if(n == 0 || n == 1) return 1;

        //Paso recursivo
        else return n * factorial(n-1);
    }

}

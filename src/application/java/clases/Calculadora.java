package application.java.clases;

public class Calculadora{
    String nombre;

    public void sumarSinRetorno (int a, int b) {
        System.out.println("El resultado de la suma es = " + a + b);
    }
    public void restarSinRetorno (int a, int b) {
        System.out.println("El resultado de la resta es " + a + b);
    }
    public void multiplicarSinRetorno(int a, int b) {
        System.out.println("El resultado de" + nombre + "en la multiplicacion es " + a * b);
    }
    public void dividirSinRetorno(int a, int b) {
        System.out.println("El resultado de la division es " + a * b);
    }

    public int sumar(int a, int b) {
        return a + b;
    }
    public int restar (int a, int b) {
        return a - b;
    }
    public int multiplicar (int a, int b) {
        return a * b;
    }
    public int dividir (int a, int b) {
        return a / b;
    }

    public Calculadora(String nombreParametro) {
        this.nombre = nombreParametro;
    }
}


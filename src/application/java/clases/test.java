package application.java.clases;

public class test {
    public static void main(String[] args) {
        // Creo el objeto Calculadora para utilizarlo en futuras operaciones
        Calculadora calc = new Calculadora();

        // Metodos sin retorno (el metodo se invoca, resuelve la operacion e imprime en pantalla)
        calc.sumarSinRetorno(1, 3);
        calc.restarSinRetorno(5, 3);
        calc.multiplicarSinRetorno(5,5);
        calc.dividirSinRetorno(10, 2);

        // Metodos con retorno
        int rtSuma = calc.sumar(10,5);
        System.out.println(rtSuma);

        int rtResta = calc.restar(15,6);
        System.out.println(rtResta);

        int rtMultiplicar = calc.multiplicar(5,5);
        System.out.println(rtMultiplicar);

        int rtDividir = calc.dividir(15,5);
        System.out.println(rtDividir);


    }
}

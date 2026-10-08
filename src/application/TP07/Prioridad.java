package application.TP07;

// Nivel de prioridad de un mail.
// El valor es el que se le pasa a la PriorityQueue:
// en nuestro TDA, el numero mas chico sale primero.
public enum Prioridad {
    ALTA("Alta", 0),
    BAJA("Baja", 1);

    private final String nombre;
    private final int valor;

    Prioridad(String nombre, int valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    public String getNombre() {
        return nombre;
    }

    public int getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return nombre;
    }
}

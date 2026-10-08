package application.TP07;

// Las 3 pestañas posibles de la bandeja de entrada.
// Al ser un enum, no se puede crear una pestaña que no exista.
public enum Pestania {
    PRINCIPAL("Principal"),
    NOTIFICACIONES("Notificaciones"),
    SPAM("Spam");

    private final String nombre;

    Pestania(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}

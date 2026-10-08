package application.TP07;

public class Mail {
    private final String asunto;
    private final Pestania pestania;
    private final Prioridad prioridad;

    public Mail(String asunto, Pestania pestania, Prioridad prioridad) {
        // El modelo rechaza datos invalidos con excepciones.
        // La interfaz de consola se encarga de que esto nunca pase.
        if (asunto == null || asunto.trim().isEmpty()) {
            throw new IllegalArgumentException("El asunto no puede estar vacio.");
        }
        if (pestania == null) {
            throw new IllegalArgumentException("La pestania no puede ser nula.");
        }
        if (prioridad == null) {
            throw new IllegalArgumentException("La prioridad no puede ser nula.");
        }

        this.asunto = asunto.trim();
        this.pestania = pestania;
        this.prioridad = prioridad;
    }

    public String getAsunto() {
        return asunto;
    }

    public Pestania getPestania() {
        return pestania;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    @Override
    public String toString() {
        return "[" + prioridad + "] " + asunto;
    }
}

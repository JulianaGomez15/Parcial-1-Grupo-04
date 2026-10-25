package application.TP07;

public class Mail {
    private final String asunto;
    private final Pestania pestania;
    private final Prioridad prioridad;

    public Mail(String asunto, Pestania pestania, Prioridad prioridad) {
        this.asunto = asunto.trim();
        this.pestania = pestania;
        this.prioridad = prioridad;
    }

    public static boolean esValido(String asunto, Pestania pestania, Prioridad prioridad) {
        return asunto != null && !asunto.trim().isEmpty() && pestania != null && prioridad != null;
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

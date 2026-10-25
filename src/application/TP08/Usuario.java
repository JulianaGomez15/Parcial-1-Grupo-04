package application.TP08;

public class Usuario {

    private static final int MAX_INTENTOS = 3;

    private final String nombreUsuario;
    private final String contrasenia;
    private int intentosFallidos = 0;
    private boolean bloqueado = false;

    public Usuario(String nombreUsuario, String contrasenia) {
        this.nombreUsuario = nombreUsuario.trim();
        this.contrasenia = contrasenia;
    }

    public static boolean esValido(String nombreUsuario, String contrasenia) {
        return nombreUsuario != null && !nombreUsuario.trim().isEmpty()
                && contrasenia != null && !contrasenia.isEmpty();
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    // Devuelve true si la contrasenia es correcta. Si no lo es, cuenta el intento
    // fallido y bloquea la cuenta al llegar a MAX_INTENTOS.
    public boolean validarContrasenia(String intento) {
        if (bloqueado) return false;

        if (contrasenia.equals(intento)) {
            intentosFallidos = 0;
            return true;
        }

        intentosFallidos++;
        if (intentosFallidos >= MAX_INTENTOS) {
            bloqueado = true;
        }
        return false;
    }
}
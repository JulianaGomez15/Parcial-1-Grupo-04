package application.TP06;

import java.util.Locale;

public class Genero {
    private final String nombre;

    public Genero(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del genero no puede estar vacio.");
        }
        this.nombre = nombre.trim();
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Genero)) return false;
        Genero genero = (Genero) other;
        return nombre.equalsIgnoreCase(genero.nombre);
    }

    @Override
    public int hashCode() {
        return nombre.toLowerCase(Locale.ROOT).hashCode();
    }

    @Override
    public String toString() {
        return nombre;
    }
}

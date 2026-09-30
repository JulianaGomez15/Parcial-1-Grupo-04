package application.TP06;

import java.util.Locale;

public class Cancion {
    private final String titulo;
    private final String artista;
    private final Genero genero;

    public Cancion(String titulo, String artista, Genero genero) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El titulo no puede estar vacio.");
        }
        if (artista == null || artista.trim().isEmpty()) {
            throw new IllegalArgumentException("El artista no puede estar vacio.");
        }
        if (genero == null) {
            throw new IllegalArgumentException("El genero no puede ser nulo.");
        }
        this.titulo = titulo.trim();
        this.artista = artista.trim();
        this.genero = genero;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getArtista() {
        return artista;
    }

    public Genero getGenero() {
        return genero;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Cancion)) return false;
        Cancion cancion = (Cancion) other;
        return titulo.equalsIgnoreCase(cancion.titulo)
                && artista.equalsIgnoreCase(cancion.artista);
    }

    @Override
    public int hashCode() {
        return 31 * titulo.toLowerCase(Locale.ROOT).hashCode()
                + artista.toLowerCase(Locale.ROOT).hashCode();
    }

    @Override
    public String toString() {
        return titulo + " - " + artista + " [" + genero + "]";
    }
}

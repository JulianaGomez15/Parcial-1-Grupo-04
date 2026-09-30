package application.TP06;

import application.setModule.SimpleArraySet;
import application.setModule.SimpleSet;

import java.util.Locale;

public class Usuario {
    private final String nombre;
    private final SimpleSet<Genero> generosFavoritos = new SimpleArraySet<>();
    private final SimpleSet<Cancion> cancionesEscuchadas = new SimpleArraySet<>();

    public Usuario(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del usuario no puede estar vacio.");
        }
        this.nombre = nombre.trim();
    }

    public String getNombre() {
        return nombre;
    }

    public SimpleSet<Genero> getGenerosFavoritos() {
        return generosFavoritos;
    }

    public SimpleSet<Cancion> getCancionesEscuchadas() {
        return cancionesEscuchadas;
    }

    public boolean agregarGeneroFavorito(Genero genero) {
        if (genero == null) throw new IllegalArgumentException("El genero no puede ser nulo.");
        return generosFavoritos.add(genero);
    }

    public boolean quitarGeneroFavorito(Genero genero) {
        if (genero == null) throw new IllegalArgumentException("El genero no puede ser nulo.");
        return generosFavoritos.remove(genero);
    }

    public boolean registrarEscuchada(Cancion cancion) {
        if (cancion == null) throw new IllegalArgumentException("La cancion no puede ser nula.");
        return cancionesEscuchadas.add(cancion);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Usuario)) return false;
        Usuario usuario = (Usuario) other;
        return nombre.equalsIgnoreCase(usuario.nombre);
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

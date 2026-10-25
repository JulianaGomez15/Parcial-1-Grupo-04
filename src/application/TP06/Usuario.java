package application.TP06;

import application.setModule.SimpleArraySet;
import application.setModule.SimpleSet;

import java.util.Locale;

public class Usuario {
    private final String nombre;
    private final SimpleSet<Genero> generosFavoritos = new SimpleArraySet<>();
    private final SimpleSet<Cancion> cancionesEscuchadas = new SimpleArraySet<>();

    public Usuario(String nombre) {
        this.nombre = nombre.trim();
    }

    public static boolean esNombreValido(String nombre) {
        return nombre != null && !nombre.trim().isEmpty();
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
        if (genero == null) return false;
        return generosFavoritos.add(genero);
    }

    public boolean quitarGeneroFavorito(Genero genero) {
        if (genero == null) return false;
        return generosFavoritos.remove(genero);
    }

    public boolean registrarEscuchada(Cancion cancion) {
        if (cancion == null) return false;
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

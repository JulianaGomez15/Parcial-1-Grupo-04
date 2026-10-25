package application.TP06;

import application.setModule.SimpleArraySet;
import application.setModule.SimpleSet;

import java.util.Optional;
import java.util.Random;

public class SistemaRecomendacion {
    private final SimpleSet<Genero> generos = new SimpleArraySet<>();
    private final SimpleSet<Cancion> canciones = new SimpleArraySet<>();
    private final SimpleSet<Usuario> usuarios = new SimpleArraySet<>();
    private final Random random = new Random();

    public boolean agregarGenero(Genero genero) {
        if (genero == null || genero.getNombre().isEmpty()) return false;
        return generos.add(genero);
    }

    public boolean agregarCancion(Cancion cancion) {
        if (cancion == null || cancion.getTitulo().isEmpty() || cancion.getArtista().isEmpty()) return false;
        if (cancion.getGenero() == null || !generos.contains(cancion.getGenero())) return false;
        return canciones.add(cancion);
    }

    public boolean agregarUsuario(Usuario usuario) {
        if (usuario == null || usuario.getNombre().isEmpty()) return false;
        return usuarios.add(usuario);
    }

    public Genero buscarGenero(String nombre) {
        if (!Genero.esNombreValido(nombre)) return null;
        Genero buscado = new Genero(nombre);
        for (Genero genero : generos.toArray(new Genero[0])) {
            if (genero.equals(buscado)) return genero;
        }
        return null;
    }

    public Usuario buscarUsuario(String nombre) {
        if (!Usuario.esNombreValido(nombre)) return null;
        Usuario buscado = new Usuario(nombre);
        for (Usuario usuario : usuarios.toArray(new Usuario[0])) {
            if (usuario.equals(buscado)) return usuario;
        }
        return null;
    }

    public Cancion buscarCancion(int posicion) {
        Cancion[] listado = canciones.toArray(new Cancion[0]);
        if (posicion < 1 || posicion > listado.length) return null;
        return listado[posicion - 1];
    }

    public boolean registrarGeneroFavorito(Usuario usuario, Genero genero) {
        if (!pertenece(usuario)) return false;
        if (genero == null || !generos.contains(genero)) return false;
        return usuario.agregarGeneroFavorito(genero);
    }

    public boolean quitarGeneroFavorito(Usuario usuario, Genero genero) {
        if (!pertenece(usuario)) return false;
        if (genero == null || !generos.contains(genero)) return false;
        return usuario.quitarGeneroFavorito(genero);
    }

    public boolean registrarEscuchada(Usuario usuario, Cancion cancion) {
        if (!pertenece(usuario)) return false;
        if (cancion == null || !canciones.contains(cancion)) return false;
        return usuario.registrarEscuchada(cancion);
    }

    public Optional<Cancion> recomendar(Usuario usuario) {
        if (!pertenece(usuario)) return Optional.empty();
        SimpleSet<Cancion> candidatas = new SimpleArraySet<>();
        for (Cancion cancion : canciones.toArray(new Cancion[0])) {
            if (usuario.getGenerosFavoritos().contains(cancion.getGenero())
                    && !usuario.getCancionesEscuchadas().contains(cancion)) {
                candidatas.add(cancion);
            }
        }
        Cancion[] opciones = candidatas.toArray(new Cancion[0]);
        if (opciones.length == 0) return Optional.empty();
        return Optional.of(opciones[random.nextInt(opciones.length)]);
    }

    public SimpleSet<Genero> getGeneros() {
        return generos;
    }

    public SimpleSet<Cancion> getCanciones() {
        return canciones;
    }

    public SimpleSet<Usuario> getUsuarios() {
        return usuarios;
    }

    private boolean pertenece(Usuario usuario) {
        return usuario != null && usuarios.contains(usuario);
    }
}

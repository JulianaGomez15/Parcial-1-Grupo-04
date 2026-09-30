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
        if (genero == null) throw new IllegalArgumentException("El genero no puede ser nulo.");
        return generos.add(genero);
    }

    public boolean agregarCancion(Cancion cancion) {
        if (cancion == null) throw new IllegalArgumentException("La cancion no puede ser nula.");
        if (!generos.contains(cancion.getGenero())) {
            throw new IllegalArgumentException("El genero de la cancion no esta registrado.");
        }
        return canciones.add(cancion);
    }

    public boolean agregarUsuario(Usuario usuario) {
        if (usuario == null) throw new IllegalArgumentException("El usuario no puede ser nulo.");
        return usuarios.add(usuario);
    }

    public Genero buscarGenero(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) return null;
        Genero buscado = new Genero(nombre);
        for (Genero genero : generos.toArray(new Genero[0])) {
            if (genero.equals(buscado)) return genero;
        }
        return null;
    }

    public Usuario buscarUsuario(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) return null;
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
        validarUsuario(usuario);
        if (genero == null || !generos.contains(genero)) {
            throw new IllegalArgumentException("El genero no esta registrado.");
        }
        return usuario.agregarGeneroFavorito(genero);
    }

    public boolean quitarGeneroFavorito(Usuario usuario, Genero genero) {
        validarUsuario(usuario);
        if (genero == null || !generos.contains(genero)) {
            throw new IllegalArgumentException("El genero no esta registrado.");
        }
        return usuario.quitarGeneroFavorito(genero);
    }

    public boolean registrarEscuchada(Usuario usuario, Cancion cancion) {
        validarUsuario(usuario);
        if (cancion == null || !canciones.contains(cancion)) {
            throw new IllegalArgumentException("La cancion no esta registrada.");
        }
        return usuario.registrarEscuchada(cancion);
    }

    public Optional<Cancion> recomendar(Usuario usuario) {
        validarUsuario(usuario);
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

    private void validarUsuario(Usuario usuario) {
        if (usuario == null || !usuarios.contains(usuario)) {
            throw new IllegalArgumentException("El usuario no pertenece al sistema.");
        }
    }
}

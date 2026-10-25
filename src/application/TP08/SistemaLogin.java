package application.TP08;

import application.dictionaryModule.SimpleDictionary;
import application.dictionaryModule.SimpleLinkedDictionary;

public class SistemaLogin {

    private static final String DOMINIO_PERMITIDO = "@uade.edu.ar";

    private final SimpleDictionary<String, Usuario> usuarios = new SimpleLinkedDictionary<>();

    public boolean registrar(String nombreUsuario, String contrasenia) {
        if (!esMailValido(nombreUsuario) || !Usuario.esValido(nombreUsuario, contrasenia)) return false;
        if (usuarios.containsKey(nombreUsuario)) return false;
        usuarios.put(nombreUsuario, new Usuario(nombreUsuario, contrasenia));
        return true;
    }

    // Solo se permiten mails @uade.edu.ar, con algo antes de la arroba
    public boolean esMailValido(String nombreUsuario) {
        if (nombreUsuario == null) return false;
        String mail = nombreUsuario.toLowerCase();
        return mail.endsWith(DOMINIO_PERMITIDO) && mail.length() > DOMINIO_PERMITIDO.length();
    }

    // Devuelve null si no existe un usuario con ese nombre
    public Usuario buscar(String nombreUsuario) {
        if (nombreUsuario == null || nombreUsuario.isEmpty()) return null;
        return usuarios.get(nombreUsuario);
    }
}
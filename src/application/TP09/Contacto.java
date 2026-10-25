package application.TP09;

import java.util.Locale;

public class Contacto implements Comparable<Contacto> {

    private String nombre;
    private String telefono;
    private String mail;

    // Lo usa la agenda despues de validar los tres datos.
    Contacto(String nombre, String telefono, String mail) {
        this.nombre = nombre.trim();
        this.telefono = telefono.trim();
        this.mail = mail.trim();
    }

    // Sirve para buscar en el BST por nombre, sin inventar telefono ni mail.
    // No se inserta en el arbol.
    private Contacto(String nombre) {
        this.nombre = nombre;
    }

    public static Contacto paraBuscar(String nombre) {
        if (!esNombreValido(nombre)) return null;
        return new Contacto(nombre.trim());
    }

    public static boolean esNombreValido(String nombre) {
        return nombre != null && !nombre.trim().isEmpty();
    }

    // Acepta digitos, espacios, +, - y parentesis, con al menos 6 digitos.
    public static boolean esTelefonoValido(String telefono) {
        if (telefono == null) return false;
        String texto = telefono.trim();
        if (texto.isEmpty()) return false;

        int digitos = 0;
        for (int i = 0; i < texto.length(); i++) {
            char caracter = texto.charAt(i);
            if (Character.isDigit(caracter)) {
                digitos++;
            } else if (caracter != ' ' && caracter != '+' && caracter != '-' && caracter != '(' && caracter != ')') {
                return false;
            }
        }
        return digitos >= 6;
    }

    // Exige texto, una sola @, y un dominio con un punto que no quede al final.
    public static boolean esMailValido(String mail) {
        if (mail == null) return false;
        String texto = mail.trim();
        int arroba = texto.indexOf('@');
        if (arroba <= 0 || texto.indexOf('@', arroba + 1) != -1) return false;

        String dominio = texto.substring(arroba + 1);
        int punto = dominio.lastIndexOf('.');
        return punto > 0 && punto < dominio.length() - 1 && !dominio.contains(" ");
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getMail() {
        return mail;
    }

    public boolean setNombre(String nombre) {
        if (!esNombreValido(nombre)) return false;
        this.nombre = nombre.trim();
        return true;
    }

    public boolean setTelefono(String telefono) {
        if (!esTelefonoValido(telefono)) return false;
        this.telefono = telefono.trim();
        return true;
    }

    public boolean setMail(String mail) {
        if (!esMailValido(mail)) return false;
        this.mail = mail.trim();
        return true;
    }

    // La clave del BST es el nombre, sin distinguir mayusculas.
    @Override
    public int compareTo(Contacto other) {
        return nombre.toLowerCase(Locale.ROOT).compareTo(other.nombre.toLowerCase(Locale.ROOT));
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Contacto)) return false;
        return nombre.equalsIgnoreCase(((Contacto) other).nombre);
    }

    @Override
    public int hashCode() {
        return nombre.toLowerCase(Locale.ROOT).hashCode();
    }

    @Override
    public String toString() {
        return nombre + " | " + telefono + " | " + mail;
    }
}

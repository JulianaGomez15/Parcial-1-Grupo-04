package application.TP09;

import application.listModule.SimpleList;
import application.treeModule.BST;

public class AgendaContactos {

    private static final String[][] DATOS_DE_PRUEBA = {
            {"Luka Ruiz Diaz", "11 5555-0101", "luka.ruizdiaz@uade.edu.ar"},
            {"Betsabe Rodriguez", "11 5555-0102", "betsabe.rodriguez@uade.edu.ar"},
            {"Trinidad Garcia Valicente", "11 5555-0103", "trinidad.garciavalicente@uade.edu.ar"},
            {"Juliana Gomez", "11 5555-0104", "juliana.gomez@uade.edu.ar"},
            {"Diego Cinquino", "11 5555-0105", "diego.cinquino@uade.edu.ar"}
    };

    private final BST<Contacto> contactos = new BST<>();

    public boolean agregar(String nombre, String telefono, String mail) {
        if (!Contacto.esNombreValido(nombre)) return false;
        if (!Contacto.esTelefonoValido(telefono)) return false;
        if (!Contacto.esMailValido(mail)) return false;
        if (existe(nombre)) return false;

        Contacto contacto = new Contacto(nombre, telefono, mail);
        contactos.insert(contacto);
        return true;
    }

    public boolean existe(String nombre) {
        if (!Contacto.esNombreValido(nombre)) return false;
        return contactos.contains(Contacto.paraBuscar(nombre));
    }

    public Contacto buscar(String nombre) {
        if (!Contacto.esNombreValido(nombre)) return null;

        return contactos.search(Contacto.paraBuscar(nombre));
    }

    public boolean editarNombre(Contacto contacto, String nuevoNombre) {
        if (!estaEnAgenda(contacto)) return false;
        if (!Contacto.esNombreValido(nuevoNombre)) return false;

        String normalizado = nuevoNombre.trim();
        if (contacto.getNombre().equalsIgnoreCase(normalizado)) {
            return contacto.setNombre(normalizado);
        }

        if (contactos.contains(Contacto.paraBuscar(normalizado))) return false;

        // El nombre es la clave del arbol: hay que sacarlo y volver a insertarlo.
        contactos.remove(contacto);
        contacto.setNombre(normalizado);
        contactos.insert(contacto);
        return true;
    }

    public boolean editarTelefono(Contacto contacto, String telefono) {
        if (!estaEnAgenda(contacto)) return false;
        if (!Contacto.esTelefonoValido(telefono)) return false;
        return contacto.setTelefono(telefono);
    }

    public boolean editarMail(Contacto contacto, String mail) {
        if (!estaEnAgenda(contacto)) return false;
        if (!Contacto.esMailValido(mail)) return false;
        return contacto.setMail(mail);
    }

    public boolean borrar(Contacto contacto) {
        if (!estaEnAgenda(contacto)) return false;
        contactos.remove(contacto);
        return true;
    }

    // Agrega los contactos de prueba que todavia no estan. Devuelve cuantos entro.
    public int cargarDatosDePrueba() {
        int agregados = 0;
        for (String[] fila : DATOS_DE_PRUEBA) {
            if (agregar(fila[0], fila[1], fila[2])) agregados++;
        }
        return agregados;
    }

    public int cantidadDatosDePrueba() {
        return DATOS_DE_PRUEBA.length;
    }

    public SimpleList<Contacto> listar() {
        return contactos.inOrder();
    }

    public int size() {
        return contactos.size();
    }

    public boolean isEmpty() {
        return contactos.isEmpty();
    }

    private boolean estaEnAgenda(Contacto contacto) {
        return contacto != null && contactos.contains(contacto);
    }

}

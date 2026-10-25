package application.TP09;

import application.listModule.SimpleArrayList;
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
    // Los mismos objetos del arbol, en orden alfabetico.
    // inOrder solo imprime, y el BST no devuelve el nodo ni el tamaño.
    private final SimpleList<Contacto> listado = new SimpleArrayList<>();

    public boolean agregar(String nombre, String telefono, String mail) {
        if (!Contacto.esNombreValido(nombre)) return false;
        if (!Contacto.esTelefonoValido(telefono)) return false;
        if (!Contacto.esMailValido(mail)) return false;
        if (existe(nombre)) return false;

        Contacto contacto = new Contacto(nombre, telefono, mail);
        contactos.insert(contacto);
        insertarOrdenado(contacto);
        return true;
    }

    public boolean existe(String nombre) {
        if (!Contacto.esNombreValido(nombre)) return false;
        return contactos.contains(Contacto.paraBuscar(nombre));
    }

    public Contacto buscar(String nombre) {
        if (!existe(nombre)) return null;
        return obtener(Contacto.paraBuscar(nombre));
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
        listado.remove(contacto);
        insertarOrdenado(contacto);
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
        listado.remove(contacto);
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
        SimpleList<Contacto> copia = new SimpleArrayList<>();
        for (int i = 0; i < listado.size(); i++) {
            copia.add(listado.get(i));
        }
        return copia;
    }

    public int size() {
        return listado.size();
    }

    public boolean isEmpty() {
        return contactos.isEmpty();
    }

    private boolean estaEnAgenda(Contacto contacto) {
        return contacto != null && contactos.contains(contacto);
    }

    private void insertarOrdenado(Contacto contacto) {
        int indice = 0;
        while (indice < listado.size() && listado.get(indice).compareTo(contacto) < 0) {
            indice++;
        }
        listado.add(indice, contacto);
    }

    private Contacto obtener(Contacto clave) {
        for (int i = 0; i < listado.size(); i++) {
            Contacto actual = listado.get(i);
            if (actual.compareTo(clave) == 0) return actual;
        }
        return null;
    }
}

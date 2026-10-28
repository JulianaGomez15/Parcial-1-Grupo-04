package application.TP10;

import application.listModule.SimpleArrayList;
import application.listModule.SimpleList;
import application.treeModule.AVL;
import application.treeModule.TreeNode;

public class AgendaContactosAVL {

    private static final String[][] DATOS_DE_PRUEBA = {
            {"Luka Ruiz Diaz", "11 5555-0101", "luka.ruizdiaz@uade.edu.ar"},
            {"Betsabe Rodriguez", "11 5555-0102", "betsabe.rodriguez@uade.edu.ar"},
            {"Trinidad Garcia Valicente", "11 5555-0103", "trinidad.garciavalicente@uade.edu.ar"},
            {"Juliana Gomez", "11 5555-0104", "juliana.gomez@uade.edu.ar"},
            {"Diego Cinquino", "11 5555-0105", "diego.cinquino@uade.edu.ar"}
    };

    private final ContactTree contactos = new ContactTree();
    private final SimpleList<ContactoAVL> listado = new SimpleArrayList<>();

    public boolean agregar(String nombre, String telefono, String mail) {
        if (!ContactoAVL.esNombreValido(nombre)) return false;
        if (!ContactoAVL.esTelefonoValido(telefono)) return false;
        if (!ContactoAVL.esMailValido(mail)) return false;
        if (existe(nombre)) return false;

        ContactoAVL contacto = new ContactoAVL(nombre, telefono, mail);
        contactos.insert(contacto);
        insertarOrdenado(contacto);
        return true;
    }

    public boolean existe(String nombre) {
        if (!ContactoAVL.esNombreValido(nombre)) return false;
        return contactos.contains(ContactoAVL.paraBuscar(nombre));
    }

    public ContactoAVL buscar(String nombre) {
        if (!existe(nombre)) return null;
        return obtener(ContactoAVL.paraBuscar(nombre));
    }

    public boolean editarNombre(ContactoAVL contacto, String nuevoNombre) {
        if (!estaEnAgenda(contacto) || !ContactoAVL.esNombreValido(nuevoNombre)) return false;

        String normalizado = nuevoNombre.trim();
        if (contacto.getNombre().equalsIgnoreCase(normalizado)) {
            return contacto.setNombre(normalizado);
        }
        if (contactos.contains(ContactoAVL.paraBuscar(normalizado))) return false;

        contactos.remove(contacto);
        contacto.setNombre(normalizado);
        contactos.insert(contacto);
        listado.remove(contacto);
        insertarOrdenado(contacto);
        return true;
    }

    public boolean editarTelefono(ContactoAVL contacto, String telefono) {
        if (!estaEnAgenda(contacto) || !ContactoAVL.esTelefonoValido(telefono)) return false;
        return contacto.setTelefono(telefono);
    }

    public boolean editarMail(ContactoAVL contacto, String mail) {
        if (!estaEnAgenda(contacto) || !ContactoAVL.esMailValido(mail)) return false;
        return contacto.setMail(mail);
    }

    public boolean borrar(ContactoAVL contacto) {
        if (!estaEnAgenda(contacto)) return false;
        contactos.remove(contacto);
        listado.remove(contacto);
        return true;
    }

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

    public SimpleList<ContactoAVL> listar() {
        SimpleList<ContactoAVL> copia = new SimpleArrayList<>();
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

    private boolean estaEnAgenda(ContactoAVL contacto) {
        return contacto != null && contactos.contains(contacto);
    }

    private void insertarOrdenado(ContactoAVL contacto) {
        int indice = 0;
        while (indice < listado.size() && listado.get(indice).compareTo(contacto) < 0) {
            indice++;
        }
        listado.add(indice, contacto);
    }

    private ContactoAVL obtener(ContactoAVL clave) {
        for (int i = 0; i < listado.size(); i++) {
            ContactoAVL actual = listado.get(i);
            if (actual.compareTo(clave) == 0) return actual;
        }
        return null;
    }

    private static class ContactTree extends AVL<ContactoAVL> {
        @Override
        protected TreeNode<ContactoAVL> removeRecursive(TreeNode<ContactoAVL> current, ContactoAVL value) {
            if (current == null) return null;

            int comparison = value.compareTo(current.value);
            if (comparison < 0) {
                current.left = removeRecursive(current.left, value);
            } else if (comparison > 0) {
                current.right = removeRecursive(current.right, value);
            } else if (current.left == null) {
                return current.right;
            } else if (current.right == null) {
                return current.left;
            } else {
                TreeNode<ContactoAVL> successor = current.right;
                while (successor.left != null) successor = successor.left;
                current.value = successor.value;
                current.right = removeRecursive(current.right, successor.value);
            }

            return rebalance(current);
        }

        private TreeNode<ContactoAVL> rebalance(TreeNode<ContactoAVL> current) {
            int balance = getNodeBalanceFactor(current);
            if (balance > 1) {
                if (getNodeBalanceFactor(current.left) < 0) {
                    current.left = rotateLeft(current.left);
                }
                return rotateRight(current);
            }
            if (balance < -1) {
                if (getNodeBalanceFactor(current.right) > 0) {
                    current.right = rotateRight(current.right);
                }
                return rotateLeft(current);
            }
            return current;
        }

        private TreeNode<ContactoAVL> rotateRight(TreeNode<ContactoAVL> root) {
            TreeNode<ContactoAVL> pivot = root.left;
            root.left = pivot.right;
            pivot.right = root;
            return pivot;
        }

        private TreeNode<ContactoAVL> rotateLeft(TreeNode<ContactoAVL> root) {
            TreeNode<ContactoAVL> pivot = root.right;
            root.right = pivot.left;
            pivot.left = root;
            return pivot;
        }
    }
}

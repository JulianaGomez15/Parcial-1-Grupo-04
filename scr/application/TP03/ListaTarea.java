package TP03;

public class ListaTarea {

    private Tarea[] listaTareas;

    private int size = 0;
    public static final int DEFAULT_CAPACITY = 3; // casilleros por defecto

    public ListaTarea() {
        listaTareas = new Tarea[DEFAULT_CAPACITY];
        // creacion de nuevo array con size por defecto.
    }

    public boolean addTarea(Tarea tarea) { // insertar a lo ultimo
        if (size >= listaTareas.length) {
            resize();
        }
        listaTareas[size] = tarea;
        size++;
        return true;
    }

    public void mostrarLista() {
        for (int i = 0; i < size; i++) { // uso size para no imprimir valores null
            System.out.println(listaTareas[i].toString());
        }
    }

    private void resize() {
        // creacion de nuevo array del doble de largo
        Tarea[] nextArray = new Tarea[listaTareas.length * 2];

        // copiamos lo q esta en el array al nuevo con un bucle
        for (int i = 0; i < listaTareas.length; i++) {
            nextArray[i] = listaTareas[i];
        }
        listaTareas = nextArray;
    }

    public boolean eliminarPorTitulo(String titulo) {
        for (int i = 0; i < size; i++) {
            if (listaTareas[i].getTitulo().equals(titulo)) {
                shiftLeft(i);
                size--;
                return true;
            }
        }
        return false;
    }

    public boolean buscarTareaParaCompletar(String tit) {
        for (int i = 0; i < size; i++) {
            if (listaTareas[i].getTitulo().equals(tit)) {
                listaTareas[i].setCompletada();
                return true;
            }
        }
        return false;

    }

    public boolean eliminarPorIndex(int indiceParaEliminar) {

        if (indiceParaEliminar >= size || indiceParaEliminar < 0) {
            return false;
        }
        listaTareas[indiceParaEliminar] = null;
        shiftLeft(indiceParaEliminar);
        size--;
        return true;
    }

    private void shiftLeft(int d) {
        for (int i = d; i < size - 1; i++) {
            listaTareas[i] = listaTareas[i + 1];
        }
        listaTareas[size - 1] = null;
    }

}

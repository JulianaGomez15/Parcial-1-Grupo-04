package application.TP03;

import java.time.LocalDateTime;

public class ListaTarea implements InterfaceListaTarea {

    private Tarea[] listaTareas;

    private int size = 0;
    public static final int DEFAULT_CAPACITY = 3; // casilleros por defecto

    public ListaTarea() {
        listaTareas = new Tarea[DEFAULT_CAPACITY];
        // creacion de nuevo array con size por defecto.
    }

    @Override
    public boolean addTarea(Tarea tarea) { // insertar a lo ultimo
        if (tarea == null) {
            throw new IllegalArgumentException("La tarea no puede ser null.");
        }
        if (size >= listaTareas.length) {
            resize();
        }
        listaTareas[size] = tarea;
        size++;
        return true;
    }

    @Override
    public void mostrarLista() {
        mostrarLista(null);
    }

    @Override
    public void mostrarLista(LocalDateTime ahora) {
        for (int i = 0; i < size; i++) { // uso size para no imprimir valores null
            String linea = (i + 1) + ". " + listaTareas[i];
            if (ahora != null && listaTareas[i].estaAtrasada(ahora)) {
                linea += " [ATRASADA]";
            }
            System.out.println(linea);
        }
    }

    private void resize() {
        Tarea[] nextArray = new Tarea[listaTareas.length * 2];
        for (int i = 0; i < listaTareas.length; i++) {
            nextArray[i] = listaTareas[i];
        }
        listaTareas = nextArray;
    }

    @Override
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

    @Override
    public boolean buscarTareaParaCompletar(String tit, LocalDateTime ahora) {
        for (int i = 0; i < size; i++) {
            if (listaTareas[i].getTitulo().equals(tit)) {
                listaTareas[i].setCompletada(ahora);
                return true;
            }
        }
        return false;

    }

    @Override
    public boolean eliminarPorIndex(int indiceParaEliminar) {

        if (indiceParaEliminar >= size || indiceParaEliminar < 0) {
            throw new IndexOutOfBoundsException();
        }
        shiftLeft(indiceParaEliminar);
        size--;
        return true;
    }

    private void shiftLeft(int index) {
        for (int i = index; i < size - 1; i++) {
            listaTareas[i] = listaTareas[i + 1];
        }
        listaTareas[size - 1] = null;
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }
}

package TP03;

import java.time.LocalDateTime;

public class ListaTarea {

    private Tarea[] listaTareas;

    private int size = 0;
    public static final int DEFAULT_CAPACITY = 3; // casilleros por defecto

    public ListaTarea() {
        listaTareas = new Tarea[DEFAULT_CAPACITY];
        // creacion de nuevo array con size por defecto.
    }

    public boolean addTarea(Tarea tarea) { // insertar a lo ultimo
        if (tarea == null) {
            throw new IllegalArgumentException("La tarea no puede ser nula.");
        }
        if (tarea.getTitulo() == null || tarea.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El titulo de la tarea no puede estar vacio.");
        }
        if (existeTitulo(tarea.getTitulo())) {
            throw new IllegalArgumentException("Ya existe una tarea con ese titulo.");
        }
        if (size >= listaTareas.length) {
            resize();
        }
        listaTareas[size] = tarea;
        size++;
        return true;
    }

    public void mostrarLista() {
        if (size == 0) {
            System.out.println("No hay tareas.");
            return;
        }
        for (int i = 0; i < size; i++) { // uso size para no imprimir valores null
            System.out.println((i + 1) + ". " + listaTareas[i].toString());
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
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El titulo no puede ser nulo ni vacio.");
        }
        for (int i = 0; i < size; i++) {
            if (listaTareas[i].getTitulo().equals(titulo)) {
                shiftLeft(i);
                size--;
                return true;
            }
        }
        return false;
    }

    public boolean buscarTareaParaCompletar(String tit, LocalDateTime fechaActual) {
        if (tit == null || tit.trim().isEmpty()) {
            throw new IllegalArgumentException("El titulo no puede ser nulo ni vacio.");
        }
        if (fechaActual == null) {
            throw new IllegalArgumentException("La fecha actual no puede ser nula.");
        }
        Tarea tarea = buscarPorTitulo(tit);
        if (tarea == null) {
            return false;
        }
        if (tarea.estaRealizada()) {
            tarea.marcarComoPendiente();
        } else {
            tarea.marcarComoRealizada(fechaActual);
        }
        return true;
    }

    public boolean eliminarPorIndex(int indiceParaEliminar) {
        if (indiceParaEliminar >= size || indiceParaEliminar < 0) {
            throw new IllegalArgumentException("Indice invalido.");
        }
        listaTareas[indiceParaEliminar] = null;
        shiftLeft(indiceParaEliminar);
        size--;
        return true;
    }

    public boolean existeTitulo(String titulo) {
        return buscarPorTitulo(titulo) != null;
    }

    public Tarea buscarPorTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return null;
        }
        for (int i = 0; i < size; i++) {
            if (listaTareas[i].getTitulo().equals(titulo)) {
                return listaTareas[i];
            }
        }
        return null;
    }

    public int getSize() {
        return size;
    }

    public Tarea getTarea(int index) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("Indice invalido.");
        }
        return listaTareas[index];
    }

    private void shiftLeft(int d) {
        for (int i = d; i < size - 1; i++) {
            listaTareas[i] = listaTareas[i + 1];
        }
        listaTareas[size - 1] = null;
    }

}

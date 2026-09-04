package application.TP03;

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
        if (size >= listaTareas.length) {
            resize();
        }
        listaTareas[size] = tarea;
        size++;
        return true;
    }

    @Override
    public void mostrarLista() {
        for (int i = 0; i < size; i++) { // uso size para no imprimir valores null
            System.out.println((i + 1) + ". " + listaTareas[i]);
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
    public boolean buscarTareaParaCompletar(String tit) {
        for (int i = 0; i < size; i++) {
            if (listaTareas[i].getTitulo().equals(tit)) {
                listaTareas[i].setCompletada();
                return true;
            }
        }
        return false;

    }

    @Override
    public boolean eliminarPorIndex(int indiceParaEliminar) {

        if (indiceParaEliminar >= size || indiceParaEliminar < 0) {
            return false;
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

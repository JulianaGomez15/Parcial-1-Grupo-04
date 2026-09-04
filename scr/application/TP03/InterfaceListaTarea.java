package application.TP03;

public interface InterfaceListaTarea {
    boolean addTarea(Tarea tarea);

    void mostrarLista();

    boolean eliminarPorTitulo(String titulo);

    boolean buscarTareaParaCompletar(String tit);

    boolean eliminarPorIndex(int indiceParaEliminar);

    int size();

    boolean isEmpty();
}

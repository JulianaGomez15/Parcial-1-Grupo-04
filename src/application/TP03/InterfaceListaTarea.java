package application.TP03;

import java.time.LocalDateTime;

public interface InterfaceListaTarea {
    boolean addTarea(Tarea tarea);

    void mostrarLista();

    void mostrarLista(LocalDateTime ahora);

    boolean eliminarPorTitulo(String titulo);

    boolean buscarTareaParaCompletar(String tit, LocalDateTime ahora);

    boolean eliminarPorIndex(int indiceParaEliminar);

    int size();

    boolean isEmpty();
}

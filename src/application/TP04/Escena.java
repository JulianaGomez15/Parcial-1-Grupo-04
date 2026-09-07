package application.TP04;

import application.listModule.SimpleArrayList;
import application.listModule.SimpleList;

public class Escena {

    private int id;
    private String descripcion;
    private SimpleList<String> opciones;
    private SimpleList<Integer> destinos; // -1 si la opcion no navega (ej: armar fogata)

    public Escena(int id, String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripcion de la escena no puede estar vacia.");
        }
        this.id = id;
        this.descripcion = descripcion;
        this.opciones = new SimpleArrayList<>();
        this.destinos = new SimpleArrayList<>();
    }

    // Opcion que lleva a otra escena
    public void agregarOpcion(String texto, int escenaDestino) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El texto de la opcion no puede estar vacio.");
        }
        opciones.add(texto);
        destinos.add(escenaDestino);
    }

    // Opcion que no navega, solo hace algo en la misma escena (ej: armar fogata)
    public void agregarOpcion(String texto) {
        agregarOpcion(texto, -1);
    }

    public int getId() { return id; }

    public String getDescripcion() { return descripcion; }

    public int cantidadOpciones() { return opciones.size(); }

    public String getTextoOpcion(int index) {
        // Si el indice es invalido, SimpleArrayList lanza IndexOutOfBoundsException
        return opciones.get(index);
    }

    public int getDestino(int index) {
        return destinos.get(index);
    }
}

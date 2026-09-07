package application.TP04;

import java.util.NoSuchElementException;

import application.listModule.SimpleArrayList;
import application.listModule.SimpleList;

public class HistoriaAventura {

    private SimpleList<Escena> escenas;

    public HistoriaAventura() {
        escenas = new SimpleArrayList<>();
    }

    public void agregarEscena(Escena escena) {
        if (escena == null) {
            throw new IllegalArgumentException("La escena no puede ser null.");
        }
        escenas.add(escena);
    }

    public Escena obtenerEscena(int id) {
        for (int i = 0; i < escenas.size(); i++) {
            if (escenas.get(i).getId() == id) {
                return escenas.get(i);
            }
        }
        // Si no existe una escena con ese id, no hay nada valido para devolver: explota
        throw new NoSuchElementException("No existe una escena con id " + id + ".");
    }
}

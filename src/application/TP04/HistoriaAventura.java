package application.TP04;

import application.listModule.SimpleArrayList;
import application.listModule.SimpleList;

public class HistoriaAventura {

    private SimpleList<Escena> escenas;

    public HistoriaAventura() {
        escenas = new SimpleArrayList<>();
    }

    public void agregarEscena(Escena escena) {
        escenas.add(escena);
    }

    public Escena obtenerEscena(int id) {
        for (int i = 0; i < escenas.size(); i++) {
            if (escenas.get(i).getId() == id) {
                return escenas.get(i);
            }
        }
        return null;
    }
}

package application.TP05;

import application.listModule.SimpleArrayList;
import application.listModule.SimpleList;
import application.queueModule.SimpleArrayQueue;
import application.queueModule.SimpleQueue;

public class ColaImpresion {
    private final SimpleQueue<Documento> colaBlancoYNegro = new SimpleArrayQueue<>();
    private final SimpleQueue<Documento> colaColor = new SimpleArrayQueue<>();

    public void enviar(Documento documento) {
        if (documento.esColor()) {
            colaColor.enqueue(documento);
        } else {
            colaBlancoYNegro.enqueue(documento);
        }
    }

    public void cancelar() {
        colaBlancoYNegro.clear();
        colaColor.clear();
    }

    public boolean isEmpty() {
        return colaBlancoYNegro.isEmpty() && colaColor.isEmpty();
    }

    // Saca el próximo documento: primero B&N, después Color.
    // Si no hay nada, dequeue del TDA lanza NoSuchElementException.
    public Documento sacarSiguiente() {
        if (!colaBlancoYNegro.isEmpty()) {
            return colaBlancoYNegro.dequeue();
        }
        return colaColor.dequeue();
    }

    // Muestra la cola en el orden real de impresión, sin vaciarla
    public SimpleList<Documento> snapshot() {
        SimpleList<Documento> documentos = new SimpleArrayList<>();
        copiarSinPerder(colaBlancoYNegro, documentos);
        copiarSinPerder(colaColor, documentos);
        return documentos;
    }

    private void copiarSinPerder(SimpleQueue<Documento> cola, SimpleList<Documento> destino) {
        SimpleQueue<Documento> auxiliar = new SimpleArrayQueue<>();

        while (!cola.isEmpty()) {
            Documento documento = cola.dequeue();
            destino.add(documento);
            auxiliar.enqueue(documento);
        }

        while (!auxiliar.isEmpty()) {
            cola.enqueue(auxiliar.dequeue());
        }
    }
}

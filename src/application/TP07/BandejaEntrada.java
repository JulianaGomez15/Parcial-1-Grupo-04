package application.TP07;

import application.listModule.SimpleArrayList;
import application.listModule.SimpleList;
import application.priorityQueueModule.SimpleArrayPriorityQueue;
import application.priorityQueueModule.SimplePriorityQueue;

// Servicio que organiza los mails en 3 pestañas.
// Cada pestaña es una PriorityQueue propia: asi los mails de
// prioridad Alta salen antes que los de Baja, y entre mails
// de la misma prioridad se respeta el orden de llegada.
public class BandejaEntrada {
    private final SimplePriorityQueue<Mail> principal = new SimpleArrayPriorityQueue<>();
    private final SimplePriorityQueue<Mail> notificaciones = new SimpleArrayPriorityQueue<>();
    private final SimplePriorityQueue<Mail> spam = new SimpleArrayPriorityQueue<>();

    // Recibe un mail y lo guarda en la pestaña que indica el propio mail
    public void recibir(Mail mail) {
        if (mail == null) {
            throw new IllegalArgumentException("El mail no puede ser nulo.");
        }
        colaDe(mail.getPestania()).enqueue(mail, mail.getPrioridad().getValor());
    }

    // Abre (y saca de la bandeja) el mail mas prioritario de la pestaña.
    // Si la pestaña esta vacia, dequeue del TDA lanza NoSuchElementException.
    public Mail abrirSiguiente(Pestania pestania) {
        return colaDe(pestania).dequeue();
    }

    public boolean isEmpty(Pestania pestania) {
        return colaDe(pestania).isEmpty();
    }

    public int cantidad(Pestania pestania) {
        return colaDe(pestania).size();
    }

    // Devuelve los mails de la pestaña en el orden en que se van a leer,
    // sin perder el contenido de la cola
    public SimpleList<Mail> snapshot(Pestania pestania) {
        SimplePriorityQueue<Mail> cola = colaDe(pestania);
        SimpleList<Mail> mails = new SimpleArrayList<>();

        // Vaciamos la cola guardando cada mail en orden
        while (!cola.isEmpty()) {
            mails.add(cola.dequeue());
        }

        // Los volvemos a encolar en el mismo orden.
        // Como la cola respeta el orden de llegada entre iguales,
        // queda exactamente como estaba.
        for (int i = 0; i < mails.size(); i++) {
            Mail mail = mails.get(i);
            cola.enqueue(mail, mail.getPrioridad().getValor());
        }

        return mails;
    }

    private SimplePriorityQueue<Mail> colaDe(Pestania pestania) {
        if (pestania == null) {
            throw new IllegalArgumentException("La pestania no puede ser nula.");
        }

        switch (pestania) {
            case PRINCIPAL:
                return principal;
            case NOTIFICACIONES:
                return notificaciones;
            case SPAM:
                return spam;
            default:
                throw new IllegalArgumentException("Pestania desconocida: " + pestania);
        }
    }
}

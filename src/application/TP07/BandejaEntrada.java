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

    // Recibe un mail y lo guarda en la pestaña que indica el propio mail.
    // Devuelve false si el mail no se puede guardar.
    public boolean recibir(Mail mail) {
        if (mail == null || mail.getPestania() == null || mail.getPrioridad() == null) return false;
        SimplePriorityQueue<Mail> cola = colaDe(mail.getPestania());
        if (cola == null) return false;
        cola.enqueue(mail, mail.getPrioridad().getValor());
        return true;
    }

    // Abre (y saca de la bandeja) el mail mas prioritario de la pestaña.
    // Si la pestaña esta vacia o no existe, no llama a dequeue y devuelve null.
    public Mail abrirSiguiente(Pestania pestania) {
        if (isEmpty(pestania)) return null;
        return colaDe(pestania).dequeue();
    }

    public boolean isEmpty(Pestania pestania) {
        SimplePriorityQueue<Mail> cola = colaDe(pestania);
        return cola == null || cola.isEmpty();
    }

    public int cantidad(Pestania pestania) {
        SimplePriorityQueue<Mail> cola = colaDe(pestania);
        if (cola == null) return 0;
        return cola.size();
    }

    // Devuelve los mails de la pestaña en el orden en que se van a leer,
    // sin perder el contenido de la cola
    public SimpleList<Mail> snapshot(Pestania pestania) {
        SimplePriorityQueue<Mail> cola = colaDe(pestania);
        SimpleList<Mail> mails = new SimpleArrayList<>();
        if (cola == null) return mails;

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
        if (pestania == null) return null;

        switch (pestania) {
            case PRINCIPAL:
                return principal;
            case NOTIFICACIONES:
                return notificaciones;
            case SPAM:
                return spam;
            default:
                return null;
        }
    }
}

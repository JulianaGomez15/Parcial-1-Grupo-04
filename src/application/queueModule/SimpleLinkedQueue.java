package application.queueModule;

import application.linkedModule.LinkedNode;

import java.util.NoSuchElementException;

public class SimpleLinkedQueue<E> implements SimpleQueue<E> {
    private LinkedNode<E> first = null;
    private LinkedNode<E> last = null;
    private int size = 0;


    @Override
    public void enqueue(E element) {
        // Creamos un nuevo nodo con el elemento a agregar
        LinkedNode<E> addedNode = new LinkedNode<>(element);

        // Si la cola esta vacia, el nuevo nodo es el primero
        if (isEmpty()) first = addedNode;

        // Si no esta vacia, conectamos el nuevo nodo al ultimo
        else last.next = addedNode;

        addedNode.prev = last;
        last = addedNode;
        size++;
    }

    @Override
    public E dequeue() {
        // Validamos que la cola no este vacia, si no lo esta, guardamos el primer elemento y
        // lo eliminamos de la cola
        if (isEmpty()) throw new NoSuchElementException("Queue is empty");
        E result = first.value;
        first = first.next;

        // Si el primer nodo no es nulo, actualizamos su prev a null,
        if (first != null) first.prev = null;

        // sino la cola queda vacia y last es null
        else last = null;

        size--;
        return result;
    }

    @Override
    public E peek() {
        if (isEmpty()) throw new NoSuchElementException("Queue is empty");
        return first.value;
    }

    @Override
    public void clear() {
        first = null;
        last = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}

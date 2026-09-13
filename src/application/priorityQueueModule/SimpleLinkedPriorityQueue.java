package application.priorityQueueModule;

import java.util.NoSuchElementException;

public class SimpleLinkedPriorityQueue <E> implements SimplePriorityQueue<E>{
    private LinkedPriorityNode<E> first = null;
    private LinkedPriorityNode<E> last = null;
    private int size = 0;

    @Override
    public void enqueue(E element, int priority) {
        LinkedPriorityNode<E> addedNode = new LinkedPriorityNode<>(element, priority);

        if (isEmpty()) {
            first = addedNode;
            last = addedNode;
        }
        else if (priority <= first.priority) {
            first.prev = addedNode;
            addedNode.next = first;
            first = addedNode;
        }
        else {
            LinkedPriorityNode<E> current = last;

            while (current.prev != null && priority < current.priority)
                current = current.prev;

            addedNode.prev = current;
            addedNode.next = current.next;

            if (current.next != null)
                current.next.prev = addedNode;

            current.next = addedNode;
        }

        size++;
    }

    @Override
    public E dequeue() {
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
    public int getHighestPriority() {
        if (isEmpty()) throw new NoSuchElementException("Queue is empty");
        return first.priority;
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

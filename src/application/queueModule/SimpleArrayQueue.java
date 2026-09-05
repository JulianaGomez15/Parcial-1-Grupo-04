package application.queueModule;


import java.util.NoSuchElementException;

public class SimpleArrayQueue<E> implements SimpleQueue<E> {
    private E[] elements;
    private int size = 0;
    private static final int DEFAULT_SIZE = 4;

    @SuppressWarnings("unchecked")
    public SimpleArrayQueue() {
        elements = (E[]) new Object[DEFAULT_SIZE];
    }

    @Override
    public void enqueue(E element) {
        // Validamos que el array tenga espacio para un elemento mas y
        // si no lo tiene, lo redimensionamos
        validateSize(size + 1);
        elements[size] = element;
        size++;
    }

    @Override
    public E dequeue() {
        // Validamos que la cola no este vacia y si no lo esta,
        // guardamos el primer elemento y corremos todos los elementos a la izquierda
        if (isEmpty()) throw new NoSuchElementException("Queue is empty");
        E result = elements[0];
        shiftLeft(0);
        size--;
        return result;
    }

    @Override
    public E peek() {
        // Validamos que la cola no este vacia y si no lo esta, devolvemos el primer elemento
        if (isEmpty()) throw new NoSuchElementException("Queue is empty");
        return elements[0];
    }

    @SuppressWarnings("unchecked")
    @Override
    public void clear() {
        elements = (E[]) new Object[DEFAULT_SIZE];
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

    private void validateSize(int newSize) {
        if(newSize >= elements.length)
            resize();
    }

    private void shiftLeft(int index) {
        // Corremos cada elemento a la izquierda
        for(int i = index; i < size - 1; i++)
            elements[i] = elements[i+1];

        // Borramos el ultimo para que no quede duplicado
        elements[size - 1] = null;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        // Creamos un nuevo array del doble de largo que el actual
        E[] nextArray = (E[]) new Object[elements.length * 2];

        // Copiamos todo lo que esta en elements al nuevo
        for(int i = 0; i < elements.length; i++)
            nextArray[i] = elements[i];

        elements = nextArray;
    }
}

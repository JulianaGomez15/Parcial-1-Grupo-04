package application.stackModule;


import java.util.NoSuchElementException;

public class SimpleArrayStack<E> implements SimpleStack<E> {

    private E[] elements;
    private int size = 0;
    private static final int DEFAULT_SIZE = 4;

    @SuppressWarnings("unchecked")
    public SimpleArrayStack() {
        elements = (E[]) new Object[DEFAULT_SIZE];
    }

    @Override
    public void push(E element) {
        // Validamos que el stack tenga espacio para un nuevo elemento, si no lo tiene,
        // lo redimensionamos
        validateSize(size + 1);
        elements[size] = element;
        size++;
    }

    @Override
    public E pop() {
        // Si el stack esta vacio, lanzamos una excepcion, sino retornamos y
        // eliminamos el ultimo elemento agregado
        if(isEmpty()) throw new NoSuchElementException("Stack is empty");
        E result = elements[size-1];
        elements[size-1] = null;
        size--;
        return result;
    }

    @Override
    public E peek() {
        // Si el stack esta vacio, lanzamos una excepcion, sino retornamos el ultimo elemento agregado
        if(isEmpty()) throw new NoSuchElementException("Stack is empty");
        return elements[size-1];
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

    private void validateSize(int newSize)
    {
        if(newSize >= elements.length)
            resize();
    }

    @SuppressWarnings("unchecked")
    private void resize()
    {
        E[] nextArray = (E[]) new Object[elements.length * 2];
        for(int i = 0; i < elements.length; i++)
            nextArray[i] = elements[i];

        elements = nextArray;
    }
}

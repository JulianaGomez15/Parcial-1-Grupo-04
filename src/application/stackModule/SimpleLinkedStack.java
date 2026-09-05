package application.stackModule;

import application.linkedModule.LinkedNode;

import java.util.NoSuchElementException;

public class SimpleLinkedStack<E> implements SimpleStack<E> {
    private LinkedNode<E> last = null;
    private int size = 0;


    @Override
    public void push(E element) {
        // Creamos un nuevo nodo con el elemento a agregar, y lo enlazamos al nodo anterior (last)
        LinkedNode<E> addedNode = new LinkedNode<>(element);
        addedNode.prev = last;
        last = addedNode;
        size++;
    }

    @Override
    public E pop() {
        // Si el stack esta vacio, lanzamos una excepcion, sino retornamos
        // y eliminamos el ultimo elemento agregado
        if (isEmpty()) throw new NoSuchElementException("Stack is empty");
        E result = last.value;
        last = last.prev;
        size--;
        return result;
    }

    @Override
    public E peek() {
        // Si el stack esta vacio, lanzamos una excepcion, sino retornamos el ultimo elemento agregado
        if (isEmpty()) throw new NoSuchElementException("Stack is empty");
        return last.value; // Retornamos el valor del ultimo nodo agregado
    }

    @Override
    public void clear() {
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

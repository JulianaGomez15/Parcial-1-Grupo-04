package application.setModule;

import java.util.Arrays;

public class SimpleArraySet <E> implements SimpleSet<E> {
    public static final int DEFAULT_CAPACITY = 4;
    private E[] elements;
    private int size = 0;

    public SimpleArraySet(SimpleSet<E> original) {
        elements = original.toArray((E[]) new Object[0]);
        size = original.size();
    }

    public SimpleArraySet() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
    }

    @Override
    public boolean add(E element) {
        if (contains(element)) return false;
        validateSize(size + 1);
        elements[size] = element;
        size++;
        return true;
    }

    @Override
    public boolean remove(E element) {
        if (element == null) throw new IllegalArgumentException("Element cannot be null");
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(element)){
                if (i< size - 1)  elements[i] = elements[size - 1];
                elements[size - 1] = null;
                size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(E element) {
        if (element == null) throw new IllegalArgumentException("Element cannot be null");
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(element)) return true;
        }
        return false;
    }

    @Override
    public void clear() {
        elements = (E[]) new Object[size];
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

    @Override
    public E[] toArray(E[] typeArray) {
        E[] result = Arrays.copyOf(typeArray, size);
        for (int i = 0; i < size; i++) {
            result[i] = elements[i];
        }
        return result;
    }

    @Override
    public SimpleSet<E> unionWith(SimpleSet<E> other) {
        if (other == null) throw new IllegalArgumentException("Other Set cannot be null");
        SimpleSet<E> result = new SimpleArraySet<E>(other);
        for (int i = 0; i < size; i++)
            result.add(elements[i]);
        return result;
    }

    @Override
    public SimpleSet<E> intersectWith(SimpleSet<E> other) {
        if (other == null) throw new IllegalArgumentException("Other Set cannot be null");
        SimpleSet<E> result = new SimpleArraySet<E>();
        for (int i = 0; i < size; i++)
            if (other.contains(elements[i])) result.add(elements[i]);
        return result;
    }

    @Override
    public SimpleSet<E> differenceWith(SimpleSet<E> other) {
        if (other == null) throw new IllegalArgumentException("Other Set cannot be null");
        SimpleSet<E> result = new SimpleArraySet<E>();
        for (int i = 0; i < size; i++)
            if (!other.contains(elements[i])) result.add(elements[i]);
        return result;
    }

    private void validateSize(int newSize)
    {
        if(newSize >= elements.length)
            resize();
    }

    private void resize()
    {
        // Creamos un nuevo array del doble de largo que el actual
        E[] nextArray = (E[]) new Object[elements.length * 2];

        // Copiamos todo lo que esta en array al nuevo
        for(int i = 0; i < elements.length; i++)
            nextArray[i] = elements[i];

        elements = nextArray;
    }

}

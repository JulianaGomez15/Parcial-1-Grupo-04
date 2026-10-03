package application.dictionaryModule;

import application.listModule.SimpleArrayList;
import application.listModule.SimpleList;

public class SimpleArrayDictionary<K,V> implements SimpleDictionary<K,V> {

    private int size = 0;
    private K[] keys;
    private V[] values;
    private static final int DEFAULT_SIZE = 4;

    @SuppressWarnings("unchecked")
    public SimpleArrayDictionary() {
        keys = (K[]) new Object[DEFAULT_SIZE];
        values = (V[]) new Object[DEFAULT_SIZE];
    }

    @Override
    public V put(K key, V value) {
        if (key == null) throw new NullPointerException("It cannot be null");
        int index = indexOf(key);
        if (index == -1) {
            validateSize(size + 1);
            keys[size] = key;
            values[size] = value;
            size++;
            return null;
        }

        V oldValue = values[index];
        values[index] = value;
        return oldValue;
    }

    @SuppressWarnings("unchecked")
    private void reSize() {

        K[] nextKeys = (K[]) new Object[keys.length * 2];
        V[] nextValues = (V[]) new Object[values.length * 2];

        for (int i = 0; i < keys.length; i++){
            nextKeys[i] = keys[i];
            nextValues[i] = values[i];
        }

        keys = nextKeys;
        values = nextValues;
    }

    private void validateSize(int newSize) {
        if  (newSize < 0) throw new IllegalArgumentException("New size cannot be negative");
    }

    @Override
    public boolean remove(K key) {
        if (key == null) throw new NullPointerException("Key cannot be null");
        int index = indexOf(key);
        if (index == -1) return false;

        // Movemos el último y lo nulleamos (no shiftLeft)
        keys[index] = keys[size - 1];
        values[index] = values[size - 1];

        keys[size - 1] = null;
        values[size - 1] = null;
        size--;
        return true;
    }

    @Override
    public boolean containsKey(K key) {
        if (key == null) throw new NullPointerException("Key cannot be null");
        return indexOf(key) != -1;
    }

    @Override
    public V get(K key) {
        if (key == null) throw new NullPointerException("Key cannot be null");
        int index = indexOf(key);

        if (index == -1) return null;
        return values[index];
    }

    @Override
    public SimpleList<K> keys() {
        SimpleList<K> result = new SimpleArrayList<K>(size);
        for (int i = 0; i < size; i++) result.add(keys[i]);
        return result;
    }

    @Override
    public SimpleList<V> values() {
        SimpleList<V> result = new SimpleArrayList<V>(size);
        for (int i = 0; i < size; i++) result.add(values[i]);
        return result;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        keys = (K[]) new Object[size];
        values = (V[]) new Object[size];
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private int indexOf(K key) {
        for (int i = 0; i < size; i++)
            if (keys[i].equals(key))
                return i;
        return -1;
    }
}

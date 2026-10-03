package application.dictionaryModule;

import application.listModule.SimpleArrayList;
import application.listModule.SimpleList;

public class SimpleLinkedDictionary<K,V> implements SimpleDictionary<K,V> {

    private LinkedDictionaryNode<K,V> first = null;
    private int size = 0;


    @Override
    public V put(K key, V value) {
        if (key == null) throw new NullPointerException("Key cannot be null");
        // Buscamos un nodo existente por key
        LinkedDictionaryNode<K,V> targetNode = getNodeByKey(key);

        // Si no existe, lo creamos
        if (targetNode == null){
            targetNode = new LinkedDictionaryNode<K,V>(key, value);
            targetNode.next = first;
            first = targetNode;
            size++;
            return null;
        }
        // Si existe, guardamos el value anterior y lo actualizamos
        V oldValue = targetNode.value;
        targetNode.value = value;
        return oldValue;
    }

    @Override
    public boolean remove(K key) {
        if (key == null) throw new NullPointerException("Key cannot be null");

        // Caso especial: El primero tiene la key
        if  (first.key.equals(key)){
            first = first.next;
            size--;
            return true;
        }
        LinkedDictionaryNode<K,V> current = first;

        while (current.next != null){
            if (current.next.key.equals(key)){
                current.next = current.next.next;
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean containsKey(K key) {
        if (key == null) throw new NullPointerException("Key cannot be null");
        return getNodeByKey(key) != null;
    }

    @Override
    public V get(K key) {
        if (key == null) throw new NullPointerException("Key cannot be null");
        LinkedDictionaryNode<K,V> node = getNodeByKey(key);
        if (node == null) return null;
        return node.value;
    }

    @Override
    public SimpleList<K> keys() {
        SimpleList<K> result = new SimpleArrayList<K>(size);
        LinkedDictionaryNode<K,V> current = first;
        while (current != null){
            result.add(current.key);
            current = current.next;
        }
        return result;
    }

    @Override
    public SimpleList<V> values() {
        SimpleList<V> result = new SimpleArrayList<V>(size);
        LinkedDictionaryNode<K,V> current = first;
        while (current != null){
            result.add(current.value);
            current = current.next;
        }
        return result;
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        first = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private LinkedDictionaryNode<K,V> getNodeByKey(K key) {
        LinkedDictionaryNode<K,V> current = first;

        while (current != null) {
            if (current.key.equals(key)) return current;
            current = current.next;
        }
        return null;
    }
}

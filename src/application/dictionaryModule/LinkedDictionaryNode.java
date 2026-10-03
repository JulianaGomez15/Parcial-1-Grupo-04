package application.dictionaryModule;

public class LinkedDictionaryNode<K,V> {
    public K key;
    public V value;
    public LinkedDictionaryNode<K,V> next = null;

    public LinkedDictionaryNode(K key, V value) {
        this.value = value;
        this.key = key;
    }
}

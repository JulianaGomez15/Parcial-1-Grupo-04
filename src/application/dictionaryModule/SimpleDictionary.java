package application.dictionaryModule;

import application.listModule.SimpleList;

public interface SimpleDictionary <K, V>{
    public V put(K key, V value);
    public boolean remove(K key);
    public boolean containsKey(K key);
    public V get(K key);
    public SimpleList<K> keys();
    public SimpleList<V> values();
    public int size();
    public void clear();
    public boolean isEmpty();


}

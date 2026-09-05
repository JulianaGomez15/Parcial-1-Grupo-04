package application.queueModule;

public interface SimpleQueue<E>{
    // <E> es un parametro de tipo generico, que permite que la interfaz
    // SimpleQueue pueda trabajar con cualquier tipo de objeto.
    public void enqueue(E element);
    public E dequeue();
    public E peek();
    public void clear();
    public int size();
    public boolean isEmpty();
}

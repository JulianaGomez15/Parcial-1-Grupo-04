package application.stackModule;

public interface SimpleStack<E> {
    public void push(E element);
    public E pop();
    public E peek();
    public void clear();
    public int size();
    public boolean isEmpty();
}

package application.listModule;

public interface SimpleList<E> {
	public boolean add(E element);
	public void add(int index, E element);
	public boolean remove(Object o);
	public E remove(int index);
	public E get(int index);
	public E set(int index, E element);
	public void clear();
	public int size();
	public boolean isEmpty();
	public boolean contains(Object o);
	public int indexOf(Object o);
}

package application.listModule;

public class SimpleArrayList<E> implements SimpleList<E> {
	// Cantidad de casilleros por defecto
	public static final int DEFAULT_CAPACITY = 4;
	
	// Array interno de la lista
	private E[] array;
	
	// Cuantos casilleros del array estan ocupados
	private int size = 0;
	
	// Este constructor se llama cuando no se especifica tamaño
	@SuppressWarnings("unchecked")
	public SimpleArrayList() {
		// Creamos un nuevo array con tamaño por defecto
		array = (E[]) new Object[DEFAULT_CAPACITY];
	}
	
	// Este se llama cuando pasamos un int por parametro en "new"
	@SuppressWarnings("unchecked")
	public SimpleArrayList(int initialCapacity)
	{
		// Creamos un nuevo array con el tamaño indicado
		array = (E[]) new Object[initialCapacity];
	}

	// Add agrega el elemento indicado al final de la lista
	@Override
	public boolean add(E element) {
		validateSize(size + 1);
		array[size] = element;
		size++;
		return true;
	}

	@Override
	public void add(int index, E element) {
		// Si justo insertamos al final, es lo mismo que add
		if(index == size)
		{
			add(element);
			return;
		}
		
		// Salvo size, chequeamos que el indice sea posible
		// Si no es valido, explota :)
		validateIndex(index);
		validateSize(size + 1);
		
		// Corremos todo lo que esta en el indice a insertar
		// hacia la derecha, para hacer lugar sin pisar nada
		shiftRight(index);
		array[index] = element;
		size++;
	}

	@Override
	public boolean remove(Object o) {
		for(int i = 0; i < size; i++)
		{
			// Si el elemento actual es el buscado
			if(array[i].equals(o))
			{
				shiftLeft(i);
				size--;
				return true;
			}
		}
		return false;
	}

	@Override
	public E remove(int index) {
		// Si se quiere remover un indice que no tiene nada, explota
		validateIndex(index);
		
		// Guardamos lo que habia en ese indice antes de borrarlo
		E element = array[index];
		
		// Movemos todo lo que le sigue al elemento 
		// hacia la izquierda (lo pisa con el siguiente)
		shiftLeft(index);
		
		size--;
		return element;
	}

	@Override
	public E get(int index) {
		validateIndex(index);
		return array[index];
	}

	@Override
	public E set(int index, E element) {
		validateIndex(index);
		E oldElement = array[index];
		array[index] = element;
		return oldElement;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void clear() {
		// Reemplazamos el array por otro vacio
		array = (E[]) new Object[array.length];
		
		// Seteamos size a 0
		size = 0;
	}

	@Override
	public int size() { return size; }

	@Override
	public boolean isEmpty() { return size == 0; }

	@Override
	public boolean contains(Object o) {
		for(int i = 0; i < size; i++)
		{
			// Si el elemento actual es el buscado
			if(array[i].equals(o))
				return true;
		}
		return false;
	}

	@Override
	public int indexOf(Object o) {
		for(int i = 0; i < size; i++)
		{
			// Si el elemento actual es el buscado
			if(array[i].equals(o))
				return i;
		}
		return -1;
	}

	private void validateSize(int newSize)
	{
		if(newSize >= array.length)
			resize();
	}
	
	private void validateIndex(int index)
	{
		if(index < 0 || index >= size)
			throw new IndexOutOfBoundsException();
	}
	
	private void shiftLeft(int index)
	{
		// Corremos cada elemento a la izquierda
		for(int i = index; i < size - 1; i++)
			array[i] = array[i+1];
		
		// Borramos el ultimo para que no quede duplicado
		array[size - 1] = null;
	}
	
	private void shiftRight(int index)
	{
		for(int i = size; i > index; i--)
			array[i] = array[i-1];
	}
	
	@SuppressWarnings("unchecked")
	private void resize()
	{
		// Creamos un nuevo array del doble de largo que el actual
		E[] nextArray = (E[]) new Object[array.length * 2];
		
		// Copiamos todo lo que esta en array al nuevo
		for(int i = 0; i < array.length; i++)
			nextArray[i] = array[i];
		
		array = nextArray;
	}
}

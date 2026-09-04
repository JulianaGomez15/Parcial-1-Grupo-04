package application.linkedModule;

import application.listModule.SimpleList;

public class SimpleLinkedList<E> implements SimpleList<E> {
	private LinkedNode<E> first = null;
	private LinkedNode<E> last = null;
	private int size = 0;

	@Override
	public boolean add(E element) {
		// Creamos un nuevo nodo con el dato a insertar
		LinkedNode<E> addedNode = new LinkedNode<E>(element);
		
		// Si la lista esta vacia (no hay elementos)
		// El nuevo nodo es el primero y ultimo
		if(isEmpty()) first = addedNode;
			
		// Si no esta vacia
		else
		{
			// Conectamos el nodo nuevo con el ultimo
			last.next = addedNode;
			addedNode.prev = last;
		}
		
		// El nodo nuevo es el ultimo
		last = addedNode;
		size++;
		return true;
	}

	@Override
	public void add(int index, E element) {
		// Si se inserta despues del ultimo, es add
		if(index == size)
		{
			add(element);
			return;
		}
		
		// Si llegamos hasta aca, index NO es size
		// Chequeamos que no sea -1 o > size
		validateIndex(index);
		LinkedNode<E> addedNode = new LinkedNode<E>(element);
		
		// Si insertamos al principio
		if(index == 0)
		{
			addedNode.next = first;
			first.prev = addedNode;
			first = addedNode;
		}
		else
		{
			// Buscamos al nodo que esta en la posicion a insertar
			LinkedNode<E> current = getNodeByIndex(index);
			
			// Conectamos al nuevo nodo a los vecinos del actual
			// (Va en el mismo lugar)
			addedNode.next = current.next;
			addedNode.prev = current.prev;
			
			// Conectamos al anterior con el nuevo nodo
			current.prev.next = addedNode;
			
			// Conectamos al actual con el nuevo nodo
			current.prev = addedNode;
		}
		
		size++; // Siempre contamos el nuevo nodo
	}

	@Override
	public boolean remove(Object o) {
		// Empezamos a buscar desde el primer nodo
		LinkedNode<E> current = first;
		
		// Toda la busqueda depende de que el actual no sea null
		while(current != null)
		{
			// Si encontramos lo que queremos borrar
			if(current.value.equals(o))
			{
				// Si solo queda uno, es clear
				if(size == 1)
				{
					clear();
					return true;
				}
				// Si es el primero
				else if(current == first)
				{
					// Queda el segundo como nuevo primero
					first = current.next;
					// Y ahora no tiene nada adelante
					first.prev = null;
				}
				// Si es el ultimo
				else if(current == last)
				{
					// Queda el anteultimo como nuevo ultimo
					last = current.prev;
					// Y ahora no tiene nada detras
					last.next = null;
				}
				// Si esta entre otros dos nodos, lo desconectamos
				else removeAndReconnect(current);
				size--;
				return true;
			}
			
			// Si no encontramos el valor, pasa al siguiente
			current = current.next;
		}
		
		// Si llegamos hasta aca, se termino la lista y no estaba
		return false;
	}

	@Override
	public E remove(int index) {
		validateIndex(index);
		
		LinkedNode<E> current = getNodeByIndex(index);
		E removedValue = current.value;
		
		// Si solo queda uno, es clear
		if(size == 1)
		{
			clear();
			return removedValue;
		}
		// Si es el primero
		else if(current == first)
		{
			// Queda el segundo como nuevo primero
			first = current.next;
			// Y ahora no tiene nada adelante
			first.prev = null;
		}
		// Si es el ultimo
		else if(current == last)
		{
			// Queda el anteultimo como nuevo ultimo
			last = current.prev;
			// Y ahora no tiene nada detras
			last.next = null;
		}
		// Si esta entre otros dos nodos, lo desconectamos
		else removeAndReconnect(current);
		
		size--;
		return removedValue;
	}

	@Override
	public E get(int index) {
		validateIndex(index);
		return getNodeByIndex(index).value;
	}

	@Override
	public E set(int index, E element) {
		validateIndex(index);
		LinkedNode<E> current = getNodeByIndex(index);
		E previousValue = current.value;
		current.value = element;
		return previousValue;
	}

	@Override
	public void clear() {
		// Si borramos el primero y el ultimo, a los demas los
		// borra el garbage collector porque quedan aislados
		first = null;
		last = null;
		size = 0;
	}

	@Override
	public int size() { return size; }

	@Override
	public boolean isEmpty() { return size == 0; }

	@Override
	public boolean contains(Object o) {
		return indexOf(o) != -1;
	}

	@Override
	public int indexOf(Object o) {
		LinkedNode<E> current = first;
		int index = 0;
		// Toda la busqueda depende de que el actual no sea null
		while(current != null)
		{	
			// Si encontramos lo que que buscamos
			if(current.value.equals(o)) return index;

			// Si no encontramos el valor, pasa al siguiente
			current = current.next;
			index++;
		}

		// Si llegamos hasta el final y no estaba
		// -1 es invalido, señal de que no estaba
		return -1;
	}

	private void validateIndex(int index)
	{
		if(index < 0 || index >= size)
			throw new IndexOutOfBoundsException();
	}
	
	private LinkedNode<E> getNodeByIndex(int index)
	{
		LinkedNode<E> current;
		
		// Chequeamos si index esta mas cerca del principio o final
		// Si esta de la mitad para abajo, empezamos por el primero
		if(index < size / 2)
		{
			current = first;
			for(int i = 0; i < index; i++)
				current = current.next;	
		}
		// Y si no, por el ultimo para atras
		else
		{
			current = last;
			for(int i = size -1; i > index; i--)
				current = current.prev;
		}
		
		return current;
	}
	
	// Esta funcion asume que el nodo tiene un prev y un next
	private void removeAndReconnect(LinkedNode<E> toRemove)
	{
		// Conectamos a los nodos vecinos entre si
		toRemove.prev.next = toRemove.next;
		toRemove.next.prev = toRemove.prev;
	}
}

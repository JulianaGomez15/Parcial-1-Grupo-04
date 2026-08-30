package LinkedModule;

import TP2ListModule.SimpleList;

public class SimpleLinkedList<E> implements SimpleList<E> {

    private LinkedNode<E> first = null;
    private LinkedNode<E> last = null;
    private int size = 0; // cuantos nodos hay en la lista

    public SimpleLinkedList() {
        // vadcia porque tiene los valores por defectos.
    }

    public void clear() { // garbage collector de java borra los datos que desconecte
        first = null;
        last = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    public boolean add(E element) {
        LinkedNode<E> addedNode = new LinkedNode<E>(element);
        if (isEmpty()) {
            first = addedNode;
        } else {
            // conecteamos el nodo nuevo con el ultimo
            last.next = addedNode;
            addedNode.prev = last;
        }
        // seteamos el nuevo nodo como el ultimo
        last = addedNode;
        size++;
        return true;
    }

    public void add(int index, E element) {
        if (index == size) { // inserto a lo ultimo
            add(element);
        }
        validateIndex(index); // me fijo que sea un indice valido
        LinkedNode<E> addedNode = new LinkedNode<E>(element);
        if (index == 0) { // inserto al principio, es el nuevo first

            addedNode.next = first;
            first.prev = addedNode;
            // marco el nuevo primero
            first = addedNode;
        } else {
            // tengo que buscar por indice, creo funcion aux
            LinkedNode<E> current = getNodeByIndex(index);
            // el nuevo nodo tendra el next y el prev del current
            addedNode.next = current.next;
            addedNode.prev = current.prev;

            // conecto el anterior con el nuevo nodo
            current.prev.next = addedNode;
            current.prev = addedNode;

        }
        size++;
    }

    private void validateIndex(int var1) {
        if (var1 < 0 || var1 >= this.size) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + var1);
        }
    }

    private LinkedNode<E> getNodeByIndex(int index) {
        LinkedNode<E> current = null;
        // chequeamos si index esta mas cerca del ppo o final
        if (index < size / 2) {
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = last;
            for (int i = size; i < index; i++) {
                current = current.prev;
            }
        }
        return current;
    }

    public boolean remove(Object s) {
        LinkedNode<E> current = first;
        while (current != null) {
            if (current.value.equals(s)) {
                if (size == 1) {
                    clear();
                    return true;
                }
                if (current == first) {
                    first = current.next;
                    first.prev = null;
                } else if (current == last) {
                    last = current.prev;
                    last.next = null;
                } else {
                    removeAndConnect(current);
                }
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    private void removeAndConnect(LinkedNode<E> toRemove) {

        toRemove.prev.next = toRemove.next;
        toRemove.next.prev = toRemove.prev;

    }

    public E remove(int index) {
        validateIndex(index);
        LinkedNode<E> current = getNodeByIndex(index);
        E removeValue = current.value;
        if (size == 1) {
            clear();
            return removeValue;
        }
        if (current == first) {
            first = current.next;
            first.prev = null;
        } else if (current == last) {
            last = current.prev;
            last.next = null;
        } else {
            removeAndConnect(current);
        }
        size--;
        return removeValue;

    }

    @Override
    public E get(int index) {
        validateIndex(index);
        LinkedNode<E> current = getNodeByIndex(index);
        return current.value;
    }

    @Override
    public boolean contains(Object o) {
        LinkedNode<E> current = first;
        while (current != null) {
            if (current.value.equals(o)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public int indexOf(Object o) {
        LinkedNode<E> current = first;
        int index = 0;
        while (current != null) {
            if (current.value.equals(o)) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }

    @Override
    public E set(int index, E element) {
        validateIndex(index);
        LinkedNode<E> current = getNodeByIndex(index);
        E previousValue = current.value;
        current.value = element;
        return previousValue;
    }
}
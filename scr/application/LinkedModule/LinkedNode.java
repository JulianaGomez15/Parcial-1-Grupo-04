package LinkedModule;

public class LinkedNode<E> {

    public E value;
    public LinkedNode<E> prev;
    public LinkedNode<E> next;

    public LinkedNode(E value) {
        this.value = value;
    }
}


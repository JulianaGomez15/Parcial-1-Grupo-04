package application.treeModule;

import application.listModule.SimpleArrayList;
import application.queueModule.SimpleLinkedQueue;

public class BST<E extends Comparable<E>> {
    private int count = 0;
    private TreeNode<E> root = null;

    public void insert(E value) {
        root = insertRecursive(root, value);
    }

    protected TreeNode<E> insertRecursive(TreeNode<E> current, E value){
        // Caso base: recibimos un espacio vacío
        // Devuelve el nodo que va a ocupar ese lugar
        if(current == null){
            count++;
            return new TreeNode<E>(value);
        }

        int comparison = value.compareTo(current.value);

        if(comparison > 0)
            current.right = insertRecursive(current.right, value);
        if(comparison < 0)
            current.left = insertRecursive(current.left, value);

        return current;
    }

    public void remove(E value) {
        root = removeRecursive(root, value);
    }

    protected TreeNode<E> removeRecursive(TreeNode<E> current, E value){

        if(current == null) return null;
        int comparison = value.compareTo(current.value);

        if(comparison == 0){
            count--;
            // Caso 1: es una hoja, no tiene hijos
            if(current.left == null && current.right == null) return null;

            // Caso 2: Tiene solo un hijo
            else if(current.right == null) return current.left;
            else if(current.left == null) return current.right;

            // Caso 3: Tiene 2 hijos
            else{
                count++;
                TreeNode<E> successor = getMinChild(current.right);
                current.value = successor.value;
                current.right = removeRecursive(current.right, successor.value);
            }
        }

        if(comparison > 0)
            current.right = removeRecursive(current.right, value);
        if(comparison < 0)
            current.left = removeRecursive(current.left, value);
        return current;
    }

    private TreeNode<E> getMinChild(TreeNode<E> current){
        while(current.left != null){
            current = current.left;
        }
        return current;
    }

    public boolean contains(E value) {
        return containsRecursive(root, value);
    }

    private boolean containsRecursive(TreeNode<E> current, E value) {
        if (current == null) return false;        // llegué al fondo: no está

        int comparison = value.compareTo(current.value);

        if (comparison == 0) return true;         // lo encontré
        if (comparison < 0)
            return containsRecursive(current.left, value);   // es menor: izquierda
        return containsRecursive(current.right, value);      // es mayor: derecha
    }

    public SimpleArrayList<E> inOrder() {
        SimpleArrayList<E> lista = new SimpleArrayList<>();

        inOrderRecursive(root, lista);

        return lista;
    }

    private void inOrderRecursive(TreeNode<E> current,
                                  SimpleArrayList<E> lista) {

        if (current == null) return;

        inOrderRecursive(current.left, lista);

        lista.add(current.value);

        inOrderRecursive(current.right, lista);
    }

    public void preOrder(){
        preOrderRecursive(root);
    }

    private void preOrderRecursive(TreeNode<E> current){

        if (current == null) return;

        System.out.print(current.value);
        preOrderRecursive(current.left);
        preOrderRecursive(current.right);
    }

    public void postOrder(){
        postOrderRecursive(root);
    }

    private void postOrderRecursive(TreeNode<E> current) {
        if (current == null) return;

        postOrderRecursive(current.left);
        postOrderRecursive(current.right);
        System.out.print(current.value);
    }

    // BFS: recorre nivel por nivel, de izquierda a derecha.
    public SimpleArrayList<E> levelOrder(){
        SimpleArrayList<E> result = new SimpleArrayList<E>(Math.max(count, 1));
        if(root == null) return result;

        SimpleLinkedQueue<TreeNode<E>> queue = new SimpleLinkedQueue<TreeNode<E>>();
        queue.enqueue(root);
        while(queue.size() > 0){
            TreeNode<E> current = queue.dequeue();
            result.add(current.value);
            if(current.left != null) queue.enqueue(current.left);
            if(current.right != null) queue.enqueue(current.right);
        }
        return result;
    }

    public boolean isEmpty(){
        if(root == null) return true;
        return false;
    }

    private int getNodeHeight(TreeNode<E> current){
        // Caso base: llegamos al fina o el arbol esta vacio
        if(current == null) return -1;

        // Paso recursivo, contamos 1 + la altura maxima entre los nodos
        return 1 + Math.max(getNodeHeight(current.left), getNodeHeight(current.right));
    }

    protected int getNodeBalanceFactor(TreeNode<E> current){
        return getNodeHeight(current.left) - getNodeHeight(current.right);
    }

    public E search(E value) {
        return searchRecursive(root, value);
    }

    private E searchRecursive(TreeNode<E> current, E value) {

        // Caso base: no se encontró el elemento
        if (current == null) return null;

        int comparison = value.compareTo(current.value);

        // Encontramos el elemento
        if (comparison == 0)
            return current.value;

        // Buscamos por la izquierda
        if (comparison < 0)
            return searchRecursive(current.left, value);

        // Buscamos por la derecha
        return searchRecursive(current.right, value);
    }

    public int size(){
        return count;
    }

}

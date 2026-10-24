package application.treeModule;

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
            else if(current.left != null) return current.left;
            else if(current.right != null) return current.right;

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

    private int getNodeHeight(TreeNode<E> current){
        // Caso base: llegamos al fina o el arbol esta vacio
        if(current == null) return -1;

        // Paso recursivo, contamos 1 + la altura maxima entre los nodos
        return 1 + Math.max(getNodeHeight(current.left), getNodeHeight(current.right));
    }

    protected int getNodeBalanceFactor(TreeNode<E> current){
        return getNodeHeight(current.left) - getNodeHeight(current.right);
    }
}

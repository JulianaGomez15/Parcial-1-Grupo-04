package application.treeModule;

public class TreeNode<E> {
    public E value;
    public TreeNode<E> left = null;
    public TreeNode<E> right  = null;

    public TreeNode(E value) {
        this.value = value;
    }
}

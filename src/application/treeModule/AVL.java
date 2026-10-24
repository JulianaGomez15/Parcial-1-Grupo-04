package application.treeModule;

public class AVL<E extends Comparable<E>> extends BST<E> {

    @Override
    protected TreeNode<E> removeRecursive(TreeNode<E> current, E value){
        return rebalanceNode(super.removeRecursive(current, value));
    }

    @Override
    protected TreeNode<E> insertRecursive(TreeNode<E> current, E value){
        return rebalanceNode(super.insertRecursive(current, value));
    }

    private TreeNode<E> rebalanceNode(TreeNode<E> current){
        int bf = getNodeBalanceFactor(current);

        // Casos izquierdos
        if(bf > 1){
            // Caso LL
            if(getNodeBalanceFactor(current.left) >= 0)
                return rotateRight(current);
            // Caso LR
            else return rotateLeftRight(current);
        }

        // Casos derechos
        if(bf < -1){
            // Caso RR
            if(getNodeBalanceFactor(current.right) <= 0)
                return rotateLeft(current);
            // Caso RL
            else return rotateRightLeft(current);
        }
        // Si no está desbalanceado, devuelve el que recibió
        return current;
    };

    private TreeNode<E> rotateRight(TreeNode<E> y){
        // Variables auxiliares
        TreeNode<E> x = y.left;
        TreeNode<E> t2 = x.right;

        // Reasignamos los nodos
        x.right = y;
        y.left = t2;

        // Queda X en la posicion de Y
        return x;
    };

    private TreeNode<E> rotateLeft(TreeNode<E> x){
        // Variables auxiliares
        TreeNode<E> y = x.right;
        TreeNode<E> t2 = y.left;

        // Reasignamos los nodos
        y.left = x;
        x.right = t2;

        // Queda Y en la posicion de X
        return y;
    };

    private TreeNode<E> rotateRightLeft(TreeNode<E> current){
        current.right = rotateRight(current.right);
        return rotateLeft(current);
    }

    private TreeNode<E> rotateLeftRight(TreeNode<E> current){
        current.left = rotateLeft(current.left);
        return rotateRight(current);
    }
}

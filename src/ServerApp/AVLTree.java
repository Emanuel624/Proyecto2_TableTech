package ServerApp;

public class AvlTree<T extends Comparable<? super T>> {
    private AvlNode<T> rootNode;

    //Constructor to set null value to the rootNode
    public AvlTree() {
        rootNode = null;
    }

    //create removeAll() method to make AVL Tree empty
    public void removeAll()
    {
        rootNode = null;
    }

    // create checkEmpty() method to check whether the AVL Tree is empty or not
    public boolean checkEmpty() {
        return rootNode == null;
    }

    // create insertElement() to insert element to the AVL Tree
    public void insertElement(T element)
    {
        rootNode = insertElement(element, rootNode);
    }

    //create getHeight() method to get the height of the AVL Tree
    private int getHeight(AvlNode<T> node )
    {
        return node == null ? -1 : node.height;
    }

    //create maxNode() method to get the maximum height from left and right node
    private int getMaxHeight(int leftNodeHeight, int rightNodeHeight)
    {
        return leftNodeHeight > rightNodeHeight ? leftNodeHeight : rightNodeHeight;
    }


    //create insertElement() method to insert data in the AVL Tree recursively
    private AvlNode<T> insertElement(T element, AvlNode<T> current) {
        if (current == null) {
            current = new AvlNode<>(element);
        } else if (element.compareTo(current.element) < 0) {
            current.leftChild = insertElement(element, current.leftChild);
            if (getHeight(current.leftChild) - getHeight(current.rightChild) == 2) {
                if (element.compareTo(current.leftChild.element) < 0) {
                    current = rotateWithLeftChild(current);
                } else {
                    current = doubleWithLeftChild(current);
                }
            }
        } else if (element.compareTo(current.element) > 0) {
            current.rightChild = insertElement(element, current.rightChild);
            if (getHeight(current.rightChild) - getHeight(current.leftChild) == 2) {
                if (element.compareTo(current.rightChild.element) > 0) {
                    current = rotateWithRightChild(current);
                } else {
                    current = doubleWithRightChild(current);
                }
            }
        } else {
            // if the element is already present in the tree, we will do nothing
        }
        current.height = getMaxHeight(getHeight(current.leftChild), getHeight(current.rightChild)) + 1;
        return current;
    }

    // creating rotateWithLeftChild() method to perform rotation of binary tree node with left child
    private AvlNode<T> rotateWithLeftChild(AvlNode<T> node2) {
        AvlNode<T> node1 = node2.leftChild;
        node2.leftChild = node1.rightChild;
        node1.rightChild = node2;
        node2.height = getMaxHeight( getHeight( node2.leftChild ), getHeight( node2.rightChild ) ) + 1;
        node1.height = getMaxHeight( getHeight( node1.leftChild ), node2.height ) + 1;
        return node1;
    }

    // creating rotateWithRightChild() method to perform rotation of binary tree node with right child
    private AvlNode<T> rotateWithRightChild(AvlNode<T> node1) {
        AvlNode<T> node2 = node1.rightChild;
        node1.rightChild = node2.leftChild;
        node2.leftChild = node1;
        node1.height = getMaxHeight( getHeight( node1.leftChild ), getHeight( node1.rightChild ) ) + 1;
        node2.height = getMaxHeight( getHeight( node2.rightChild ), node1.height ) + 1;
        return node2;
    }

    //create doubleWithLeftChild() method to perform double rotation of binary tree node. This method first rotate the left child with its right child, and after that, node3 with the new left child
    private AvlNode<T> doubleWithLeftChild(AvlNode<T> node3) {
        node3.leftChild = rotateWithRightChild( node3.leftChild );
        return rotateWithLeftChild( node3 );
    }

    //create doubleWithRightChild() method to perform double rotation of binary tree node. This method first rotate the right child with its left child and after that node1 with the new right child
    private AvlNode<T> doubleWithRightChild(AvlNode<T> node1) {
        node1.rightChild = rotateWithLeftChild( node1.rightChild );
        return rotateWithRightChild( node1 );
    }
    //Holasda
}

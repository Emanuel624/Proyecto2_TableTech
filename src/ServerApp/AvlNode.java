package ServerApp;

public class AvlNode<T> {
    //HOLA
    T element;
    AvlNode<T> leftChild;
    AvlNode<T> rightChild;
    int height;
    public AvlNode(T element) {
        this(element, null, null);
    }
    public AvlNode(T element, AvlNode<T> left, AvlNode<T> right) {
        this.element = element;
        this.leftChild = left;
        this.rightChild = right;
        this.height = 0;
    }

}

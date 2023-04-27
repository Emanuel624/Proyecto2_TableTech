package ServerApp;

public class AvlNode {
    int element;
    AvlNode leftChild;
    AvlNode rightChild;
    int height;
    public AvlNode(int element) {
        this(element, null, null);
    }
    public AvlNode(int element, AvlNode left, AvlNode right) {
        this.element = element;
        this.leftChild = left;
        this.rightChild = right;
        this.height = 0;
    }
}

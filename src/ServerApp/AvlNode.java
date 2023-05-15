package ServerApp;

/**
 * @authors Randall Bryan Bolañoz López, Octavio Sanchez Soto, Emanuel Chavarría Hernández.
 * @version 1.0
 */

/**
 * Esta clase publica, se relaciona con la implementación desde 0, de la estructura lineal de un nodo para un Arbol AVL.
 * @param <T> este parametro, permite añadir, modificar o eliminar elementos al nodo.
 */
public class AvlNode<T> {
    T element;
    AvlNode<T> leftChild;
    AvlNode<T> rightChild;
    int height;
    
    /**
     * El public de este nodo, se encarga de recibir un elemento el cual puede añadirse elementos al mismo
     * @param element es el parametro el cual añade elementos a dicho nodo
     */
    public AvlNode(T element) {
        this(element, null, null);
    }
    
    /**
     * El nodo public que contiene la lógica, debe recibir informacioón de cada nodo izquierdo o derecho para pode realizar acciones con el mismo.
     * @param element parametro relacionado al elemento o data dentro de cada nodo.
     * @param left parametro relacionado con el nodo izquierdo del árbol.
     * @param right parametro relacionado con el nodo derecho del árbol.
     */
    public AvlNode(T element, AvlNode<T> left, AvlNode<T> right) {
        this.element = element;
        this.leftChild = left;
        this.rightChild = right;
        this.height = 0;
    }

}

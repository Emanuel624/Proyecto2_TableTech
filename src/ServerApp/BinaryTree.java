 
package ServerApp;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * @authors Randall Bryan Bolañoz López, Octavio Sanchez Soto, Emanuel Chavarría Hernández.
 * @version 1.0
 */

//Esta clase es un arbol binario de busqueda utilizado para generar validaciones de datos
/**
 * Esta clase publica, se relaciona con la implementación desde 0, de la estructura lineal para un Arbol Binario como tal, ademas de ser serializable para poder ser enviado y recibo con sockets.
 * @param <T> este parametro permite que a la clase como tal se le puedan agregar, modifcar o eliminar elementos
 */
public class BinaryTree <T extends Comparable<? super T>> implements Serializable{
    private Node<T> root;
    
    
    /**
     * El público de esta clase, permite inicializar la creación del árbol como tal.
     */
    public BinaryTree(){
        this.root = null;
    }
    
    
    /**
     * Este booleano publicom, permite conocer si el arbol está vacío.
     * @return un valor nulo en este caso.
     */
    public boolean isEmpty(){
        return this.root == null;
    }
     
    
    /**
     * Este booleano públic permite saber si un elemento se encuentra dentro de los nodos del árbol binario o no.
     * @param element este parametro es el elemento por buscar.
     * @return un valor boolean si el elmento se encuentra o no.
     */
    public boolean contains(T element){
        return this.contains(element, this.root);
    }
    
    
    /**
     * Este booleano privado, contiene toda la lógica detras del booleano publico contains.
     * @param element este parametro es el elemento por buscar.
     * @param node este parametro es el nodo, por el cual se va a buscar el elemento en cuestión.
     * @return 
     */
    private boolean contains(T element, Node<T> node){
        if (node == null){
            return false;
        }else{
            int compareResult = element.compareTo(node.element);
            
            if (compareResult < 0)
                return contains(element, node.left);
            else if (compareResult > 0)
                return contains(element,node.right);
            else
                return true;
        }
    }
    
    
    /**
     * Este nodo público permite conocer el valor de la raíz de cualquier ábol binario implementado con esta clase. 
     * @return retorna la raíz como tal.
     */
    public Node<T> getRoot() {
        return this.root;
    }

    
    /**
     * Lista enlazada pública, implementada mediante java, la cual recorre todo el arbol y devuelve una lista con todos los datos del mismo.
     * @return devuelve una lista con todos los datos del ábol como tal.
     */
    public LinkedList<T> inOrderTraversal() {
        LinkedList<T> list = new LinkedList<>();
        inOrderTraversal(this.root, list);
        return list;
    }
    
    
    /** 
     * Método privado que se encarga de la lógica detrás de "inOrderTraversal"
     * @param node parametro que utiliza un nodo, para recorrer el árbol binario.
     * @param list parametro que utiliza una lista enlazada para añadir los elementos del árbol a la misma.
     */
    private void inOrderTraversal(Node<T> node, LinkedList<T> list) {
        if (node != null) {
            inOrderTraversal(node.left, list);
            list.add(node.element);
            inOrderTraversal(node.right, list);
        }
    }


    /**
     * Este nodo públic, permite encontrar el nodo con menor valor dentro del árbol binario.
     * @return el valor mínimo del árbol binario.
     */
    public Node<T> findMin(){
        if (this.isEmpty()){
            return null;
        }else{
            return this.findMin(this.root);
        }
    }
    
    
    /**
     * Este nodo públic, permite encontrar el nodo con mayor valor dentro del árbol binario.
     * @return el valor máximo del árbol binario.
     */
    public Node<T> findMax(){
        if (this.isEmpty()){
            return null;
        }else{
            return this.findMax(this.root);   
        }
    }
     
    
    /**
     * Este nodo privado, se encarga de la lógica detrás del nodo público "findMin()".
     * @param node este parámetro utiliza un nodo para poder recorrer el árbol binario.
     * @return el valor mínimo del árbol binario.
     */
    private Node<T> findMin(Node<T> node){
        if (node == null)
            return null;
        else if (node.left == null)
            return node;
        else
            return findMin(node.left);
    }
    
    
    /**
     * Este nodo privado, se encarga de la lógica detrás del nodo público "findMax()".
     * @param node este parámetro utiliza un nodo para poder recorrer el árbol binario.
     * @return el valor máximo del árbol binario.
     */
    private Node<T> findMax(Node<T> node){
        if (node != null)
            while (node.right != null){
                node = node.right;
            }
        return node;
    }
    
    
    /**
     * Este método public permite añadir elementos al árbol binario.
     * @param element este parametro, se refiere al elemento que se desea agregar como tal.
     */
    public void insert(T element){
        this.root = this.insert(element, this.root);
    }
    
    
    /**
     * Este nodo privado contiene la lógica detras del método público "insert".
     * @param element parametro que obtiene el elemento por agregar.
     * @param current nodo que permiet recorrer el arbol binario para luego ser agregado.
     * @return el árbol binario con el nuevo elemento agregado.
     */
    private Node<T> insert(T element, Node<T> current){
        if (current == null) {
            return new Node<T>(element, null, null);
        }

        int compareResult = element.compareTo(current.element);

        if (compareResult < 0) {
            current.left = this.insert(element, current.left);
        } else if (compareResult > 0) {
            current.right = this.insert(element, current.right);
        }

        return current;
    }
    
  
/**
 * Nodo público, que permite buscar un elemento en específico dentro del árbol binario.
 * @param element este parametro es el elemento por buscar.
 * @return el elemento dentro del árbol binario o no.
 */    
public Node<T> find(T element) {
    return find(element, root);
}


/**
 * Este nodo privado, contiene la lógica detrás del nodo público "find".
 * @param element este parametro es el elemento por buscar.
 * @param node este prametro es el nodo, por el cual se va recorrer el árbol binario.
 * @return el elemento dentro del árbol binario o no.
 */
private Node<T> find(T element, Node<T> node) {
    if (node == null) {
        return null;
    } else {
        int compareResult = element.compareTo(node.element);

        if (compareResult < 0) {
            return find(element, node.left);
        } else if (compareResult > 0) {
            return find(element, node.right);
        } else {
            return node;
        }
    }
}

    
    /**
     * Este nodo público contiene la lógica detras de poder remover un elemento dentro del árbol binariol.
     * @param element este parametro es el elemento por eliminar.
     * @param node este prametro es el nodo, por el cual se va recorrer el árbol binario.
     * @return retorna el árbol binario, con el elemento removido del mismo.
     */
    public Node<T> remove(T element, Node<T> node){
        if (node == null)
            throw new NoSuchElementException("Elemento no encontrado");

        int compareResult = element.compareTo(node.element);

        if (compareResult < 0)
            node.left = remove(element, node.left);
        else if (compareResult > 0)
            node.right = remove(element, node.right);
        else if (node.left != null && node.right != null){
            node.element = findMin(node.right).element;
            node.right = remove(node.element, node.right);
        }else{
            node = node.left != null ? node.left : node.right;
        }
        return node;
    } 
}


package ServerApp;

import java.io.Serializable;
import org.w3c.dom.Element;

/**
 * Esta Clase publica, contiene el elemento nodo para poder recorrer el arbol binario.
 * @authors Randall Bryan Bolañoz López, Octavio Sanchez Soto, Emanuel Chavarría Hernández.
 * @version 1.0
 * @param <T> elemento que se puede añadir, modficar o eliminar dentro del nodo.
 */
public class Node <T extends Comparable<? super T>> implements Serializable{
     T element;
     Node <T> left;
     Node <T> right;
    
     
     /**
      * Constructor público de la clase nodo.
      * @param element este parametro es el elemento que se puede añadir, modficar o eliminar dentro del nodo.
      * @param left este parametro es el nodo izquierdo del arbol.
      * @param right este parametro es el nodo derecho del arbol.
      */
    public Node(T element, Node<T> left, Node<T> right){
        this.element = element;
        this.left = left;
        this.right = right;
    }
    
    
    /**
     * Este metodo permite agregar un hijo como nodo.
     * @param nuevoAdmin parametro que obtiene un nuevo admin para ser agregado al nodo.
     */
    void appendChild(Element nuevoAdmin) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}

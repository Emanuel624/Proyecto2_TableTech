
package ServerApp;

import java.io.Serializable;
import org.w3c.dom.Element;


public class Node <T extends Comparable<? super T>> implements Serializable{
     T element;
     Node <T> left;
     Node <T> right;
    
    public Node(T element, Node<T> left, Node<T> right){
        this.element = element;
        this.left = left;
        this.right = right;
    }

    void appendChild(Element nuevoAdmin) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

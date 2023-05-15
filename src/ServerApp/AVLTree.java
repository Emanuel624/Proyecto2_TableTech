package ServerApp;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @authors Randall Bryan Bolañoz López, Octavio Sanchez Soto, Emanuel Chavarría Hernández.
 * @version 1.0
 */

/**
 * Esta clase publica, se relaciona con la implementación desde 0, de la estructura lineal para un Arbol AVL como tal, ademas de ser serializable para poder ser enviado y recibo con sockets.
 * @param <T> este parametro permite que a la clase como tal se le puedan agregar, modifcar o eliminar elementos
 */
public class AvlTree<T extends Comparable<? super T>> implements Serializable{
    private AvlNode<T> rootNode;
    /**
     * Publico de la clase, para poder comenzar con la creación del arbol AVL como tal.
     */
    public AvlTree() {
        rootNode = null;
    }

    
    /**
     * Método que remueve todos los nodos del arbol AVL.
     */
    public void removeAll()
    {
        rootNode = null;
    }

    
    /**
     * Booleano público que verfica si la raíz del arbol AVL, está vacía.
     * @return un nulo de la raíz del arbol.
     */
    public boolean checkEmpty() {
        return rootNode == null;
    }

    
    /**
     * Método que permite insertar elemento al arbol.
     * @param element el elemento para agregar como tal.
     */
    public void insertElement(T element)
    {
        rootNode = insertElement(element, rootNode);
    }

    
    /**
     * int privado, que permite conocer la altura del arbol AVL.
     * @param node parametro que permite conocer el nodo al cual se desea conocer su altura.
     * @return la altura como tal.
     */
    private int getHeight(AvlNode<T> node )
    {
        return node == null ? -1 : node.height;
    }

    
    /**
     * int privado, que permite conocer la altura máxima del arbol AVL.
     * @param leftNodeHeight parametro del nodo izquierdo que se quiere evaluar.
     * @param rightNodeHeight parametro del nodo derecho que se quiere evaluar.
     * @return retorna la altura maxima como tal
     */
    private int getMaxHeight(int leftNodeHeight, int rightNodeHeight)
    {
        return leftNodeHeight > rightNodeHeight ? leftNodeHeight : rightNodeHeight;
    }
    
    
    /**
     * booleano public que permite conocer si un elemento se encuentra o no dentro del arbol AVL.
     * @param element parametro que contiene el elmento por compara como tal.
     * @return retorna false o verdadero
     */
    public boolean contains(T element) {
        return contains(element, rootNode);
    }
    
    
    /**
     * Lógica detras del public de contains.
     * @param element parametro que contiene el elmento por compara como tal.
     * @param node el comienzo de nodos por el cual se va a comparar.
     * @return un valor booleano.
     */
    private boolean contains(T element, AvlNode<T> node) {
        if (node == null) {
            return false;
        }

        int cmp = element.compareTo(node.element);

        if (cmp < 0) {
            return contains(element, node.leftChild);
        } else if (cmp > 0) {
            return contains(element, node.rightChild);
        } else {
            return true;
        }
    }
    
    
    /**
     * Método publico que permite remover elementos del arbol AVL.
     * @param element parametro del elemento como tal que se quiere remover.
     */
    public void removeElement(T element) {
        rootNode = removeElement(element, rootNode);
    }
    
    
    /**
     * Lógica detras del public de "removeElement".
     * @param element parametro del elemento como tal que se quiere remover.
     * @param current paramentro del nodo del arbol AVL actual para trabjar la comparación y el proceso de eliminicación.
     * @return el arbol con el elemento eliminado.
     */
    private AvlNode<T> removeElement(T element, AvlNode<T> current) {
        if (current == null) {
            return null;
        } else if (element.compareTo(current.element) < 0) {
            current.leftChild = removeElement(element, current.leftChild);
            if (getHeight(current.rightChild) - getHeight(current.leftChild) == 2) {
                if (getHeight(current.rightChild.rightChild) >= getHeight(current.rightChild.leftChild)) {
                    current = rotateWithRightChild(current);
                } else {
                    current = doubleWithRightChild(current);
                }
            }
        } else if (element.compareTo(current.element) > 0) {
            current.rightChild = removeElement(element, current.rightChild);
            if (getHeight(current.leftChild) - getHeight(current.rightChild) == 2) {
                if (getHeight(current.leftChild.leftChild) >= getHeight(current.leftChild.rightChild)) {
                    current = rotateWithLeftChild(current);
                } else {
                    current = doubleWithLeftChild(current);
                }
            }
        } else {
            if (current.leftChild != null && current.rightChild != null) {
                // the node to remove has two children
                AvlNode<T> successor = getSuccessor(current);
                current.element = successor.element;
                current.rightChild = removeElement(successor.element, current.rightChild);
            } else {
                // the node to remove has one or zero children
                current = (current.leftChild != null) ? current.leftChild : current.rightChild;
            }
        }
        if (current != null) {
            current.height = getMaxHeight(getHeight(current.leftChild), getHeight(current.rightChild)) + 1;
        }
        return current;
    }
    
    
    /**
     * Privado del arbol AVL, que permite conocer el nodo sucesor al actual que se estaría evaluando
     * @param current parametro es conocer el nodo que se esta trabajando actualmente.
     * @return retorna el nodo sucesor.
     */
    private AvlNode<T> getSuccessor(AvlNode<T> current) {
        AvlNode<T> node = current.rightChild;
        while (node.leftChild != null) {
            node = node.leftChild;
        }
        return node;
    }

    
    /**
     * Public del arrayList implementado por llava, el cual permite obtener todos los datos de todos los elementos de la lista, para poder ser enviado por Sockets evitando errores.
     * @return la lista de todos los elementos del arbol AVL.
     */
    public ArrayList<T> inOrderTraversal() {
        ArrayList<T> list = new ArrayList<>();
        inOrderTraversal(this.rootNode, list);
        return list;
    }
    
    
    /**
     * Método con la lógica detras del public de inOrderTraversal.
     * @param node parametro con el nodo del arbol AVL.
     * @param list parametro que conlleva una arraList para poder añadir elementos a el mismo.
     */
    private void inOrderTraversal(AvlNode<T> node, ArrayList<T> list) {
        if (node != null) {
            inOrderTraversal(node.leftChild, list);
            list.add(node.element);
            inOrderTraversal(node.rightChild, list);
        }
    }

    
    /**
     * Privado que contiene la lógica de "insertElement" 
     * @param element parametro que contiene el elemento por incertar
     * @param current parametro relacionado con el nodo actualmente trabajdo
     * @return retorna el arbol AVL con el nuevo elemento añadido y ordenado correctamente.
     */
    private AvlNode<T> insertElement(T element, AvlNode<T> current) {
        if (current == null) {
            current = new AvlNode<T>(element);
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
            // Si el elemento ya esta en el arbol no hacer nada.
        }
        current.height = getMaxHeight(getHeight(current.leftChild), getHeight(current.rightChild)) + 1;
        return current;
    }

    
    /**
     * Priavdo que permite hacer rotaciones con el hijo izquierdo.
     * @param node2 parametro que utiliza un nodo para poder recorrer y rotar el arbol AVL.
     * @return retorna la rotación del nodo dentro del arbol AVL.
     */
    private AvlNode<T> rotateWithLeftChild(AvlNode<T> node2) {
        AvlNode<T> node1 = node2.leftChild;
        node2.leftChild = node1.rightChild;
        node1.rightChild = node2;
        node2.height = getMaxHeight( getHeight( node2.leftChild ), getHeight( node2.rightChild ) ) + 1;
        node1.height = getMaxHeight( getHeight( node1.leftChild ), node2.height ) + 1;
        return node1;
    }

    
    /**
     * Priavdo que permite hacer rotaciones con el hijo derecho.
     * @param node1 parametro que utiliza un nodo para poder recorrer y rotar el arbol AVL.
     * @return retorna la rotación del nodo dentro del arbol AVL.
     */
    private AvlNode<T> rotateWithRightChild(AvlNode<T> node1) {
        AvlNode<T> node2 = node1.rightChild;
        node1.rightChild = node2.leftChild;
        node2.leftChild = node1;
        node1.height = getMaxHeight( getHeight( node1.leftChild ), getHeight( node1.rightChild ) ) + 1;
        node2.height = getMaxHeight( getHeight( node2.rightChild ), node1.height ) + 1;
        return node2;
    }

    
    /**
     * Priavdo que permite hacer dobles rotaciones con el hijo izquierdo.
     * @param node3 parametro que utiliza un nodo para poder recorrer y rotar el arbol AVL.
     * @return retorna la doble rotación del nodo dentro del arbol AVL.
     */
    private AvlNode<T> doubleWithLeftChild(AvlNode<T> node3) {
        node3.leftChild = rotateWithRightChild( node3.leftChild );
        return rotateWithLeftChild( node3 );
    }

    
    /**
     * Priavdo que permite hacer dobles rotaciones con el hijo derecho.
     * @param node1 parametro que utiliza un nodo para poder recorrer y rotar el arbol AVL.
     * @return retorna la doble rotación del nodo dentro del arbol AVL.
     */
    private AvlNode<T> doubleWithRightChild(AvlNode<T> node1) {
        node1.rightChild = rotateWithLeftChild( node1.rightChild );
        return rotateWithRightChild( node1 );
    }
}

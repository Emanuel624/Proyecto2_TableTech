package ServerApp;

import java.util.function.Consumer;
import java.io.Serializable;

public class ListaEnlazada<C> implements Serializable {
    private NodeLista<C> head;
    private int size;

    /**
     * constructor de la clase lista enlazada
     */
    public ListaEnlazada() {
        this.head = null;
        this.size = 0;

    } //Hola aaa

    /**
     * metodo que verifica si la lista esta vacia
     *
     * @return devuelve si el primer elemento es nulo
     */
    public boolean isEmpty() {
        return this.head == null;
    }

    /**
     * metodo que dice el tamano de la lista
     *
     * @return tamano int
     */
    public int size() {
        return this.size;
    }

    /**
     * inserta una celda a la lista
     *
     * @param data celda
     */
    public void add(C data) {
        NodeLista<C> newNode = new NodeLista<C>(data);
        newNode.setNext(this.head);;
        this.head = newNode;
        this.size++;
    }

    /**
     * borra el primer elemento de la lista
     *
     * @return nulo node
     */
    public NodeLista<C> deleteFirst() {
        if (this.head != null) {
            NodeLista<C> temp = this.head;
            this.head = this.head.getNext();
            this.size--;
            return temp;
        } else {
            return null;
        }
    }

    /**
     * imprime la lista con sus elementos
     */
    public void displayList() {
        NodeLista<C> current = this.head;
        while (current != null) {
            System.out.println(current.getData());
            current = current.getNext();
        }

    }

    /**
     * metodo que elimina cierto valor especifico
     *
     * @param searchValue valor a eliminar
     * @return nulo node
     */
    public NodeLista<C> delete(Object searchValue) {
        NodeLista<C> current = this.head;
        NodeLista<C> previous = this.head;
        while (current != null) {
            if (current.getData().equals(searchValue)) {
                if (current == this.head) {
                    this.head = this.head.getNext();
                } else {
                    previous.setNext(current.getNext());
                }
                return current;
            } else {
                previous = current;
                current = current.getNext();
            }
        }
        return null;
    }

    public C[] toArray(C[] array) {
        if (array.length < size) {
            array = (C[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), size);
        }

        NodeLista<C> current = head;
        for (int i = 0; i < size; i++) {
            array[i] = current.getData();
            current = current.getNext();
        }

        return array;
    }

    /**
     * metodo que aplica una accion a cada uno de los nodos de la lista
     *
     * @param action accion
     */
    public void forEach(Consumer<? super C> action) {
        for (NodeLista<C> current = head; current != null; current = current.getNext()) {
            action.accept(current.getData());
        }
    }
}

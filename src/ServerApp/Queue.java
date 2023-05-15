package ServerApp;

/**
 * Esta Clase publica, contiene una implementación desde 0, de una cola.
 * @authors Randall Bryan Bolañoz López, Octavio Sanchez Soto, Emanuel Chavarría Hernández.
 * @version 1.0
 * @param <C> elemento que se puede añadir, modficar o eliminar dentro del nodo.
 */
public class Queue<C> {
    private ListaEnlazada<C> listaEnlazada;
    
    
    /**
     * Constructor público de la clase cola.
     */
    public Queue() {
        listaEnlazada = new ListaEnlazada<>();
    }

    
    /**
     * Booleano publico que permite verficar si la cola implementada por medio de ListasEnlazadas está vacía.
     * @return un boobleano si la lista Enlazada que permite el funcionamiento del queque está vacía o o no.
     */
    public boolean isEmpty() {
        return listaEnlazada.isEmpty();
    }
    
    
    /**
     * Permite conocer el tamaño que tiene la ListaEnlazada que permite implementar la Queue.
     * @return el tamaño de dicha lista Enlazada.
     */
    public int size() {
        return listaEnlazada.size();
    }
    
    
    /**
     * Método que añade elmentos a la lista Enlazada, es decir a la cola.
     * @param data parametro que tiene el dato que se desea añadir.
     */
    public void enqueue(C data) {
        listaEnlazada.add(data);
    }
    
    
    /**
     * Método que permite sacar de la lista Enlazada un elemento, es decir sacarlo de la cola.
     * @return 
     */
    public C dequeue() {
        if (isEmpty()) {
            return null;
        } else {
            C data = listaEnlazada.deleteFirst().getData();
            return data;
        }
    }
    
    
    /**
     * Método que permite desplegar la informacíón de la cola.
     */
    public void displayQueue() {
        listaEnlazada.displayList();
    }
}

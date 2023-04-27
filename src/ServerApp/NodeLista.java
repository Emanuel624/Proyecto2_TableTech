package ServerApp;

public class NodeLista<C extends Comparable<? super C>> {

    private C data;
    private NodeLista<C> next;

    /***
     * Metodo de la clase nodo que crea una lista de nodos
     * @param data the data
     */
    public NodeLista(C data) {
        this.next = null;
        this.data = data;
    }

    /***
     * Get de la clase nodo que devuelve un dato
     * @return devuelve el dato que está guardado en data
     */
    public C getData() {
        return this.data;
    }

    /***
     * Metodo que establece un dato dentro de la variable data
     * @param data información a establecer (canción)
     */
    public void setData(C data) {
        this.data = data;
    }

    /***
     * Metodo que devuelve el siguiente elemento de la lista
     * @return devuelve el siguiente elemento
     */
    public NodeLista<C> getNext() {
        return this.next;
    }

    /***
     * Metodo que establece el siguiente elemento (siempre va a ser null)
     * @param node elemento de la lista
     */
    public void setNext(NodeLista<C> node) {
        this.next = node;
    }
}

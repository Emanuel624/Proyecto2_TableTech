package ServerApp;

public class Queue<C> {
    private ListaEnlazada<C> listaEnlazada;

    public Queue() {
        listaEnlazada = new ListaEnlazada<>();
    }

    public boolean isEmpty() {
        return listaEnlazada.isEmpty();
    }

    public int size() {
        return listaEnlazada.size();
    }

    public void enqueue(C data) {
        listaEnlazada.add(data);
    }

    public C dequeue() {
        if (isEmpty()) {
            return null;
        } else {
            C data = listaEnlazada.deleteFirst().getData();
            return data;
        }
    }

    public void displayQueue() {
        listaEnlazada.displayList();
    }
}

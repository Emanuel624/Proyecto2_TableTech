package ServerApp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class ListaEnlazadaView<C> extends ListaEnlazada<C> {
    private ListView<C> listView;

    public ListaEnlazadaView() {
        super();
        listView = new ListView<>();
    }

    public ListView<C> getListView() {
        return listView;
    }

    @Override
    public void add(C data) {
        super.add(data);
        updateListView();
    }
    
    public ListaEnlazada<C> getListaEnlazada() {
        ListaEnlazada<C> listaEnlazada = new ListaEnlazada<>();
        forEach(listaEnlazada::add);
        return listaEnlazada;
    }
    @Override
    public NodeLista<C> deleteFirst() {
        NodeLista<C> deletedNode = super.deleteFirst();
        updateListView();
        return deletedNode;
    }

    @Override
    public NodeLista<C> delete(Object searchValue) {
        NodeLista<C> deletedNode = super.delete(searchValue);
        updateListView();
        return deletedNode;
    }

    private void updateListView() {
        ObservableList<C> observableList = FXCollections.observableArrayList();
        forEach(observableList::add);
        listView.setItems(observableList);
    }
}

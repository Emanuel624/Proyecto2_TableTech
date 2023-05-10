package ServerApp;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class ListaEnlazadaView<C> extends ListaEnlazada<C> {
    private ListView<C> listView;
    private ObservableList<C> observableList;

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
    public void setListaEnlazada(ListaEnlazada<C> lista) {
        listView.getItems().clear();
        if (lista != null) {
            lista.forEach(item -> listView.getItems().add(item));
        }
    }

    public void remove(C item) {
        listView.getItems().remove(item);
    }

public void setItems(ObservableList<C> items) {
        listView.setItems(items);
    }
public ObservableList<C> getObservableList() {
        ObservableList<C> observableList = FXCollections.observableArrayList();
        forEach(observableList::add);
        return observableList;
    }
public void refresh() {
        // Limpiar y volver a agregar los elementos a la lista observable
        List<C> tempList = new ArrayList<>(observableList);
        observableList.clear();
        observableList.addAll(tempList);
    }}
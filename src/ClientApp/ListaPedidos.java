package ClientApp;
import ServerApp.ListaEnlazadaView;
import MasterApp.Pedido;
import ServerApp.NodeLista;
import javafx.scene.control.ListView;

public class ListaPedidos extends ListaEnlazadaView<Pedido> {
    private ListView<Pedido> listViewPedidos;

    public ListaPedidos() {
        super();
        listViewPedidos = new ListView<>();
    }

    public ListView<Pedido> getListViewPedidos() {
        return listViewPedidos;
    }

    @Override
    public void add(Pedido data) {
        super.add(data);
        updateListViewPedidos();
    }

    @Override
    public NodeLista<Pedido> deleteFirst() {
        NodeLista<Pedido> deletedNode = super.deleteFirst();
        updateListViewPedidos();
        return deletedNode;
    }

    @Override
    public NodeLista<Pedido> delete(Object searchValue) {
        NodeLista<Pedido> deletedNode = super.delete(searchValue);
        updateListViewPedidos();
        return deletedNode;
    }

    private void updateListViewPedidos() {
        listViewPedidos.getItems().clear();
        forEach(pedido -> listViewPedidos.getItems().add(pedido));
    }
}

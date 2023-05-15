package ClientApp;
import ServerApp.ListaEnlazadaView;
import MasterApp.Pedido;
import ServerApp.NodeLista;
import javafx.scene.control.ListView;

/**
 * @authors Randall Bryan Bolañoz López, Octavio Sanchez Soto, Emanuel Chavarría Hernández.
 * @version 1.0
 */

/**
 * Esta clase publica ListaPedidos la cual tiene herencia de la clase ListaEnlazadaView, esta relacianda a contener la información de los pedidos de una mas comodo y comprimida.
 */
public class ListaPedidos extends ListaEnlazadaView<Pedido> {
   //Parametro y objeto necesario para el funcionamiento de esta clase. 
    private ListView<Pedido> listViewPedidos;
    
    
    /**
     * Utilizado, para obtener la lista de pedidos.
     */
    public ListaPedidos() {
        super();
        listViewPedidos = new ListView<>();
    }
    
    
    /**
     * Esta "ListaView" permite observar la listaPedidos mediante otra clase, de una forma facil de leer por el programador y usuario.
     * @return se retorna la lista como tal.
     */
    public ListView<Pedido> getListViewPedidos() {
        return listViewPedidos;
    }
    
    
    /**
     * Este método implementado mediante sobrelectura permite añadir pedidos, a la lista como tal
     * @param data es el dato que se va añadir a la lista, neceasario para el funcionamiento del método.
     */
    @Override
    public void add(Pedido data) {
        super.add(data);
        updateListViewPedidos();
    }
    
    
    /**
     * El nodo de la lista pedidos, de forma publica, utilizada para eliminar el primer dato de dicha lista.
     * @return se retorna el nodo que se va a eliminar de dicha lista.
     */
    @Override
    public NodeLista<Pedido> deleteFirst() {
        NodeLista<Pedido> deletedNode = super.deleteFirst();
        updateListViewPedidos();
        return deletedNode;
    }
    
    
    /**
     * El nodo de la lista pedidos, de forma publica, utilizada para eliminar cualquier dato de dicha lista.
     * @param searchValue este parametro es necesario para saber que dato se debe buscar y si se encuentra ser eliminado.
     * @return se retorna el nodo a por eliminar.
     */
    @Override
    public NodeLista<Pedido> delete(Object searchValue) {
        NodeLista<Pedido> deletedNode = super.delete(searchValue);
        updateListViewPedidos();
        return deletedNode;
    }
    
    
    /**
     * Método utilizada para actualizar la listaViewPedidos como tal, y poder notar sus cambios.
     */
    private void updateListViewPedidos() {
        listViewPedidos.getItems().clear();
        forEach(pedido -> listViewPedidos.getItems().add(pedido));
    }
}

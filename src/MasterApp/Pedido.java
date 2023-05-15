package MasterApp;

import java.io.Serializable;
import ServerApp.ListaEnlazada;

/**
 * @authors Randall Bryan Bolañoz López, Octavio Sanchez Soto, Emanuel Chavarría Hernández.
 * @version 1.0
 */


/**
 * Esta clase publica, la cual implementa ser Serializable para poder ser enviada por Sockets, permite contener la del pedido relacionada a los clientes de una manera mas comoda.
 */
public class Pedido implements Serializable {
    private int id;
    private ListaEnlazada<Platillos> platillos;
    private int tiempoTotal;
    private int progreso;
    private String estado;


/**
 * El público de pedidos, recibe una listaEnlazada de platillos, la cual obtiene todos los datos necesesarios para los pedidos.
 * @param platillos es la información de los platillos en formato ListaEnlazada.
 */    
public Pedido(ListaEnlazada<Platillos> platillos) {
    this.id = 0; // Establecer un ID predeterminado. Ajusta esto según tus requerimientos.
    this.platillos = platillos;
    tiempoTotal = 0;
    progreso = 0;
    this.estado = "En preparación";
    
    platillos.forEach(platillo -> {
        tiempoTotal += platillo.getTiempoPreparacion();
    });
}

    /**
     * Esta string pública permite pasar la información reciba a String, para ser mas amigable con el programador y usuario.
     * @return retorna los datos en formato String.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID Pedido: ").append(id).append(" - Platillos:\n");

        platillos.forEach(platillo -> {
            sb.append("\t").append(platillo.getNombre()).append('\n');
        });

        return sb.toString();
    }
    
    
    /**
     * getId es un int público el cual retorna la información del id del pedido
     * @return el int del id.
     */
    public int getId() {
        return id;
    }

    
    /**
     * La ListaEnlazada publica de platillos, getPlatillos, permite obtener la información general de los platillos obtenidos en la listaEnlazada para los pedidos
     * @return returna los platillos dentro de esta listaEnlazada
     */
    public ListaEnlazada<Platillos> getPlatillos() {
        return platillos;
    }
    
    
    /**
     * Es método tiene la funcionalidad de agregar platillos a los pedidos con toda su información necesaria.
     * @param platillo  este parametro, requiero un objeto Platillos, para poder mantener el funcionamiento del método.
     */
    public void agregarPlatillo(Platillos platillo) {
        platillos.add(platillo);
        tiempoTotal += platillo.getTiempoPreparacion();
    }
    
    
    /**
     * Este int public, permite obtener la información del tiempo de preparación asociado a cada platillo.
     * @return el int del tiempo de prepación.
     */
    public int getTiempoTotal() {
        return tiempoTotal;
    }
    
    
    /**
     * Este método perimte observar el progreso de preparación de cada pedido.
     * @param progreso recibe un int, de forma de parametro para el funcionamiento del método.
     */
    public void setProgreso(int progreso) {
        this.progreso = progreso;
    }
    
    
    /**
     * Este int público permite obtener la información de progreso relacionado a cada pedido.
     * @return el dato obtenido del progreso.
     */
    public int getProgreso() {
        return this.progreso;
    }
    
    
    /**
     *  Este String público permite obtener el estado acutal del pedido. 
     * @return el estado actual como tal.
     */
    public String getEstado() {
        return estado;
    }
    
    
    /**
     * Este método permite colocar un estado especifico a cada pedido
     * @param estado devuevl el estado agregado.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
}

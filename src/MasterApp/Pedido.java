package MasterApp;

import java.io.Serializable;
import ServerApp.ListaEnlazada;

public class Pedido implements Serializable {
    private int id;
    private ListaEnlazada<Platillos> platillos;
    private int tiempoTotal;
    private int progreso;
    private String estado;
    
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
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID Pedido: ").append(id).append(" - Platillos:\n");

        platillos.forEach(platillo -> {
            sb.append("\t").append(platillo.getNombre()).append('\n');
        });

        return sb.toString();
    }

    public int getId() {
        return id;
    }

    public ListaEnlazada<Platillos> getPlatillos() {
        return platillos;
    }

    public void agregarPlatillo(Platillos platillo) {
        platillos.add(platillo);
        tiempoTotal += platillo.getTiempoPreparacion();
    }

    public int getTiempoTotal() {
        return tiempoTotal;
    }

    public void setProgreso(int progreso) {
        this.progreso = progreso;
    }

    public int getProgreso() {
        return this.progreso;
    }
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}

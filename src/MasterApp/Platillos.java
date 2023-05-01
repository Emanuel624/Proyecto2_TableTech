package MasterApp;

import java.io.Serializable;

public class Platillos implements Serializable {
    private String nombre;
    private int cantidadCalorias;
    private int tiempoPreparacion;
    private int precio;

    public Platillos (String nombre, int cantidadCalorias, int tiempoPreparacion, int precio){
        this.nombre = nombre;
        this.cantidadCalorias = cantidadCalorias;
        this.tiempoPreparacion = tiempoPreparacion;
        this.precio = precio;
    }
    public void setNombre(String a){
        nombre = a;
    }
    public void setCantidadCalorias(int b){
        cantidadCalorias = b;
    }
    public void setTiempoPreparacion (int c){
        tiempoPreparacion = c;
    }
    public void setPrecio (int d){
        precio = d;
    }
    public String getNombre (){
        return this.nombre;
    }
    public int getCantidadCalorias (){
        return this.cantidadCalorias;
    }
    public int getTiempoPreparacion(){
        return this.tiempoPreparacion;
    }
    public int getPrecio (){
        return this.precio;
    }
}

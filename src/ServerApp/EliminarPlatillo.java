/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerApp;

import java.io.Serializable;

public class EliminarPlatillo implements Serializable {
    private String nombre;
    private int cantidadCalorias;
    private int tiempoPreparacion;
    private int precio;

    public EliminarPlatillo (String nombre, int cantidadCalorias, int tiempoPreparacion, int precio){
        this.nombre = nombre;
        this.cantidadCalorias = cantidadCalorias;
        this.tiempoPreparacion = tiempoPreparacion;
        this.precio = precio;
    }
    //Hola
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
    @Override
    public String toString() {
    return this.nombre;
    }
}

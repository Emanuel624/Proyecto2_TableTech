/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerApp;

import java.io.Serializable;

/**
 * @authors Randall Bryan Bolañoz López, Octavio Sanchez Soto, Emanuel Chavarría Hernández.
 * @version 1.0
 */

/**
 * Esta clase publica, la cual implementa ser Serializable para poder ser enviada por Sockets, permite contener la información relacionada con la data de los platillos a eliminar de una manera mas comoda.
 */
public class EliminarPlatillo implements Serializable {
    private String nombre;
    private int cantidadCalorias;
    private int tiempoPreparacion;
    private int precio;
    
    
    /**
     * El publico de la clase EliminarPlatillo permite obtener los datos necesarios para tener un objeto EliminarPlatillo.
     * @param nombre parametro en formato String con el nombre del platillo.
     * @param cantidadCalorias parametro en formato int, con la cantidad de calorias del platillo.
     * @param tiempoPreparacion parametro en formato int, con el tiempo de preparación del platillo.
     * @param precio parametro en formato int, con el precio del platillo. 
     */
    public EliminarPlatillo (String nombre, int cantidadCalorias, int tiempoPreparacion, int precio){
        this.nombre = nombre;
        this.cantidadCalorias = cantidadCalorias;
        this.tiempoPreparacion = tiempoPreparacion;
        this.precio = precio;
    }

    
    /**
     * Método que se encarga de colocar un nuevo nombre al platillo por eliminar.
     * @param a parametro String con el nuevo nombre por eliminar.
     */
    public void setNombre(String a){
        nombre = a;
    }
    
    
    /**
     * Método que se encarga de colocar una nueva cantidad de calorias al platillo por eliminar.
     * @param b parametro int con la nueva cantidad de calorias al platillo por eliminar.
     */
    public void setCantidadCalorias(int b){
        cantidadCalorias = b;
    }
    
    
    /**
     * Método que se encarga de colocar un nuevo tiempo de preración al platillo por eliminar.
     * @param c parametro int con el nuevo tiempo de preración al platillo por eliminar.
     */
    public void setTiempoPreparacion (int c){
        tiempoPreparacion = c;
    }
    
    
    /**
     * Método que se encarga de colocar un nuevo precio al platillo por eliminar.
     * @param d parametro int con el precio al platillo por eliminar.
     */
    public void setPrecio (int d){
        precio = d;
    }
    
    
    /**
     * String públic que permite obtener el valor del nombre del platillo por eliminar de una cadena de texto.
     * @return el valor del nombre obtenido.
     */
    public String getNombre (){
        return this.nombre;
    }
    
    
    /**
     * Int públic que permite obtener el valor de las calorias del platillo por eliminar de una cadena de texto.
     * @return el valor de las calorias obtenido. 
     */
    public int getCantidadCalorias (){
        return this.cantidadCalorias;
    }
    
    
    /**
     * Int públic que permite obtener el tiempo de preparación del platillo por eliminar de una cadena de texto.
     * @return el valor del tiempo de preparación obtenido. 
     */
    public int getTiempoPreparacion(){
        return this.tiempoPreparacion;
    }
    
    
    /**
     * Int públic que permite obtener el precio del platillo por eliminar de una cadena de texto.
     * @return el valor del precio obtenido.
     */
    public int getPrecio (){
        return this.precio;
    }
    
    
    /**
     * String publica que permite obser la información de la clase en formato String, lo cual es mas amigable con el usuario y programador.
     * @return los datos en formato String.
     */
    @Override
    public String toString() {
    return this.nombre;
    }
}

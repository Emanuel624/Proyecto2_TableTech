package MasterApp;

import java.io.Serializable;

/**
 * @authors Randall Bryan Bolañoz López, Octavio Sanchez Soto, Emanuel Chavarría Hernández.
 * @version 1.0
 */


/**
 * Esta clase publica, la cual implementa ser Serializable para poder ser enviada por Sockets, permite contenerlos platillos relacionada a los clientes de una manera mas comoda.
 */
public class Platillos implements Serializable {
    //Variables y objetos necesarios para el funcionamiento de la clase.
    private String nombre;
    private int cantidadCalorias;
    private int tiempoPreparacion;
    private int precio;
    
    
    /**
     * El público de los platillos consta de la implementación publica de la clase como tal, permite retener la información total de cada platillo.
     * @param nombre parametro en formato String del nombre del platillo.
     * @param cantidadCalorias paramentro en formato int del número de calorias.
     * @param tiempoPreparacion parametro en formato int del tiempo de preparaicón para el platillo.
     * @param precio parámetro en formato int del precio del platillo.
     */
    public Platillos (String nombre, int cantidadCalorias, int tiempoPreparacion, int precio){
        this.nombre = nombre;
        this.cantidadCalorias = cantidadCalorias;
        this.tiempoPreparacion = tiempoPreparacion;
        this.precio = precio;
    }
    
    
    /**
     * Método que coloca un nuevo nombre al platillo.
     * @param a String del nuevo nombre por colocar  al platillo.
     */
    public void setNombre(String a){
        nombre = a;
    }
    
    
    /**
     * Método que coloca una nueva cantidad de calorias al platillo.
     * @param b parametro en formato int, con la cantaidad de calorias por colocar.
     */
    public void setCantidadCalorias(int b){
        cantidadCalorias = b;
    }
    
    
    /**
     * Método que coloca una nueva cantidad de tiempo de preparación al platillo.
     * @param c parametro en formato int, con la nueva canitdad de tiempo por colocar.
     */
    public void setTiempoPreparacion (int c){
        tiempoPreparacion = c;
    }
    
    
    /**
     * Método que coloca una nueva cantidad de precio al platillo.
     * @param d parametro en formato int, con la nueva canitdad de precio por colocar.
     */
    public void setPrecio (int d){
        precio = d;
    }
    
    
    /**
     * String publica que obtiene el nombre, del platillo en cuestión de una cadena de texto.
     * @return retorna el nombre del platillo como tal.
     */
    public String getNombre (){
        return this.nombre;
    }
    
    
    /**
     * Int público que obtiene la cantidad de calorias, del platillo en cuestión de una cadena de ints.
     * @return retorna la cantidad de calorias del platillo como tal.
     */
    public int getCantidadCalorias (){
        return this.cantidadCalorias;
    }
    
    
    /**
     * Int público que obtiene el tiempo de preparación, del platillo en cuestión de una cadena de ints.
     * @return retorna el tiempo de preparación del  platillo como tal.
     */
    public int getTiempoPreparacion(){
        return this.tiempoPreparacion;
    }
    
    
    /**
     * Int público que obtiene el precio, del platillo en cuestión de una cadena de ints.
     * @return retorna el precio del  platillo como tal.
     */
    public int getPrecio (){
        return this.precio;
    }
    
    
    /**
     * String publica que permite pasar a formato String, la información de esta clase, para ser mas amigable con el usuario y programador.
     * @return retorna el elemento en formato String como tal
     */
    @Override
    public String toString() {
    return this.nombre;
    }
}

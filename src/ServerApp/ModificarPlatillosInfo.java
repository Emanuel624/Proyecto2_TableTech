
package ServerApp;

import java.io.Serializable;

/**
 * @authors Randall Bryan Bolañoz López, Octavio Sanchez Soto, Emanuel Chavarría Hernández.
 * @version 1.0
 */

/**
 * Esta clase publica, la cual implementa ser Serializable para poder ser enviada por Sockets, permite contener la información relacionada con la data de los administradores por modificar de una manera mas comoda.
 */
public class ModificarPlatillosInfo implements Serializable {
    private String PlatilloNoModNombre;
    private int PlatilloNoModCalorias;
    private int PlatilloNoModTiempo;
    private int PlatilloNoModPrecio;
    
    private String nombre;
    private int caloriasInt;
    private int preparacionInt;
    private int precioInt;
    
    
    /**
     * Este es el contrusctor de esta clase.
     * @param PlatilloNoModNombre String que contiene el nombre del platillo por modificar.
     * @param PlatilloNoModCalorias int que contiene las calorias del platillo por modificar.
     * @param PlatilloNoModTiempo int que contiene el tiempo de preparación del platillo por modificar.
     * @param PlatilloNoModPrecio int que contiene el precio del platillo por modificar.
     * @param nombre String que contiene el nuevo nombre del platillo.
     * @param caloriasInt int que contien el nuevo valor de calorias del platillo.
     * @param preparacionInt int que contiene el nuevo tiempo de preparación del platillo.
     * @param precioInt int que contiene el nuevo precio del platillo.
     */
    public ModificarPlatillosInfo(String PlatilloNoModNombre, int PlatilloNoModCalorias, int PlatilloNoModTiempo, int PlatilloNoModPrecio, String nombre, int caloriasInt, int preparacionInt, int precioInt) {
        this.PlatilloNoModNombre = PlatilloNoModNombre;
        this.PlatilloNoModCalorias = PlatilloNoModCalorias;
        this.PlatilloNoModTiempo = PlatilloNoModTiempo;
        this.PlatilloNoModPrecio = PlatilloNoModPrecio;
        
        
        this.nombre = nombre;
        this.caloriasInt = caloriasInt;
        this.preparacionInt = preparacionInt;
        this.precioInt = precioInt;
    }
    
    //Metodos para los datos por modificar
    /**
     * String pública para obtener el nombre del platillo sin modificar.
     * @return retorna el nombre del platillo sin modificar.
     */
    public String getNombre() {
        return PlatilloNoModNombre;
    }
    
    
    /**
     * Int público que obtiene el dato de las calorias del platillo sin modificar.
     * @return retorna las calorias del platillo sin modificar.
     */
    public int getCalorias() {
        return PlatilloNoModCalorias;
    }
    
     
    /**
     * Int público que obtiene el dato del tiempo de preparación del platillo sin modificar.
     * @return retorna el tiempo de preración del platillo sin modificar.
     */
    public int getTiempo() {
        return PlatilloNoModTiempo;
    }
    
    
    /**
     * Int público que obtiene el dato del precio del platillo sin modificar.
     * @return retorna el precio del platillo sin modificar.
     */
    public int getPrecio() {
        return PlatilloNoModPrecio;
    }
    
    
    //Metodos para los datos sin modificar
    /**
     * String pública para obtener el nombre del platillo modificado.
     * @return retorna el nombre del platillo modificado.
     */
    public String getNombreMod() {
        return nombre;
    }
    
    
    /**
     * Int público que obtiene el dato de las calorias del platillo modificado.
     * @return retorna las calorias del platillo modificado.
     */
    public int getCaloriasMod() {
        return caloriasInt;
    }
    
    
    /**
     * Int público que obtiene el dato del tiempo de preparación del platillo modificado.
     * @return retorna el tiempo de preración del platillo modificado.
     */
    public int getTiempoMod() {
        return preparacionInt;
    }

    
    /**
     * Int público que obtiene el dato del precio del platillo modificado.
     * @return retorna el precio del platillo modificado.
     */
    public int getPrecioMod() {
        return precioInt;
    }
}
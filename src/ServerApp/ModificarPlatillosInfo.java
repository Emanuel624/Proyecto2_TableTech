
package ServerApp;

import java.io.Serializable;

public class ModificarPlatillosInfo implements Serializable {
    private String PlatilloNoModNombre;
    private int PlatilloNoModCalorias;
    private int PlatilloNoModTiempo;
    private int PlatilloNoModPrecio;
    
    private String nombre;
    private int caloriasInt;
    private int preparacionInt;
    private int precioInt;
    
    
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
    public String getNombre() {
        return PlatilloNoModNombre;
    }
    
    public int getCalorias() {
        return PlatilloNoModCalorias;
    }
    
    public int getTiempo() {
        return PlatilloNoModTiempo;
    }

    public int getPrecio() {
        return PlatilloNoModPrecio;
    }
    
    
    //Metodos para los datos sin modificar
    public String getNombreMod() {
        return nombre;
    }
    
    public int getCaloriasMod() {
        return caloriasInt;
    }
    
    public int getTiempoMod() {
        return preparacionInt;
    }

    public int getPrecioMod() {
        return precioInt;
    }
}

package ClientApp;

import java.io.Serializable;
import MasterApp.Platillos;

/**
 * @authors Randall Bryan Bolañoz López, Octavio Sanchez Soto, Emanuel Chavarría Hernández.
 * @version 1.0
 */

/**
 * Esta clase publica, la cual implementa ser Serializable para poder ser enviada por Sockets, permite contener la información relacionada a los clientes de una manera mas comoda.
 */
public class Cliente implements Serializable {
    //Varialbles y objetos utilizados a lo largo de esta clase.
    private static final long serialVersionUID = 1L;

    private String username;
    private String contrasena;
    private Platillos platilloSeleccionado;
    
    
    /**
     * Este Cliente publico, permite obtener la información total relacionada a un cliente como tal, para mantenerse de una forma comprimida y concisa  
     * @param username este parametro se encarga de recibir la información del nombre de usuario, relacionado especificamente con el cliente para ser utilizado en el futuro.
     * @param contrasena este parametro se encarga de recibir la información dea la contraseña de usuario, relacionado especificamente con el cliente para ser utilizado en el futuro.
     */
    public Cliente(String username, String contrasena) {
        this.username = username;
        this.contrasena = contrasena;
    }
    
    
    /**
     * Esta string púbica permite obtener la información relacionada al username de los usarios
     * @return se retorn el valor del nombre de usuario de la string en cuestión.
     */
    public String getUsername() {
        return username;
    }
    
    
    /**
     * Método utilizado para colocar un nuevo username, mediante una string recibida.
     * @param username este parametro recibe una cadena de texto (String) tomada como nuevo username a colocar.
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    
    /**
     * String pública utilizada para obtener la contraseña de usuario de una cadena de texto String
     * @return la contraseña obtenida de dicha cadena de texto.
     */
    public String getContrasena() {
        return contrasena;
    }
    
    
    /**
     * Método utilizado para colocar una nueva contraseña, mediante una string recibida.
     * @param contrasena este parametro recibe una cadena de texto (String) tomada como nueva contraseña a colocar.
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    
    
    /**
     * Parametro de Platillos públicos utilizado para obtener el platillo seleccionado por determinado cliente.
     * @return el platillo seleccionado como tal.
     */
    public Platillos getPlatilloSeleccionado() {
        return platilloSeleccionado;
    }
    
    
    /**
     * Este método permite colocar un nuevo platillo seleccionado relacionado con su usuario.
     * @param platilloSeleccionado el parametro recibe un objeto platillo.
     */
    public void setPlatilloSeleccionado(Platillos platilloSeleccionado) {
        this.platilloSeleccionado = platilloSeleccionado;
    }
}

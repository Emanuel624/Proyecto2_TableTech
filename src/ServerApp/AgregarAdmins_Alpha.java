
package ServerApp;

import java.io.Serializable;

/**
 * @authors Randall Bryan Bolañoz López, Octavio Sanchez Soto, Emanuel Chavarría Hernández.
 * @version 1.0
 */

/**
 * Esta clase publica, la cual implementa ser Serializable para poder ser enviada por Sockets, permite contener la información relacionada con la data de los administradores por agregar de una manera mas comoda.
 */
public class AgregarAdmins_Alpha implements Serializable {
    private String username;
    private String contrasena;

    
        
    /**
     * El publico de la clase AgregarAdmins permite obtener los datos necesarios para tener un objeto AgregarAdmins.
     * @param username parametro String, asociado directamente con el nombre de usuarios por agregar.
     * @param contrasena parametro String, asociado directamente con la contraseña de usuarios por agregar.
     */
    public AgregarAdmins_Alpha(String username, String contrasena) {
        this.username = username;
        this.contrasena = contrasena;
    }
    
    
        
    /**
     * String publica que permite obtener el nombre del usuario por agregar de una cadena de texto.
     * @return el nombre de usuario por agregar como tal.
     */
    public String getUsername() {
        return username;
    }
    
    
        
    /**
     * String publica que permite obtener la contraseña del usuario por agregar de una cadena de texto.
     * @return la contraseña del usuario por agregar como tal.
     */
    public String getContrasena() {
        return contrasena;
    }
}

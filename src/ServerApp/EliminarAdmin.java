
package ServerApp;

import java.io.Serializable;

/**
 * @authors Randall Bryan Bolañoz López, Octavio Sanchez Soto, Emanuel Chavarría Hernández.
 * @version 1.0
 */

/**
 * Esta clase publica, la cual implementa ser Serializable para poder ser enviada por Sockets, permite contener la información relacionada con la data de los administradores por eliminar de una manera mas comoda.
 */
public class EliminarAdmin implements Serializable {
    private String username;
    private String contrasena;
    
    
    /**
     * El publico de la clase EliminarAdmin permite obtener los datos necesarios para tener un objeto administrador por eliminar.
     * @param username parametro String, asociado directamente con el nombre de usuarios administradores por eliminar.
     * @param contrasena parametro String, asociado directamente con la contraseña de usuarios administradores por eliminar.
     */
    public EliminarAdmin(String username, String contrasena) {
        this.username = username;
        this.contrasena = contrasena;
    }
    
    
    /**
     * String publica que permite obtener el nombre del usuario del administrador por eliminar de una cadena de texto.
     * @return el nombre de usuario administrador por eliminar como tal.
     */
    public String getUsername() {
        return username;
    }
    
    
    /**
     * String publica que permite obtener la contraseña del usuario administrador por eliminar de una cadena de texto.
     * @return la contraseña del el usuario administrador por eliminar como tal.
     */
    public String getContrasena() {
        return contrasena;
    }
    
    
    /**
     * Método que se encarga de colocar un nuevo nombre del usuario administrador por eliminar al usuario.
     * @param a parámetro en formato String que coloca el nuevo nombre de usuario al administrador por eliminar. 
     */
    public void setUsername (String a){
        username = a;
    }
    
    
    /**
     * Método que se encarga de colocar una nueva contraseñña al usuario administrador por eliminar.
     * @param b parámetro en formato String que coloca la nueva contraseña al usuarioa dministrador por eliminar.
     */
    public void setContrasena (String b){
        contrasena = b;
    }
}


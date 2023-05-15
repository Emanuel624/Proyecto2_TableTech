
package ServerApp;

import java.io.Serializable;

/**
 * @authors Randall Bryan Bolañoz López, Octavio Sanchez Soto, Emanuel Chavarría Hernández.
 * @version 1.0
 */

/**
 * Esta clase publica, la cual implementa ser Serializable para poder ser enviada por Sockets, permite contener la información relacionada con la data de los administradores de una manera mas comoda.
 */
public class Administrador implements Serializable {
    private String username;
    private String contrasena;
    
    
    /**
     * El publico de la clase administrador permite obtener los datos necesarios para tener un objeto administrador.
     * @param username parametro String, asociado directamente con el nombre de usuarios administradores.
     * @param contrasena parametro String, asociado directamente con la contraseña de usuarios administradores.
     */
    public Administrador(String username, String contrasena) {
        this.username = username;
        this.contrasena = contrasena;
    }
    
    
    /**
     * String publica que permite obtener el nombre del usuario de una cadena de texto.
     * @return el nombre de usuario como tal.
     */
    public String getUsername() {
        return username;
    }
    
    
    /**
     * String publica que permite obtener la contraseña de una cadena de texto.
     * @return la contraseña del usuario como tal.
     */
    public String getContrasena() {
        return contrasena;
    }
    
    
    /**
     * Método que se encarga de colocar un nuevo nombre del usuario al usuario.
     * @param a parámetro en formato String que coloca el nuevo nombre de usuario.
     */
    public void setUsername (String a){
        username = a;
    }
    
    
    /**
     * Método que se encarga de colocar una nueva contraseñña al usuario.
     * @param b parámetro en formato String que coloca la nueva contraseña al usuario.
     */
    public void setContrasena (String b){
        contrasena = b;
    }
}

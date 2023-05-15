
package ServerApp;

import java.io.Serializable;

/**
 * @authors Randall Bryan Bolañoz López, Octavio Sanchez Soto, Emanuel Chavarría Hernández.
 * @version 1.0
 */

/**
 * Esta clase publica, la cual implementa ser Serializable para poder ser enviada por Sockets, permite contener la información relacionada con la data de los administradores por modificar de una manera mas comoda.
 */
public class ModificarAdminInfo implements Serializable {
    private String adminNoModNombre;
    private String adminNoModContraseña;
    private String username;
    private String password;
    
    
    /**
     * El constructor de la clase ModificarAdminInfo.
     * @param adminNoModNombre String que se relaciona al nombre de usuario del administrador que se quiere modificar.
     * @param adminNoModContraseña String que se relaciona a la contraseña de usuario del administrador que se quiere modificar.
     * @param username String que contiene el nuevo nombre de usuario.
     * @param password String que contiene la nueva contraseña del administrador modificado.
     */
    public ModificarAdminInfo(String adminNoModNombre, String adminNoModContraseña, String username, String password) {
        this.adminNoModNombre = adminNoModNombre;
        this.adminNoModContraseña = adminNoModContraseña;
        this.username = username;
        this.password = password;
    }
    
    
    /**
     * Esta String publica, permite obtener el nombre de usuario del administrador.
     * @return el nombre del usuario que quiere ser modificado
     */
    public String getUsername() {
        return adminNoModNombre;
    }
    
    
    /**
     * Esta String publica, permite obtener la contraseña del administrador.
     * @return la contraseña del usuario que se quiere modificar.
     */
    public String getContrasena() {
        return adminNoModContraseña;
    }
    
    
    /**
     * Esta String publica, permite obtener el nuevo nombre de usuario.
     * @return se retorna el nuevo nombre de usuario modificado.
     */
    public String getUsernameMod() {
        return username;
    }
    
    
    /**
     * Esta String publica, permite obtener la nueva contraseña del administrador.
     * @return se retorna la nueva contraseña del administrador modificado.
     */
    public String getContrasenaMod() {
        return password;
    }

}

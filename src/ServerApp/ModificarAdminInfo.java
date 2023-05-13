/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerApp;

import java.io.Serializable;

public class ModificarAdminInfo implements Serializable {
    private String adminNoModNombre;
    private String adminNoModContraseña;
    private String username;
    private String password;

    public ModificarAdminInfo(String adminNoModNombre, String adminNoModContraseña, String username, String password) {
        this.adminNoModNombre = adminNoModNombre;
        this.adminNoModContraseña = adminNoModContraseña;
        this.username = username;
        this.password = password;
    }
    
    public String getUsername() {
        return adminNoModNombre;
    }

    public String getContrasena() {
        return adminNoModContraseña;
    }
    
    public String getUsernameMod() {
        return username;
    }

    public String getContrasenaMod() {
        return password;
    }

}

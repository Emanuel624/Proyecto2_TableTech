
package ServerApp;

import java.io.Serializable;

public class Administrador implements Serializable {
    private String username;
    private String contrasena;

    public Administrador(String username, String contrasena) {
        this.username = username;
        this.contrasena = contrasena;
    }

    public String getUsername() {
        return username;
    }

    public String getContrasena() {
        return contrasena;
    }

}


package ServerApp;

import java.io.Serializable;

public class AgregarAdmins_Alpha implements Serializable {
    private String username;
    private String contrasena;

    public AgregarAdmins_Alpha(String username, String contrasena) {
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

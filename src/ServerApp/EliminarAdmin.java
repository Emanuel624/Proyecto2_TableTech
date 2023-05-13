
package ServerApp;



import java.io.Serializable;

public class EliminarAdmin implements Serializable {
    private String username;
    private String contrasena;

    public EliminarAdmin(String username, String contrasena) {
        this.username = username;
        this.contrasena = contrasena;
    }

    public String getUsername() {
        return username;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setUsername (String a){
        username = a;
    }
    public void setContrasena (String b){
        contrasena = b;
    }
}


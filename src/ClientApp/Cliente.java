
package ClientApp;

import java.io.Serializable;
import MasterApp.Platillos;

public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String contrasena;
    private Platillos platilloSeleccionado;

    public Cliente(String username, String contrasena) {
        this.username = username;
        this.contrasena = contrasena;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    public Platillos getPlatilloSeleccionado() {
        return platilloSeleccionado;
    }

    public void setPlatilloSeleccionado(Platillos platilloSeleccionado) {
        this.platilloSeleccionado = platilloSeleccionado;
    }
}

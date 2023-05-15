package ServerApp;
import java.io.Serializable;

/**
 * @authors Randall Bryan Bolañoz López, Octavio Sanchez Soto, Emanuel Chavarría Hernández.
 * @version 1.0
 */

/**
 * Esta clase publica, la cual implementa ser Serializable para poder ser enviada por Sockets, permite contener la información relacionada con la data de los administradores de una manera mas comoda.
 */
public class AdminData implements Serializable {
    private String name;
    private String username;
    private String password;
    // otros atributos y métodos
}


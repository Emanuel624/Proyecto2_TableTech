/**
 * Basicamente esta clase es la "Clase cliente" en la relación cliente/servidor de los sockets
 */
package ClientApp;

import ServerApp.Administrador;
import java.io.IOException;
import java.net.Socket;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ClientApp.Cliente;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javafx.application.Platform;


public class Client_Beta extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // Crear los nodos necesarios
        Label lblTitle = new Label("Inicio de Sesión Clientes");
        Label lblUser = new Label("Usuario:");
        Label lblPass = new Label("Contraseña:");
        TextField txtUser = new TextField();
        PasswordField txtPass = new PasswordField();
        Button btnLogin = new Button("Iniciar Sesión");

        // Crear un contenedor para los nodos de usuario
        HBox hboxUser = new HBox(lblUser, txtUser);
        hboxUser.setSpacing(15);
        hboxUser.setAlignment(Pos.CENTER);

        // Crear un contenedor para los nodos de contraseña
        HBox hboxPass = new HBox(lblPass, txtPass);
        hboxPass.setSpacing(10);
        hboxPass.setAlignment(Pos.CENTER);

        // Crear un contenedor para los botones
        HBox hboxButtons = new HBox(btnLogin);
        hboxButtons.setSpacing(10);
        hboxButtons.setAlignment(Pos.CENTER);

        // Crear un contenedor para todos los nodos
        VBox vbox = new VBox(lblTitle, hboxUser, hboxPass, hboxButtons);
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        // Crear la escena y agregarla al escenario
        Scene scene = new Scene(vbox, 400, 300);
        stage.setScene(scene);
        Socket socket = new Socket("localhost", 8080);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

        // Agregar manejador de eventos al botón Iniciar Sesión
        btnLogin.setOnAction(event -> {
            try{
            String username = txtUser.getText();
            String contrasena = txtPass.getText();
            Cliente cliente = new Cliente(username, contrasena);
            out.writeObject(cliente);
            out.flush();
        } catch (IOException ex) {
                System.err.println("Error sending login info to server: " + ex.getMessage());
            }
        });
            Thread thread = new Thread(() -> {
            try {
                // Crear el stream de entrada para recibir la respuesta del servidor
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                // Leer la respuesta del servidor y mostrarla en la consola
                while(true){
                boolean loginSuccess = (boolean) in.readObject();
                Platform.runLater(() -> {
                if (loginSuccess) {
                    System.out.println("Inicio de sesión exitoso");
                    // Aquí puedes agregar el código para manejar un inicio de sesión exitoso
                } else {
                    System.out.println("Inicio de sesión fallido");
                    // Aquí puedes agregar el código para manejar un inicio de sesión fallido
                }});

               
            }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
            
        thread.start();
        stage.show();
    }
               

    public static void main(String[] args) {
        launch(args);
    }
}
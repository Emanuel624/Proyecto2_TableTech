package MasterApp;

import ServerApp.Administrador;
import java.io.IOException;
import java.io.ObjectOutputStream;
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

public class Admin_Beta extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // Crear los nodos necesarios
        Label lblTitle = new Label("Inicio de Sesión Administradores");
        Label lblUser = new Label("Usuario:");
        Label lblPass = new Label("Contraseña:");
        TextField txtUser = new TextField();
        PasswordField txtPass = new PasswordField();
        Button btnLogin = new Button("Iniciar Sesión");
        Button btnRegister = new Button("Registrarse");

        // Crear un contenedor para los nodos de usuario
        HBox hboxUser = new HBox(lblUser, txtUser);
        hboxUser.setSpacing(10);
        hboxUser.setAlignment(Pos.CENTER);

        // Crear un contenedor para los nodos de contraseña
        HBox hboxPass = new HBox(lblPass, txtPass);
        hboxPass.setSpacing(10);
        hboxPass.setAlignment(Pos.CENTER);

        // Crear un contenedor para los botones
        HBox hboxButtons = new HBox(btnLogin, btnRegister);
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

        // Crear el socket para conectarse al servidor
        Socket socket = new Socket("localhost", 8080);

        // Crear el stream de salida para enviar la información al servidor
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

        // Agregar la acción al botón de inicio de sesión para enviar la información al servidor
        btnLogin.setOnAction(e -> {
            try {
                String username = txtUser.getText();
                String password = txtPass.getText();

                // Crear un objeto con la información de inicio de sesión y enviarlo al servidor
                Administrador loginInfo = new Administrador(username, password);
                out.writeObject(loginInfo);
                out.flush();

                // Limpiar los campos de usuario y contraseña
                txtUser.clear();
                txtPass.clear();
            } catch (IOException ex) {
                System.err.println("Error sending login info to server: " + ex.getMessage());
            }
        });

        // Mostrar la ventana
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

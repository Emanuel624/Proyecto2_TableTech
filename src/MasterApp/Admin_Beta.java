package MasterApp;

import ServerApp.Administrador;
import ServerApp.AgregarAdmins_Alpha;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
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
        TextField txtNombre = new TextField();
        TextField txtCalorias = new TextField();
        TextField txtPrecio = new TextField();
        TextField txtTiempo = new TextField();
        Label lblNombre = new Label("Nombre:");
        Label lblCalorias = new Label("Calorias:");
        Label lblTiempo = new Label("Tiempo:");
        Label lblPrecio = new Label("Precio");
        Button btnLogin = new Button("Iniciar Sesión");
        Button btnRegistrer = new Button("Registrate");
        Button btnAgregar = new Button("Agregar Platillo");



        HBox hboxNombre = new HBox(lblNombre, txtNombre);
        hboxNombre.setSpacing(15);
        hboxNombre.setAlignment(Pos.TOP_RIGHT);

        HBox hboxCalorias = new HBox(lblCalorias, txtCalorias);
        hboxCalorias.setSpacing(15);
        hboxCalorias.setAlignment(Pos.TOP_RIGHT);

        HBox hboxTiempo = new HBox(lblTiempo, txtTiempo);
        hboxTiempo.setSpacing(15);
        hboxTiempo.setAlignment(Pos.TOP_RIGHT);

        HBox hboxPrecio = new HBox(lblPrecio, txtPrecio);
        hboxPrecio.setSpacing(15);
        hboxPrecio.setAlignment(Pos.TOP_RIGHT);

        // Crear un contenedor para los nodos de usuario
        HBox hboxUser = new HBox(lblUser, txtUser);
        hboxUser.setSpacing(15);
        hboxUser.setAlignment(Pos.CENTER);

        // Crear un contenedor para los nodos de contraseña
        HBox hboxPass = new HBox(lblPass, txtPass);
        hboxPass.setSpacing(10);
        hboxPass.setAlignment(Pos.CENTER);

        // Crear un contenedor para los botones
        HBox hboxButtons = new HBox(btnLogin, btnRegistrer, btnAgregar);
        hboxButtons.setSpacing(10);
        hboxButtons.setAlignment(Pos.CENTER); 

        // Crear un contenedor para todos los nodos
        VBox vbox = new VBox(lblTitle, hboxUser, hboxPass, hboxButtons, hboxNombre, hboxCalorias, hboxTiempo, hboxPrecio);
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        // Crear la escena y agregarla al escenario
        Scene scene = new Scene(vbox, 400, 400);
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
       
        //¿?
        btnAgregar.setOnAction(event -> {
            try{
                String nombre =  txtNombre.getText();
                int calorias = Integer.parseInt(txtCalorias.getText());
                int tiempo = Integer.parseInt(txtTiempo.getText());
                int precio = Integer.parseInt(txtPrecio.getText());

                Platillos nuevoPlatillo =  new Platillos(nombre, calorias, tiempo, precio);
                out.writeObject(nuevoPlatillo);
                out.flush();

                txtNombre.clear();
                txtCalorias.clear();
                txtTiempo.clear();
                txtPrecio.clear();
            }catch (IOException e){
                e.printStackTrace();
            }

        });


        Thread thread = new Thread(() -> {
            try {
                // Crear el stream de entrada para recibir información del servidor
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                while (true) {
                    // Leer el objeto recibido del servidor
                    Object obj = in.readObject();

                    // Verificar si el objeto recibido es un mensaje de éxito de inicio de sesión
                    if (obj instanceof Boolean loginSuccess) {
                        if (loginSuccess) {
                            // Si el inicio de sesión fue exitoso, mostrar un mensaje de éxito y cerrar la ventana
                            Platform.runLater(() -> {
                                btnLogin.setText("Sesión iniciada");
                                btnLogin.setDisable(true);
                                txtUser.setPromptText("Añade Admins");
                                txtPass.setPromptText("Añade Admins");

                                //Acciones de boton registro
                                if(btnLogin.isDisabled()) {
                                    // Agregar la acción al botón de registro para enviar la información al servidor
                                    btnRegistrer.setOnAction(e -> {
                                        try {
                                            String username = txtUser.getText();
                                            String password = txtPass.getText();

                                            // Crear un objeto con la información de inicio de sesión y enviarlo al servidor
                                            AgregarAdmins_Alpha RegistrerInfo = new AgregarAdmins_Alpha(username, password);
                                            out.writeObject(RegistrerInfo);
                                            out.flush();

                                            // Limpiar los campos de usuario y contraseña
                                            txtUser.clear();
                                            txtPass.clear();
                                        } catch (IOException ex) {
                                            System.err.println("Error sending login info to server: " + ex.getMessage());
                                        }
                                    });
                                } else {
                                    //Hacer nada
                                }
                            });

                        } else {
                            // Si el inicio de sesión no fue exitoso, mostrar un mensaje de error
                            Platform.runLater(() -> {
                                txtUser.setPromptText("Usuario o contraseña incorrecta");
                                txtPass.setPromptText("Usuario o contraseña incorrecta");
                            });
                        }
                    }
                    // Si el objeto recibido es de otro tipo, hacer algo con él según sea necesario
                }


            } catch (IOException | ClassNotFoundException ex) {
                System.err.println("Error receiving data from server: " + ex.getMessage());
            }
        });

        // Iniciar el hilo
        thread.start();


        // Mostrar la ventana
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}


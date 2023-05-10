package MasterApp;

import ClientApp.Cliente;
import ServerApp.Administrador;
import ServerApp.AgregarAdmins_Alpha;
import ServerApp.BinaryTree;
import ServerApp.ListaEnlazada;
import ServerApp.ListaEnlazadaView;
import com.google.gson.Gson;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox; 
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Admin_Beta extends Application {
    private ObjectOutputStream out;
    private ObjectInputStream in;
      
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
        
        out = new ObjectOutputStream(socket.getOutputStream());
        
        in = new ObjectInputStream(socket.getInputStream());
        
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

                while (true) {
                    // Leer el objeto recibido del servidor
                    Object obj = in.readObject();

                    // Verificar si el objeto recibido es un mensaje de éxito de inicio de sesión
                    if (obj instanceof Boolean loginSuccess) {
                        if (loginSuccess) {
                            // Si el inicio de sesión fue exitoso, mostrar un mensaje de éxito y cerrar la ventana
                            Platform.runLater(() -> {
                                //Se llama al metodo con la otra cara de la GUI
                                Menu(stage);
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
    
    
    //Diversos metodos para el funcionamiento de la MasterApp
    private ListaEnlazadaView<Platillos> listViewPlatillos;
    private ListaEnlazadaView<Platillos> listViewCarrito;
    
    private Button btnAgregarAdmin;
    private Button btnModificarAdmin;
    private Button btnEliminarAdmin;
    
    private Button btnAgregarPlatillo;
    private Button btnModificarPlatillo;
    private Button btnEliminarPlatillo;
    
    
    //Método para mostar venta que agrega admins
    private void AgregarAdmins(Stage primaryStage) {
        
        Label titulo = new Label("Agregar administrador");
        
        Label nombreLabel = new Label("Nombre:");
        
        TextField nombreTextField = new TextField();

        Label contrasenaLabel = new Label("Contraseña:");
        
        PasswordField contrasenaPasswordField = new PasswordField();
        
        Button agregarButton = new Button("Agregar");

        agregarButton.setOnAction(event -> {
            try {
                String username = nombreTextField.getText();
                String password = contrasenaPasswordField.getText();

                // Validar que los campos no estén vacíos
                if (username.isEmpty() || password.isEmpty()) {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setHeaderText(null);
                    alert.setContentText("Los campos no pueden estar vacíos.");
                    alert.showAndWait();
                    return;
                } else {
                    AgregarAdmins_Alpha RegistrerInfo = new AgregarAdmins_Alpha(username, password);
                    out.writeObject(RegistrerInfo);
                    out.flush(); 
                }

                // Limpiar los campos de usuario y contraseña
                nombreTextField.clear();
                contrasenaPasswordField.clear();
            } catch (IOException ex) {
                System.err.println("Error sending login info to server: " + ex.getMessage());
            }
        });
        
        Button atrasButton = new Button("Regresar");
        atrasButton.setOnAction(event -> {
            Menu(primaryStage);     
        });
            
        VBox vbox = new VBox(titulo, nombreLabel, nombreTextField, contrasenaLabel, contrasenaPasswordField, agregarButton, atrasButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

        Group root = new Group(vbox);

        VBox container = new VBox(root);
        container.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(container, 500, 400));
        primaryStage.show();
    }
    
    //Método para mostar venta que agrega admins
    private void ModificarAdmins(Stage primaryStage){
        Label titulo = new Label("Escoge el administrador que quieres modificar");
        titulo.setAlignment(Pos.CENTER);

        Label nombreLabel = new Label("Nombre:");
        TextField nombreTextField = new TextField();

        Label contrasenaLabel = new Label("Contraseña:");
        PasswordField contrasenaPasswordField = new PasswordField();

        // Crear el dropdown list
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll();

        Button modButton = new Button("Modificar");
        modButton.setAlignment(Pos.CENTER);
        modButton.setOnAction(event -> {
            Thread thread = new Thread(() -> {
                try {
                    ListaEnlazada<String> elementos = (ListaEnlazada<String>) in.readObject();

                    Platform.runLater(() -> {
                        System.out.println(elementos);
                    });


                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        });

        Button atrasButton = new Button("Regresar");
        atrasButton.setAlignment(Pos.CENTER);
        atrasButton.setOnAction(event -> {
            Menu(primaryStage);
        });

        VBox vbox = new VBox(titulo, comboBox, nombreLabel, nombreTextField, contrasenaLabel, contrasenaPasswordField, modButton, atrasButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

        Group root = new Group(vbox);

        VBox container = new VBox(root);
        container.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(container, 500, 400));
        primaryStage.show();
    }


    
    //Metodo de GUI para visualizar eliminar Admins
    private void elminarAdmins(Stage primaryStage){
        Label titulo = new Label("Escoge el administrador que quieres eliminar");
        titulo.setAlignment(Pos.CENTER);
      
        Button atrasButton = new Button("Regresar");
        atrasButton.setAlignment(Pos.CENTER);
        atrasButton.setOnAction(event -> {
            Menu(primaryStage);     
        });
        
        
        Button eliminarButton = new Button("Elimina");
        eliminarButton.setAlignment(Pos.CENTER);
        eliminarButton.setOnAction(event -> {
             
        });
        
        // Crear el dropdown list
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll();

        VBox vbox = new VBox(titulo, comboBox, eliminarButton, atrasButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

        Group root = new Group(vbox);

        VBox container = new VBox(root);
        container.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(container, 500, 400));
        primaryStage.show();
    }
    
    
///////////////////////////////////////////////////////////////////////////////Metodos platillos//////////////////////////////    
    //Metodos para agregar platillos
    private void AgregarPlatillos(Stage primaryStage) {
        
        Label titulo = new Label("Agregar Platillo");
        
        Label nombreLabel = new Label("Nombre:");
        
        TextField nombreTextField = new TextField();

        Label calorias = new Label("Calorias:");
        
        TextField caloriasTextField = new TextField();
        
        Label preparacion = new Label("Tiempo de preparacion(s):");
        
        TextField preparacionTextField = new TextField();
        
        Label precio = new Label("Precio:");
        
        TextField precioTextField = new TextField();
        
        
        Button agregarButton = new Button("Agregar");
        agregarButton.setOnAction(event -> {
                 
        });


        Button atrasButton = new Button("Regresar");
        atrasButton.setOnAction(event -> {
            Menu(primaryStage);     
        });
            
        VBox vbox = new VBox(titulo, nombreLabel, nombreTextField,  calorias,caloriasTextField,preparacion,preparacionTextField,precio,precioTextField,agregarButton, atrasButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

        Group root = new Group(vbox);

        VBox container = new VBox(root);
        container.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(container, 500, 400));
        primaryStage.show();
    }
    
    //Metodo para modificar platillos
    private void modificarPlatillos(Stage primaryStage) {
        
        Label titulo = new Label("Modifica un platillo");
        
        Label nombreLabel = new Label("Nombre:");
        
        TextField nombreTextField = new TextField();

        Label calorias = new Label("Calorias:");
        
        TextField caloriasTextField = new TextField();
        
        Label preparacion = new Label("Tiempo de preparacion(s):");
        
        TextField preparacionTextField = new TextField();
        
        Label precio = new Label("Precio:");
        
        TextField precioTextField = new TextField();
        
        
        // Crear el dropdown list
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll();
        
        
        Button modificarButton = new Button("Modificar");
        modificarButton.setOnAction(event -> {
                 
        });

        Button atrasButton = new Button("Regresar");
        atrasButton.setOnAction(event -> {
            Menu(primaryStage);     
        });
            
        VBox vbox = new VBox(titulo,comboBox, nombreLabel, nombreTextField, calorias,caloriasTextField,preparacion,preparacionTextField,precio,precioTextField,modificarButton, atrasButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

        Group root = new Group(vbox);

        VBox container = new VBox(root);
        container.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(container, 500, 400));
        primaryStage.show();
    }
    
    //Metodo de GUI para mostrar como eliminar un platillo
    private void eliminarPlatillos(Stage primaryStage) {
        
        Label titulo = new Label("Elimina un platillo");
     
        // Crear el dropdown list
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll();
        
        
        Button eliminarButton = new Button("Eliminar");
        eliminarButton.setOnAction(event -> {
                 
        });

        Button atrasButton = new Button("Regresar");
        atrasButton.setOnAction(event -> {
            Menu(primaryStage);     
        });
            
        VBox vbox = new VBox(titulo,comboBox,  eliminarButton, atrasButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

        Group root = new Group(vbox);

        VBox container = new VBox(root);
        container.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(container, 500, 400));
        primaryStage.show();
    }
    
    
    
     
    //Metodo para el menú principal de eleccción
    private void Menu(Stage primaryStage) {
        btnAgregarAdmin = new Button("Agregar");
        btnModificarAdmin = new Button("Modificar");
        btnEliminarAdmin = new Button("Eliminar");

        btnAgregarPlatillo = new Button("Agregar");
        btnModificarPlatillo = new Button("Modificar");
        btnEliminarPlatillo = new Button("Eliminar");

        
        btnAgregarAdmin.setOnAction(event -> {
            AgregarAdmins(primaryStage);
        });
        btnModificarAdmin.setOnAction(event -> {
            try {
                out.writeObject("modificaAdmins");
                out.flush(); 
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            ModificarAdmins(primaryStage);
            
        });

        btnEliminarAdmin.setOnAction(event ->{
            elminarAdmins(primaryStage);
        });
        
        
        btnAgregarPlatillo.setOnAction(event -> {
            AgregarPlatillos(primaryStage);
        });
        btnModificarPlatillo.setOnAction(event -> {
            modificarPlatillos(primaryStage);
        });
        btnEliminarPlatillo.setOnAction(event ->{
            eliminarPlatillos(primaryStage);
        });

        Label Titulo = new Label("¿Qué deseas hacer administrador?");
        Titulo.setAlignment(Pos.CENTER);

        Label Admins = new Label("Gestión de administradores");
        Label Platillos = new Label("Gestión de platillos");

        VBox vboxAdmins = new VBox(Admins, btnAgregarAdmin, btnModificarAdmin, btnEliminarAdmin);
        vboxAdmins.setAlignment(Pos.CENTER);
        vboxAdmins.setSpacing(10);


        VBox vboxPlatillos = new VBox(Platillos, btnAgregarPlatillo, btnModificarPlatillo, btnEliminarPlatillo);
        vboxPlatillos.setAlignment(Pos.CENTER);
        vboxPlatillos.setSpacing(10);


        HBox hboxLabels = new HBox(vboxAdmins, vboxPlatillos);
        hboxLabels.setAlignment(Pos.CENTER);
        hboxLabels.setSpacing(100);

        VBox vboxMenu = new VBox(Titulo, hboxLabels);
        vboxMenu.setAlignment(Pos.CENTER);
        vboxMenu.setSpacing(20);

        Group root = new Group(vboxMenu);

        VBox container = new VBox(root);
        container.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(container, 500, 400));
        primaryStage.show();
    }
    
    
    public static void main(String[] args) {
        launch(args);
    }


}


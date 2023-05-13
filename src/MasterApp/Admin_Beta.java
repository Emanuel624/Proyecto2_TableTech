package MasterApp;

import ServerApp.Administrador;
import ServerApp.AgregarAdmins_Alpha;
import ServerApp.EliminarAdmin;
import ServerApp.EliminarPlatillo;
import ServerApp.ListaEnlazadaView;
import ServerApp.ModificarAdminInfo;
import ServerApp.ModificarPlatillosInfo;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox; 
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;



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
                    
                    if (obj instanceof LinkedList) {
                        LinkedList<String> adminList = (LinkedList<String>) obj;
                        System.out.println("Platillos asdasd: " + adminList);

                        // Recibir el valor booleano
                        boolean value = in.readBoolean();
                        System.out.println("Valor booleano recibido: " + value);

                        // Llamar al método ModificarAdmins si el valor booleano es verdadero
                        if (value) {
                            Platform.runLater(() -> {
                                ModificarAdmins(stage, adminList);
                            });
                        } else {
                            Platform.runLater(() -> {
                                elminarAdmins(stage, adminList);
                        });
                        }    
                    }
                    
                    //Recibir la información de los platillos 
                    if (obj instanceof ArrayList) {
                        ArrayList<String> adminPlatillo = (ArrayList<String>) obj;
                        System.out.println("Platillos asdasd: " + adminPlatillo);

                        // Recibir el valor booleano
                        boolean value = in.readBoolean();
                        System.out.println("Valor booleano recibido: " + value);

                        // Llamar al método ModificarAdmins si el valor booleano es verdadero
                        if (value) {
                            Platform.runLater(() -> {
                                modificarPlatillos(stage, adminPlatillo);
                            });
                        } else {
                            Platform.runLater(() -> {
                                eliminarPlatillos(stage, adminPlatillo);
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
        agregarButton.setTextFill(Color.GREEN);
        agregarButton.setOnAction(event -> {
            try {
                String username = nombreTextField.getText();
                String password = contrasenaPasswordField.getText();

                // Validar que los campos no estén vacíos
                if (username.isEmpty() || password.isEmpty()) {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setHeaderText(null);
                    alert.setContentText("Todos los campos deben de estar rellenos.");
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
        agregarButton.setTextFill(Color.GREEN);
        
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
    private void ModificarAdmins(Stage primaryStage, LinkedList<String> adminList) {
        System.out.println(adminList);

        Label titulo = new Label("Escoge el administrador que quieres modificar");
        titulo.setAlignment(Pos.CENTER);

        Label nombreLabel = new Label("Nombre:");
        TextField nombreTextField = new TextField();

        Label contrasenaLabel = new Label("Contraseña:");
        TextField contrasenaPasswordField = new TextField();

        // Crear una ListView
        ListView<String> listView = new ListView<>();
        listView.setItems(FXCollections.observableArrayList(adminList));
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        Button modButton = new Button("Modificar");
        modButton.setTextFill(Color.GREEN);
        modButton.setAlignment(Pos.CENTER);
        modButton.setOnAction(event -> {
            try {
                String selected = listView.getSelectionModel().getSelectedItem();
                String username = nombreTextField.getText(); //Datos para validaciones
                String password = contrasenaPasswordField.getText();
                if (selected != null) {
                    if (username.isEmpty() || password.isEmpty()) {
                        Alert alert = new Alert(AlertType.WARNING);
                        alert.setHeaderText(null);
                        alert.setContentText("Todos los campos deben de estar rellenos.");
                        alert.showAndWait();  
                    } else {
                        String[] sinMod = selected.split(":");
                        String adminNoModNombre = sinMod[0];
                        String adminNoModContraseña = sinMod[1];

                        ModificarAdminInfo modAdminInfo = new ModificarAdminInfo(adminNoModNombre, adminNoModContraseña, username, password);
                        out.writeObject(modAdminInfo);
                        out.flush();       
                    }
                } else {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setHeaderText(null);
                    alert.setContentText("Para modificar debes escoger un administrador.");
                    alert.showAndWait();       
                }
            } catch (IOException ex) {
                System.err.println("Error sending login info to server: " + ex.getMessage());    
            }

        });

        
        Button escoger = new Button("Escoger");
        escoger.setTextFill(Color.BLUE);
        escoger.setOnAction(event ->{
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String[] datosAdmin = selected.split(":");
                nombreTextField.setText(datosAdmin[0]);
                contrasenaPasswordField.setText(datosAdmin[1]);
            }
        });

        Button atrasButton = new Button("Regresar");
        atrasButton.setAlignment(Pos.CENTER);
        atrasButton.setOnAction(event -> {
            Menu(primaryStage);
        });

        VBox vbox = new VBox(titulo, listView, nombreLabel, nombreTextField, contrasenaLabel, contrasenaPasswordField, escoger, modButton, atrasButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

        Group root = new Group(vbox);

        VBox container = new VBox(root);
        container.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(container, 500, 670));
        primaryStage.show();
    }


   
    //Metodo de GUI para visualizar eliminar Admins
    private void elminarAdmins(Stage primaryStage, LinkedList<String> adminList) {
        Label titulo = new Label("Escoge el administrador que quieres eliminar");
        titulo.setAlignment(Pos.CENTER);
      
        ListView<String> listView = new ListView<>();
        listView.setItems(FXCollections.observableArrayList(adminList));
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

                
        Button eliminarButton = new Button("Elimina");
        eliminarButton.setAlignment(Pos.CENTER);
        eliminarButton.setTextFill(Color.RED);
        eliminarButton.setOnAction(event -> {
            try {
                // Obtener el elemento seleccionado en la ListView
                String selectedAdmin = listView.getSelectionModel().getSelectedItem();

                // Separar el usuario y la contraseña en dos variables diferentes
                String[] adminInfo = selectedAdmin.split(":");
                String username = adminInfo[0];
                String password = adminInfo[1];

                // Crear un objeto con la información del elemento seleccionado y enviarlo al servidor
                EliminarAdmin eliminadoInfo = new EliminarAdmin(username, password);
                out.writeObject(eliminadoInfo);
                out.flush();

            } catch (IOException ex) {
                System.err.println("Error sending admin info to server: " + ex.getMessage());
            }
        });
        
        Button atrasButton = new Button("Regresar");
        atrasButton.setAlignment(Pos.CENTER);
        atrasButton.setOnAction(event -> {
            Menu(primaryStage);     
        });

        
        VBox vbox = new VBox(titulo, listView, eliminarButton, atrasButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

        Group root = new Group(vbox);

        VBox container = new VBox(root);
        container.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(container, 500, 670));
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
            try {
                String nombre = nombreTextField.getText();
                String caloriasStr = caloriasTextField.getText();
                String preparacionStr = preparacionTextField.getText();
                String precioStr = precioTextField.getText();

                if (nombre.trim().isEmpty() || caloriasStr.trim().isEmpty() || preparacionStr.trim().isEmpty() || precioStr.trim().isEmpty()) {
                    // Muestra un mensaje de error si alguno de los campos de texto está vacío
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Todos los campos deben de estar rellenos.");
                    alert.showAndWait();
                } else {
                    // Convierte los valores de los campos de texto a los tipos de datos correspondientes
                    int caloriasInt = Integer.parseInt(caloriasStr);
                    int tiempoInt = Integer.parseInt(preparacionStr);
                    int precioInt = Integer.parseInt(precioStr);

                    // Crea un objeto Platillos con los valores de los campos de texto y lo envía al servidor
                    Platillos nuevoPlatillo = new Platillos(nombre, caloriasInt, tiempoInt, precioInt);
                    out.writeObject(nuevoPlatillo);
                    out.flush();

                    // Limpia los campos de texto después de enviar la información al servidor
                    nombreTextField.clear();
                    caloriasTextField.clear();
                    preparacionTextField.clear();
                    precioTextField.clear();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
                 
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
    private void modificarPlatillos(Stage primaryStage, ArrayList<String> adminPlatillo) {
        
        Label titulo = new Label("Modifica un platillo");
        
        // Crear una ListView
        ListView<String> listView = new ListView<>();
        listView.setItems(FXCollections.observableArrayList(adminPlatillo));
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        Label nombreLabel = new Label("Nombre:");
        
        TextField nombreTextField = new TextField();

        Label calorias = new Label("Calorias:");
        
        TextField caloriasTextField = new TextField();
        
        Label preparacion = new Label("Tiempo de preparacion(s):");
        
        TextField preparacionTextField = new TextField();
        
        Label precio = new Label("Precio:");
        
        TextField precioTextField = new TextField();
        
        
        Button escogerButton = new Button("Escoger");
        escogerButton.setTextFill(Color.BLUE);
        escogerButton.setOnAction(event -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String[] datosPlatillos = selected.split(":");
                nombreTextField.setText(datosPlatillos[0]);
                caloriasTextField.setText(datosPlatillos[1]);
                preparacionTextField.setText(datosPlatillos[2]);
                precioTextField.setText(datosPlatillos[3]);
            }
        });
        
        
        Button modButton = new Button("Modificar");
        modButton.setTextFill(Color.GREEN);
        modButton.setAlignment(Pos.CENTER);
        modButton.setOnAction(event -> {
            try {
                String selected = listView.getSelectionModel().getSelectedItem();
                String nombre = nombreTextField.getText(); //Datos para validaciones
                String caloriasText = caloriasTextField.getText();
                String preparacionText = preparacionTextField.getText();
                String precioText = precioTextField.getText();
                
                int caloriasInt = Integer.parseInt(caloriasText);
                int preparacionInt = Integer.parseInt(preparacionText);
                int precioInt = Integer.parseInt(precioText);
                               
                
                if (selected != null) {
                    if (nombre.trim().isEmpty() || caloriasText.trim().isEmpty() || preparacionText.trim().isEmpty() || precioText.trim().isEmpty()) {
                        Alert alert = new Alert(AlertType.WARNING);
                        alert.setHeaderText(null);
                        alert.setContentText("Todos los campos deben de estar rellenos.");
                        alert.showAndWait();  
                    } else {
                        String[] sinMod = selected.split(":");
                        String PlatilloNoModNombre = sinMod[0];
                        String PlatilloNoModCalorias = sinMod[1];
                        String PlatilloNoModTiempo = sinMod[2];
                        String PlatilloNoModPrecio = sinMod[3];
                        
                        
                        int intPlatilloNoModCalorias = Integer.parseInt(PlatilloNoModCalorias);
                        int intPlatilloNoModTiempo = Integer.parseInt(PlatilloNoModTiempo);
                        int intPlatilloNoModPrecio = Integer.parseInt(PlatilloNoModPrecio);
                                               

                        ModificarPlatillosInfo modPlatillosInfo = new ModificarPlatillosInfo(PlatilloNoModNombre, intPlatilloNoModCalorias, intPlatilloNoModTiempo, intPlatilloNoModPrecio, nombre, caloriasInt, preparacionInt, precioInt);
                        out.writeObject(modPlatillosInfo);
                        out.flush();       
                    }
                } else {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setHeaderText(null);
                    alert.setContentText("Para modificar debes escoger un administrador.");
                    alert.showAndWait();       
                }
            } catch (IOException ex) {
                System.err.println("Error sending login info to server: " + ex.getMessage());    
            }
            
            nombreTextField.clear();
            caloriasTextField.clear();
            preparacionTextField.clear();
            precioTextField.clear();
        });
        
        Button atrasButton = new Button("Regresar");
        atrasButton.setOnAction(event -> {
            Menu(primaryStage);     
        });
            
        VBox vbox = new VBox(titulo,listView, nombreLabel, nombreTextField, calorias,caloriasTextField,preparacion,preparacionTextField,precio,precioTextField,escogerButton,modButton, atrasButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

        Group root = new Group(vbox);

        VBox container = new VBox(root);
        container.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(container, 500, 800));
        primaryStage.show();
    }
    
    //Metodo de GUI para mostrar como eliminar un platillo
    private void eliminarPlatillos(Stage primaryStage, ArrayList<String> adminPlatillo) {
        
        Label titulo = new Label("Elimina un platillo");
     
        // Crear una ListView
        ListView<String> listView = new ListView<>();
        listView.setItems(FXCollections.observableArrayList(adminPlatillo));
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        Button eliminarButton = new Button("Eliminar");
        eliminarButton.setTextFill(Color.RED);
        eliminarButton.setOnAction(event -> {     
            try {
                // Obtener el elemento seleccionado en la ListView
                String selectedPlatillo = listView.getSelectionModel().getSelectedItem();

                // Separar el usuario y la contraseña en dos variables diferentes
                String[] platilloInfo = selectedPlatillo.split(":");
                String nombre = platilloInfo[0];
                String calorias = platilloInfo[1];
                String preparacion = platilloInfo[2];
                String precio = platilloInfo[3];
                
                int caloriasInt = Integer.parseInt(calorias);
                int preparacionInt = Integer.parseInt(preparacion);
                int precioInt = Integer.parseInt(precio);

                // Crear un objeto con la información del elemento seleccionado y enviarlo al servidor
                EliminarPlatillo PlatInfoEliminado = new EliminarPlatillo(nombre, caloriasInt, preparacionInt, precioInt);
                out.writeObject(PlatInfoEliminado);
                out.flush();

            } catch (IOException ex) {
                System.err.println("Error sending admin info to server: " + ex.getMessage());
            }
        });
        

        Button atrasButton = new Button("Regresar");
        atrasButton.setOnAction(event -> {
            Menu(primaryStage);     
        });
            
        VBox vbox = new VBox(titulo,listView,eliminarButton, atrasButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

        Group root = new Group(vbox);

        VBox container = new VBox(root);
        container.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(container, 500, 700));
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
            // Enviar el mensaje al servidor
            try {
                // enviar solicitud al servidor
                out.writeObject("modificaAdmins");
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
            
        });

        btnEliminarAdmin.setOnAction(event ->{
            // Enviar el mensaje al servidor
            try {
                // enviar solicitud al servidor
                out.writeObject("eliminaAdmins");
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        
        
        btnAgregarPlatillo.setOnAction(event -> {
            AgregarPlatillos(primaryStage);
        });
        
        btnModificarPlatillo.setOnAction(event -> {
             // Enviar el mensaje al servidor
            try {
                // enviar solicitud al servidor
                out.writeObject("modificarPlatillos");
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        
        btnEliminarPlatillo.setOnAction(event ->{
            // Enviar el mensaje al servidor
            try {
                // enviar solicitud al servidor
                out.writeObject("eliminarPlatillos");
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
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


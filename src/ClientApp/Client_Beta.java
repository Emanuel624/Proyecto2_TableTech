/**
 * Basicamente esta clase es la "Clase cliente" en la relación cliente/servidor de los sockets
 */
package ClientApp;


import static javafx.application.Application.launch;
import ClientApp.Cliente;
import ServerApp.ListaEnlazadaView;
import ServerApp.ListaEnlazada;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javafx.application.Application;
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
import com.google.gson.Gson;
import MasterApp.Platillos;
import java.nio.file.Files;
import java.nio.file.Paths;
import MasterApp.Pedido;


public class Client_Beta extends Application {
    
    private ListaEnlazadaView<Platillos> listViewPlatillos;
    private ListaEnlazadaView<Platillos> listViewCarrito;
    private Button btnAgregar;
    private Button btnHacerPedido;
    private ListaEnlazada<Platillos> carrito;
    private ObjectOutputStream out;

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
        out = new ObjectOutputStream(socket.getOutputStream());

        // Agregar manejador de eventos al botón Iniciar Sesión
        btnLogin.setOnAction(event -> {
            try {
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
                while (true) {
                    boolean loginSuccess = (boolean) in.readObject();
                    Platform.runLater(() -> {
                        if (loginSuccess) {
                            System.out.println("Inicio de sesión exitoso");
                            mostrarVentanaPedidos(stage);
                        } else {
                            System.out.println("Inicio de sesión fallido");
                            // Aquí puedes agregar el código para manejar un inicio de sesión fallido
                        }
                    });
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        thread.start();
        stage.show();
    }
    
    private void mostrarVentanaPedidos(Stage primaryStage) {
    listViewPlatillos = new ListaEnlazadaView<>();
    listViewCarrito = new ListaEnlazadaView<>();
    btnAgregar = new Button("Agregar al carrito");
    btnHacerPedido = new Button("Hacer pedido");
    carrito = new ListaEnlazada<>();

    ListaEnlazada<Platillos> platillos = leerPlatillosJSON();
    platillos.forEach(platillo -> listViewPlatillos.add(platillo));

    
    btnAgregar.setOnAction(event -> agregarPlatillo());
    btnHacerPedido.setOnAction(event -> hacerPedido());

    Label lblNombre = new Label("Nombre: ");
    Label lblCalorias = new Label("Calorías: ");
    Label lblPrecio = new Label("Precio: ");
    Label lblTiempoPreparacion = new Label("Tiempo de preparación: ");

    listViewPlatillos.getListView().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue != null) {
            lblNombre.setText("Nombre: " + newValue.getNombre());
            lblCalorias.setText("Calorías: " + newValue.getCantidadCalorias());
            lblPrecio.setText("Precio: " + newValue.getPrecio());
            lblTiempoPreparacion.setText("Tiempo de preparación: " + newValue.getTiempoPreparacion() + " segundos");
        }
    });

    VBox vboxMenu = new VBox(listViewPlatillos.getListView(), lblNombre, lblCalorias, lblPrecio, lblTiempoPreparacion, btnAgregar);
    VBox vboxCarrito = new VBox(listViewCarrito.getListView(), btnHacerPedido);
    HBox hbox = new HBox(vboxMenu, vboxCarrito);

    primaryStage.setScene(new Scene(hbox, 800, 400));
    primaryStage.show();
}


private void agregarPlatillo() {
    Platillos seleccionado = listViewPlatillos.getListView().getSelectionModel().getSelectedItem();
    if (seleccionado != null) {
        listViewCarrito.add(seleccionado);
    }
}

private void hacerPedido() {
    ListaEnlazada<Platillos> pedidoPlatillos = listViewCarrito.getListaEnlazada();
    Pedido pedido = new Pedido(pedidoPlatillos); // Asumiendo que Pedido tiene un constructor que toma ListaEnlazada<Platillos>

    try {
        System.out.println("Pedido Realizado, espera a que esté listo");
        out.writeObject(pedido);
        out.flush();
    } catch (IOException e) {
        e.printStackTrace();
    }
}



    private ListaEnlazada<Platillos> leerPlatillosJSON() {
    try {
        String jsonContent = new String(Files.readAllBytes(Paths.get("platillos.json")));
        Gson gson = new Gson();
        Platillos[] platillosArray = gson.fromJson(jsonContent, Platillos[].class);

        ListaEnlazada<Platillos> platillosList = new ListaEnlazada<>();
        for (Platillos platillo : platillosArray) {
            platillosList.add(platillo);
        }
        return platillosList;
    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }
}


public static void main(String[] args) {
    launch(args);
}
}

        

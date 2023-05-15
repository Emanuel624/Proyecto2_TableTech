package ClientApp;

import static javafx.application.Application.launch;

import ClientApp.Cliente;
import ServerApp.ListaEnlazadaView;
import ServerApp.ListaEnlazada;
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
import MasterApp.Platillos;
import MasterApp.Pedido;
import java.io.DataInputStream;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @authors Randall Bryan Bolañoz López, Octavio Sanchez Soto, Emanuel Chavarría Hernández.
 * @version 1.0
 */


/**
 * Esta clase presenta la GUI necesaria para el funcionamiento de la ClientApp, la cual se comunica por medio de sockets para lo lógica del sistema en general.
 */
public class Client_Beta extends Application {
    //Se presentan algunos parametros generales para el funcionamiento de la aplicación.
    private ListaEnlazadaView<Platillos> listViewPlatillos;
    private ListaEnlazadaView<Platillos> listViewCarrito;
    private Button btnAgregar;
    private Button btnHacerPedido;
    private Button btnVerHistorial;
    private ListaEnlazada<Platillos> carrito;
    private ObjectOutputStream out;
    private int numeroPedido = 1;
    private ListView<String> listViewPedidosActivos;
    private ListView<String> listViewPedidosPendientes;
    
    private Socket socket;
    private ObjectInputStream in;
    
    
    /**
     * Método encargado de iniciar la aplicación GUI como tal.
     * @param stage permite presentar y hacer el "Launch" de la aplicación GUI, por medio de JavaFX
     * @throws Exception en caso de que surja un error en la aplicación a la hora de ejecutarse
     */
    @Override
    public void start(Stage stage) throws Exception {
        listViewPedidosActivos = new ListView<>();
        listViewPedidosPendientes = new ListView<>();

       
        // Crear los nodos necesarios para la ventana de inicio de sesión
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

        // Agregar manejador de eventos al botón Iniciar Sesión
        btnLogin.setOnAction(event -> {
            
            //Mediante un try/catcht se manda un socket para verificar los datos de inicio de sesión
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
        
        //Mediante un nuevo hilo, se recibe información para poder realizar acciones en la GUI de la aplicaicón cliente
        Thread thread = new Thread(() -> {
            try {
                // Crear el socket y conectar al servidor
                socket = new Socket("localhost", 8080);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                // Leer la respuesta del servidor
                boolean loginSuccess = (boolean) in.readObject();
                Platform.runLater(() -> {
                    if (loginSuccess) {
                        System.out.println("Inicio de sesión exitoso");
                        mostrarVentanaPedidos(stage);
                        recibirMensajesServidor();
                    } else {
                        System.out.println("Inicio de sesión fallido");
                        // Aquí puedes agregar el código para manejar un inicio de sesión fallido
                    }
                });
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        thread.start();
        stage.show();
    }
    
    
    /**
     * Se crea un nuevo método, para poder cambiar la aparencia así como los sockets enviados, con el objetigo de mostrar una ventana que muestre los pedidos y formas de realizarlos
     * @param primaryStage es el parametro utilizado para poder mostrar la GUI como tal.
     */
    private void mostrarVentanaPedidos(Stage primaryStage) {
    //Diversas variables y objetos necesarios para el funcionamiento de este método.    
    listViewPlatillos = new ListaEnlazadaView<>();
    listViewCarrito = new ListaEnlazadaView<>();
    btnAgregar = new Button("Agregar al carrito");
    btnHacerPedido = new Button("Hacer pedido");
    carrito = new ListaEnlazada<>();

    ListaEnlazada<Platillos> platillos = leerPlatillosJSON();
    platillos.forEach(platillo -> listViewPlatillos.add(platillo));
    
    
    btnVerHistorial = new Button("Ver Historial de Pedidos");
    btnVerHistorial.setOnAction(event -> mostrarVentanaHistorial());

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
    
    TabPane tabPane = new TabPane();
    // Crear pestaña "Pedidos Activos"
    Tab tabPedidosActivos = new Tab("Pedidos Activos");
    listViewPedidosActivos = new ListView<>();
    tabPedidosActivos.setContent(listViewPedidosActivos);
    tabPane.getTabs().add(tabPedidosActivos);

    // Crear pestaña "Pedidos Pendientes de Entrega"
    Tab tabPedidosPendientes = new Tab("Pedidos Pendientes de Entrega");
    listViewPedidosPendientes = new ListView<>();
    tabPedidosPendientes.setContent(listViewPedidosPendientes);
    tabPane.getTabs().add(tabPedidosPendientes);
    VBox vboxMenu = new VBox(listViewPlatillos.getListView(), lblNombre, lblCalorias, lblPrecio, lblTiempoPreparacion, btnAgregar);
    VBox vboxCarrito = new VBox(listViewCarrito.getListView(), btnHacerPedido,btnVerHistorial);
    
    HBox hbox = new HBox(vboxMenu, vboxCarrito, tabPane);

    primaryStage.setScene(new Scene(hbox, 800, 400));
    primaryStage.show();
}
    
    
    /**
     * Método encargado de agregar los platillos a la lista enlazada necesaria.
     */
    private void agregarPlatillo() {
        Platillos seleccionado = listViewPlatillos.getListView().getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            listViewCarrito.add(seleccionado);
        }
    }
    
    
    /**
     * Método que se encarga de realizar la función de realizar el pedido como tal.
     */
    private void hacerPedido() {
    ListaEnlazada<Platillos> pedidoPlatillos = listViewCarrito.getListaEnlazada();
    Pedido pedido = new Pedido(pedidoPlatillos); // Asumiendo que Pedido tiene un constructor que toma ListaEnlazada<Platillos>

    try {
        System.out.println("Pedido Realizado, espera a que esté listo");
        out.writeObject(pedido);
        out.flush();
        guardarPedidoEnHistorial(pedido);

        // Agregar el número del pedido a la lista de "Pedidos Activos"
        String pedidoActivo = "Pedido " + numeroPedido;
        listViewPedidosActivos.getItems().add(pedidoActivo);
        numeroPedido++;
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    
/**
 * Método que se encarga de recibir los mensaje de respuesta recibidos por medios de sockets del servidor hacia la clase cliente, todo esto mediante un nuevo hilo que solamente
 * recibe mensajes de respuesta.
 */
private void recibirMensajesServidor() {
    //Creación del nuevo hilo
    Thread thread = new Thread(() -> {
        try {
            DataInputStream inMensaje = new DataInputStream(socket.getInputStream());
            while (true) {
                boolean mensaje = inMensaje.readBoolean(); // Recibir un valor booleano
                Platform.runLater(() -> {
                    if (mensaje) {
                        if (!listViewPedidosActivos.getItems().isEmpty()) {
                            String pedidoEntregado = listViewPedidosActivos.getItems().remove(0);
                            listViewPedidosPendientes.getItems().add(pedidoEntregado);
                            System.out.println("El pedido está listo para ser entregado");
                        }
                    } else {
                        // Otro mensaje del cliente
                        System.out.println("Mensaje del cliente: " + mensaje);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    });

    thread.start();
}
   

    /**
     * ListaEnlazada encargada de leer el JSON con los platillos.
     * @return retorna la lista enlazada con los datos que se lee del JSON de platillos.
     */
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
    
    
/**
 * Lógica detras del metodo que se encarga de guardar el pedido realizar en el historial.
 * @param pedido este parametro es necesaria ya que es la inforamción que se guarda como pedido.
 */    
private void guardarPedidoEnHistorial(Pedido pedido) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    ArrayList<Pedido> pedidos = leerHistorialPedidos();
    pedidos.add(pedido);

    try (FileWriter writer = new FileWriter("historial_pedidos.json")) {
        gson.toJson(pedidos, writer);
    } catch (IOException e) {
        e.printStackTrace();
    }
}


/**
 * Un arrayList necesarios para leer el historia de pedidos para evitar compatibilidades.
 * @return la misma lista enlazada con la información obtenida depues de leer el archivo Json.
 */
private ArrayList<Pedido> leerHistorialPedidos() {
    try {
        String jsonContent = new String(Files.readAllBytes(Paths.get("historial_pedidos.json")));
        Gson gson = new Gson();
        Pedido[] pedidosArray = gson.fromJson(jsonContent, Pedido[].class);
        return new ArrayList<>(Arrays.asList(pedidosArray));
    } catch (IOException e) {
        e.printStackTrace();
        return new ArrayList<>();
    }
}


/**
 * Este método muestra la ventana GUI, en la que se enseñan los datos relacionados al historial de pedidos.
 */
private void mostrarVentanaHistorial() {
    //Variables y objetos necesarios para el funcionamiento del método
    Stage historialStage = new Stage();
    historialStage.setTitle("Historial de Pedidos");

    ListView<String> listViewHistorial = new ListView<>();
    ArrayList<Pedido> pedidos = leerHistorialPedidos();

    pedidos.forEach(pedido -> listViewHistorial.getItems().add(pedido.toString()));

    VBox vboxHistorial = new VBox(listViewHistorial);
    vboxHistorial.setPadding(new Insets(10));

    Scene sceneHistorial = new Scene(vboxHistorial, 400, 300);
    historialStage.setScene(sceneHistorial);
    historialStage.show();
}


    /**
     * Método estático para poder lanzar la ejecución de la GUI como tal.
     * @param args parametro necesario para la ejecuín del main.
     */
    public static void main(String[] args) {
        launch(args);
    }
}


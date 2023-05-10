package ServerApp;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.concurrent.atomic.AtomicInteger;

import MasterApp.Platillos;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import MasterApp.Pedido;
import ClientApp.Cliente;

public class ServerAppMain {
    private static final Queue<Pedido> pedidosQueue = new Queue<>();
    private static Socket socket;
    
    public static void main(String[] args) throws ParserConfigurationException, SAXException, TransformerConfigurationException, TransformerException {
        
        // Elementos necesario para la lectura del archivo XML dentro del server App
        File xmlFile = new File("administradores.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
        // Elementos necesarios para la lectura del archivo XML de clientes dentro del server App
        File xmlFileClientes = new File("clientes.xml");
        DocumentBuilderFactory factoryClientes = DocumentBuilderFactory.newInstance();
        BinaryTree<String> treeClientes = new BinaryTree<>();
        
        
        try {
            // Construccion de parametros necesarios para contruir herramientas para recorrer el archivo de administradores
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            
            // Construcción de parámetros necesarios para construir herramientas para recorrer el archivo de clientes
            DocumentBuilder builderClientes = factoryClientes.newDocumentBuilder();
            Document docClientes = builderClientes.parse(xmlFileClientes);
            docClientes.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("administrador");
            BinaryTree<String> tree = new BinaryTree<>();
            
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String username = element.getElementsByTagName("username").item(0).getTextContent();
                String contrasena = element.getElementsByTagName("contrasena").item(0).getTextContent();
                String data = username + ":" + contrasena;
                tree.insert(data);
            }

            NodeList nodeListClientes = docClientes.getElementsByTagName("cliente");

            for (int i = 0; i < nodeListClientes.getLength(); i++) {
                Element element = (Element) nodeListClientes.item(i);
                String username = element.getElementsByTagName("username").item(0).getTextContent();
                String contrasena = element.getElementsByTagName("contrasena").item(0).getTextContent();
                String data = username + ":" + contrasena;
                treeClientes.insert(data);
            }
           
            // Crear el socket del servidor en el puerto 8080
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("Servidor iniciado");
            Thread procesarPedidosThread = new Thread(ServerAppMain::procesarPedidos);
            procesarPedidosThread.start();
            while (true) {
               // Esperar por una conexión del cliente
               socket = serverSocket.accept();
               System.out.println("Cliente conectado");

               // Crear el stream de entrada para recibir la información del Administrador
               ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
               // Crear el stream de salida para enviar la respuesta al cliente
               ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                while (true) {
                    // Leer la información de inicio de sesión y mostrarla en la consola
                    Object obj = in.readObject();
                    if (obj == null) {
                    System.err.println("Se recibió un objeto nulo en la solicitud de inicio de sesión");
                    break;
                }
                    System.out.println("Objeto recibido: " + obj.getClass().getName());
                    // Condición para verificar si el objeto recibido es un pedido
                    if (obj instanceof Object[] pedido) {
                    System.out.println("Se recibió un pedido:");
                    for (Object platillo : pedido) {
                        System.out.println(((Platillos) platillo).getNombre());
                    }}
                    // Parte para manejar el objeto Pedido
                    if (obj instanceof Pedido) {
                        Pedido pedido = (Pedido) obj;
                        agregarPedido(pedido);
                    }
                    if (obj instanceof Administrador loginInfo) {
                        System.out.println("Se recibió información de inicio de sesión: " + loginInfo.getUsername() + ":" + loginInfo.getContrasena());

                        // Buscar el elemento en el árbol
                        boolean encontrado = tree.contains(loginInfo.getUsername() + ":" + loginInfo.getContrasena());

                        // Enviar la respuesta al cliente
                        out.writeObject(encontrado);
                        out.flush();

                        // Verificar si se recibió el comando de salida
                        if (loginInfo.getUsername().equals("exit") && loginInfo.getContrasena().equals("exit")) {
                            break; // Salir del ciclo interno
                        }
                        
                    System.out.println("Antes de verificar si obj es una instancia de Cliente");  
                    
                    } else if (obj instanceof Cliente loginInfoCliente) {
                        System.out.println("Después de verificar si obj es una instancia de Cliente");
                        System.out.println("Se recibió información de inicio de sesión de cliente: " + loginInfoCliente.getUsername() + ":" + loginInfoCliente.getContrasena());

                        // Buscar el elemento en el árbol de clientes
                        System.out.println("Antes de buscar el elemento en el árbol");
                        boolean encontradoCliente = treeClientes.contains(loginInfoCliente.getUsername() + ":" + loginInfoCliente.getContrasena());
                        System.out.println("Después de buscar el elemento en el árbol");
                        // Enviar la respuesta al cliente
                        out.writeObject(encontradoCliente);
                        out.flush();

                        // Verificar si se recibió el comando de salida
                        if (loginInfoCliente.getUsername().equals("exit") && loginInfoCliente.getContrasena().equals("exit")) {
                            break; // Salir del ciclo interno
                        }
                        System.out.println("Respuesta enviada al cliente: " + encontradoCliente);
                    } else if (obj instanceof AgregarAdmins_Alpha registrerInfo) {
                        System.out.println("Se recibió información para agregar un nuevo administrador: " + registrerInfo.getUsername() + ":" + registrerInfo.getContrasena());

                        // Agregar el nuevo administrador al árbol
                        String data = registrerInfo.getUsername() + ":" + registrerInfo.getContrasena();
                        tree.insert(data);

                        // Agregar el nuevo administrador al documento XML
                        Element newAdmin = doc.createElement("administrador");
                        Element newUsername = doc.createElement("username");
                        Element newContrasena = doc.createElement("contrasena");
                        newUsername.appendChild(doc.createTextNode(registrerInfo.getUsername()));
                        newContrasena.appendChild(doc.createTextNode(registrerInfo.getContrasena()));
                        newAdmin.appendChild(newUsername);
                        newAdmin.appendChild(newContrasena);
                        doc.getDocumentElement().appendChild(newAdmin);

                        // Guardar los cambios en el archivo XML
                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        Transformer transformer = transformerFactory.newTransformer();
                        DOMSource source = new DOMSource(doc);
                        StreamResult result = new StreamResult(new FileOutputStream(xmlFile));
                        transformer.transform(source, result);

                        // Enviar la respuesta al cliente
                        out.writeObject(true);
                        out.flush();
                    } else if (obj instanceof Platillos nuevoPlatillo) {
                        //recibir la info del nuevo platillo que se creo
                        AvlTree<String> avlTreePlatillo = new AvlTree<>();

                        //guardar el platillo a un archivo json
                        try(FileReader reader = new FileReader("platillos.json")){
                            Type platilloListType = new TypeToken<ArrayList<Platillos>>(){}.getType();
                            Gson gson = new Gson();
                            ArrayList<Platillos> listaPlatillos = gson.fromJson(reader, platilloListType);
                            reader.close();
                            listaPlatillos.add(new Platillos(nuevoPlatillo.getNombre(), nuevoPlatillo.getCantidadCalorias(), nuevoPlatillo.getTiempoPreparacion(), nuevoPlatillo.getPrecio()));
                            FileWriter writer = new FileWriter("platillos.json");
                            gson.toJson(listaPlatillos, writer);
                            writer.close();
                            //recorrer la lista de platillos del json y guardar la info en un avl
                            for (Platillos p : listaPlatillos) {
                                String dataPlatillo = p.getNombre() + "" + p.getCantidadCalorias() + "" + p.getTiempoPreparacion()
                                        + "" + p.getPrecio();
                                avlTreePlatillo.insertElement(dataPlatillo);
                                System.out.println(dataPlatillo);
                            }


                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }

                }


                // Cerrar el stream y el socket (Si se cierra la aplicación tambien se cierra a la fuerza)
                in.close();
                socket.close();
                System.out.println("Cliente desconocetado del servidor");
            }
        } catch (NullPointerException ex) {
    System.err.println("Error handling login request due to null value: " + ex.getMessage());
    ex.printStackTrace();
    System.err.println("Last object received before exception: " + "");
}
        catch (IOException | ClassNotFoundException ex) {
    System.err.println("Error handling login request: " + ex.getMessage());
    ex.printStackTrace();
        }
    }
    

private static void procesarPedidos() {
    while (true) {
        if (!pedidosQueue.isEmpty()) {
            Pedido pedido = pedidosQueue.dequeue();
            System.out.println("Procesando pedido:");
            int totalPlatillos = pedido.getPlatillos().size();
            AtomicInteger platillosPreparados = new AtomicInteger(0);
            AtomicInteger rangoProgresoAnterior = new AtomicInteger(0);

            pedido.getPlatillos().forEach(platillo -> {
                System.out.println("Preparando: " + platillo.getNombre());
                try {
                    Thread.sleep(platillo.getTiempoPreparacion() * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                platillosPreparados.incrementAndGet();
                int progreso = (platillosPreparados.get() * 100) / totalPlatillos;
                pedido.setProgreso(progreso);
                System.out.println("Progreso del pedido: " + progreso + "%");

                int rangoProgresoActual = progreso / 25;
                if (rangoProgresoActual > rangoProgresoAnterior.get()) {
                    rangoProgresoAnterior.set(rangoProgresoActual);
                    switch (rangoProgresoActual) {
                        case 1:
                            EncenderLuces25();
                            break;
                        case 2:
                            EncenderLuces50();
                            break;
                        case 3:
                            EncenderLuces75();
                            break;
                        case 4:
                            EncenderLuces100();
                            pedido.setEstado("Pendiente de entrega");
                            enviarMensajeListo();
                            
                            break;
                    }
                }
            });
        } else {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


private static void EncenderLuces25() {
    System.out.println("Encendiendo luces al 25%");
      // Implementar codigo de arduino
}

private static void EncenderLuces50() {
    System.out.println("Encendiendo luces al 50%");
    // Implementar codigo de arduino
}

private static void EncenderLuces75() {
    System.out.println("Encendiendo luces al 75%");
    // Implementar codigo de arduino
}

private static void EncenderLuces100() {
    System.out.println("Encendiendo luces al 100%");
    // Implementar codigo de arduino
}

public static void agregarPedido(Pedido pedido) {
    pedidosQueue.enqueue(pedido);
    System.out.println("Pedido encolado: " + pedido);
}
private static void enviarMensajeListo() {
    try {
        DataOutputStream outMensaje = new DataOutputStream(socket.getOutputStream());
        outMensaje.writeBoolean(true); // Valor booleano a enviar
        outMensaje.flush();
        System.out.println("Se ha enviado un Listo al ClientApp");
    } catch (IOException e) {
        e.printStackTrace();
    }
}

}

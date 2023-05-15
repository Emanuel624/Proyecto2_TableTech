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
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Node;

/**
 * @authors Randall Bryan Bolañoz López, Octavio Sanchez Soto, Emanuel Chavarría Hernández.
 * @version 1.0
 */

/**
 * Esta clase publica, es el "Main" del servidor, en el cual sucede toda la lógica del sistema.
 */
public class ServerAppMain {
    private static final Queue<Pedido> pedidosQueue = new Queue<>();
    private static Socket socket;
    
    /**
     * El método prinicpal el cual permite la ejecución de la lógica como tal.
     * @param args parametro necesario para la ejecuín del main. 
     * @throws ParserConfigurationException excepción en caso de error.
     * @throws SAXException excepción en caso de error.
     * @throws TransformerConfigurationException excepción en caso de error.
     * @throws TransformerException excepción en caso de error.
     */
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
            
            
            AvlTree<String> avlTreePlatillo = new AvlTree<>();

            try (FileReader reader = new FileReader("platillos.json")) {
                Type platilloListType = new TypeToken<ArrayList<Platillos>>(){}.getType();
                Gson gson = new Gson();
                ArrayList<Platillos> listaPlatillos = gson.fromJson(reader, platilloListType);
                reader.close();

                //recorrer la lista de platillos del json y guardar la info en un avl
                for (Platillos p : listaPlatillos) {
                    String dataPlatillo = p.getNombre() + ":" + p.getCantidadCalorias() + ":" + p.getTiempoPreparacion() + ":" + p.getPrecio();
                    avlTreePlatillo.insertElement(dataPlatillo);
                }
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
                        
                        
                    //Lógica para eliminar Administradores del arbol binario y del XML    
                    } else if (obj instanceof EliminarAdmin eliminadoInfo) {
                        System.out.println("Administrador por eliminar: " + eliminadoInfo.getUsername() + ":" + eliminadoInfo.getContrasena());
                        String data = eliminadoInfo.getUsername() + ":" + eliminadoInfo.getContrasena();
                        ServerApp.Node<String> current = tree.getRoot();
                        if (tree.contains(data)) {
                            current = tree.remove(data, current);

                            // Eliminar el administrador del archivo XML
                            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                            doc.getDocumentElement().normalize();

                            List<Node> nodesToRemove = new ArrayList<>();
                            for (int i = 0; i < nodeList.getLength(); i++) {
                                Node node = (Node) nodeList.item(i);
                                if (node.getNodeType() == Node.ELEMENT_NODE) {
                                    Element element = (Element) node;
                                    String username = element.getElementsByTagName("username").item(0).getTextContent();
                                    String password = element.getElementsByTagName("contrasena").item(0).getTextContent();
                                    if (username.equals(eliminadoInfo.getUsername()) && password.equals(eliminadoInfo.getContrasena())) {
                                        nodesToRemove.add(node);
                                    }
                                }
                            }

                            for (Node nodeToRemove : nodesToRemove) {
                                nodeToRemove.getParentNode().removeChild(nodeToRemove);
                            }

                            // Guardar los cambios en el archivo XML
                            TransformerFactory transformerFactory = TransformerFactory.newInstance();
                            Transformer transformer = transformerFactory.newTransformer();
                            DOMSource source = new DOMSource(doc);
                            StreamResult result = new StreamResult(new FileOutputStream(xmlFile));
                            transformer.transform(source, result);
                        }

                        
                    //Lógica para recibir el socket de ModficiarAdmins y hacer los cambios en XML y arbol binario   
                    } else if (obj instanceof ModificarAdminInfo modAdminInfo) {
                        System.out.println("Administrador por modificar: " + modAdminInfo.getUsernameMod()+ ":" + modAdminInfo.getContrasenaMod() + ":" + modAdminInfo.getUsername() + ":" + modAdminInfo.getContrasena());
                        
                        String adminParaMod = modAdminInfo.getUsernameMod()+ ":" + modAdminInfo.getContrasenaMod();
                        String adminSinMod = modAdminInfo.getUsername() + ":" + modAdminInfo.getContrasena();
                        ServerApp.Node<String> current = tree.getRoot();
                        if (tree.contains(adminSinMod)) {
                            current = tree.remove(adminSinMod, current);
                            
                            // Eliminar el administrador del archivo XML
                            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                            doc.getDocumentElement().normalize();

                            List<Node> nodesToRemove = new ArrayList<>();
                            for (int i = 0; i < nodeList.getLength(); i++) {
                                Node node = (Node) nodeList.item(i);
                                if (node.getNodeType() == Node.ELEMENT_NODE) {
                                    Element element = (Element) node;
                                    String username = element.getElementsByTagName("username").item(0).getTextContent();
                                    String password = element.getElementsByTagName("contrasena").item(0).getTextContent();
                                    if (username.equals(modAdminInfo.getUsername()) && password.equals(modAdminInfo.getContrasena())) {
                                        nodesToRemove.add(node);
                                    }
                                }
                            }

                            for (Node nodeToRemove : nodesToRemove) {
                                nodeToRemove.getParentNode().removeChild(nodeToRemove);
                            }

                            // Guardar los cambios en el archivo XML
                            TransformerFactory transformerFactory = TransformerFactory.newInstance();
                            Transformer transformer = transformerFactory.newTransformer();
                            DOMSource source = new DOMSource(doc);
                            StreamResult result = new StreamResult(new FileOutputStream(xmlFile));
                            transformer.transform(source, result);
                        }
                        
                        tree.insert(adminParaMod);
                        // Agregar el nuevo administrador al documento XML
                        Element newAdmin = doc.createElement("administrador");
                        Element newUsername = doc.createElement("username");
                        Element newContrasena = doc.createElement("contrasena");
                        newUsername.appendChild(doc.createTextNode(modAdminInfo.getUsernameMod()));
                        newContrasena.appendChild(doc.createTextNode(modAdminInfo.getContrasenaMod()));
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
                    

                    //Lógica que recibe los datos modificados de los platillos    
                    } else if (obj instanceof ModificarPlatillosInfo modPlatillosInfo) {
                        System.out.println("Platillos por modificar: " + modPlatillosInfo.getNombre()+ ":" + modPlatillosInfo.getCalorias() + ":" + modPlatillosInfo.getTiempoMod() + ":" + modPlatillosInfo.getPrecio() + ":" + modPlatillosInfo.getNombreMod()+ ":" + modPlatillosInfo.getCaloriasMod() + ":" + modPlatillosInfo.getTiempoMod() + ":" + modPlatillosInfo.getPrecioMod());                        
                        String dataSinMod = modPlatillosInfo.getNombre()+ ":" + modPlatillosInfo.getCalorias() + ":" + modPlatillosInfo.getTiempo() + ":" + modPlatillosInfo.getPrecio();
                        String DataMod = modPlatillosInfo.getNombreMod()+ ":" + modPlatillosInfo.getCaloriasMod() + ":" + modPlatillosInfo.getTiempoMod() + ":" + modPlatillosInfo.getPrecioMod();

                        if (avlTreePlatillo.contains(dataSinMod)){
                            avlTreePlatillo.removeElement(dataSinMod);
                        }
                        // eliminar el platillo del archivo JSON
                        try (FileReader reader = new FileReader("platillos.json")) {
                            Type platilloListType1 = new TypeToken<ArrayList<Platillos>>(){}.getType();
                            Gson gson = new Gson();
                            ArrayList<Platillos> listaPlatillos1 = gson.fromJson(reader, platilloListType1);
                            reader.close();

                            // buscar y eliminar el objeto correspondiente
                            for (int i = 0; i < listaPlatillos1.size(); i++) {
                                Platillos platillo1 = listaPlatillos1.get(i);
                                if (platillo1.getNombre().equals(modPlatillosInfo.getNombre())
                                    && platillo1.getCantidadCalorias() == modPlatillosInfo.getCalorias()
                                    && platillo1.getTiempoPreparacion() == modPlatillosInfo.getTiempo()
                                    && platillo1.getPrecio() == modPlatillosInfo.getPrecio()) {
                                    listaPlatillos1.remove(i);
                                }
                            }

                            // escribir la lista actualizada en el archivo
                            FileWriter writer = new FileWriter("platillos.json");
                            gson.toJson(listaPlatillos1, writer);
                            writer.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        
                        
                        avlTreePlatillo.insertElement(DataMod);
                        //guardar el platillo a un archivo json
                        try(FileReader reader = new FileReader("platillos.json")){
                            Type platilloListType = new TypeToken<ArrayList<Platillos>>(){}.getType();
                            Gson gson = new Gson();
                            ArrayList<Platillos> listaPlatillos = gson.fromJson(reader, platilloListType);
                            reader.close();
                            listaPlatillos.add(new Platillos(modPlatillosInfo.getNombreMod(), modPlatillosInfo.getCaloriasMod(), modPlatillosInfo.getTiempoMod(), modPlatillosInfo.getPrecioMod()));
                            FileWriter writer = new FileWriter("platillos.json");
                            gson.toJson(listaPlatillos, writer);
                            writer.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        
                        
                        
                    //Lógica para agregar adniministradores al arbol y al XML  
                    } else if (obj instanceof AgregarAdmins_Alpha registrerInfo) {
                        System.out.println("Administrador por agregar: " + registrerInfo.getUsername() + ":" + registrerInfo.getContrasena());

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

                        
                    //Enviar datos del arbol con información de los admins    
                    // En el lado del servidor
                    } else if (obj instanceof String && obj.equals("modificaAdmins")) {
                        LinkedList<String> adminList = tree.inOrderTraversal();
                        System.out.println("Enviando lista de administradores: " + adminList);
                        out.writeObject(adminList); // enviar lista de cadenas de texto
                        out.flush();
                        out.writeBoolean(true); // enviar valor booleano
                        out.flush();
                        
                        
                    } else if (obj instanceof String && obj.equals("eliminaAdmins")) {
                        LinkedList<String> adminList = tree.inOrderTraversal();
                        System.out.println("Enviando lista de administradores: " + adminList);
                        out.writeObject(adminList); // enviar lista de cadenas de texto
                        out.flush();
                        out.writeBoolean(false); // enviar valor booleano
                        out.flush();
                        
                        
                    } else if (obj instanceof String && obj.equals("modificaAdmins")) {
                        LinkedList<String> adminList = tree.inOrderTraversal();
                        System.out.println("Enviando lista de administradores: " + adminList);
                        out.writeObject(adminList); // enviar lista de cadenas de texto
                        out.flush();
                        out.writeBoolean(true); // enviar valor booleano
                        out.flush();
                        
                    //Desplegar información de platillos    
                    } else if (obj instanceof String && obj.equals("modificarPlatillos")) {
                        ArrayList<String> adminPlatillo = avlTreePlatillo.inOrderTraversal();
                        System.out.println("Enviando lista de platillos: " + adminPlatillo);
                        out.writeObject(adminPlatillo); // enviar lista de cadenas de texto
                        out.flush();
                        out.writeBoolean(true); // enviar valor booleano
                        out.flush();
                    
                        
                    } else if (obj instanceof String && obj.equals("eliminarPlatillos")) {
                        ArrayList<String> adminPlatillo = avlTreePlatillo.inOrderTraversal();
                        System.out.println("Enviando lista de platillos: " + adminPlatillo);
                        out.writeObject(adminPlatillo); // enviar lista de cadenas de texto
                        out.flush();
                        out.writeBoolean(false); // enviar valor booleano
                        out.flush();
                                                     

                    } else if (obj instanceof Platillos nuevoPlatillo) {
                        //recibir la info del nuevo platillo que se creo y agregarla al arbol AVL
                        String data = nuevoPlatillo.getNombre() + ":" + nuevoPlatillo.getCantidadCalorias() + ":" +nuevoPlatillo.getTiempoPreparacion() + ":" + nuevoPlatillo.getPrecio();
                        avlTreePlatillo.insertElement(data);

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


                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        
                        
                        //Recibir mensaje para eliminar platillos
                        } else if (obj instanceof EliminarPlatillo PlatInfoEliminado) {
                        //recibir la info del nuevo platillo que se creo y agregarla al arbol AVL
                        String data = PlatInfoEliminado.getNombre() + ":" + PlatInfoEliminado.getCantidadCalorias() + ":" +PlatInfoEliminado.getTiempoPreparacion() + ":" + PlatInfoEliminado.getPrecio();
                        if (avlTreePlatillo.contains(data)){
                            avlTreePlatillo.removeElement(data);
                        }

                        // eliminar el platillo del archivo JSON
                        try (FileReader reader = new FileReader("platillos.json")) {
                            Type platilloListType = new TypeToken<ArrayList<Platillos>>(){}.getType();
                            Gson gson = new Gson();
                            ArrayList<Platillos> listaPlatillos = gson.fromJson(reader, platilloListType);
                            reader.close();

                            // buscar y eliminar el objeto correspondiente
                            for (int i = 0; i < listaPlatillos.size(); i++) {
                                Platillos platillo = listaPlatillos.get(i);
                                if (platillo.getNombre().equals(PlatInfoEliminado.getNombre())
                                    && platillo.getCantidadCalorias() == PlatInfoEliminado.getCantidadCalorias()
                                    && platillo.getTiempoPreparacion() == PlatInfoEliminado.getTiempoPreparacion()
                                    && platillo.getPrecio() == PlatInfoEliminado.getPrecio()) {
                                    listaPlatillos.remove(i);
                                    break;
                                }
                            }

                            // escribir la lista actualizada en el archivo
                            FileWriter writer = new FileWriter("platillos.json");
                            gson.toJson(listaPlatillos, writer);
                            writer.close();


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
    
/**
 * Método privado y estatico el cual permite procesar los diversos pedidos de la clase ClientApp
 */
private static void procesarPedidos() {
    ArduinoLEDControl.iniciarArduino();
    while (true) {
        if (!pedidosQueue.isEmpty()) {
            Pedido pedido = pedidosQueue.dequeue();
            System.out.println("Procesando pedido:");
            esperar(3000);
            ArduinoLEDControl.apagarLeds();
            esperar(3000);
            AtomicInteger tiempoTranscurrido = new AtomicInteger(0);
            AtomicInteger rangoProgresoAnterior = new AtomicInteger(0);

            AtomicInteger tiempoTotalPreparacion = new AtomicInteger(0);
            pedido.getPlatillos().forEach(platillo -> tiempoTotalPreparacion.addAndGet(platillo.getTiempoPreparacion()));

            pedido.getPlatillos().forEach(platillo -> {
                System.out.println("Preparando: " + platillo.getNombre());
                int tiempoPreparacion = platillo.getTiempoPreparacion();
                for (int i = 0; i < tiempoPreparacion; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    tiempoTranscurrido.incrementAndGet();
                    int progreso = (tiempoTranscurrido.get() * 100) / tiempoTotalPreparacion.get();
                    pedido.setProgreso(progreso);
                    System.out.println("Progreso del pedido: " + progreso + "%");
                    int tiempoRestante = tiempoTotalPreparacion.get() - tiempoTranscurrido.get();
                    System.out.println("Progreso del pedido: " + progreso + "%, Tiempo restante: " + tiempoRestante + " segundos");

                    int rangoProgresoActual = progreso / 25;
                    if (rangoProgresoActual > rangoProgresoAnterior.get()) {
                        rangoProgresoAnterior.set(rangoProgresoActual);
                        switch (rangoProgresoActual) {
                            case 1:
                                esperar(2000);
                                ArduinoLEDControl.encenderLeds25Porciento();
                                EncenderLuces25();
                                break;
                            case 2:
                                esperar(2000);
                                ArduinoLEDControl.encenderLeds50Porciento();
                                EncenderLuces50();
                                break;
                            case 3:
                                esperar(2000);
                                ArduinoLEDControl.encenderLeds75Porciento();
                                EncenderLuces75();
                                break;
                            case 4:
                                esperar(2000);
                                ArduinoLEDControl.encenderLeds100Porciento();
                                EncenderLuces100();
                                pedido.setEstado("Pendiente de entrega");
                                esperar(2000);
                                ArduinoLEDControl.restarUno();
                                esperar(2000);
                                ArduinoLEDControl.tocarBocinaCadaCuartoSeg();
                                esperar(3000);
                                enviarMensajeListo();
                                esperar(3000);
                                break;
                        }
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


/**
 * Método estático que se relaciona encer los leds cuando se está al 25%.
 */
private static void EncenderLuces25() {
    System.out.println("Encendiendo luces al 25%");
      // Implementar codigo de arduino
}

/**
 * Método estático que se relaciona encer los leds cuando se está al 50%.
 */
private static void EncenderLuces50() {
    System.out.println("Encendiendo luces al 50%");
    // Implementar codigo de arduino
}

/**
 * Método estático que se relaciona encer los leds cuando se está al 75%.
 */
private static void EncenderLuces75() {
    System.out.println("Encendiendo luces al 75%");
    // Implementar codigo de arduino
}

/**
 * Método estático que se relaciona encer los leds cuando se está al 100%.
 */
private static void EncenderLuces100() {
    System.out.println("Encendiendo luces al 100%");
    // Implementar codigo de arduino
}


/**
 * Méto estatico público relacionado a agregar pedidos a la cola
 * @param pedido este paremetro requiero un objeto de tipo pedidos, para ser añadidos.
 */
public static void agregarPedido(Pedido pedido) {
    
    pedidosQueue.enqueue(pedido);
    esperar(2000);
    ArduinoLEDControl.tocarBocina1Seg();
    esperar(3000);
    ArduinoLEDControl.sumarUno();
    System.out.println("Pedido encolado: " + pedido);
}


/**
 * Método privado estático que envio un mensaje de "Listo" al clientApp.
 */
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

/**
 * Método estático que genera una espera entre las instrucciones que se le dan al Arduino
 * @param milisegundos es el intervalo de tiempo que se desea utilizar.
 */
private static void esperar(int milisegundos) {
    try {
        Thread.sleep(milisegundos);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
}


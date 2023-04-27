package ServerApp;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ServerAppMain {
    public static void main(String[] args) throws ParserConfigurationException, SAXException {
        
        //Elementos necesario para la lectura del archivo XML dentro del server App
        File xmlFile = new File("administradores.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
        try {
            //Construccion de parametros necesarios para contruir herramientas para recorrer el archivo
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            
            NodeList nodeList = doc.getElementsByTagName("administrador");
            BinaryTree<String> tree = new BinaryTree<>();
            
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String username = element.getElementsByTagName("username").item(0).getTextContent();
                String contrasena = element.getElementsByTagName("contrasena").item(0).getTextContent();
                String data = username + ":" + contrasena;
                tree.insert(data);
            }
               
            
            // Crear el socket del servidor en el puerto 8080
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("Servidor iniciado");

            while (true) {
               // Esperar por una conexión del cliente
               Socket socket = serverSocket.accept();
               System.out.println("Cliente conectado");

               // Crear el stream de entrada para recibir la información del Administrador
               ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
               // Crear el stream de salida para enviar la respuesta al cliente
               ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

               while (true) {
                   // Leer la información de inicio de sesión
                   Administrador loginInfo = (Administrador) in.readObject();

                   // Buscar el elemento en el árbol
                   boolean encontrado = tree.contains(loginInfo.getUsername() + ":" + loginInfo.getContrasena());

                   // Enviar la respuesta al cliente
                   out.writeObject(encontrado);
                   out.flush();

                   // Verificar si se recibió el comando de salida
                   if (loginInfo.getUsername().equals("exit") && loginInfo.getContrasena().equals("exit")) {
                       break; // Salir del ciclo interno
                   }
               }

                // Cerrar el stream y el socket (Si se cierra la aplicación tambine se cierra a la fuerza)
                in.close();
                socket.close();
                System.out.println("Cliente desconocetado del servidor");
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Error handling login request: " + ex.getMessage());
        }
    }
}

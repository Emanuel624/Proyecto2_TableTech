package ServerApp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerAppMain {
    public static void main(String[] args) {
        try {
            // Crear el socket del servidor en el puerto 8080
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("Server started");

            while (true) {
                // Esperar por una conexión del cliente
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");

                // Crear el stream de entrada para recibir la información del cliente
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                while (true) {
                    // Leer la información de inicio de sesión y mostrarla en la consola
                    Administrador loginInfo = (Administrador) in.readObject();
                    System.out.println("Username: " + loginInfo.getUsername());
                    System.out.println("Password: " + loginInfo.getContrasena());

                    // Verificar si se recibió el comando de salida
                    if (loginInfo.getUsername().equals("exit") && loginInfo.getContrasena().equals("exit")) {
                        break; // Salir del ciclo interno
                    }
                }

                // Cerrar el stream y el socket
                in.close();
                socket.close();
                System.out.println("Client disconnected");
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Error handling login request: " + ex.getMessage());
        }
    }
}

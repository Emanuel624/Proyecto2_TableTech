package ServerApp;
import java.net.Socket;

public class SocketHandler {
    private static Socket socket;

    public static void setSocket(Socket socket) {
        SocketHandler.socket = socket;
    }

    public static Socket getSocket() {
        return socket;
    }
}

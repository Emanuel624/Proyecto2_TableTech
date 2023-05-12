package ServerApp;

import com.fazecast.jSerialComm.*;

public class ArduinoLEDControl {
    private static SerialPort arduinoPort;

public static void iniciarArduino() {
        arduinoPort = SerialPort.getCommPort("COM3");
        arduinoPort.openPort();
        arduinoPort.setComPortParameters(9600, 8, 1, 0);
        System.out.println("Arduino Conectado");

        // Espera a que Arduino esté listo para recibir datos
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void encenderLeds25Porciento() {
        enviarPorcentaje(25);
    }

    public static void encenderLeds50Porciento() {
        enviarPorcentaje(50);
    }

    public static void encenderLeds75Porciento() {
        enviarPorcentaje(75);
    }

    public static void encenderLeds100Porciento() {
        enviarPorcentaje(100);
    }

    public static void apagarLeds() {
        enviarPorcentaje(0);
    }
    
    public static void sumarUno() {
        enviarValor(103);
    }

    public static void restarUno() {
        enviarValor(104);
    }
    public static void tocarBocina1Seg() {
            enviarValor(101);
        }

        public static void tocarBocinaCadaCuartoSeg() {
            enviarValor(102);
        }

        private static void enviarValor(int valor) {
            String valorStr = String.valueOf(valor);
            arduinoPort.writeBytes(valorStr.getBytes(), valorStr.length());
        }
        private static void enviarPorcentaje(int porcentaje) {
            String porcentajeStr = String.valueOf(porcentaje);
            arduinoPort.writeBytes(porcentajeStr.getBytes(), porcentajeStr.length());
        }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}





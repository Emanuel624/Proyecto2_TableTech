package ServerApp;

import com.fazecast.jSerialComm.*;

/**
 * @authors Randall Bryan Bolañoz López, Octavio Sanchez Soto, Emanuel Chavarría Hernández.
 * @version 1.0
 */

/**
 * Esta clase publica, relacionada con la comunicación serial entre Java y Arduino.
 */
public class ArduinoLEDControl {
    private static SerialPort arduinoPort;

    
/**
 * Este método permite inicializar la comunicación con el Arduino, la biblioteca jserialComms y Java. 
 */    
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
    

    /**
     * Este método estatico, envia el mensaje al arduino para encender la led asignada al porcentaje de 25%
     */
    public static void encenderLeds25Porciento() {
        enviarPorcentaje(25);
    }
    
    
    /**
     * Este método estatico, envia el mensaje al arduino para encender las leds asignadas al porcentaje de 50%
     */
    public static void encenderLeds50Porciento() {
        enviarPorcentaje(50);
    }
    
    
    /**
     * Este método estatico, envia el mensaje al arduino para encender las leds asignadas al porcentaje de 75%
     */
    public static void encenderLeds75Porciento() {
        enviarPorcentaje(75);
    }
    
    /**
     * Este método estatico, envia el mensaje al arduino para encender las leds asignadas al porcentaje de 100%
     */
    public static void encenderLeds100Porciento() {
        enviarPorcentaje(100);
    }
    
    
    /**
     * Este método estatico, envia el mensaje al arduino para apagar las Leds.
     */
    public static void apagarLeds() {
        enviarPorcentaje(0);
    }
    
    
    /**
     * Este método estatico, envia el mensaje al arduino para sumar un valor a la lógica dentro del arduino.
     */
    public static void sumarUno() {
        enviarValor(103);
    }
    
    
    /**
     * Este método estatico, envia el mensaje al arduino para restar un valor a la lógica dentro del arduino.
     */
    public static void restarUno() {
        enviarValor(104);
    }
    
    
    /**
     * Este método estatico, envia el mensaje al arduino para emitir un sonido por medio del buffer.
     */
    public static void tocarBocina1Seg() {
            enviarValor(101);
        }
        
    
        /**
         * Este método estatico, envia el mensaje al arduino para emitir un sonido por medio del buffer cada 4 segundos.
         */
        public static void tocarBocinaCadaCuartoSeg() {
            enviarValor(102);
        }
        
        
        /**
         * Este método estatico, permite enviar un valor al arduino para poder ser recibido dentro de la lógica del Arduino como tal.
         * @param valor el parametro recibido debe ser un int, para poder ser enviado al arduino.
         */
        private static void enviarValor(int valor) {
            String valorStr = String.valueOf(valor);
            arduinoPort.writeBytes(valorStr.getBytes(), valorStr.length());
        }
        
        
        /**
         * Este método estatico, permite enviar un valor en forma de porcentaje al arduino para poder ser recibido dentro de la lógica del Arduino como tal.
         * @param porcentaje el parametro recibido debe ser un int en forma de porcentaje, para poder ser enviado al arduino.
         */
        private static void enviarPorcentaje(int porcentaje) {
            String porcentajeStr = String.valueOf(porcentaje);
            arduinoPort.writeBytes(porcentajeStr.getBytes(), porcentajeStr.length());
        }
        
        
    /**
     * Este método estatico, permite poner al arduino en tiempo de "espera" o añadir un delay al proceso como tal.
     * @param millis el parametro recibido debe ser un int, para poder ser enviado al arduin.o
     */    
    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}





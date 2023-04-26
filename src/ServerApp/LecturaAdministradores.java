
package ServerApp;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LecturaAdministradores {

    public static void main(String[] args) {
        try {
            File archivoXML = new File("administradores.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document documento = dBuilder.parse(archivoXML);
            documento.getDocumentElement().normalize();

            NodeList listaAdministradores = documento.getElementsByTagName("administrador");

            for (int i = 0; i < listaAdministradores.getLength(); i++) {
                Node nodoAdministrador = listaAdministradores.item(i);

                if (nodoAdministrador.getNodeType() == Node.ELEMENT_NODE) {
                    Element elementoAdministrador = (Element) nodoAdministrador;

                    String id = elementoAdministrador.getAttribute("id");
                    String usuario = elementoAdministrador.getElementsByTagName("username").item(0).getTextContent();
                    String contrasena = elementoAdministrador.getElementsByTagName("contrasena").item(0).getTextContent();

                    System.out.println("Administrador con id " + id);
                    System.out.println("Username: " + usuario);
                    System.out.println("Contraseña: " + contrasena);
                }
            }
        } catch (IOException | ParserConfigurationException | DOMException | SAXException e) {
            e.printStackTrace();
        }
    }

}


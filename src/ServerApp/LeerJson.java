package ServerApp;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.Iterator;

public class LeerJson {
    public static void main(String[] args) {

        try (Reader reader = new FileReader("platillos.json")) {

            JsonArray jsonArray = (JsonArray) Jsoner.deserialize(reader);

            for (Object obj: jsonArray){
                JsonObject jsonObject = (JsonObject) obj;
                System.out.println(obj);

                String nombre = (String) jsonObject.get("nombre");
                System.out.println("nombre:"+ "" + nombre);

                BigDecimal calorias = (BigDecimal) jsonObject.get("cantidadCalorias");
                System.out.println("calorias:" + "" + calorias);

                BigDecimal precio = (BigDecimal) jsonObject.get("precio");
                System.out.println("precio:" + "" + precio);

                BigDecimal tiempo = (BigDecimal) jsonObject.get("tiempoPreparacion");
                System.out.println("tiempo:" + "" + tiempo);

        }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonException e) {
            throw new RuntimeException(e);
        }

    }
}


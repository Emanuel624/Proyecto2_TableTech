package ServerApp;


import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CrearJson {
    public static void main (String[] args) throws IOException, ParseException {
        try {

            FileReader reader = new FileReader("platillos.json");

            reader.close();
            JSONObject obj = new JSONObject();
            obj.put("nombre", "Huevo frito");
            obj.put("cantidadCalorias", 100);
            obj.put("precio", 200);
            obj.put("tiempoPreparacion", 1000);


            FileWriter file = new FileWriter("platillos.json", true);
            file.write(obj.toJSONString());
            file.flush();
            System.out.println(obj);

        }catch (IOException e){
            e.printStackTrace();
        }

    }
}

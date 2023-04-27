package ServerApp;


import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

import java.io.FileWriter;
import java.io.IOException;

public class CrearJSON {
    public void crearJSON(){

        JsonObject obj = new JsonObject();
        obj.put("nombre","Gallo pinto");
        /*
        JsonArray list = new JsonArray();
        list.add("Gallo pinto");
        list.add("Huevo frito");
        obj.put("nombres", list);

         */
        try (FileWriter file = new FileWriter("platillos.json")){
            file.write(obj.toString());
            file.flush();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        System.out.println(obj);
    }
}

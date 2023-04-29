package ServerApp;


import MasterApp.Platillos;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class CrearJson {
    public static void main (String[] args) throws IOException, ParseException {
        try(FileReader reader = new FileReader("platillos.json")){
            Type platilloListType = new TypeToken<ArrayList<Platillos>>(){}.getType();
            Gson gson = new Gson();
            ArrayList<Platillos> listaPlatillos = gson.fromJson(reader, platilloListType);
            reader.close();
            //String nombre = txtNombre.getText();
            //int calorias = Integer.parseInt(txtCalorias.getText());
            //int precio = Integer.parseInt(txtPrecio.getText());
            //int tiempo = Integer.parseInt(txtTiempo.getText());
            listaPlatillos.add(new Platillos("Gallo pinto", 120, 2000, 200));
            FileWriter writer = new FileWriter("platillos.json");
            gson.toJson(listaPlatillos, writer);
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }


    }
}

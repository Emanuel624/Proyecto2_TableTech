package ServerApp;

import MasterApp.Platillos;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/*
public class LeerJson {
    public static void main(String[] args) {
        //HOLA

        AvlTree<String> avlTree = new AvlTree<>();
        java.util.ArrayList<Platillos> listaPlatillos;
        try {
            FileReader fileReader = new FileReader("platillos.json");
            Type type = new TypeToken<ArrayList<Platillos>>(){}.getType();
            Gson gson = new Gson();
            listaPlatillos = gson.fromJson(fileReader,type);
            fileReader.close();
            //for (Platillos p : listaPlatillos){
                System.out.println("nombre:"+ p.getNombre() + "" + "calorias:" + "" + p.getCantidadCalorias() + "precio:" + p.getPrecio()
                        + "tiempo:" + p.getTiempoPreparacion());

            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }//hola
}

 */


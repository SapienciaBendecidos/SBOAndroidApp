package link.software.nfcapp;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonFileActions {
    public boolean writeToFile(){
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(dir, "sboEbenezer/example.txt");

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.append("Writing to file!");
            fileWriter.flush();

            return true;
        } catch (IOException e) {
            //Handle exception
            return false;
        }
    }

    public ArrayList<HashMap<String, String>> readJsonFile(){
        ArrayList<HashMap<String, String>> parsedDataList = null;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File yourFile = new File(dir, "sboEbenezer/clientes.json");
            FileInputStream stream = new FileInputStream(yourFile);
            String jsonStr = null;

            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                jsonStr = Charset.defaultCharset().decode(bb).toString();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            finally {
                stream.close();
            }

            JSONObject jsonObj = new JSONObject(jsonStr);

            // Getting data JSON Array nodes
            JSONArray data  = jsonObj.getJSONArray("data");

            // looping through All nodes
            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);

                String id = c.getString("idCliente");
                String title = c.getString("primerNombre");
                //String duration = c.getString("segundoNombre");

                // tmp hashmap for single node
                HashMap<String, String> parsedData = new HashMap<String, String>();

                // adding each child node to HashMap key => value
                parsedData.put("idCliente", id);
                parsedData.put("primerNombre", title);
                //parsedData.put("segundoNombre", duration);
                // do what do you want on your interface
                parsedDataList.add(parsedData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return parsedDataList;
    }
}

package link.software.nfcapp;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;


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

    public String readJsonFile(){
        String json = null;

        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File file = new File(dir, "sboEbenezer/clientes.json");
            InputStream is = new FileInputStream(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }
}

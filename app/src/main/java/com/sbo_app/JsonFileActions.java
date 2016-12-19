package com.sbo_app;

import android.os.Environment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class JsonFileActions {
    private String setPath;

    public JsonFileActions(){
        setPath = "0/Android/data/com.sbo_app/files/";
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        //Android/data/com.sbo_app/files
        String abPath = dir.getAbsolutePath();
        String [] dirs = abPath.split("/");

        String newPath = "";

        for(int i = 0; i < 3; ++i)
            newPath += dirs[i] + "/";

        newPath += setPath;
        setPath = newPath;
    }

    public String writeToFile(){

        String newPath = setPath + "examples.txt";
        File file = new File(newPath);

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.append("Writing to file by Raim!");
            fileWriter.flush();

            return newPath;
        } catch (IOException e) {
            //Handle exception
            return newPath + "hey!";
        }
    }

    public String readJsonFile(){
        String json;

        try {
            File file = new File(setPath + "cardsInformation.txt");
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

package com.sbo_app;

import android.os.Environment;

import org.json.JSONObject;

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
        this.setPath = newPath;
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
            return "Dir not found!";
        }
    }
    public String writeToTripsFile(JSONObject tripJson){
        /*JSONObject jo = new JSONObject();
        JSONArray ja = new JSONArray();
        JSONObject mainObj = new JSONObject();

        try{
            jo.put("firstName", "John");
            jo.put("lastName", "Doe");
            ja.put(jo);
            mainObj.put("employees", ja);
        }catch (JSONException e){
            return "Couldn't write JSON!";
        }*/

        String newPath = setPath + "trips/newTrip.txt";
        File file = new File(newPath);
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.append(tripJson.toString());
            fileWriter.flush();

            return "Written!";
        }catch(IOException e){
            return "Couldn't write!";
        }
    }

    public String readJsonFile(String fileName){
        String json;

        try {
            File file = new File(setPath + fileName);
            InputStream is = new FileInputStream(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return "Not found!" + setPath + fileName;
        }

        return json;
    }
}

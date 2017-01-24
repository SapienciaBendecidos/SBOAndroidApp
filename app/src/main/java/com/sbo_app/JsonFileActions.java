package com.sbo_app;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class JsonFileActions {
    private String setPath;

    public JsonFileActions(){
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        String abPath = dir.getAbsolutePath();

        this.setPath = abPath;
        File tripsDir = new File(setPath + "/trips");
        if(!tripsDir.exists()){
            tripsDir.mkdir();
            System.out.println("Created trips dir");
        }
    }

    public String writeToFile(){

        String newPath = setPath + "/trips/examples.txt";
        File file = new File(newPath);

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.append("Writing to file by Raim!");
            fileWriter.flush();
            System.out.println("Wrote file in trips dir");
            return newPath;
        } catch (IOException e) {
            //Handle exception
            System.out.println(e.getMessage());
            return "Dir not found!";
        }
    }

    public String write(JSONObject tripJson, String path){
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

        String newPath = setPath + path;
        File file = new File(newPath);
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.append(tripJson.toString());
            fileWriter.flush();

            return "Written!";
        }catch(IOException e){
            return "Couldn't write! "+ e.getMessage();
        }
    }

    public String writeToFile(String data,String fileName,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            return "Written!";
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
            return e.getMessage();
        }
    }

    public  String readFromFile(String fileName,Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(fileName);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
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

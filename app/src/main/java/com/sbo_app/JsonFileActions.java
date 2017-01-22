package com.sbo_app;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static android.content.Context.MODE_PRIVATE;

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

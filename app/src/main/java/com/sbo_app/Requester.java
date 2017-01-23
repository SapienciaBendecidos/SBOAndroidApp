package com.sbo_app;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Requester {
    static final String API_URL = "http://9611b196.ngrok.io/";
    public Requester(){}

    public String getRutas(){
        //System.out.println("aqui");
        HttpURLConnection request;
        try {
            URL reqURL = new URL("http://9611b196.ngrok.io/api/Rutas"); //the URL we will send the request to
            request = (HttpURLConnection) (reqURL.openConnection());
            request.setRequestMethod("GET");
            try {
                request.connect();
                //System.out.println(request.getResponseMessage());
                return request.getResponseMessage();
            }finally {
                request.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return "error";
        //return request("api/Rutas");
    }

    private String request(String path){
        try {
            URL url = new URL(API_URL + path);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            }catch(Exception e){
                return e.getMessage();
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return e.getMessage();
        }
    }

}


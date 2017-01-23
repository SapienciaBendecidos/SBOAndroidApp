package com.sbo_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import link.software.nfcapp.R;


import link.software.nfcapp.R;

import static com.sbo_app.Requester.API_URL;

public class HomeActivity extends AppCompatActivity {
    private JsonFileActions jsonFileAction;
    private Requester requester;
    String data="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        jsonFileAction = new JsonFileActions();
        requester = new Requester();
        findViewById(R.id.cargarRutasButton).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(HomeActivity.this,
                                RutasActivity.class));
                    }
                });
    }

    public void writeRutas(View view){
        /*String rutas = "["+
                "{\"nombre\" : \"Planeta\", \"idRuta\" : 1, \"costo\": 25}"+
                "{\"nombre\" : \"Juan Lindo\", \"idRuta\" : 2, \"costo\": 8}"+
        "]";*/
        try {
            String data = requester.getRutas();//"[{\"nombre\":\"Planeta\",\"idRuta\":\"1\", \"costo\": \"25\"}, {\"nombre\":\"Country\",\"idRuta\":\"2\", \"costo\": \"23\"} ]";
            //String returned = jsonFileAction.writeToFile(data, "routes.txt",getApplicationContext());
            //Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
            new RetrieveFeedTask().execute();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {


        protected void onPreExecute() {
            System.out.println("Begining rutas' reuquest");
        }

        protected String doInBackground(Void... urls) {

            try {
                URL url = new URL(API_URL + "api/Rutas");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    data = stringBuilder.toString();
                    System.out.println(data);
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            String returned = jsonFileAction.writeToFile(data, "routes.txt",getApplicationContext());
            System.out.println(returned);
            Toast.makeText(HomeActivity.this, data, Toast.LENGTH_SHORT).show();
        }
    }

}



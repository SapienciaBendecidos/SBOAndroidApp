package com.sbo_app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.apache.http.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.HttpURLConnection;

import link.software.nfcapp.R;

import static android.R.attr.path;

public class MainActivity extends AppCompatActivity {

    EditText emailText;
    TextView responseView;
    ProgressBar progressBar;
    static final String API_URL = "https://api.github.com/users/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        responseView = (TextView) findViewById(R.id.responseView);
        emailText = (EditText) findViewById(R.id.emailText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Button queryButton = (Button) findViewById(R.id.queryButton);
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new RetrieveFeedTask().execute();
                    PostAction();
            }
        });
    }

    public void PostAction(){
        /*try{
            String urlT = "http://9611b196.ngrok.io/api/Viajes/postVariousTransactions/";
            URL url = new URL(urlT);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
*/
            //String input = "{\"routeId\":96,\"routeDirection\":\"entrada\",\"busPlate\":\"89222\",\"routeName\":\"Span\", " +
              //      "\"date\":1482190498933,\"state\":\"available\",\"passengers\":[{\"idTarjeta\":2},{\"idTarjeta\":5},{\"idTarjeta\":8}]}";
/*
            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output = "";
            //System.out.println("Output from Server .... \n");
            while ((output += br.readLine()) != null) {
                //System.out.println(output);
            }

            conn.disconnect();
            Toast.makeText(this, output.toString(), Toast.LENGTH_LONG).show();
        }catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
*/
        try{
            //instantiates httpclient to make request
            DefaultHttpClient httpclient = new DefaultHttpClient();

            //url with the post data
            HttpPost httpost = new HttpPost("http://9611b196.ngrok.io/api/Rutas");

            //convert parameters into JSON object
            //JSONObject holder = getJsonObjectFromMap(params);

            //passes the results to a string builder/entity
            String input = "{\"nombre\": \"ruta2\","
                      + "\"descripcion\": \"por ese lado\","
                      + "\"idRuta\": 1,"
                      + "\"costo\": 100}";

            StringEntity se = new StringEntity(input.toString());

            //sets the post request as the resulting string
            httpost.setEntity(se);
            //sets a request header so the page receving the request
            //will know what to do with it
            httpost.setHeader("Accept", "application/json");
            httpost.setHeader("Content-type", "application/json");

            //Handles what is returned from the page
            ResponseHandler responseHandler = new BasicResponseHandler();
            httpclient.execute(httpost, responseHandler);
        }catch(Exception e){
            Toast.makeText(this, "Error con post: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            responseView.setText("");
        }

        protected String doInBackground(Void... urls) {

            // Do some validation here

            /*try {
                URL url = new URL(API_URL + email);
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
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }*/

            try{
                String url = "http://9611b196.ngrok.io/api/Viajes/postVariousTransactions";
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                //add request header
                con.setRequestMethod("POST");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("Content-Type", "application/json");

                String urlParameters = "{\"routeId\":96,\"routeDirection\":\"entrada\",\"busPlate\":\"89222\",\"routeName\":\"Span\", " +
                        "\"date\":1482190498933,\"state\":\"available\",\"passengers\":[{\"idTarjeta\":2},{\"idTarjeta\":5},{\"idTarjeta\":8}]}";

                // Send post request
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                int responseCode = con.getResponseCode();
                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters);
                System.out.println("Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                String code = "" + responseCode;
                //response.append("" + responseCode);
                in.close();

                //print result
                //System.out.println(response.toString());
                //Toast.makeText(this, response.toString(), Toast.LENGTH_LONG).show();

                //return response.toString();
                return code;
            }catch(Exception e){
                //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

                return e.getMessage();
            }
        }

        protected void onPostExecute(String response) {
            response = response == null ? "ERROR, ERROR!" : "Got something!" + response;
            progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);
            responseView.setText(response);
        }
    }

}

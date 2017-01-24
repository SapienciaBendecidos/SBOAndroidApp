package com.sbo_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import link.software.nfcapp.R;
import java.util.Date;
import java.util.ListIterator;

import link.software.nfcapp.R;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class HomeActivity extends AppCompatActivity {
    private JsonFileActions jsonFileAction;
    private static String url = "http://9611b196.ngrok.io/api";
    private RequestInterceptor requestInterceptor;
    private RestAdapter radapter;
    private EndPointsInterface restInt;
    String data="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        jsonFileAction = new JsonFileActions();
        requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Accept", "application/json");
                request.addHeader("Content-Type", "application/json");
            }
        };

        radapter = new RestAdapter.Builder()
                .setRequestInterceptor(requestInterceptor)
                .setEndpoint(url).
                        build();

        restInt = radapter.create(EndPointsInterface.class);
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
            /*restInt.getRutas(new Callback<List<Ruta>>() {
                @Override
                public void success(List<Ruta> model, Response response) {
                    String rutas = new Gson().toJson(model);
                    String returned = jsonFileAction.writeToFile(rutas, "routes.txt",getApplicationContext());
                    //System.out.println(returned);
                    Toast.makeText(HomeActivity.this, returned, Toast.LENGTH_SHORT).show();
                }
                @Override
                public void failure(RetrofitError error) {
                    String err = error.getMessage();
                    System.out.println("Error: " + err);
                    Toast.makeText(HomeActivity.this, err, Toast.LENGTH_SHORT).show();
                }
            });

            restInt.getCards(new Callback<Cards>() {
                @Override
                public void success(Cards cards, Response response) {
                    String cardsInfo = new Gson().toJson(cards.getGetWithClient());
                    String returned = jsonFileAction.writeToFile(cardsInfo, "cardsInformation.txt",getApplicationContext());
                    Toast.makeText(HomeActivity.this, returned, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void failure(RetrofitError error) {
                    String err = error.getMessage();
                    System.out.println("Error: " + err);
                    Toast.makeText(HomeActivity.this, err, Toast.LENGTH_SHORT).show();
                }
            });*/
            TripRequest trip = new TripRequest();
            trip.setBusConductor("nada");
            trip.setBusPlaca("123asd");
            trip.setIdRuta(new Integer(5));
            trip.setTipoMovimiento("entrada");
            trip.setFecha(new Date().toString());
            List<Integer> cards = new ArrayList<Integer>();
            cards.add(new Integer(1));
            cards.add(new Integer(3));
            cards.add(new Integer(2));
            trip.setTransacciones(cards);

            restInt.postTrip(trip, new Callback<TripRequest>() {
                @Override
                public void success(TripRequest tripRequest, Response response) {
                    Toast.makeText(HomeActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}



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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
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

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.squareup.okhttp.Protocol.get;


public class HomeActivity extends AppCompatActivity {
    private JsonFileActions jsonFileAction;
    private static String url = /*"http://dba6a8f6.ngrok.io/api";*/"http://45.55.145.116:3000/api";
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
                request.addHeader("Authorization","PmBosIUDHTtRGwIxaRwppFswWKCjsqd9aQssLNBNP9o8Amn2phZomYnrYJHeeQDf");
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
            restInt.getRutas(new Callback<List<Ruta>>() {
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
            });
            /*TripRequest trip = new TripRequest();
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
            });*/
            //String tripData = "{\"idRuta\": 5, \"busPlaca\": \"nose\", \"busConductor\":\"luis\", \"tipoMovimiento\": \"entrada\", \"fecha\": \"2017-01-24T05:47:58.000Z\", \"transacciones\":[\"0bwMcjlzjkR\",\"0ETWGgz\",\"0fFB3pw\"]}";
            //String returned = jsonFileAction.writeTrip(tripData, "/trips/trip1.txt");
            //Toast.makeText(HomeActivity.this, returned, Toast.LENGTH_SHORT).show();

            //String data =jsonFileAction.readJsonFile("/trips/trip1.txt");
            //JSONObject jsonObject = new JSONObject(data);
            //Toast.makeText(HomeActivity.this, jsonObject.getString("busConductor"), Toast.LENGTH_SHORT).show();
            //JSONArray array = jsonObject.getJSONArray("transacciones");
            //for(int i =0; i < array.length();i++){
              //  int cardID = Integer.parseInt();
                //Toast.makeText(HomeActivity.this, "id: "+array.get(0), Toast.LENGTH_SHORT).show();
            //}

            List<String>array = new ArrayList<String>();
            boolean encontro = jsonFileAction.getTripsName(array);
            if(encontro){
                for(int i =0; i < array.size();i++){
                    final String name = array.get(i);
                    String data =jsonFileAction.readJsonFile("/trips/"+name);
                    JSONObject jsonObject = new JSONObject(data);
                    TripRequest trip = new TripRequest();
                    trip.setBusConductor(jsonObject.getString("driverName"));
                    trip.setBusPlaca(jsonObject.getString("busPlate"));
                    trip.setIdRuta(jsonObject.getInt("routeId"));
                    trip.setTipoMovimiento(jsonObject.getString("routeDirection"));
                    trip.setFecha(jsonObject.getString("date"));
                    List<String> cards = new ArrayList<String>();
                    JSONArray trans = jsonObject.getJSONArray("passengers");
                    for(int j=0; j< trans.length();j++){
                        JSONObject passenger = trans.getJSONObject(i);
                        Toast.makeText(HomeActivity.this, "idss: "+passenger.getString("idTarjeta"), Toast.LENGTH_SHORT).show();
                        cards.add(passenger.getString("idTarjeta"));
                    }
                    trip.setTransacciones(cards);

                    restInt.postTrip(trip, new Callback<TripRequest>() {
                        @Override
                        public void success(TripRequest tripRequest, Response response) {
                            boolean deleted = jsonFileAction.deleteFile(name);
                            Toast.makeText(HomeActivity.this, deleted?"Posted: "+name: "error al borrar", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    //Toast.makeText(HomeActivity.this, "ids: "+array.get(i), Toast.LENGTH_SHORT).show();
                }
            }else
                Toast.makeText(HomeActivity.this, "No hay viajes pendientes ", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}



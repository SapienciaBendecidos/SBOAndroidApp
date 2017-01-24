package com.sbo_app;
import android.telecom.Call;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
public interface EndPointsInterface {
    @GET("/Rutas")
    void getRutas(Callback<List<Ruta>> rutaCb);

    @POST("/Rutas")
    void createRuta(@Body Ruta ruta, Callback<Ruta> rutaCb);

    @GET("/Tarjetas/getWithClient")
    void getCards(Callback<Cards> cardsCb);
}

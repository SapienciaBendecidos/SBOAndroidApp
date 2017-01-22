package com.sbo_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import link.software.nfcapp.R;


import link.software.nfcapp.R;

public class HomeActivity extends AppCompatActivity {
    private JsonFileActions jsonFileAction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        jsonFileAction = new JsonFileActions();
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
            String data = "[{\"nombre\":\"Planeta\",\"idRuta\":\"1\", \"costo\": \"25\"}, {\"nombre\":\"Country\",\"idRuta\":\"2\", \"costo\": \"23\"} ]";
            String returned = jsonFileAction.writeToFile(data, "routes.txt",getApplicationContext());
            Toast.makeText(this, returned, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

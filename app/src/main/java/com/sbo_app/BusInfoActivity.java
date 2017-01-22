package com.sbo_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import link.software.nfcapp.R;

import static android.R.attr.data;
import static link.software.nfcapp.R.id.idRuta;

public class BusInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_info);

        try {
            Intent intent = getIntent();
            int idRuta = intent.getIntExtra("idRuta", 0);
            Toast.makeText(this, "id ruta: "+idRuta, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

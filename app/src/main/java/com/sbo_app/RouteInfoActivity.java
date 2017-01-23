package com.sbo_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import junit.runner.Version;

import link.software.nfcapp.R;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class RouteInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_info);
        Intent intent = getIntent();
        final int idRuta = intent.getIntExtra("idRuta", 0);
        final String nombre = intent.getStringExtra("nombre");
        findViewById(R.id.entranceButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setClass(RouteInfoActivity.this, BusInfoActivity.class);
                intent.putExtra("routeDirection", 1);
                intent.putExtra("idRuta", idRuta);
                intent.putExtra("nombre", nombre);
                startActivity(intent);
            }
        });

        findViewById(R.id.exitButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setClass(RouteInfoActivity.this, BusInfoActivity.class);
                intent.putExtra("routeDirection", 2);
                intent.putExtra("idRuta", idRuta);
                intent.putExtra("nombre", nombre);
                startActivity(intent);
            }
        });
    }
}

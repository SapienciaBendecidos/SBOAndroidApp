package com.sbo_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import link.software.nfcapp.R;

import static android.R.attr.data;
import static android.R.attr.name;
import static link.software.nfcapp.R.id.idRuta;
import static link.software.nfcapp.R.id.idRutaText;

public class BusInfoActivity extends AppCompatActivity {

    private TextView idRutaText;
    private TextView nombreText;
    private TextView dirText;
    private EditText plateField;
    private int idRuta;
    private int dir;
    private String nombre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_info);
        idRutaText = (TextView)findViewById(R.id.idRutaText);
        nombreText = (TextView)findViewById(R.id.nombreText);
        dirText = (TextView)findViewById(R.id.direccionText);
        plateField = (EditText) findViewById(R.id.plateEditText);

        try {
            Intent intent = getIntent();
            idRuta = intent.getIntExtra("idRuta", 0);
            nombre = intent.getStringExtra("nombre");
            dir = intent.getIntExtra("routeDirection", 0);
            idRutaText.setText(idRutaText.getText().toString() + idRuta);
            nombreText.setText(nombreText.getText().toString()+nombre);
            dirText.setText(dirText.getText().toString() + (dir==1? "Entrada":"Salida"));
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        findViewById(R.id.continuarButton).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String plate = plateField.getText().toString();
                        Intent intent = new Intent();
                        intent.setClass(BusInfoActivity.this, ScanActivity.class);
                        intent.putExtra("idRuta", idRuta);
                        intent.putExtra("nombre", nombre);
                        intent.putExtra("rutaDir", dir);
                        intent.putExtra("plate", plate);
                        startActivity(intent);
                    }
                });
    }
}

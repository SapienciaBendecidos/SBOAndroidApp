package com.sbo_app;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import link.software.nfcapp.R;

import static android.R.attr.data;

public class RutasActivity extends AppCompatActivity {

    private JsonFileActions jsonFileActions;
    private ListView lv;

    private ArrayList<HashMap<String, String>> lista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutas);

        jsonFileActions = new JsonFileActions();
        lv = (ListView)findViewById(R.id.list);
        lista = new ArrayList<>();
        fillListofRutas();

        // ListView Item Click Listener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item value
                HashMap<String, String> itemValue    = (HashMap<String, String>) lv.getItemAtPosition(position);
                String idRuta = itemValue.get("idRuta");
                String nombre = itemValue.get("nombre");
                // Then you start a new Activity via Intent
                Intent intent = new Intent();
                intent.setClass(RutasActivity.this, RouteInfoActivity.class);
                intent.putExtra("idRuta", Integer.parseInt(idRuta));
                intent.putExtra("nombre", nombre);
                startActivity(intent);
            }

        });
    }

    private void fillListofRutas(){
        try {
            String data =jsonFileActions.readFromFile("routes.txt" , getApplicationContext());
            //Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
            JSONArray jsonarray = new JSONArray(data);
            fillListAdapter(jsonarray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillListAdapter(JSONArray jsonarray)  throws JSONException {

        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject ruta = jsonarray.getJSONObject(i);
            String idRuta = ruta.getString("idRuta");
            String nombre = ruta.getString("nombre");
            HashMap<String, String> rutaItem = new HashMap<>();
            rutaItem.put("idRuta", idRuta);
            rutaItem.put("nombre", nombre.toString());
            lista.add(rutaItem);
        }
        setListViewAdapter();

    }

    private void setListViewAdapter() {
        ListAdapter adapter = new SimpleAdapter(RutasActivity.this, lista,
                R.layout.rutas_list_item, new String[]{"idRuta", "nombre"},
                new int[]{R.id.idRuta, R.id.nombre});

        lv.setAdapter(adapter);
    }
}

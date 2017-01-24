package com.sbo_app;

import android.media.AudioManager;
import android.media.SoundPool;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import link.software.nfcapp.R;

public class ScanActivity extends AppCompatActivity {
    //UI and NFC components
    private NfcAdapter nfcAdapter;
    private Button btnCantPasajeros;
    private EditText txtTagContent;
    private Button btnWrite;
    private JsonFileActions jsonFileAction;
    private ListView viajerosListView;
    //Attributes and lists
    private String currentId;
    private ArrayList<HashMap<String, String>> clientes;
    private int cantidadDePasajeros;
    private final String cantPasajerosText = "Cantidad de pasajeros: ";
    private String jsonTripsString;
    private Trip trip;
    private ArrayList<HashMap<String, String>> listaViajeros = new ArrayList<>();
    //SoundPool attributes
    private SoundPool soundPool;
    private int soundID;
    private boolean soundLoaded = false;
    public static final int MAX_NUMBER_STREAMS = 2;
    public static final int SOURCE_QUALITY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        btnCantPasajeros = (Button) findViewById(R.id.tglCantPasajeros);
        txtTagContent = (EditText) findViewById(R.id.txtTagContent);
        btnWrite = (Button) findViewById(R.id.btnWrite);
        viajerosListView = (ListView) findViewById(R.id.list);
        jsonFileAction = new JsonFileActions();
        clientes = new ArrayList<>();
        currentId = "";
        cantidadDePasajeros = 0;
        btnCantPasajeros.setText(cantPasajerosText + cantidadDePasajeros);
        initTripInfo();
        initSound();
        initLoadButton();
    }

    private void initTripInfo() {
        try {
            Intent intent = getIntent();
            int idRuta = intent.getIntExtra("idRuta", 0);
            String nombre = intent.getStringExtra("nombre");
            int dir = intent.getIntExtra("routeDirection", 0);
            String direccion = dir == 1 ? "Entrada" : "Salida";
            String plate = intent.getStringExtra("plate");

            this.trip = new Trip(idRuta, direccion, plate, nombre);
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initSound() {
        soundPool = new SoundPool(MAX_NUMBER_STREAMS, AudioManager.STREAM_MUSIC, SOURCE_QUALITY);
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundLoaded = true;
            }
        });
        soundID = soundPool.load(this, R.raw.microwave_beep, 1);
    }

    private void initLoadButton() {
        loadAndShowJsonData();
        btnWrite.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Date dateForFile = new Date();
                    String tripFileName = "trips/" + dateForFile.getTime();
                    System.out.println(tripFileName);
                    String response = jsonFileAction.write(sendTripJson(trip), tripFileName);
                    txtTagContent.setText(response);
                }
                return true;
            }
        });
    }

    private JSONObject sendTripJson(Trip trip) {
        JSONObject tripJson = new JSONObject();;
        try{
            tripJson.put("routeId", trip.getRouteId());
            tripJson.put("routeDirection", trip.getRouteDirection());
            tripJson.put("busPlate", trip.getBusPlate());
            tripJson.put("routeName", trip.getRouteName());
            tripJson.put("passengers", trip.getPassengers());
        }catch(JSONException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return tripJson;
    }

    private void loadAndShowJsonData() {
        try {
            JSONArray jArray = new JSONArray(jsonFileAction.readJsonFile("cardsInformation.txt"));
            fillClientesListWithJsonArray(jArray);
        } catch (final JSONException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Json parsing error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void setListViewAdapter(ArrayList<HashMap<String, String>> list) {
        ListAdapter adapter = new SimpleAdapter(ScanActivity.this, list,
                R.layout.list_item, new String[]{"id_tarjeta", "nombre"},
                new int[]{R.id.id_tarjeta, R.id.nombre});

        viajerosListView.setAdapter(adapter);
    }

    private void fillClientesListWithJsonArray(JSONArray jArray) throws JSONException {
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject cliente = jArray.getJSONObject(i);
            StringBuilder sBuilder = new StringBuilder();

            String idCliente = cliente.getString("id_tarjeta");
            sBuilder.append(cliente.getString("primer_nombre") + " ");
            sBuilder.append(cliente.getString("primer_apellido") + " ");

            HashMap<String, String> client = new HashMap<>();
            client.put("id_tarjeta", idCliente);
            client.put("nombre", sBuilder.toString());

            clientes.add(client);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        enableForegroundDispatchSystem();
    }

    @Override
    protected void onPause() {
        super.onPause();

        disableForegroundDispatchSystem();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        NfcA tag = NfcA.get((Tag)intent.getParcelableExtra(NfcAdapter.EXTRA_TAG));
        try {
            tag.connect();
            ByteBuffer bb = ByteBuffer.wrap(tag.getTag().getId());
            this.currentId = byteArrayToHexString(bb.array());
            searchByCardSerial();
            Toast.makeText(this, this.currentId, Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void searchByCardSerial() {
        //Search if client already exists
        HashMap<String, String> client;
        if( (client = searchForClient(this.currentId.trim(), listaViajeros)) != null)
        {
            Toast.makeText(this, "El viajero está siendo registrado de nuevo.", Toast.LENGTH_SHORT).show();
        }else
        {
            client = searchForClient(this.currentId.trim(), clientes);
        }

        if(client != null){
            listaViajeros.add(client);
            setListViewAdapter(listaViajeros);
        }
    }

    @Nullable
    private HashMap<String, String> searchForClient(String tagContent, List<HashMap<String, String> > clientes) {
        final String keyToCompare = "id_tarjeta";
        for (HashMap<String, String> hMap : clientes) {
            String clientId = hMap.get(keyToCompare);

            if (clientId.trim().equals(tagContent)) {
                clientFoundActions(tagContent);
                return hMap;
            }
        }
        return null;
    }

    private void clientFoundActions(String tagContent) {
        playSound();
        btnCantPasajeros.setText(cantPasajerosText + (++cantidadDePasajeros));
        JSONObject passenger = new JSONObject();
        try {
            passenger.put("idTarjeta", tagContent);
        } catch(JSONException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        trip.addPassenger(passenger);
    }

    private void playSound() {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        float actualVolume = (float) audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = actualVolume / maxVolume;
        if (soundLoaded)
            soundPool.play(soundID, volume, volume, 1, 0, 1f);
    }

    private void enableForegroundDispatchSystem() {
        Intent intent = new Intent(this, ScanActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[]{};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }

    private void disableForegroundDispatchSystem() {
        nfcAdapter.disableForegroundDispatch(this);
    }

    private static String byteArrayToHexString(byte[] b) throws Exception {
        String result = "";
        for (int i=0; i < b.length; i++) {
            result +=
                    Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }

}

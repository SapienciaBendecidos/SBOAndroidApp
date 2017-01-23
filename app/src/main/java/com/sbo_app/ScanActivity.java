package com.sbo_app;

import android.media.AudioManager;
import android.media.SoundPool;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.os.Bundle;
import android.util.Log;
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

import java.io.UnsupportedEncodingException;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import link.software.nfcapp.R;

public class ScanActivity extends AppCompatActivity {
    private NfcAdapter nfcAdapter;
    private Button btnCantPasajeros;
    private EditText txtTagContent;
    private Button btnWrite;
    private JsonFileActions jsonFileAction;
    private ListView lv;
    private String currentId;
    private ArrayList<HashMap<String, String>> clientes;
    private int cantidadDePasajeros;
    private final String cantPasajerosText = "Cantidad de pasajeros: ";
    private String jsonTripsString;
    private Trip trip;
    //SoundPool attributes
    private SoundPool soundPool;
    private int soundID;
    private boolean soundLoaded = false;
    public static final int MAX_NUMBER_STREAMS = 2;
    public static final int SOURCE_QUALITY = 0;

    //NFC Operation Constants

    private String nfcCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        btnCantPasajeros = (Button) findViewById(R.id.tglCantPasajeros);
        txtTagContent = (EditText) findViewById(R.id.txtTagContent);
        btnWrite = (Button) findViewById(R.id.btnWrite);
        lv = (ListView) findViewById(R.id.list);
        jsonFileAction = new JsonFileActions();
        clientes = new ArrayList<>();
        currentId = "";
        cantidadDePasajeros = 0;
        btnCantPasajeros.setText(cantPasajerosText + cantidadDePasajeros);
        String elPath = jsonFileAction.writeToFile();
        initSound();
        initLoadButton();
        Intent intent = getIntent();
        String plate = intent.getStringExtra("plate");
        Toast.makeText(this, plate, Toast.LENGTH_SHORT).show();
        //readTripsInformation();
    }

    private static String byteArrayToHexString(byte[] b) throws Exception {
        String result = "";
        for (int i=0; i < b.length; i++) {
            result +=
                    Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }

    private void readTripsInformation() {
        this.jsonTripsString = jsonFileAction.readJsonFile("trips/1482186161168.txt");
        try{
            Toast.makeText(this, jsonTripsString, Toast.LENGTH_SHORT).show();
            JSONObject jsonTripsFile = new JSONObject(jsonTripsString);
            this.trip = new Trip(jsonTripsFile.getString("routeId"),
                    jsonTripsFile.getString("routeDirection"),
                    jsonTripsFile.getString("busPlate"),
                    jsonTripsFile.getString("routeName"));
            txtTagContent.setText(trip.getRouteName());
        }catch (JSONException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                    String response = jsonFileAction.write(sendTripJson(trip), "trips/newTrip.txt");
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

        lv.setAdapter(adapter);
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

        MifareClassic tag = MifareClassic.get( (Tag)intent.getParcelableExtra(NfcAdapter.EXTRA_TAG));
        try {
            tag.connect();
            ByteBuffer bb = ByteBuffer.wrap(tag.getTag().getId());
            this.nfcCode = byteArrayToHexString(bb.array());

            Toast.makeText(this, nfcCode, Toast.LENGTH_LONG).show();

            btnCantPasajeros.setText(cantPasajerosText + (++cantidadDePasajeros));
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if (parcelables != null && parcelables.length > 0) {
                readTextFromMessage((NdefMessage) parcelables[0]);
            } else {
                Toast.makeText(this, "No NDEF messages found!", Toast.LENGTH_LONG).show();
            }
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

    private void readTextFromMessage(NdefMessage ndefMessage) {
        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if (ndefRecords != null && ndefRecords.length > 0) {
            readNdefRecords(ndefRecords);
        } else {
            Toast.makeText(this, "No NDEF records found!", Toast.LENGTH_SHORT).show();
        }
    }

    private void readNdefRecords(NdefRecord[] ndefRecords) {
        String tagContent = "";
        for (NdefRecord ndefRecord : ndefRecords) {
            tagContent += getTextFromNdefRecord(ndefRecord);
        }
        this.currentId = tagContent;
        HashMap<String, String> client = searchForClient(this.currentId.trim());
        ArrayList<HashMap<String, String>> lista = new ArrayList<>();
        lista.add(client);
        setListViewAdapter(lista);
    }

    @Nullable
    private HashMap<String, String> searchForClient(String tagContent) {
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
        //1trip.addPassenger(passenger);
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

    public String getTextFromNdefRecord(NdefRecord ndefRecord) {
        String tagContent = null;
        try {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize + 1,
                    payload.length - languageSize - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("getTextFromNdefRecord", e.getMessage(), e);
        }
        return tagContent;
    }

}

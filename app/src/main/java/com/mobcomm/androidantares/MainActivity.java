package com.mobcomm.androidantares;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import id.co.telkom.iot.AntaresHTTPAPI;
import id.co.telkom.iot.AntaresResponse;

public class MainActivity extends AppCompatActivity implements AntaresHTTPAPI.OnResponseListener {

    private Button btnRefresh;
    private Button btnOn;
    private Button btnOff;
    private TextView txtData;
    private AntaresHTTPAPI antaresAPIHTTP;
    private String dataDevice;

    private final String TAG = "ANTARES-API";
    private final String APIKEY = "004cbd64ff8a7fd4:53c77e7cf8c628fd";
    private final String APPNAME = "MyMedication";
    private final String DEVICENAME = "KotakObat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRefresh = findViewById(R.id.btnRefresh);
        btnOff = findViewById(R.id.btnOff);
        btnOn = findViewById(R.id.btnOn);

        txtData = findViewById(R.id.txtData);

        antaresAPIHTTP = new AntaresHTTPAPI();
        antaresAPIHTTP.addListener(this);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                antaresAPIHTTP.getLatestDataofDevice(APIKEY, APPNAME, DEVICENAME);

            }
        });

        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                antaresAPIHTTP.storeDataofDevice(8443, APIKEY, APPNAME, DEVICENAME, "{\\\"Status\\\":1}");

            }
        });

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                antaresAPIHTTP.storeDataofDevice(8443, APIKEY, APPNAME, DEVICENAME, "{\\\"Status\\\":0}");
            }
        });
    }

    @Override
    public void onResponse(AntaresResponse antaresResponse) {
        Log.d(TAG, Integer.toString(antaresResponse.getRequestCode()));
        if (antaresResponse.getRequestCode() == 0) {
            try {
                JSONObject body = new JSONObject(antaresResponse.getBody());
                dataDevice = body.getJSONObject("m2m:cin").getString("con");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtData.setText(dataDevice);
                    }
                });
                Log.d(TAG, dataDevice);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
package com.gatikman.cbs.cph_public_toilets;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class json_display extends AppCompatActivity {

    private TextView jsonOutput;
    private Button btnFetchJson;
    private JSONObject geoJson;

////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_display);

        jsonOutput = (TextView) findViewById(R.id.txtJsonOut);


        btnFetchJson = (Button) findViewById(R.id.btnFetchJson);
        btnFetchJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new fetchJsonAsync().execute();
            }
        });


    }

    public class fetchJsonAsync extends AsyncTask<Void, String, JSONObject> {

        HttpURLConnection urlConnection;
        /* private ProgressDialog spinner;

        public fetchJsonAsync(json_display activity) {
            spinner = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            spinner.setMessage("Please wait...");
            spinner.setCancelable(true);
            spinner.show();

        }
        */

        @Override
        protected JSONObject doInBackground(Void... args) {

            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL("http://wfs-kbhkort.kk.dk/k101/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=k101:toilet&outputFormat=json&SRSNAME=EPSG:4326");
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                geoJson =  new JSONObject(result.toString());

            }catch( Exception e) {
                e.printStackTrace();
            }
            finally {
                urlConnection.disconnect();
            }


            return geoJson;
        }

        @Override
        protected void onPostExecute(JSONObject geoJson) {

            jsonOutput.setText(geoJson.toString());

        }

    }
}

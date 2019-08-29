package com.example.poojapatel.galleryart;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Artistchoice extends AppCompatActivity {
    Spinner spinner;
    String[] SPINNERVALUES = {"Ancient","Modern"};
    String SpinnerValue;
    Button buttonUploadNew;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artistchoice);
        buttonUploadNew = findViewById(R.id.button_upload_new);
        spinner = findViewById(R.id.spinner);

        new GetArt(Artistchoice.this).execute();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tutorialsName = parent.getItemAtPosition(position).toString();
                if(!tutorialsName.equalsIgnoreCase("Select")) {
                    new GoToArt(Artistchoice.this, tutorialsName).execute();
                    Toast.makeText(Artistchoice.this, "Selected: " + tutorialsName, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

        buttonUploadNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Artistchoice.this,UploadNewArt.class);
                startActivity(intent);
            }
        });
    }

    private class GetArt extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;

        public GetArt(Artistchoice activity){
            progressBar = new ProgressDialog(activity);
        }

        protected void onPreExecute(){
            progressBar.setMessage("Loading...");
            progressBar.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            URL url = null;
            BufferedReader reader = null;
            StringBuilder stringBuilder = new StringBuilder();
            Session s = new Session(getApplicationContext());
            int artistId = Integer.parseInt(s.get("artistid"));

            try {

                url = new URL("http://10.0.2.2:8080/WebApplication5/webresources/mobile/ArtListWithArtist&"+artistId);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                // uncomment this if you want to write output to this url
                connection.setDoInput(true);
                // give it 15 seconds to respond
                connection.setReadTimeout(15*1000);
                connection.connect();

                // read the output from the server
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                System.out.println(stringBuilder.toString());
            }
            catch (Exception e) {
                e.printStackTrace();
                try {
                    throw e;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            finally {
                if (reader != null) {
                    try{
                        reader.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return stringBuilder.toString();
        }
        @Override

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println(result);

            try {
                JSONObject loginResponse = new JSONObject(result);
                if(loginResponse.length()!=0 &&
                        loginResponse.get("status").toString().equalsIgnoreCase("ok") ) {

                    JSONArray jsonArray = loginResponse.getJSONArray("ArtList");
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add("Select");
                    for(int i =0;i<jsonArray.length();i++){
                        JSONObject response = jsonArray.getJSONObject(i);
                        arrayList.add(response.get("name").toString());
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, arrayList);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(arrayAdapter);

                    progressBar.dismiss();
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(),"Please try again later...",Toast.LENGTH_LONG);
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class GoToArt extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;
        private String name;

        public GoToArt(Artistchoice activity, String name){
            progressBar = new ProgressDialog(activity);
            this.name = name;
        }

        protected void onPreExecute(){
            progressBar.setMessage("Loading...");
            progressBar.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            URL url = null;
            BufferedReader reader = null;
            StringBuilder stringBuilder = new StringBuilder();
            Session s = new Session(getApplicationContext());
            int artistId = Integer.parseInt(s.get("artistid"));

            try {

                url = new URL("http://10.0.2.2:8080/WebApplication5/webresources/mobile/ArtListWithArtName&"+name);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                // uncomment this if you want to write output to this url
                connection.setDoInput(true);
                // give it 15 seconds to respond
                connection.setReadTimeout(15*1000);
                connection.connect();

                // read the output from the server
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                System.out.println(stringBuilder.toString());
            }
            catch (Exception e) {
                e.printStackTrace();
                try {
                    throw e;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            finally {
                if (reader != null) {
                    try{
                        reader.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return stringBuilder.toString();
        }
        @Override

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println(result);

            try {
                JSONObject loginResponse = new JSONObject(result);
                if(loginResponse.length()!=0 &&
                        loginResponse.get("status").toString().equalsIgnoreCase("ok") ) {

                    JSONArray jsonArray = loginResponse.getJSONArray("ArtList");
                    Intent intent = new Intent(Artistchoice.this, UploadNewArt.class);
                    for(int i =0;i<jsonArray.length();i++){
                        JSONObject response = jsonArray.getJSONObject(i);
                        intent.putExtra("artId",response.getString("art_id"));
                        intent.putExtra("artName",response.getString("Art_name"));
                        intent.putExtra("artYear",response.getInt("year"));
                        intent.putExtra("artPrice",response.getInt("price"));
                        intent.putExtra("artDescription",response.getString("artDescription"));
                        intent.putExtra("artBLOBimg",response.getString("image"));
                    }
                    startActivity(intent);

                    progressBar.dismiss();
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(),"Please try again later...",Toast.LENGTH_LONG);
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

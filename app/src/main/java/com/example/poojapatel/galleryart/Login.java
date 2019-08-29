package com.example.poojapatel.galleryart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Login extends Activity {
    private Button button_login, button_register, button_forgot_pass;
    private EditText inputUsername, inputPassword;
    private ProgressDialog pd;
    private String username="", password="";


    // button_login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Session session = new Session(getApplicationContext());
        session.clear();
        button_login = findViewById(R.id.button_login);
        button_register = findViewById(R.id.button_register);
        //button_forgot_pass = (Button) findViewById(R.id.button_forgot_password);
        inputUsername= findViewById(R.id.input_username);
        inputPassword = findViewById(R.id.input_password);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = inputUsername.getText().toString().trim();
                password = inputPassword.getText().toString().trim();
                Toast toast;
                if(username.isEmpty()| password.isEmpty()){
                    if(username.isEmpty()){
                        inputUsername.requestFocus();
                        inputUsername.setText("");
                        toast = Toast.makeText(getApplicationContext(), "Please enter correct Username", Toast.LENGTH_LONG);
                    }else{
                        inputPassword.requestFocus();
                        inputPassword.setText("");
                        toast = Toast.makeText(getApplicationContext(), "Password cannot be empty or contains only whitespace(s)", Toast.LENGTH_LONG);
                    }
                    toast.show();
                    return;
                }
                new LoginTask(Login.this, inputUsername.getText().toString(), inputPassword.getText().toString()).execute();
            }
        });

        /*button_forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = inputUsername.getText().toString().trim();
                if(username.isEmpty()) {
                    inputUsername.requestFocus();
                    Toast toast = Toast.makeText(getApplicationContext(), "Please enter correct Username", Toast.LENGTH_LONG);
                    toast.show();
                }else {
                    new ForgotPasswordTask(Login.this, inputUsername.getText().toString()).execute();
                }
            }
        });*/

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpPage.class);
                startActivity(intent);
            }
        });
    }

    private class LoginTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;
        private String username;
        private String password;

        public LoginTask(Login activity, String username, String password){
            progressBar = new ProgressDialog(activity);
            this.username = username;
            this.password = password;
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

            try {
                url = new URL("http://10.0.2.2:8080/WebApplication5/webresources/mobile/login&"+username+"&"+password);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                // uncomment this if you want to write output to this url

                connection.setDoInput(true);
                connection.setInstanceFollowRedirects( false );

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
                        loginResponse.get("status").toString().equalsIgnoreCase("ok") &&
                        !loginResponse.get("user_id").toString().equalsIgnoreCase("")) {
                    Session session = new Session(getApplicationContext());
                    session.set("user_id",loginResponse.get("user_id").toString());
                    session.set("email",loginResponse.get("email").toString());
                    session.set("first_name",loginResponse.get("first_name").toString());
                    session.set("last_name",loginResponse.get("last_name").toString());
                    session.set("usertype",loginResponse.get("usertype").toString());
                    if(loginResponse.get("usertype").toString().equalsIgnoreCase("2")){
                        session.set("artistid",loginResponse.get("artistid").toString());
                    }


                    Log.d("Userid: ", session.get("user_id"));
                    if(Integer.parseInt(session.get("usertype"))==Constant.CUSTOMER){

                    }
                    Intent intent = new Intent(getApplicationContext(), HomePage.class);
                    startActivity(intent);
                    progressBar.dismiss();
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(),"Please enter correct credentials",Toast.LENGTH_LONG);
                    inputUsername.setText("");
                    inputUsername.requestFocus();
                    inputPassword.setText("");
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class ForgotPasswordTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;
        private String username;

        public ForgotPasswordTask(Login activity, String username){
            progressBar = new ProgressDialog(activity);
            this.username = username;
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

            try {
                url = new URL("http://10.0.2.2:8080/WebApplication5/webresources/mobile/forgotpassword&"+username);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                // uncomment this if you want to write output to this url

                connection.setDoInput(true);
                connection.setInstanceFollowRedirects( false );

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
                    Toast toast = Toast.makeText(getApplicationContext(),"Your password has been reseted to default(123456789) password",Toast.LENGTH_LONG);
                    toast.show();
                    progressBar.dismiss();
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(),"Please try again.",Toast.LENGTH_LONG);
                    inputUsername.requestFocus();
                    inputPassword.setText("");
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

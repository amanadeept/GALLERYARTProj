package com.example.poojapatel.galleryart;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class HomePage extends AppCompatActivity {
private Button buttonUpdateProfile, buttonMyArtWork, buttonMyCart, buttonLogout, buttonSearch;
private LinearLayout layout_search;
private Spinner input_spinner;
EditText input_search;
String spinnerName;
int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Session session = new Session(getApplicationContext());
        
        buttonUpdateProfile= findViewById(R.id.button_update_profile);
        buttonMyArtWork = findViewById(R.id.button_my_art_work);
        buttonMyCart = findViewById(R.id.button_my_cart);
        buttonSearch = findViewById(R.id.button_search);
        buttonLogout = findViewById(R.id.button_logout);

        layout_search = findViewById(R.id.layout_search);

        input_spinner =  findViewById(R.id.input_spinner);
        new GetCategories(HomePage.this).execute();


        input_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerName = parent.getItemAtPosition(position).toString();
                if(!spinnerName.equalsIgnoreCase("Search by Category")) {
                    if (spinnerName.equalsIgnoreCase(Constant.CATEGORY_MODERN_ART_STR)) {
                        categoryId = Constant.CATEGORY_MODERN_ART;
                    } else if (spinnerName.equalsIgnoreCase(Constant.CATEGORY_APPLIED_ART_STR)) {
                        categoryId = Constant.CATEGORY_APPLIED_ART;
                    } else if (spinnerName.equalsIgnoreCase(Constant.CATEGORY_VISUAL_ART_STR)) {
                        categoryId = Constant.CATEGORY_VISUAL_ART;
                    } else if (spinnerName.equalsIgnoreCase(Constant.CATEGORY_TRADITION_ART_STR)) {
                        categoryId = Constant.CATEGORY_TRADITION_ART;
                    }
                    Session session = new Session(getApplicationContext());
                    int userId = Integer.parseInt(session.get("user_id"));
                    new Search(HomePage.this, userId, categoryId).execute();
                }
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
        //customer
        if(Integer.parseInt(session.get("usertype"))==Constant.CUSTOMER) {
            buttonMyArtWork.setVisibility(View.GONE);
            buttonMyCart.setVisibility(View.VISIBLE);
            layout_search.setVisibility(View.VISIBLE);
        }else if(Integer.parseInt(session.get("usertype"))==Constant.ARTIST){
            //Artist
            buttonMyCart.setVisibility(View.GONE);
            layout_search.setVisibility(View.GONE);
            buttonMyArtWork.setVisibility(View.VISIBLE);
        }

        buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int userId = Integer.parseInt(session.get("user_id"));
                int userType = Integer.parseInt(session.get("usertype"));

                System.out.println("Home page UserId: "+userId);

                new UpdateProfile(HomePage.this, userId, userType).execute();
                }
        });
        buttonMyArtWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent  intent =new Intent(HomePage.this,Artistchoice.class);
                startActivity(intent);
            }
        });

        buttonMyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int userId = Integer.parseInt(session.get("user_id"));
                new UserCartAsyncTask(HomePage.this, userId).execute();
            }
        });

        /*buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search = input_search.getText().toString();
                if(search.equalsIgnoreCase("")){
                    input_search.setError("Please enter category");
                }else{
                    //ModernArt TraditionArt VisualArt AppliedArt
                    Session session = new Session(getApplicationContext());
                    int userId = Integer.parseInt(session.get("user_id"));
                    new Search(HomePage.this,userId,search).execute();
                    Intent  intent =new Intent(HomePage.this,SignUpPage.class);
                    startActivity(intent);
                }

            }
        });*/

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent =new Intent(HomePage.this,Login.class);
                startActivity(intent);
            }
        });



    }

    private class UpdateProfile extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;
        private int userId;
        private int userType;


        public UpdateProfile(HomePage activity, int userId, int userType){
            progressBar = new ProgressDialog(activity);
            this.userId = userId;
            this.userType = userType;

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
                String profile = "CustomerProfile";
                // create the HttpURLConnection
                if(userType==Constant.ARTIST){
                    profile = "ArtistProfile";
                }
                url = new URL("http://10.0.2.2:8080/WebApplication5/webresources/mobile/"+profile+"&"+userId);

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
            int customerid, userid, usertype;
            String fname, uname, lname, pass, email, contact, add, gender, bdate,biography;

            try {
                JSONObject loginResponse = new JSONObject(result);
                if(loginResponse.length()!=0 &&
                        loginResponse.get("status").toString().equalsIgnoreCase("ok") ) {

                    fname = loginResponse.getString("fname");
                    uname = loginResponse.getString("uname");
                    lname = loginResponse.getString("lname");
                    pass = loginResponse.getString("pass");
                    email = loginResponse.getString("email");
                    contact = loginResponse.getString("contact");
                    add = loginResponse.getString("add");
                    gender = loginResponse.getString("gender");
                    bdate = loginResponse.getString("bdate");
                    userid = loginResponse.getInt("userid");
                    usertype = loginResponse.getInt("usertype");


                    Intent intent = new Intent(getApplicationContext(), SignUpPage.class);
                    intent.putExtra("firstName",fname);
                    intent.putExtra("lastName",lname);
                    intent.putExtra("userName",uname);
                    intent.putExtra("password",pass);
                    intent.putExtra("cPassword",pass);
                    intent.putExtra("email",email);
                    intent.putExtra("contact",contact);
                    intent.putExtra("address",add);
                    intent.putExtra("gender",gender);
                    intent.putExtra("birthDate",bdate);
                    intent.putExtra("userId",userid);
                    intent.putExtra("userType",String.valueOf(usertype));

                    if(usertype==Constant.ARTIST){
                        biography = loginResponse.getString("biography");
                        intent.putExtra("biography", biography);
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

    private class Search extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;
        private int userId;
        private int categoryId;


        public Search(HomePage activity, int userId, int categoryId){
            progressBar = new ProgressDialog(activity);
            this.userId = userId;
            this.categoryId = categoryId;

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

                url = new URL("http://10.0.2.2:8080/WebApplication5/webresources/mobile/searchByCategory&"+userId+"&"+categoryId);

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
                JSONArray jsonArray = new JSONArray();
                if(loginResponse.length()!=0 &&
                        loginResponse.get("status").toString().equalsIgnoreCase("ok") ) {

                    jsonArray = loginResponse.getJSONArray("artList");
                    ArrayList<HashMap<String, String>> artArrayList = new ArrayList<>();
                    Intent intent = new Intent(getApplicationContext(), SearchResult.class);
                    if(jsonArray.length()!=0){
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            String art_name = object.getString("art_name");
                            int art_id = object.getInt("art_id");
                            int year = object.getInt("year");
                            int price = object.getInt("price");
                            String description = object.getString("description");
                            int artistartist_id = object.getInt("artistartist_id");
                            int categorycategory_id = object.getInt("categorycategory_id");
                            String image = object.getString("image");

                            HashMap<String,String> hashMap = new HashMap<>();
                            hashMap.put("art_name",art_name);
                            hashMap.put("description",description);
                            hashMap.put("art_id",String.valueOf(art_id));
                            hashMap.put("year",String.valueOf(year));
                            hashMap.put("price",String.valueOf(price));
                            hashMap.put("artistartist_id",String.valueOf(artistartist_id));
                            hashMap.put("categorycategory_id",String.valueOf(categorycategory_id));
                            hashMap.put("image",image);
                            intent.putExtra("a", hashMap);
                            artArrayList.add(hashMap);
                        }

                    }

                    intent.putExtra("artArrayList",artArrayList);

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

    private class GetCategories extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;

        public GetCategories(HomePage activity){
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

            try {

                url = new URL("http://10.0.2.2:8080/WebApplication5/webresources/mobile/AllCategory");

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

                    JSONArray jsonArray = loginResponse.getJSONArray("Info");
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add("Search by Category");
                    for(int i =0;i<jsonArray.length();i++){
                        JSONObject response = jsonArray.getJSONObject(i);
                        arrayList.add(response.get("name").toString());
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, arrayList);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    input_spinner.setAdapter(arrayAdapter);

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

    private class UserCartAsyncTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;
        private int userId;


        public UserCartAsyncTask(HomePage activity, int userId) {
            progressBar = new ProgressDialog(activity);
            this.userId = userId;

        }

        protected void onPreExecute() {
            progressBar.setMessage("Loading...");
            progressBar.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            URL url = null;
            BufferedReader reader = null;
            StringBuilder stringBuilder = new StringBuilder();


            try {

                url = new URL("http://10.0.2.2:8080/WebApplication5/webresources/mobile/getUserCart&" + userId);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                // uncomment this if you want to write output to this url

                connection.setDoInput(true);
                // give it 15 seconds to respond
                connection.setReadTimeout(15 * 1000);
                connection.connect();

                // read the output from the server
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                System.out.println(stringBuilder.toString());
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    throw e;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
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
                JSONArray jsonArray = new JSONArray();
                if (loginResponse.length() != 0 &&
                        loginResponse.get("status").toString().equalsIgnoreCase("ok")) {

                    jsonArray = loginResponse.getJSONArray("Option_List");
                    ArrayList<HashMap<String, String>> artArrayList = new ArrayList<>();

                    if (jsonArray.length() != 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String art_name = object.getString("art_name");
                            int art_id = object.getInt("art_id");
                            int year = object.getInt("year");
                            int price = object.getInt("price");
                            String description = object.getString("description");
                            int artistartist_id = object.getInt("artistartist_id");
                            int categorycategory_id = object.getInt("categorycategory_id");
                            String image = object.getString("image");
                            int shoppingCart_id = object.getInt("cart_id");
                            String status = object.getString("status");
                            int customercustomer_id = object.getInt("customercustomer_id");

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("art_name", art_name);
                            hashMap.put("price", String.valueOf(price));
                            hashMap.put("year", String.valueOf(year));
                            hashMap.put("image", image);
                            hashMap.put("description", description);
                            hashMap.put("artistartist_id", String.valueOf(artistartist_id));
                            hashMap.put("categorycategory_id", String.valueOf(categorycategory_id));
                            hashMap.put("art_id", String.valueOf(art_id));
                            hashMap.put("shoppingCart_id", String.valueOf(shoppingCart_id));
                            hashMap.put("customercustomer_id", String.valueOf(customercustomer_id));
                            hashMap.put("status", String.valueOf(status));

                            artArrayList.add(hashMap);
                        }

                    }
                    Intent intent = new Intent(getApplicationContext(), UserCart.class);
                    intent.putExtra("myCartArrayList", artArrayList);

                    startActivity(intent);
                    progressBar.dismiss();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please try again later...", Toast.LENGTH_LONG);
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

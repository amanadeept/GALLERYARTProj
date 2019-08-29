package com.example.poojapatel.galleryart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class UploadNewArt extends AppCompatActivity {

    EditText inputName, inputCategory, inputYear, inputPrice, inputDescription;
    ImageView imgArt;
    Spinner spinnerCategory;
    Button buttonUpdate, buttonBack;

    String artName, artCategory, artDescription, artImage, spinnerName, encodedImgSTR, imgPath ;
    int artYear, artPrice, categoryId;

    int artId = 0;
    String process = "added";
    boolean update= false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_new_art);

        new getCategories(UploadNewArt.this).execute();

        inputName = findViewById(R.id.input_name);
        inputYear  = findViewById(R.id.input_year);
        inputPrice = findViewById(R.id.input_price);
        inputDescription = findViewById(R.id.input_description);

        buttonBack = findViewById(R.id.button_back);
        buttonUpdate = findViewById(R.id.button_update);

        spinnerCategory = findViewById(R.id.spinner_category);

        imgArt = findViewById(R.id.img_art);

        Intent myIntent = getIntent();
        if(myIntent.hasExtra("artName")){
            update = true;
            process = "updated";
            ((Button)findViewById(R.id.button_update)).setText("Update");

            ((EditText)findViewById(R.id.input_name)).setText(myIntent.getStringExtra("artName"));
            ((EditText)findViewById(R.id.input_price)).setText(String.valueOf(myIntent.getIntExtra("artPrice",0)));
            ((EditText)findViewById(R.id.input_year)).setText(String.valueOf(myIntent.getIntExtra("artYear", 0)));
            ((EditText)findViewById(R.id.input_description)).setText(myIntent.getStringExtra("artDescription"));
            artId = Integer.parseInt(myIntent.getStringExtra("artId"));
            System.out.println("artId: "+artId);
            //mySpinner.setSelection(arrayAdapter.getPosition("Category 2"));

            /*Bitmap decodedImgbmp = Utility.base64StringToBitMap(myIntent.getStringExtra("artBLOBimg"));
            ImageView i = (ImageView) findViewById(R.id.img_art);
            i.setImageBitmap(decodedImgbmp);*/
            imgArt.setImageBitmap(BitmapFactory.decodeFile(myIntent.getStringExtra("artBLOBimg")));

            buttonUpdate.setText("Update");

        }

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerName = parent.getItemAtPosition(position).toString();
                if(spinnerName.equalsIgnoreCase(Constant.CATEGORY_MODERN_ART_STR)){
                    categoryId=Constant.CATEGORY_MODERN_ART;
                }else if(spinnerName.equalsIgnoreCase(Constant.CATEGORY_APPLIED_ART_STR)){
                    categoryId=Constant.CATEGORY_APPLIED_ART;
                }else if(spinnerName.equalsIgnoreCase(Constant.CATEGORY_VISUAL_ART_STR)){
                    categoryId=Constant.CATEGORY_VISUAL_ART;
                }else if(spinnerName.equalsIgnoreCase(Constant.CATEGORY_TRADITION_ART_STR)){
                    categoryId=Constant.CATEGORY_TRADITION_ART;
                }
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                artName = inputName.getText().toString();
                artCategory = spinnerName;
                artYear = Integer.parseInt(inputYear.getText().toString());
                artPrice = Integer.parseInt(inputPrice.getText().toString());
                artDescription = inputDescription.getText().toString();

                //validation
                if(imgArt.getDrawable()==null){
                    inputName.setError("Please select Image");
                    imgArt.setFocusable(true);
                    return;
                }
                if(inputName.getText().toString().trim().equalsIgnoreCase("")){
                    inputName.setError("Please enter name");
                    inputName.setFocusable(true);
                    return;
                }
                if(spinnerName.trim().equalsIgnoreCase("")){
                    inputName.setError("Please select category");
                    spinnerCategory.setFocusable(true);
                    return;
                }
                if(inputYear.getText().toString().trim().equalsIgnoreCase("")){
                    inputYear.setError("Please enter category");
                    inputYear.setFocusable(true);
                    return;
                }
                if(inputPrice.getText().toString().trim().equalsIgnoreCase("")){
                    inputPrice.setError("Please enter category");
                    inputPrice.setFocusable(true);
                    return;
                }
                if(inputDescription.getText().toString().trim().equalsIgnoreCase("")){
                    inputDescription.setError("Please enter category");
                    inputDescription.setFocusable(true);
                    return;
                }

                //encoading Image
                BitmapDrawable abmp = (BitmapDrawable)imgArt.getDrawable();
                Bitmap bmp = abmp.getBitmap();
                Session session = new Session(getApplicationContext());
                if(bmp!=null) {
                    String encodedImgSTR = Utility.bitMapToBase64String(bmp);
                    session.set("encoadedImg", encodedImgSTR);
                }else{
                    session.set("encoadedImg", "");
                }
                int userId = Integer.parseInt(session.get("user_id"));
                new newArt(UploadNewArt.this, userId, imgPath).execute();

            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UploadNewArt.this, Artistchoice.class);
                startActivity(i);
            }
        });
        imgArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });



    }
    private int GALLERY_REQUEST_CODE = 1;
        private void pickFromGallery(){
            //Create an Intent with action as ACTION_PICK
            Intent intent=new Intent(Intent.ACTION_PICK);
            // Sets the type as image/*. This ensures only components of type image are selected
            intent.setType("image/*");
            //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
            // Launching the Intent
            startActivityForResult(intent,1);
        }

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case 1:
                    //data.getData return the content URI for the selected Image
                    Uri selectedImage = data.getData();
                    System.out.println("uri : img : "+selectedImage);
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    // Get the cursor
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();
                    //Get the column index of MediaStore.Images.Media.DATA
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    //Gets the String value in the column
                    String imgDecodableString = cursor.getString(columnIndex);
                    System.out.println("encoded: "+imgDecodableString);
                    Session s = new Session(getApplicationContext());
                    imgPath = imgDecodableString;
                    s.set("imgPath",imgDecodableString);

                    cursor.close();
                    // Set the Image in ImageView after decoding the String
                    System.out.println("bitmap : "+BitmapFactory.decodeFile(imgDecodableString));
                    imgArt.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                    break;

            }
    }

    private class newArt extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;
        private int userId;
        private String encoadedImg;

        public newArt(UploadNewArt activity, int userId, String encoadedImg){
            progressBar = new ProgressDialog(activity);
            this.userId = userId;
            this.encoadedImg = encoadedImg;

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
            String pathEnd = "addart";
            if(update){
                pathEnd = "updateart";
            }

            System.out.println("artid: background method: "+artId);
            try {
                Session se = new Session(getApplicationContext());
                String path = se.get("imgPath");

                //path = se.get("encoadedImg");
                //artname artcategory artyear artprice artdescription path userid categoryid
                    String urlParameters  = "artname="+artName+"&artcategory="+artCategory
                        +"&artyear="+artYear+"&artprice="+artPrice+"&artdescription="+artDescription
                        +"&path="+path+"&userid="+userId+"&artistid="+Integer.parseInt(se.get("artistid"))+
                        "&categoryid="+categoryId+"&artid="+artId;


                byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
                int    postDataLength = postData.length;
                // create the HttpURLConnection
                url = new URL("http://10.0.2.2:8080/WebApplication5/webresources/mobile/"+pathEnd);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                // uncomment this if you want to write output to this url
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setInstanceFollowRedirects( false );
                connection.setRequestProperty( "charset", "utf-8");
                connection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
                connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
                connection.setUseCaches( false );
                // give it 15 seconds to respond
                connection.setReadTimeout(45*1000);

                try( DataOutputStream wr = new DataOutputStream( connection.getOutputStream())) {
                    wr.write( postData );
                }


                connection.connect();




                /*System.out.println("path: "+path);
                url = new URL("http://10.0.2.2:8080/WebApplication5/webresources/mobile/addart" +
                        "&"+artName+"&"+artCategory+"&"+artYear+"&"+artPrice+"&"+artDescription+"&"+path+"&"+userId+"&"+categoryId);
                //System.out.println("url addart: "+url.toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                // uncomment this if you want to write output to this url
                connection.setDoInput(true);
                // give it 15 seconds to respond
                connection.setReadTimeout(15*1000);
                connection.connect();*/

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

                    Intent intent = new Intent(UploadNewArt.this, Artistchoice.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"Your art has been "+process+" successfully.",Toast.LENGTH_LONG).show();
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

    private class getCategories extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;

        public getCategories(UploadNewArt activity){
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
                    for(int i =0;i<jsonArray.length();i++){
                        JSONObject response = jsonArray.getJSONObject(i);
                        arrayList.add(response.get("name").toString());
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, arrayList);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategory.setAdapter(arrayAdapter);

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

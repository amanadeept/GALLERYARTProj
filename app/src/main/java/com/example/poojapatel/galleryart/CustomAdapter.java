package com.example.poojapatel.galleryart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

class CustomAdapter extends AppCompatActivity implements ListAdapter  {
    ArrayList<ArtData> arrayList;
    Context context;
    public CustomAdapter(Context context, ArrayList<ArtData> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
    @Override
    public boolean isEnabled(int position) {
        return true;
    }
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ArtData artData = arrayList.get(position);
        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.list_row, null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            TextView artName = convertView.findViewById(R.id.art_name);
            TextView tittle = convertView.findViewById(R.id.art_name);
            TextView artDescription = convertView.findViewById(R.id.art_desc);
            TextView artPrice = convertView.findViewById(R.id.art_price);
            TextView artYear = convertView.findViewById(R.id.art_year);
            ImageView artImage = convertView.findViewById(R.id.img_art);
            artName.setText(artData.name);
            artDescription.setText(artData.description);
            artYear.setText(String.valueOf(artData.year));
            artPrice.setText(String.valueOf(artData.price));
            try {
                artImage.setImageBitmap(BitmapFactory.decodeFile(artData.image));
            }catch(Exception e){
                e.printStackTrace();
            }

            Button share = convertView.findViewById(R.id.button_share);
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, Share.class);
                    context.startActivity(intent);
                }
            });

            Button b = convertView.findViewById(R.id.button_add_to_cart);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Session s = new Session(context);
                    int userId = Integer.parseInt(s.get("user_id"));
                    int artId = artData.artId;
                    System.out.println("artId : "+artData.artId);
                    /*HomePage homePage = new HomePage();
                    homePage.addToCart(userId, artId);*/
                    new AddToCart(CustomAdapter.this, userId,artId).execute();

                }
            });
            /*Picasso.with(context)
                    .load(subjectData.Image)
                    .into(imag);*/
        }
        return convertView;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getViewTypeCount() {
        return arrayList.size();
    }
    @Override
    public boolean isEmpty() {
        return false;
    }




        public class AddToCart extends AsyncTask<Void, Void, String> {
            private ProgressDialog progressBar;
            private int userId;
            private int artId;


            public AddToCart(CustomAdapter activity, int userId, int artId){
                progressBar = new ProgressDialog(context);
                this.userId = userId;
                this.artId = artId;
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

                    url = new URL("http://10.0.2.2:8080/WebApplication5/webresources/mobile/customershoppingcart&"+userId+"&"+artId);

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

                        Toast toast = Toast.makeText(context,"Added to your Cart",Toast.LENGTH_LONG);
                        toast.show();
                        progressBar.dismiss();
                    }
                    else{
                        Toast toast = Toast.makeText(context,"Please try again later...",Toast.LENGTH_LONG);
                        progressBar.dismiss();
                        toast.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
}

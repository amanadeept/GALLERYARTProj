package com.example.poojapatel.galleryart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SearchResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ImageView img1 = findViewById(R.id.img_art);
        Button button_share = (Button)findViewById(R.id.button_share);
        Button addtocart = findViewById(R.id.button_add_to_cart);
        Intent myIntent = getIntent();


        if(myIntent.hasExtra("artArrayList")){

            ArrayList<HashMap<String, String>> list = (ArrayList<HashMap<String, String>>)myIntent.getSerializableExtra("artArrayList");
            ArrayList<ArtData> list2 = new ArrayList<>();

            if(!list.isEmpty()){
                HashMap<String, String> map;
                for(int i = 0; i<list.size();i++){
                    map = new HashMap<>();
                    map = list.get(i);
                    list2.add(new ArtData(map.get("art_name"),Integer.parseInt(map.get("price")),
                            Integer.parseInt(map.get("year")), map.get("image"),
                            map.get("description"),Integer.parseInt(map.get("artistartist_id")),
                            Integer.parseInt(map.get("categorycategory_id")), Integer.parseInt(map.get("art_id"))));


                }
                ListView lv = findViewById(R.id.listview_art);
            /*ListAdapter  adapter = new SimpleAdapter(SearchResult.this, list, R.layout.list_row,
                    new String[]{"art_name","art_id","year","price"},
                    new int[]{R.id.art_name,R.id.art_desc, R.id.art_year});*/
                CustomAdapter customAdapter = new CustomAdapter(this,list2);
                lv.setAdapter(customAdapter);
            }



            //img1.setImageBitmap(BitmapFactory.decodeFile(myIntent.getStringExtra("image")));
        }

        /*button_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent i = new Intent(getApplicationContext(), Share.class);
                    startActivity(i);
                    //Toast.makeText(context,artName+" has been shared.",Toast.LENGTH_LONG).show();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
        });*/

        /*addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/


        /*button_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchResult.this,Share.class);
                startActivity(intent);
            }
        });*/
    }
}

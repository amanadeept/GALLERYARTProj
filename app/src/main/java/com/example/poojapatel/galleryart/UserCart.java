package com.example.poojapatel.galleryart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class UserCart extends AppCompatActivity {


    public void redirect(){
        Intent intent = new Intent(getApplicationContext(), Share.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cart);

        Intent myIntent = getIntent();

        if(myIntent.hasExtra("myCartArrayList")) {

            ArrayList<HashMap<String, String>> list = (ArrayList<HashMap<String, String>>) myIntent.getSerializableExtra("myCartArrayList");
            ArrayList<ArtData> list2 = new ArrayList<>();

            if (!list.isEmpty()) {
                HashMap<String, String> map;
                for (int i = 0; i < list.size(); i++) {
                    map = new HashMap<>();
                    map = list.get(i);
                    list2.add(new ArtData(map.get("art_name").toString(), Integer.parseInt(map.get("price").toString()),
                            Integer.parseInt(map.get("year").toString()), map.get("image").toString(),
                            map.get("description").toString(), Integer.parseInt(map.get("artistartist_id").toString()),
                            Integer.parseInt(map.get("categorycategory_id").toString()), Integer.parseInt(map.get("art_id").toString()),
                            Integer.parseInt(map.get("shoppingCart_id").toString()), Integer.parseInt(map.get("customercustomer_id").toString()),
                            map.get("status").toString()));

                }
                ListView lv = (ListView) findViewById(R.id.listview_my_cart);

                MyCartAdapter customAdapter = new MyCartAdapter(UserCart.this, list2);
                lv.setAdapter(customAdapter);


            }
        }
    }


}

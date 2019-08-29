package com.example.poojapatel.galleryart;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MyCartAdapter  extends AppCompatActivity implements ListAdapter {
    ArrayList<ArtData> arrayList;
    Context context;
    public MyCartAdapter(Context context, ArrayList<ArtData> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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
            convertView = layoutInflater.inflate(R.layout.my_cart_row, null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            Button buttonShare = (Button)convertView.findViewById(R.id.button_share);
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



            buttonShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        Intent i = new Intent(context, Share.class);
                        context.startActivity(i);
                        //Toast.makeText(context,artName+" has been shared.",Toast.LENGTH_LONG).show();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }


                }
            });



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
}

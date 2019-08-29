package com.example.poojapatel.galleryart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Share extends AppCompatActivity {
    private Button sharee, button_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share);
        sharee= findViewById(R.id.share1);
        button_back= findViewById(R.id.button_back);
        sharee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Share.this, "Art has been shared...", Toast.LENGTH_SHORT).show();
                return;
            }
        });

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),HomePage.class);
                startActivity(intent);
            }
        });
    }
}

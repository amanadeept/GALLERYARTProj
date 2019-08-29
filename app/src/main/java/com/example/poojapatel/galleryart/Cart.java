package com.example.poojapatel.galleryart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Cart extends AppCompatActivity {
private Button bi;
    int minteger = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);



        bi=(Button)findViewById(R.id.submitkk);
        bi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Payment.class);


                startActivity(intent);
            }
        });


    }

    public void decreaseInteger(View view) {

        minteger = minteger - 1;
        display(minteger);
    }

    private void display(int number) {
        TextView displayInteger = (TextView) findViewById(
            R.id.integer_number);

        displayInteger.setText("" + number);

    }

    public void increaseInteger(View view) {


        minteger = minteger + 1;
        display(minteger);

    }
}

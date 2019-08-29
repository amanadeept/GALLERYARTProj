package com.example.poojapatel.galleryart;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Context;
import android.widget.Toast;


public class Payment extends AppCompatActivity {
    private Button bjj;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        bjj = (Button) findViewById(R.id.submit_order);
        bjj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

                // set title
                alertDialogBuilder.setTitle("want to continue");

                // set dialog message
                alertDialogBuilder
                    .setMessage("Click yes to exit!")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, close
                            // current activity
                            Payment.this.finish();
                            Intent intent=new Intent(getApplicationContext(),HomePage.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            Intent intent=new Intent(getApplicationContext(),Reviews.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(),"reviews",Toast.LENGTH_LONG).show();

                            //dialog.cancel();
                        }
                    });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });
    }
}


















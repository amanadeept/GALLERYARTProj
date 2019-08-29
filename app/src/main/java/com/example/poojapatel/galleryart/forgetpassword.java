package com.example.poojapatel.galleryart;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class forgetpassword extends Activity {
private Button fp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpassword);
        fp=(Button)findViewById(R.id.button4);
        fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), CustomerRegistration.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"Reset link has been sent on your email address",Toast.LENGTH_LONG).show();
            }
        });
    }

}

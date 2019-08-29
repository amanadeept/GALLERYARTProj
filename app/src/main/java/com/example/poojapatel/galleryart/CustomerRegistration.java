package com.example.poojapatel.galleryart;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.service.autofill.UserData;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CustomerRegistration extends AppCompatActivity {

    private EditText Fn, Ln, email, address, password, confpass, contact;
    private RadioButton r1, r2;
    private CheckBox r3;
    private Button bb1;

    String First_Name, Last_Name, Address, Password, ConfirmPassword, Email;
    String sex = "", user = "", Contact;
    RadioGroup rg1;
    RadioGroup rg2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_sign_up_page);


        Fn = findViewById(R.id.input_fullName);
        Ln = findViewById(R.id.input_lastName);
        email = findViewById(R.id.Em1);
        address = findViewById(R.id.inputaddress);
        password = findViewById(R.id.input_password);
        confpass = findViewById(R.id.input_password_confirm);
        contact = findViewById(R.id.inputcontact);

        rg1 = (RadioGroup) findViewById(R.id.rg);


        r1 = findViewById(R.id.radio);
        r2 = findViewById(R.id.radio1);
        r3 = findViewById(R.id.checkbox);


        bb1 = findViewById(R.id.button_register_singnupPage);



        bb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();

            }

        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;


    }


    private void submitForm() {


        int sexRadio = rg1.getCheckedRadioButtonId();

        // If nothing is selected from Radio Group, then it return -1
        if (sexRadio != -1) {

            RadioButton selectedRadioButton = (RadioButton) findViewById(sexRadio);
            sex = selectedRadioButton.getText().toString();
        } else {
            sex = "Male";
        }

        int customerRadio = rg2.getCheckedRadioButtonId();

        // If nothing is selected from Radio Group, then it return -1
        if (customerRadio != -1) {

            RadioButton selectedRadioButton = (RadioButton) findViewById(customerRadio);
            user = selectedRadioButton.getText().toString();
        } else {
            user = "Customer";
        }


        //*******************************************
//        if (Fn.getText().toString().isEmpty()) {
//            Fn.setError("First name not entered");
//
//            Fn.requestFocus();
//
//            return;
//        }
//        if (Ln.getText().toString().isEmpty()) {
//            Ln.setError("Last name not entered");
//            Ln.requestFocus();
//            return;
//
//        }
//
//        if (address.getText().toString().isEmpty()) {
//            address.setError("Address is Required");
//            address.requestFocus();
//            return;
//        }
//        if (password.getText().toString().isEmpty()) {
//            password.setError("Password not entered");
//            password.requestFocus();
//            return;
//        }
//        if (contact.getText().toString().isEmpty()) {
//            contact.setError("Required");
//            contact.requestFocus();
//            return;
//        }
//
//        if (confpass.getText().toString().isEmpty()) {
//            confpass.setError("Please confirm password");
//            confpass.requestFocus();
//            return;
//        }
//        if (!password.getText().toString().equals(confpass.getText().toString())) {
//            confpass.setError("Password Not matched");
//            confpass.requestFocus();
//
//        }
//        if (password.getText().toString().length() < 8) {
//            password.setError("Password should be atleast of 8 charactors");
//            password.requestFocus();
//
//        }

        First_Name = Fn.getText().toString().trim();
        Last_Name = Ln.getText().toString();
        Address = address.getText().toString();
        Email = email.getText().toString();

        Contact = contact.getText().toString();
        Password = password.getText().toString();
        ConfirmPassword = confpass.getText().toString();
        r1.setChecked(true);


        if (r1.isChecked()) {
            sex = r1.getText().toString();

        } else if (r2.isChecked()) {
            sex = r2.getText().toString();
        } else {
            Toast.makeText(getApplicationContext(), "ERROR Please select the gender!!", Toast.LENGTH_LONG);
            return;
        }







    }
}

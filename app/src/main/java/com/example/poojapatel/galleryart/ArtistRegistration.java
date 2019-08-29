package com.example.poojapatel.galleryart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ArtistRegistration extends AppCompatActivity {
    private EditText aFn, aLn, aemail, Aaddress, Apassword, Aconfpass, Acontact,Abiography;
    private RadioButton Ar1, Ar2;
    private Button Abb1;


    String AFirst_Name, ALast_Name, AAddress, APassword, AConfirmPassword, AEmail;
    String Asex = "", Auser = "", AContact;
    RadioGroup Arg1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_registration);


        aFn = findViewById(R.id.input_ArtistfullName);
        // FN = fn+"#ARTIST;
        aLn = findViewById(R.id.input_ArtistlastName);
        aemail = findViewById(R.id.ARTISTEm1);
        Aaddress = findViewById(R.id.Artistaddress);
        Apassword = findViewById(R.id.Artist_password);
        Aconfpass = findViewById(R.id.Artist_password_confirm);
        Acontact = findViewById(R.id.ARTISTcontact);;
Abiography=findViewById(R.id.Bio1);
       Arg1 = (RadioGroup) findViewById(R.id.Arg);


        Ar1 = findViewById(R.id.ARTISTM);
        Ar2 = findViewById(R.id.ARTISTF);

Abb1=findViewById(R.id.Artist_register_singnupPage);

        Arg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }

//    private void test() {
//        Fn.setText("Pooja ");
//        Ln.setText("Patel");
//        address.setText("ncsils");
//        email.setText("abc00gmail.com");
//        r1.setChecked(true);
//        contact.setText("9878956489");
//        password.setText("Pooja2908");
//        confpass.setText("Pooja2908");
//
//    }

    public void submit(){

        //  COLLECT DATA FROM UI HERE
        AFirst_Name=aFn.getText().toString();
        ALast_Name=aLn.getText().toString();
        AAddress=Aaddress.getText().toString();
        AEmail=aemail.getText().toString();
        AContact=Acontact.getText().toString();
        APassword=Apassword.getText().toString();
        AConfirmPassword=Aconfpass.getText().toString();



        // PUT VALIDATION HERE

        if (aFn.getText().toString().isEmpty()) {
            aFn.setError("First name not entered");

            aFn.requestFocus();

            return;
        }
        if (aLn.getText().toString().isEmpty()) {
            aLn.setError("Last name not entered");
           aLn.requestFocus();
            return;

        }

        if (Aaddress.getText().toString().isEmpty()) {
            Aaddress.setError("Address is Required");
            Aaddress.requestFocus();
            return;
        }
        if (Apassword.getText().toString().isEmpty()) {
        Apassword.requestFocus();
            return;
        }
        if (Acontact.getText().toString().isEmpty()) {
            Acontact.setError("Required");
            Acontact.requestFocus();
            return;
        }

        if (Aconfpass.getText().toString().isEmpty()) {
            Aconfpass.setError("Please ENTER password");
            Aconfpass.requestFocus();
            return;
        }
        if (!Apassword.getText().toString().equals(Aconfpass.getText().toString())) {
            Aconfpass.setError("Password Not matched");
            Aconfpass.requestFocus();
            return;
        }
        if (Apassword.getText().toString().length() < 8) {
            Apassword.setError("Password should be atleast of 8 charactors");
            Apassword.requestFocus();
            return;
        }

        if (Abiography.getText().toString().isEmpty()) {
            Abiography.setError("Biography is Required");
            Abiography.requestFocus();
            return;
        }

        //  ADD ALL COLLECTED DATA IN HASHMAP

        HashMap<String, String> data = new HashMap<>();

        data.put("first_name", AFirst_Name);
        data.put("last_name", ALast_Name);
        // data.put("sex", sex);
        // data.put("user", user);
        data.put("address", AAddress);
        data.put("contact", AContact);
        data.put("email", AEmail);
        data.put("Password", APassword);
        data.put("Confirmpassword.",AConfirmPassword);
//data.put("Confirm_pass", ConfirmPassword);

        //  THAT'S IT.
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("GalleryArt");
        ref.child(AFirst_Name).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                FirebaseAuth auth = FirebaseAuth.getInstance();
                String Email = null,Password = null;
                auth.createUserWithEmailAndPassword(Email,Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                    }
                });

                ArtistRegistration.this.finish();
            }
        });


    }
}




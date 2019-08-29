package com.example.poojapatel.galleryart;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class SignUpPage extends AppCompatActivity {




    private EditText Fn, Ln, email, address, password, confpass, contact, dob, input_username, input_biography;
    private RadioButton male, female, admin, artist, customer;
    private Button button_register;
    private CheckBox ch;
    String Occupation;
    String First_Name, Last_Name, Address, Password, ConfirmPassword, Email,    biography;
    String sex = "", Contact, dateofbirth, username;
    int userType = 0;
    RadioGroup radioGroupGender, radioGroupUserType;
    LinearLayout biography_layout;
    boolean update=false;
    String toastMsgText = "Registration";
    int user_id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regiscustomer);
        // hide biography field on load of signup page.
        biography_layout = findViewById(R.id.biography_layout);
        biography_layout.setVisibility(LinearLayout.GONE);



        Intent myIntent = getIntent();
        if(myIntent.hasExtra("firstName")){
            update = true;
            toastMsgText = "Update";

            ((TextView)findViewById(R.id.lable)).setText("Update Page");
            findViewById(R.id.layout_username).setVisibility(LinearLayout.GONE);
            findViewById(R.id.layout_gender).setVisibility(LinearLayout.GONE);
            findViewById(R.id.layout_user_type).setVisibility(LinearLayout.GONE);
            ((Button)findViewById(R.id.button_register_singnupPage)).setText("Update");
            Session session = new Session(getApplicationContext());
            user_id = Integer.parseInt(session.get("user_id"));
            userType = Integer.parseInt(session.get("usertype"));
            if(userType==Constant.ARTIST){
                biography_layout.setVisibility(LinearLayout.VISIBLE);
                ((EditText)findViewById(R.id.input_biography)).setText(myIntent.getStringExtra("biography"));
            }

            /*if(myIntent.getStringExtra("gender").equalsIgnoreCase("M")){
                ((RadioButton)findViewById(R.id.radio_male)).setChecked(true);
            }else{
                ((RadioButton)findViewById(R.id.radio_female)).setChecked(true);
            }
            if(Integer.parseInt(myIntent.getStringExtra("userType"))==Constant.ARTIST){
                ((RadioButton)findViewById(R.id.radio_artist)).setChecked(true);
                biography_layout.setVisibility(LinearLayout.VISIBLE);
                ((EditText)findViewById(R.id.input_biography)).setText(myIntent.getStringExtra("biography"));
            }else{
                ((RadioButton)findViewById(R.id.radio_customer)).setChecked(true);
            }
            ((EditText)findViewById(R.id.input_username)).setText(myIntent.getStringExtra("userName"));*/

            System.out.println("birthdate: "+myIntent.getStringExtra("birthDate"));
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            try {
                String bdate = formatter.format(parser.parse(myIntent.getStringExtra("birthDate")));
                ((EditText)findViewById(R.id.input_dob)).setText(bdate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ((EditText)findViewById(R.id.input_fullName)).setText(myIntent.getStringExtra("firstName"));
            ((EditText)findViewById(R.id.input_lastName)).setText(myIntent.getStringExtra("lastName"));
            ((EditText)findViewById(R.id.Em1)).setText(myIntent.getStringExtra("email"));
            ((EditText)findViewById(R.id.inputaddress)).setText(myIntent.getStringExtra("address"));
            ((EditText)findViewById(R.id.input_password)).setText(myIntent.getStringExtra("password"));
            ((EditText)findViewById(R.id.input_password_confirm)).setText(myIntent.getStringExtra("cPassword"));
            ((EditText)findViewById(R.id.inputcontact)).setText(myIntent.getStringExtra("contact"));
        }



        Fn = findViewById(R.id.input_fullName);
        // FN = fn+"#ARTIST;
        Ln = findViewById(R.id.input_lastName);
        email = findViewById(R.id.Em1);
        address = findViewById(R.id.inputaddress);
        password = findViewById(R.id.input_password);
        confpass = findViewById(R.id.input_password_confirm);
        contact = findViewById(R.id.inputcontact);
        dob = findViewById(R.id.input_dob);
        input_username = findViewById(R.id.input_username);


        radioGroupGender = findViewById(R.id.radiogroup_gender);
        radioGroupUserType = findViewById(R.id.radiogroup_usertype);

        button_register = findViewById(R.id.button_register_singnupPage);

        //test();
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });

        radioGroupUserType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb= findViewById(checkedId);
                if(rb.getText().toString().equalsIgnoreCase("Artist")){
                    biography_layout.setVisibility(LinearLayout.VISIBLE);
                }else{
                    biography_layout.setVisibility(LinearLayout.GONE);
                }
            }
        });
    }

    public void submit() {

        //  COLLECT DATA FROM UI HERE
        First_Name = Fn.getText().toString();
        Last_Name = Ln.getText().toString();
        Address = address.getText().toString();
        Email = email.getText().toString();
        Contact = contact.getText().toString();
        Password = password.getText().toString();
        dateofbirth = dob.getText().toString();
        username = input_username.getText().toString();
        int selectedGender = radioGroupGender.getCheckedRadioButtonId();
        int selectedUserType = radioGroupGender.getCheckedRadioButtonId();
        male = findViewById(R.id.radio_male);
        female = findViewById(R.id.radio_female);
        artist = findViewById(R.id.radio_artist);
        customer = findViewById(R.id.radio_customer);
        input_biography = findViewById(R.id.input_biography);
        biography = input_biography.getText().toString();
        //admin = findViewById(R.id.radio_admin);

        if (!email.getText().toString().contains("@") ) {
            email.setError("Invalid Email");
            email.requestFocus();
            return;
        }
        if ( email.getText().toString().isEmpty() ) {
            email.setError("Invalid Email");
            email.requestFocus();
            return;
        }
        if (Fn.getText().toString().isEmpty()) {
            Fn.setError("First name not entered");
            Fn.requestFocus();
            return;
        }
        if (Ln.getText().toString().isEmpty()) {
            Ln.setError("Last name not entered");
            Ln.requestFocus();
            return;
        }
        if (userType==Constant.ARTIST && input_biography.getText().toString().trim().equalsIgnoreCase("")) {
            input_biography.setError("Please enter Biography");
            input_biography.requestFocus();
            return;
        }
        if(!update){
            if (input_username.getText().toString().equalsIgnoreCase("")) {
                input_username.setError("Username not entered");
                input_username.requestFocus();
                return;
            }
            if (radioGroupGender.getCheckedRadioButtonId() == -1) {
                male.setError("Gender not selected");
                male.requestFocus();
                return;
            }
            if (male.isChecked() && !update) {
                sex = "M";
            } else if (female.isChecked() && !update) {
                sex = "F";
            }
            if (radioGroupUserType.getCheckedRadioButtonId() == -1) {
                artist.setError("User Type not selected");
                artist.requestFocus();
                return;
            }
            if (artist.isChecked()) {
                userType = Constant.ARTIST;
                if (input_biography.getText().toString().trim().equalsIgnoreCase("")) {
                    input_biography.setError("Please enter Biography");
                    input_biography.requestFocus();
                    return;
                }
            } else if (customer.isChecked()) {
                userType = Constant.CUSTOMER;
            } else if (admin.isChecked() && !update) {
                userType = Constant.ADMIN;
            }
        }
        if (contact.getText().toString().isEmpty()) {
            contact.setError("Email Required");
            contact.requestFocus();
            return;
        }

        if (address.getText().toString().isEmpty()) {
            address.setError("Address is Required");
            address.requestFocus();
            return;
        }
        if (password.getText().toString().isEmpty()) {
            password.setError("Password not entered");
            password.requestFocus();
            return;
        }
        if (confpass.getText().toString().isEmpty()) {
            confpass.setError("Please enter confirm password");
            confpass.requestFocus();
            return;
        }
        if (!password.getText().toString().equals(confpass.getText().toString())) {
            confpass.setError("Password Not matched");
            confpass.requestFocus();
            return;
        }
        if (password.getText().toString().length() < 8) {
            password.setError("Password should be atleast of 8 charactors");
            password.requestFocus();
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        if(true){
            try {
                Date d = sdf.parse(dob.getText().toString());
                Date date = new Date();
                if(d.after(date)){}

                if(d.getDate()>=31 || d.getDate()<0){dob.setError("Please enter day of month between 1 to 31");return; }
                if(d.getMonth()>12 || d.getMonth()<=0){dob.setError("Please enter month between 1 to 12");return;}
                if(d.getYear()>date.getYear() || d.getYear()<60){dob.setError("Please enter year between 1960 to "+(date.getYear()+1900));return;}
            } catch (ParseException e) {
                dob.setError("Please enter date in formate dd-MM-yyyy");
                e.printStackTrace();
            }
        }


        new MyTask(SignUpPage.this).execute();
    }

    private class MyTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;


        public MyTask(SignUpPage activity){
            progressBar = new ProgressDialog(activity);

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
            String registeration = "registeration", customerRegisteration = "customerregisteration";

            try {
                // create the HttpURLConnection
                if(update){
                    url = new URL("http://10.0.2.2:8080/WebApplication5/webresources/mobile/updateartist&"
                            +user_id+"&"+First_Name+"&"+Last_Name+"&"+Password+"&" +
                            Contact+"&"+Email+"&"+Address+"&"+dateofbirth+"&"+biography+"&"+userType);
                    if(userType==Constant.CUSTOMER){
                        url = new URL("http://10.0.2.2:8080/WebApplication5/webresources/mobile/updatecustomer&"
                                +user_id+"&"+First_Name+"&"+Last_Name+"&"+Password+"&" +
                                Contact+"&"+Email+"&"+Address+"&"+dateofbirth+"&"+userType);
                    }
                }else{
                    url = new URL("http://10.0.2.2:8080/WebApplication5/webresources/mobile/"+registeration+"&"
                            +First_Name+"&"+Last_Name+"&"+username+"&"+Password+"&"+sex+"&" +
                            +userType+"&"+Contact+"&"+Email+"&"+Address+"&"+biography+"&"+dateofbirth);
                    if(userType==Constant.CUSTOMER){
                        url = new URL("http://10.0.2.2:8080/WebApplication5/webresources/mobile/"+customerRegisteration+"&"
                                +First_Name+"&"+Last_Name+"&"+username+"&"+Password+"&"+sex+"&" +
                                +userType+"&"+Contact+"&"+Email+"&"+Address+"&"+dateofbirth);
                    }
                }

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
            try {
                JSONObject loginResponse = new JSONObject(result);
                if(loginResponse.length()!=0 &&
                        loginResponse.get("status").toString().equalsIgnoreCase("ok") ) {

                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    Toast toast = Toast.makeText(getApplicationContext(),toastMsgText+" Successful.",Toast.LENGTH_LONG);
                    toast.show();
                    progressBar.dismiss();
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(),"Verify your details and register again.",Toast.LENGTH_LONG);
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

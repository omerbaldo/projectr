package com.example.amalkheir.project_r;

import android.app.LoaderManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.io.*;
import java.net.*;
import com.google.gson.Gson;

import static com.example.amalkheir.project_r.R.id.Register;

public class register extends AppCompatActivity implements Handler.Callback {
    //This is a callback handler
        private Handler mFragmentHandler;

    //UI Elements
        private UserRegisterTask mAuthTask = null;
        EditText name;
        EditText Email;
        EditText Username;
        EditText Password;
        EditText Passwordconfirm;
        DatePicker DOB;
        String errorMessage;
        boolean isError;


    //Set UI Elements to point to page
        public void setUIElements(){
            //Error message is set and
            this.errorMessage = "Error Creating Account";
            this.isError = false;

            //Set variables to point to their elements
            Button Register = (Button) findViewById(R.id.Register);
            name = (EditText) findViewById(R.id.Name);
            Email = (EditText) findViewById(R.id.Email);
            Username = (EditText) findViewById(R.id.Username);
            Password = (EditText) findViewById(R.id.Password);
            Passwordconfirm = (EditText) findViewById(R.id.Passwordconfirm);
            DOB = (DatePicker) findViewById(R.id.datePicker);
            Register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptRegister();
                }
            });

        }
    //Called when register page is created
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);
            setUIElements();
        }


    /**
     * Attempt to register when register button is clicked
     * Also deals with error checking
     */
        public void attemptRegister() {

            //1) Get information from Fields
                String sname = name.getText().toString();
                String sEmail = Email.getText().toString();
                String sUsername = Username.getText().toString();
                String sPassword = Password.getText().toString();
                String sPasswordconfirm = Passwordconfirm.getText().toString();
                int day = DOB.getDayOfMonth();
                int month = DOB.getMonth() + 1;
                int year = DOB.getYear();
                String sDOB = Integer.toString(year) + "-";
                if (month <= 9) {
                    sDOB = sDOB + "0" + Integer.toString(month);
                } else {
                    sDOB = sDOB + Integer.toString(month);
                }
                sDOB = sDOB + "-";
                if (day <= 9) {
                    sDOB = sDOB + "0" + Integer.toString(day);
                } else {
                    sDOB = sDOB + Integer.toString(day);
                }

            //2) Reset Errors

                name.setError(null);
                Email.setError(null);
                Username.setError(null);
                Password.setError(null);
                Passwordconfirm.setError(null);
                View focusView = null;

            //3) Check for Errors

                if (TextUtils.isEmpty(sname)) {
                    name.setError(getString(R.string.error_field_required));
                    focusView = name;
                    name.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(sEmail)) {
                    Email.setError(getString(R.string.error_field_required));
                    focusView = Email;
                    Email.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(sUsername)) {
                    Username.setError(getString(R.string.error_field_required));
                    focusView = Username;
                    Username.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(sPassword)) {
                    Password.setError(getString(R.string.error_field_required));
                    focusView = Password;
                    Password.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(sPasswordconfirm)) {
                    Passwordconfirm.setError(getString(R.string.error_field_required));
                    focusView = Passwordconfirm;
                    Passwordconfirm.requestFocus();
                    return;
                }


                if (checkName(sname) == false) {
                    return;
                }
                if (checkEmail(sEmail) == false) {
                    return;
                }
                if (checkPassword(sPassword) == false) {
                    return;
                }
                if (checkPasswordConfirm(sPassword, sPasswordconfirm) == false) {
                    return;
                }

                //4) Attempt login by spawning a new thread. After the thread is complete it calls
                //    mFragmenet handler
                    mFragmentHandler = new Handler(this);

                    mAuthTask = new UserRegisterTask(mFragmentHandler, sname, sEmail, sUsername, sPassword, sDOB, sPasswordconfirm);
                    mAuthTask.execute((Void) null);
        }


    /**
     * Checks name by length
     * @param sname
     * @return
     */
    public boolean checkName(String sname) {
        if (sname.length() > 10) {
            name.setError("Name is too long ");
            View focusView = name;
            name.requestFocus();
            return false;
        } else if (sname.length() == 1) {
            name.setError("Name is too short ");
            View focusView = name;
            name.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Checks email by validating agianst an API
     * @param email
     * @return
     */
    public boolean checkEmail(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            Email.setError("Email Error ");
            View focusView = Email;
            Email.requestFocus();
            result = false;
        }
        return result;
    }

    /**
     * Checks password by length. Version 2 should be smarter.
     * @param
     * @return
     */
    public boolean checkPassword(String password) {
        if (password.length() < 4) {
            View focusView = null;
            Password.setError("Password too short ");
            focusView = Password;
            Password.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Makes sure passwords match
     * @param
     * @return
     */
    public boolean checkPasswordConfirm(String password, String passwordConfirm) {
        if (password.equals(passwordConfirm) == false) {
            View focusView = null;
            Passwordconfirm.setError("Passwords don't match!!");
            focusView = Password;
            Password.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Check if there is a user with the same email or username
     * @param
     * @return
     */
    public void registrationErrorPopup(String message) {
        if(message.equals("{\"non_field_errors\":[\"A user with this username or email exists already\"]}")){
            message = "A user with this username or email exists already";
        }


        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle("Error");


        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                /*.setNegativeButton("Cancel to exit, but not save game", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });*/

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }




    /**
     * Is called after the thread (that deals with network logging in) is done.
     * Msg contains an object that you send to it
     * @param
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean handleMessage(Message msg) {

        boolean result = (boolean) msg.obj;
        if(result == false){
            System.out.println("message false");
            registrationErrorPopup(this.errorMessage);
        }else {

            Intent intent = new Intent(getBaseContext(), regristrationSuccess.class);
            intent.putExtra("name", name.getText().toString());
            intent.putExtra("username",Username.getText().toString());

            startActivity(intent);
        }

        return false;
    }











    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String sname;
        private final String sEmail;
        private final String sUsername;
        private final String sPassword;
        private final String sPasswordConfirm;
        public static final int MSG_FINISHED = 1001;
        private Handler handler;


        private final String sDOB;

        UserRegisterTask(Handler handler, String sname, String sEmail, String sUsername, String sPassword, String sDOB, String sPasswordConfirm) {
           this.handler = handler;
            this.sname = sname;
            this.sEmail = sEmail;
            this.sDOB = sDOB;
            this.sPassword = sPassword;
            this.sUsername = sUsername;
            this.sPasswordConfirm = sPasswordConfirm;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            errorMessage = "Error Creating Account";
            isError = false;

            // TODO: attempt authentication against a network service.
            Log.i("doInBackground()", "Attempting register");

            System.out.println("sname is + " + this.sname);
            System.out.println("sEmail is + " + this.sEmail);
            System.out.println("sUsername is + " + this.sUsername);
            System.out.println("sPassword is + " + this.sPassword);
            System.out.println("sDOB is + " + this.sDOB);

            try{
                //Open connection to login server ?format=json
                URL url = new URL("http://159.203.136.133/api/v2/register/");
                HttpURLConnection urlConn =  (HttpURLConnection) url.openConnection();
                urlConn.setRequestProperty("Content-Type", "application/json");
                urlConn.setRequestMethod("POST");
                urlConn.setDoOutput(true);

                //Create JSON Object with credentials
                registerUser u = new registerUser(this.sname,this.sEmail,this.sPassword,this.sUsername,this.sDOB);
                Gson gson = new Gson();
                String jsonObject = gson.toJson(u);
                System.out.println(jsonObject);

                //Connect and attempt to write
                urlConn.connect();
                OutputStream os = urlConn.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                osw.write(jsonObject);
                osw.flush();
                osw.close();

                //Get response message
                int responseCode = urlConn.getResponseCode();
                System.out.println("POST Response Code :: " + responseCode);
                if (responseCode == HttpURLConnection.HTTP_CREATED) { //success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            urlConn.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);

                    }
                    in.close();
                    // print result
                    System.out.println(response.toString());

                    return true;
                }else if(responseCode == HttpURLConnection.HTTP_BAD_REQUEST){

                    System.out.println(urlConn.getResponseMessage());

                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            urlConn.getErrorStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);

                    }
                    in.close();
                    // print result
                    System.out.println(response.toString());
                    isError = true;
                    errorMessage = response.toString();


                    //pop up with the message
                }else{
                    isError = true;
                }






            }catch(Exception e){
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                System.out.println("Sucess, Preparing to load up success page");
                handler.sendMessage( Message.obtain( handler, MSG_FINISHED, true));

                finish();
            } else {
                handler.sendMessage( Message.obtain( handler, MSG_FINISHED, false));

                System.out.println("State that there was an unknown error (popup)");
                //errors
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }












    /**
     * We need this class so that we can serialize the data using
     * Googles GSON Object
     * */
    public static class registerUser{
        String username;
        String email;
        String password;
        String dob;
        String full_name;
        int phone_number;
        String school;
        String work;
        String bio;
        String city;
        String country;

        public registerUser(String f, String e, String p, String u, String dob){
            this.full_name = f; this.email = e; this.password = p; this.username = u; this.dob = dob;
            this.school = "";
            this.work = "";
            this.bio = "";
            this.city = "";
            this.country = "";
        }
    }







}

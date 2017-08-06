package com.example.amalkheir.project_r;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.*;
import android.app.Activity;
import android.content.Intent;
import com.example.amalkheir.project_r.objects.session;

public class Intro extends AppCompatActivity  {
   /**
    * Session object which contains session token (for security),
    * loggedin user, and goals. static so that all page threads can
    * see it.
    */
    public static session sesh;

    /**
     * Called when the home page is created
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("onCreate()", "creating intro");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);

        //Set Button Listeners------
            Button registerbtn = (Button)findViewById(R.id.registerButton);
            registerbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("register clicked");

                    Intent Intent = new Intent(view.getContext(), register.class);
                    view.getContext().startActivity(Intent);}

            });


            Button loginbtn = (Button)findViewById(R.id.loginbtn);
            loginbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("login clicked");

                    Intent Intent = new Intent(view.getContext(), login.class);
                    view.getContext().startActivity(Intent);
                }
            });

        //Create a new session object (later check if user is logged in)

            sesh = new session();

    }

}

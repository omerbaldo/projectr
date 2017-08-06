package com.example.amalkheir.project_r;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class regristrationSuccess extends AppCompatActivity {

    TextView welcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regristration_success);
        String s = getIntent().getStringExtra("name");
        welcome  = (TextView) findViewById(R.id.Welcome);
        welcome.setText("Welcome " + s);

        Button loginbtn = (Button)findViewById(R.id.loginbtn);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("login clicked");

                Intent Intent = new Intent(view.getContext(), login.class);
                view.getContext().startActivity(Intent);
            }
        });

    }
}

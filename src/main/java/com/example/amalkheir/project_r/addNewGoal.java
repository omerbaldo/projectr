package com.example.amalkheir.project_r;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Switch;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.amalkheir.project_r.Intro.sesh;
import static com.example.amalkheir.project_r.R.id.Username;

public class addNewGoal extends AppCompatActivity implements Handler.Callback{
    EditText title;
    Button add;
    Switch SwitchStick;
    private Handler mFragmentHandler;
    private addGoalTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_goal);
        title = (EditText) findViewById(R.id.title);
        add = (Button) findViewById(R.id.Register);
        SwitchStick = (Switch) findViewById(R.id.switch1);

        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addGoal();
            }
        });




    }


    @SuppressWarnings("unchecked")
    @Override
    public boolean handleMessage(Message msg) {
        boolean result = (boolean) msg.obj;
        if(result == false){
        }else {
            Intent intent = new Intent(getBaseContext(), dashBoard.class);
            startActivity(intent);
        }
        return false;
    }







    public void addGoal(){
        String name = title.getText().toString();
        boolean isPublic = SwitchStick.isChecked();
        mFragmentHandler = new Handler(this);

        mAuthTask = new addGoalTask(mFragmentHandler, name, isPublic);
        mAuthTask.execute((Void) null);
    }






    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class addGoalTask extends AsyncTask<Void, Void, Boolean> {

        private final String title;
        private final boolean isPublic;
        public static final int MSG_FINISHED = 1001;
        private Handler handler;


        addGoalTask(Handler handler,String title, boolean isPublic) {
            this.handler = handler;
            this.title = title;
            this.isPublic = isPublic;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return Intro.sesh.getUser().addGoal(this.title, this.isPublic);
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
        }
    }




}

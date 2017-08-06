package com.example.amalkheir.project_r;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.app.SearchManager;

import android.view.View.OnAttachStateChangeListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;


import android.widget.ListView;

import android.view.View.OnClickListener;
import android.widget.Toast;

import com.example.amalkheir.project_r.objects.goal;
import com.example.amalkheir.project_r.objects.raceList;
import com.example.amalkheir.project_r.objects.user;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.amalkheir.project_r.R.id.Username;
import static com.example.amalkheir.project_r.R.id.email;


public class dashBoard extends AppCompatActivity implements Handler.Callback {

    LinearLayout dash;      /* User Dashboard elements. Hides when search is started */
    Toolbar toolbar;        /* Tool Bar */
    ImageView profilePic;   /* Profile Pic **** Need to download pic next time */
    TextView name;          /* Username */
    TextView job;           /* Job */
    TextView bio;           /* Bio */
    TextView raceList;      /* Goals */
    TextView watchList;     /* Goals you are watching */
    ListView lv;            /* Listview to hold race / watch list */

    ListView searchResults; /* Search results that holds all user names  */

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private Handler mFragmentHandler;
    private populateRaceList mAuthTask = null;

    private populateSearchList sAuthTask = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dash_board);

        // 1) Set up buttons
        profilePic = (ImageView) findViewById(R.id.profilePic);
        name = (TextView) findViewById(R.id.name);
        job = (TextView) findViewById(R.id.job);
        bio = (TextView) findViewById(R.id.bio);
        raceList = (TextView) findViewById(R.id.raceList);
        watchList = (TextView) findViewById(R.id.watchList);
        FloatingActionButton addGoal = (FloatingActionButton) findViewById(R.id.addNewGoal);
        dash = (LinearLayout) findViewById(R.id.dash);

        //set tool bar
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        addGoal.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), addNewGoal.class);
                startActivity(intent);
            }
        });


        // 2) Set Listeners


        raceList.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        watchList.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        // 3) Set Initial Values
        updateDescription();

        // 4)
        mFragmentHandler = new Handler(this);



        mAuthTask = new populateRaceList(mFragmentHandler);
        mAuthTask.execute((Void) null);
        sAuthTask = new populateSearchList(mFragmentHandler);
        sAuthTask.execute((Void) null);




        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    public void updateDescription() {

        user loggedInUser = Intro.sesh.getUser();

        //Get info
        String username = loggedInUser.getUserName();
        String bio = loggedInUser.getBio();
        String work = loggedInUser.getWork();
        String picURL = loggedInUser.getProfilePicUrl();


        //Call Method To Set The Profile Picture
        //method needs to connect to network to download photo. run background process / thread to do so
        // profilePic.set = (ImageView )findViewById(R.id.profilePic);

        this.name.setText(username);
        if (bio == "") {
            this.bio.setText("bio");

        } else {
            this.bio.setText(bio);

        }
        if (work == "") {
            this.job.setText("job");

        } else {
            this.bio.setText(work);

        }

    }


    @SuppressWarnings("unchecked")
    @Override
    public boolean handleMessage(Message msg) {

        switch (msg.what) {

            case populateRaceList.MSG_FINISHED:

                displayGoals();
                return true;

            case populateSearchList.MSG_FINISHED:

                setUpSearch();
                return true;


        }
        return false;
    }




    public void displayGoals() {

        lv = (ListView) findViewById(R.id.listView);


        user u = Intro.sesh.getUser();
        System.out.println("the logged in user is  " + u.getUserName());



        ArrayList<goal> watchThis;

        if(Intro.sesh.getUser().RL == null){ //first time adding goals

            Intro.sesh.getUser().RL = new raceList();


            watchThis = Intro.sesh.getUser().RL.getGoals();

        }else {
            Intro.sesh.getUser().RL.fillGoals();
          watchThis = Intro.sesh.getUser().RL.getGoals();
        }
        System.out.println("watchthis size is  " + watchThis.size());


        ArrayAdapter<goal> arrayAdapter = new ArrayAdapter<goal>(
                this,
                android.R.layout.simple_list_item_1,
                watchThis);


        lv.setAdapter(arrayAdapter);


    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("dashBoard Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }



 //-------METHODS TO SET UP MENU / SEARCH

    ArrayAdapter<user> adapter;
    public void setUpSearch() {
        //Set up list view to look at an array of user names
        searchResults = (ListView) findViewById(R.id.listViewCountry);
        ArrayList<String> arrayCountry = new ArrayList<>();
        arrayCountry.addAll(Arrays.asList(getResources().getStringArray(R.array.arraycountry)));


        adapter = new ArrayAdapter<user>(
                this,
                android.R.layout.simple_list_item_1,
                Intro.sesh.allUsers);


        searchResults.setAdapter(adapter);
        searchResults.setVisibility(View.GONE);

    }


    /*
        WHEN MENU IS CREATED SET UP THE SEARCH BAR
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                String s = "new string " + newText;
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

                adapter.getFilter().filter(newText);
                return false;
            }
        });

        //HIDE/SHOW BASED ON SEARCH
        searchView.addOnAttachStateChangeListener(new OnAttachStateChangeListener() {

            @Override
            public void onViewDetachedFromWindow(View arg0) {
                dash.setVisibility(View.VISIBLE); // can see user dashboard
                searchResults.setVisibility(View.GONE); // cant see search view
                // search was detached/closed
            }

            @Override
            public void onViewAttachedToWindow(View arg0) {
                dash.setVisibility(View.GONE); // cant see user dashboard
                searchResults.setVisibility(View.VISIBLE); //can see search view
                // search was opened
            }
        });
    return true;
    }



    /*
         When you select an option from the menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int res_id = item.getItemId();
        if (res_id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "You select settings option", Toast.LENGTH_LONG).show();
        }
        return true;
    }















//TASK TO RETRIEVE USER INFORMATION----------------------------------------------------------------


    /**
     * A Thread that gets information from the DB about the user
     */
    public class populateRaceList extends AsyncTask<Void, Void, Boolean> {

        private Handler handler;
        public static final int MSG_FINISHED = 1001;

        populateRaceList(Handler handler) {
            this.handler = handler;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Log.i("doInBackground()", "Attempting login");




            if(Intro.sesh.getUser().listGoals() == false){
                //errorField = (TextView) findViewById(R.id.loginInError);
                //errorField.setText("Error logging in ");
                System.out.println("Failure");

                handler.sendMessage( Message.obtain( handler, MSG_FINISHED, false));

                return false;
            }else{
                System.out.println("Success");
            }
            handler.sendMessage( Message.obtain( handler, MSG_FINISHED, true));
            return true;
        }


        /**
         * Callback for the doInTheBackground
         *
         * @param success
         */
        @Override
        protected void onPostExecute(final Boolean success) {

        }

        @Override
        protected void onCancelled() {

        }
    }

    /**
     * A Thread that gets information from the DB about the user
     */
    public class populateSearchList extends AsyncTask<Void, Void, Boolean> {

        private Handler handler;
        public static final int MSG_FINISHED = 2000;

        populateSearchList(Handler handler) {
            this.handler = handler;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Log.i("doInBackground()", "Attempting searchhhhhh");

            Intro.sesh.fillUsers();


            handler.sendMessage( Message.obtain( handler, MSG_FINISHED, false));

            return true;
        }


        /**
         * Callback for the doInTheBackground
         *
         * @param success
         */
        @Override
        protected void onPostExecute(final Boolean success) {

        }

        @Override
        protected void onCancelled() {

        }
    }

















}

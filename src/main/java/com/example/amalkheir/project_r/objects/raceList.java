package com.example.amalkheir.project_r.objects;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by amalkheir on 6/3/17.
 */

public class raceList {
    int owner;
    ArrayList<String> user_goals; //URLS To Goals
    ArrayList<goal> goals;
    public raceList(){
        user_goals = new ArrayList<String>();
        goals = new ArrayList<goal>();
    }

    public ArrayList<goal> getGoals(){

        return this.goals;
    }


    public boolean fillGoals(){
        try {
            for(String s: user_goals){
                URL url = new URL(s);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    System.out.println("response: " + response);

                    Gson g = new Gson();
                    goal GL = g.fromJson(response.toString(), goal.class);
                    System.out.println("goal " + GL.title);
                    this.goals.add(GL);
                    System.out.println("al size " + this.goals.size());

                }

            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;


    }

}

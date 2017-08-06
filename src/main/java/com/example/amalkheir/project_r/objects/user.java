package com.example.amalkheir.project_r.objects;
import java.io.*;
import java.net.*;

import com.example.amalkheir.project_r.Intro;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by amalkheir on 5/25/17.
 */

public class user {

    public int pk;
    public String username;
    public String email;
    public String dob;
    public String phone_number;
    public String school;
    public String work;
    public String bio;
    public String city;
    public String country;
    public String raceList;
    public String profilePicURL;

    public raceList RL;

    public user(){
    }

    public String toString(){
        return this.username;
    }

    public int compareTo(Object o){
        String other = (String) o;
        return this.username.compareTo(other);
    }

    public user(String name, String email, String password, String username, String dob){
        this.username = username;
        this.dob = dob;
        RL = new raceList();
    }
    public String getUserName(){
        if(username==null){return "";}
        return username;
    }
    public String getBio(){
        if(bio==null){return "";}
        return bio;
    }
    public String getWork(){
        if(work==null){return "";}
        return work;
    }
    public String getProfilePicUrl(){
        if(profilePicURL==null){return "";}
        return profilePicURL;
    }

    public boolean listGoals(){
        // 1. Create Connection
        HttpURLConnection connection = null;
        String targetURL = "http://159.203.136.133/api/v2/racelists/" + this.pk +"/?format=json";


        try {
            //2 Open connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
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
                raceList RL = g.fromJson(response.toString(), raceList.class);
                this.RL = RL;

                for(String s: this.RL.user_goals){
                    System.out.println("url to goal " + s);
                }

                this.RL.fillGoals();

                for(goal gg : this.RL.goals){
                    System.out.println("goal " + gg);

                }


                return true;
            }else{
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }








    public static class loginJSON{
        String username;
        String email;
        String password;
        public loginJSON(String u, String e,String p){
            this.username = u; this.email = e; this.password = p;
        }
    }




    public boolean addGoal(String title, boolean isPublic){

        try{
            //Open connection to login server
            URL url = new URL("http://159.203.136.133/api/v2/goals/");
            HttpURLConnection urlConn =  (HttpURLConnection) url.openConnection();
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.setRequestMethod("POST");
            urlConn.setDoOutput(true);


            urlConn.setRequestProperty("token", Intro.sesh.getToken());

            //Create JSON Object with goal object
            ArrayList<Integer> watchPks = new ArrayList<Integer>();
            goal g = new goal(this.pk, this.pk, title, isPublic, null, watchPks);
            Gson gson = new Gson();
            String jsonObject = gson.toJson(g);
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
            }else {
                System.out.println("Error message is : ");
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
            }


            if(this.RL == null){
                this.RL = new raceList();
                this.RL.fillGoals();
            }




        }catch(Exception e){
            e.printStackTrace();
        }


        return false;
    }




}

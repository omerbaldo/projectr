package com.example.amalkheir.project_r.objects;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amalkheir on 5/25/17.
 */

public class session {
    private String token;
    private user loggedInUser;
    public ArrayList<user> allUsers;

    public session (){
        this.loggedInUser = new user();
        this.token = null;
    }

    public String getToken(){
        return this.token;
    }
    public user getUser(){
        return this.loggedInUser;
    }

    public void logoff(){
        this.loggedInUser = null;
    }
    public int setUser(){
        return -1;
    }
    public boolean isLoggedIn(){
        if (token == null)
                return false;
        return true;
    }
    public ArrayList<user> fillUsers(){
        if(this.allUsers == null)
            this.allUsers = new ArrayList<user>();

        HttpURLConnection connection = null;
        String targetURL = "http://208.68.38.60/api/v2/users/?format=json";
        try {
            URL url = new URL(targetURL);                                   //Create connection
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            Gson g = new Gson();
            this.allUsers = g.fromJson(response.toString(), new TypeToken<List<user>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.allUsers;
    }

    public boolean attemptLogin(String username, String password){

        try{
            //Open connection to login server
              URL url = new URL("http://159.203.136.133/api/v2/login/?format=json");
            HttpURLConnection urlConn =  (HttpURLConnection) url.openConnection();
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.setRequestMethod("POST");
            urlConn.setDoOutput(true);

            //Create JSON Object with credentials
            user.loginJSON j = new user.loginJSON(username, null, password);
            Gson gson = new Gson();
            String jsonObject = gson.toJson(j);

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
            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        urlConn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);

                }
                in.close();

                //Set the user and token stuff.
                    loginResponse lr = gson.fromJson(response.toString(), loginResponse.class);
                    this.token = lr.token;

                    if(this.loggedInUser == null){
                        this.loggedInUser = new user();
                    }


                    this.loggedInUser.pk = lr.pk;
                    this.loggedInUser.username = lr.username;
                    this.loggedInUser.email = lr.email;
                    this.loggedInUser.dob = lr.dob;
                    this.loggedInUser.phone_number =  lr.phone_number;
                    this.loggedInUser.school =  lr.school;
                    this.loggedInUser.work =  lr.work;
                    this.loggedInUser.bio =  lr.bio;
                    this.loggedInUser.city =  lr.city;
                    this.loggedInUser.country =  lr.country;
                    this.loggedInUser.raceList = lr.raceList;
                System.out.println("Sucess, Login Token is " + this.token);
                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
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


    public static class loginResponse{
        public int pk;
        public String username;
        public String email;
        public String token;
        public String dob;
        public String phone_number;
        public String school;
        public String work;
        public String bio;
        public String city;
        public String country;
        public String raceList;
    }









}

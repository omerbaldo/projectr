package com.example.amalkheir.project_r.objects;

import java.util.ArrayList;

/**
 * Created by amalkheir on 6/3/17.
 */


public class goal{
    int owner;
    String title;
    int racelist;
    String deadline;
    boolean isPublic;
    boolean isFinished;
    ArrayList<Integer> watchers1;
    ArrayList<Integer> watchers;


    public goal(int userPK, int raceListPK, String title, boolean isPublic, String deadline, ArrayList<Integer> watchPks){
        this.owner = userPK;
        this.title = title;
        this.racelist = raceListPK;
        this.deadline = deadline;
        this.isPublic = isPublic;
        this.isFinished = false;
        this.watchers1 = watchPks;
    }

    public String toString() {
        return this.title;
    }

}

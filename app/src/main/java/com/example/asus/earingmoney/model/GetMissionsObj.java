package com.example.asus.earingmoney.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class GetMissionsObj {

    @SerializedName("MissionNum")
    private int MissionNum;

    @SerializedName("AllMissions")
    private JsonArray AllMissions;

    public int getMissionNum() {
        return MissionNum;
    }

    public ArrayList<Mission> getAllMissions() {
        ArrayList<Mission> missions = new ArrayList<Mission>();
        for(JsonElement i : AllMissions){
            Mission mission = new Mission();
            Gson gson = new Gson();
            String json = i.toString();
            mission = gson.fromJson(json,Mission.class);
            missions.add(mission);
        }
        return missions;
    }
}

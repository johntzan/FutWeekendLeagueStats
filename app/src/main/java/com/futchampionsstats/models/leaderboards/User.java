package com.futchampionsstats.models.leaderboards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yiannitzan on 5/3/17.
 */

public class User implements Serializable{

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("console")
    @Expose
    private String console;
    @SerializedName("rankings")
    @Expose
    private List<Month> rankings = null;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getConsole() {
        return console;
    }

    public void setConsole(String console) {
        this.console = console;
    }

    public List<Month> getRankings() {
        return rankings;
    }

    public void setRankings(List<Month> rankings) {
        this.rankings = rankings;
    }

    public class Month implements Serializable{

        @SerializedName("month")
        @Expose
        private List<String> month = null;


        public List<String> getMonth() {
            return month;
        }

        public void setMonth(List<String> month) {
            this.month = month;
        }

    }


}

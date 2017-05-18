package com.futchampionsstats.models.leaderboards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Top100 {

    @SerializedName("console")
    @Expose
    private List<String[]> console = null;

    public List<String[]> getConsole() {
        return console;
    }

    public void setConsole(List<String[]> console) {
        this.console = console;
    }

}
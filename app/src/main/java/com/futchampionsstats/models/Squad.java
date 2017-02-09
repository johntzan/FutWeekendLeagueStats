package com.futchampionsstats.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;

/**
 * Created by yiannitzan on 2/8/17.
 */

public class Squad extends BaseObservable implements Serializable {

    @Bindable
    private String name;
    @Bindable
    private String formation;
    @Bindable
    private String team_rating;

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Bindable
    public String getFormation() {
        return formation;
    }

    public void setFormation(String formation) {
        this.formation = formation;
    }

    @Bindable
    public String getTeam_rating() {
        return team_rating;
    }

    public void setTeam_rating(String team_rating) {
        this.team_rating = team_rating;
    }

}

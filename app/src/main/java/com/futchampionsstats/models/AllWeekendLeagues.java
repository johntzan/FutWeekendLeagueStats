package com.futchampionsstats.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.futchampionsstats.BR;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by yiannitzan on 12/30/16.
 */

public class AllWeekendLeagues extends BaseObservable implements Serializable{

    @Bindable
    private ArrayList<WeekendLeague> allWeekendLeagues;

    public ArrayList<WeekendLeague> getAllWeekendLeagues() {
        return allWeekendLeagues;
    }

    public void setAllWeekendLeagues(ArrayList<WeekendLeague> allWeekendLeagues) {
        this.allWeekendLeagues = allWeekendLeagues;
        notifyPropertyChanged(BR._all);
    }
}

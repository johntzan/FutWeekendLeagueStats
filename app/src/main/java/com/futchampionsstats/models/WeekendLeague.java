package com.futchampionsstats.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.futchampionsstats.BR;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by yiannitzan on 12/30/16.
 */

public class WeekendLeague extends BaseObservable implements Serializable{

    @Bindable
    private ArrayList<Game> weekendLeague;

    @Bindable
    public ArrayList<Game> getWeekendLeague() {
        return weekendLeague;
    }

    public void setWeekendLeague(ArrayList<Game> weekendLeague) {
        this.weekendLeague = weekendLeague;
    }

    @Bindable
    public String getWinTotal(){
        int gamesWon = 0;
        if(this.weekendLeague.size()>0){
            int i = 0;
            while(i<this.weekendLeague.size()){
                Game currGame = this.weekendLeague.get(i);
                if(currGame.getUser_won()){
                    gamesWon++;
                }
                i++;
            }
        }
        return Integer.toString(gamesWon);
    }

    @Bindable
    public String getDisconnectTotal(){
        int gamesDisconnected = 0;
        if(this.weekendLeague.size()>0){
            int i = 0;
            while(i<this.weekendLeague.size()){
                Game currGame = this.weekendLeague.get(i);
                if(currGame.getGame_disconnected()){
                    gamesDisconnected++;
                }
                i++;
            }
        }
        return Integer.toString(gamesDisconnected);
    }

    @Bindable
    public String getQuitTotal(){
        int totalQuits = 0;
        if(this.weekendLeague.size()>0){
            int i = 0;
            while(i<this.weekendLeague.size()){
                Game currGame = this.weekendLeague.get(i);
                if(currGame.isRage_quit()){
                    totalQuits++;
                }
                i++;
            }
        }
        return Integer.toString(totalQuits);
    }

    public void clearWL(){
        this.weekendLeague.clear();
        notifyPropertyChanged(BR._all);
    }


}

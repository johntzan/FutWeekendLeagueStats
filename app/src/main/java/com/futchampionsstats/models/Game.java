package com.futchampionsstats.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.futchampionsstats.BR;

import java.io.Serializable;

import static android.content.ContentValues.TAG;

/**
 * Created by yiannitzan on 12/29/16.
 */

public class Game extends BaseObservable implements Serializable{

    private String game_id;

    @Bindable
    private Boolean user_won;

    @Bindable
    private String opp_name;

    @Bindable
    private String user_goals;
    @Bindable
    private String opp_goals;

    @Bindable
    private String user_shots;
    @Bindable
    private String opp_shots;

    @Bindable
    private String user_sog;
    @Bindable
    private String opp_sog;

    @Bindable
    private String user_possession;
    @Bindable
    private String opp_possession;

    @Bindable
    private String user_tackles;
    @Bindable
    private String opp_tackles;

    @Bindable
    private String user_corners;
    @Bindable
    private String opp_corners;

    @Bindable
    private String user_team;
    @Bindable
    private String opp_team;

    @Bindable
    private String user_formation;
    @Bindable
    private String opp_formation;

    @Bindable
    private String user_team_rating;
    @Bindable
    private String opp_team_rating;

    @Bindable
    private boolean penalties;
    @Bindable
    private String user_pen_score;
    @Bindable
    private String opp_pen_score;

    @Bindable
    private boolean rage_quit;
    @Bindable
    private String minute_rage_quit;

    @Bindable
    private String game_notes;

    @Bindable
    private Boolean game_disconnected = false;

    @Bindable
    public String getUser_goals() {
        return user_goals;
    }

    public void setUser_goals(String user_goals) {
        this.user_goals = user_goals;
        notifyPropertyChanged(BR._all);
    }

    @Bindable
    public String getOpp_goals() {
        return opp_goals;
    }

    public void setOpp_goals(String opp_goals) {
        this.opp_goals = opp_goals;
        notifyPropertyChanged(BR._all);
    }

    @Bindable
    public String getUser_shots() {
        return user_shots;
    }

    public void setUser_shots(String user_shots) {
        this.user_shots = user_shots;
    }

    @Bindable
    public String getOpp_shots() {
        return opp_shots;
    }

    public void setOpp_shots(String opp_shots) {
        this.opp_shots = opp_shots;
    }

    @Bindable
    public String getUser_sog() {
        return user_sog;
    }

    public void setUser_sog(String user_sog) {
        this.user_sog = user_sog;
    }

    @Bindable
    public String getOpp_sog() {
        return opp_sog;
    }

    public void setOpp_sog(String opp_sog) {
        this.opp_sog = opp_sog;
    }

    @Bindable
    public String getUser_possession() {
        return user_possession;
    }

    public void setUser_possession(String user_possession) {
        this.user_possession = user_possession;
        notifyPropertyChanged(BR._all);
    }

    @Bindable
    public String getOpp_possession() {
        return opp_possession;
    }

    public void setOpp_possession(String opp_possession) {
        this.opp_possession = opp_possession;
        notifyPropertyChanged(BR._all);
    }

    @Bindable
    public String getUser_tackles() {
        return user_tackles;
    }

    public void setUser_tackles(String user_tackles) {
        this.user_tackles = user_tackles;
    }

    @Bindable
    public String getOpp_tackles() {
        return opp_tackles;
    }

    public void setOpp_tackles(String opp_tackles) {
        this.opp_tackles = opp_tackles;
    }

    @Bindable
    public String getUser_corners() {
        return user_corners;
    }

    public void setUser_corners(String user_corners) {
        this.user_corners = user_corners;
    }

    @Bindable
    public String getOpp_corners() {
        return opp_corners;
    }

    public void setOpp_corners(String opp_corners) {
        this.opp_corners = opp_corners;
    }

    @Bindable
    public String getUser_team() {
        return user_team;
    }

    public void setUser_team(String user_team) {
        this.user_team = user_team;
    }

    @Bindable
    public String getOpp_team() {
        return opp_team;
    }

    public void setOpp_team(String opp_team) {
        this.opp_team = opp_team;
    }

    @Bindable
    public String getUser_formation() {
        return user_formation;
    }

    public void setUser_formation(String user_formation) {
        this.user_formation = user_formation;
    }

    @Bindable
    public String getOpp_formation() {
        return opp_formation;
    }

    public void setOpp_formation(String opp_formation) {
        this.opp_formation = opp_formation;
    }

    @Bindable
    public String getUser_team_rating() {
        return user_team_rating;
    }

    public void setUser_team_rating(String user_team_rating) {
        this.user_team_rating = user_team_rating;
    }

    @Bindable
    public String getOpp_team_rating() {
        return opp_team_rating;
    }

    public void setOpp_team_rating(String opp_team_rating) {
        this.opp_team_rating = opp_team_rating;
    }

    @Bindable
    public boolean isPenalties() {
        return penalties;
    }

    public boolean checkPenaltiesView(){
        if(this.user_goals!=null && this.opp_goals!=null){
            if(this.user_goals.equals(this.opp_goals)){
                if(this.user_goals.equals("") || this.opp_goals.equals("")){
                    resetPenScore();
                    return false;
                }
                this.setPenalties(true);
                return true;
            }
            else{
                resetPenScore();
                return false;
            }
        }
        else{
            resetPenScore();
            return false;
        }
    }

    private void resetPenScore(){
        this.setPenalties(false);
        this.setUser_pen_score(null);
        this.setOpp_pen_score(null);
    }


    public void setPenalties(boolean penalties) {
        this.penalties = penalties;
        notifyPropertyChanged(BR._all);
    }

    @Bindable
    public String getUser_pen_score() {
        return user_pen_score;
    }

    public void setUser_pen_score(String user_pen_score) {
        this.user_pen_score = user_pen_score;
    }

    @Bindable
    public String getOpp_pen_score() {
        return opp_pen_score;
    }

    public void setOpp_pen_score(String opp_pen_score) {
        this.opp_pen_score = opp_pen_score;
    }

    @Bindable
    public boolean isRage_quit() {
        return rage_quit;
    }

    public void setRage_quit(boolean rage_quit) {
        this.rage_quit = rage_quit;
        notifyPropertyChanged(BR._all);
    }

    @Bindable
    public String getMinute_rage_quit() {
        return minute_rage_quit;
    }

    public void setMinute_rage_quit(String minute_rage_quit) {
        this.minute_rage_quit = minute_rage_quit;
    }

    @Bindable
    public String getGame_notes() {
        return game_notes;
    }

    public void setGame_notes(String game_notes) {
        this.game_notes = game_notes;
    }

    public Boolean getUser_won() {
        return user_won;
    }

    public void setUser_won(Boolean user_won) {
        this.user_won = user_won;
    }

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    @Bindable
    public String getOpp_name() {
        return opp_name;
    }

    public void setOpp_name(String opp_name) {
        this.opp_name = opp_name;
    }

    @Bindable
    public Boolean getGame_disconnected() {
        if(this.game_disconnected!=null){
            return game_disconnected;
        }
        else{
            return false;
        }
    }

    public void setGame_disconnected(Boolean game_disconnected) {
        this.game_disconnected = game_disconnected;
        notifyPropertyChanged(BR._all);
    }

    public boolean checkIfNotNull(){
        return (this.getUser_goals()!=null && this.getOpp_goals()!=null && this.getUser_shots()!=null && this.getOpp_shots()!=null &&
                this.getUser_sog()!=null && this.getOpp_sog()!=null && this.getUser_possession()!=null && this.getOpp_possession()!=null &&
                this.getUser_tackles()!=null && this.getOpp_tackles()!=null && this.getUser_corners()!=null && this.getOpp_corners()!=null &&
                this.getUser_team()!=null && this.getOpp_team()!=null && this.getUser_formation()!=null && this.getOpp_formation()!=null &&
                this.getUser_team_rating()!=null && this.getOpp_team_rating()!=null && this.getOpp_name()!=null);

    }

    public boolean checkIfNotEmpty(){

        return (!this.getUser_goals().equals("") && !this.getOpp_goals().equals("") && !this.getUser_shots().equals("") && !this.getOpp_shots().equals("") &&
                !this.getUser_sog().equals("") && !this.getOpp_sog().equals("") && !this.getUser_possession().equals("") && !this.getOpp_possession().equals("") &&
                !this.getUser_tackles().equals("") && !this.getOpp_tackles().equals("") && !this.getUser_corners().equals("") && !this.getOpp_corners().equals("") &&
                !this.getUser_team().equals("") && !this.getOpp_team().equals("") && !this.getUser_formation().equals("")&& !this.getOpp_formation().equals("") &&
                !this.getUser_team_rating().equals("") && !this.getOpp_team_rating().equals("") && !this.getOpp_name().equals(""));
    }


    public void finished(){
        Log.d(TAG, "finished: " + this.getUser_goals() +  " opp: " + this.getOpp_goals());
    }
}

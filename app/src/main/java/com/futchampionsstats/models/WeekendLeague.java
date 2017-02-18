package com.futchampionsstats.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.futchampionsstats.BR;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

import static com.futchampionsstats.Utils.Utils.calculateAverage;

/**
 * Created by yiannitzan on 12/30/16.
 */

public class WeekendLeague extends BaseObservable implements Serializable{

    public static final String TAG = WeekendLeague.class.getSimpleName();
    @Bindable
    public String dateOfWL;

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
    public String getDateOfWL() {
        return dateOfWL;
    }

    public void setDateOfWL(String dateOfWL) {
        this.dateOfWL = dateOfWL;
        notifyPropertyChanged(BR.dateOfWL);
    }

    public static String getWinTotal(WeekendLeague weekendLeague){
        int gamesWon = 0;
        if(weekendLeague.getWeekendLeague().size()>0){
            int i = 0;
            while(i<weekendLeague.getWeekendLeague().size()){
                Game currGame = weekendLeague.getWeekendLeague().get(i);
                if(currGame.getUser_won()){
                    gamesWon++;
                }
                i++;
            }
        }
        return Integer.toString(gamesWon);
    }

    public static String getGamesLeft(WeekendLeague wl){
        return String.valueOf(40 - wl.getWeekendLeague().size());
    }

    public static String[] getAvgShotsFor(WeekendLeague wl){

        if(wl.getWeekendLeague()!=null){

                try{
                    ArrayList<Integer> shotsFor = new ArrayList<>();
                    ArrayList<Integer> shotsForOnGoal = new ArrayList<>();
                    for(Game game : wl.getWeekendLeague()){
                        if((game.getGame_disconnected()!=null && !game.getGame_disconnected()) && (game.getUser_shots()!=null || game.getUser_sog()!=null)){
                            shotsFor.add(Integer.parseInt(game.getUser_shots()));
                            shotsForOnGoal.add(Integer.parseInt(game.getUser_sog()));
                        }
                    }
                    Double avgShotsFor = calculateAverage(shotsFor);
                    Double avgShotsOnG = calculateAverage(shotsForOnGoal);

                    return new String[]{String.format(Locale.US, "%.2f", avgShotsFor), String.format(Locale.US, "%.2f", avgShotsOnG)};
                }
                catch(NumberFormatException e){
                    Log.d(TAG, "avgShotsWatcher: " + e);
                    return new String[]{"0.00", "0.00"};
                }
            }else{
            return new String[]{"0.00", "0.00"};
        }

    }

    public static String getAvgShotsString(WeekendLeague wl){
        return getAvgShotsFor(wl)[0] + "(" + getAvgShotsFor(wl)[1] + ")";
    }

    public static String[] getAvgPoss(WeekendLeague wl){
        if(wl.getWeekendLeague()!=null){

                try{

                    ArrayList<Integer> possFor = new ArrayList<>();
                    ArrayList<Integer> possAgainst = new ArrayList<>();
                    for(Game game : wl.getWeekendLeague()){
                        if(!game.getGame_disconnected() && (game.getUser_possession()!=null || game.getOpp_possession()!=null)){
                            possFor.add(Integer.parseInt(game.getUser_possession()));
                            possAgainst.add(Integer.parseInt(game.getOpp_possession()));
                        }
                    }
                    Double avgPossFor = calculateAverage(possFor);
                    Double avgPossAgainst= calculateAverage(possAgainst);

                    return new String[]{String.format(Locale.US, "%.2f", avgPossFor), String.format(Locale.US, "%.2f", avgPossAgainst)};
                }
                catch(NumberFormatException e){
                    Log.d(TAG, "avgPossWatcher: " + e);
                    return new String[]{"0.00", "0.00"};
                }
        }else{
            return new String[]{"0.00", "0.00"};
        }
    }

    public static String getAvgPossString(WeekendLeague wl){
        return getAvgPoss(wl)[0] + "%(" + getAvgPoss(wl)[1] + "%)";
    }

    public static String[] getAvgPassAcc(WeekendLeague wl){
        if(wl.getWeekendLeague()!=null){

            try{

                ArrayList<Integer> passFor = new ArrayList<>();
                ArrayList<Integer> passAgainst = new ArrayList<>();
                for(Game game : wl.getWeekendLeague()){
                    if(!game.getGame_disconnected() && (game.getUser_pass_acc()!=null || game.getOpp_pass_acc()!=null)){
                        passFor.add(Integer.parseInt(game.getUser_pass_acc()));
                        passAgainst.add(Integer.parseInt(game.getOpp_pass_acc()));
                    }
                }
                Double avgPassFor = calculateAverage(passFor);
                Double avgPassAgainst= calculateAverage(passAgainst);

                return new String[]{String.format(Locale.US, "%.2f", avgPassFor), String.format(Locale.US, "%.2f", avgPassAgainst)};
            }
            catch(NumberFormatException e){
                Log.d(TAG, "avgPassWatcher: " + e);
                return new String[]{"0.00", "0.00"};
            }
        }else{
            return new String[]{"0.00", "0.00"};
        }
    }

    public static String getAvgPassAccString(WeekendLeague wl){
        return getAvgPassAcc(wl)[0] + "%(" + getAvgPassAcc(wl)[1] + "%)";
    }

    public static String getAvgGoalPerShotString(WeekendLeague wl){
            Double userGoalsAvg = Double.parseDouble(getAvgGoals(wl)[0]);
            Double userSOGAvg = Double.parseDouble(getAvgShotsFor(wl)[1]);
            Double userAvgGPSOG =  userGoalsAvg / userSOGAvg;

            Double oppGoalsAvg = Double.parseDouble(getAvgGoals(wl)[1]);
            Double oppSOGAvg = Double.parseDouble(getAvgShotsAgainst(wl)[1]);
            Double oppAvgGPSOG =  oppGoalsAvg / oppSOGAvg;


        if(!userAvgGPSOG.isNaN() && !oppAvgGPSOG.isNaN()){
            return String.format(Locale.US, "%.2f", userAvgGPSOG) + "(" + String.format(Locale.US, "%.2f", oppAvgGPSOG) + ")";
        }
        else{
            return "0.00(0.00)";
        }

    }

    public static String[] getAvgShotsAgainst(WeekendLeague wl){
        if(wl.getWeekendLeague()!=null){
                try{

                    ArrayList<Integer> shotsAgainstFor = new ArrayList<>();
                    ArrayList<Integer> shotsAgainstOnGoal = new ArrayList<>();
                    for(Game game : wl.getWeekendLeague()){
                        if(!game.getGame_disconnected() && (game.getOpp_shots()!=null || game.getOpp_sog()!=null)){
                            shotsAgainstFor.add(Integer.parseInt(game.getOpp_shots()));
                            shotsAgainstOnGoal.add(Integer.parseInt(game.getOpp_sog()));
                        }
                    }
                    Double avgShotsAgainstFor = calculateAverage(shotsAgainstFor);
                    Double avgShotsAgainstOnG = calculateAverage(shotsAgainstOnGoal);

                    return new String[]{String.format(Locale.US, "%.2f", avgShotsAgainstFor), String.format(Locale.US, "%.2f", avgShotsAgainstOnG)};
                }
                catch(NumberFormatException e){
                    Log.d(TAG, "avgShotsAgainstWatcher: " + e);
                    return new String[]{"0.00", "0.00"};
                }
        }else{
            return new String[]{"0.00", "0.00"};
        }

    }

    public static String getAvgShotsAgainstString(WeekendLeague wl){
        return getAvgShotsAgainst(wl)[0] + "(" + getAvgShotsAgainst(wl)[1] + ")";
    }

    public static String[] getAvgGoals(WeekendLeague wl){
        if(wl.getWeekendLeague()!=null){
            try{

                ArrayList<Integer> goalsFor = new ArrayList<>();
                ArrayList<Integer> goalsAgainst = new ArrayList<>();
                for(Game game : wl.getWeekendLeague()){
                    if(!game.getGame_disconnected() && (game.getUser_goals()!=null || game.getOpp_goals()!=null)){
                        goalsFor.add(Integer.parseInt(game.getUser_goals()));
                        goalsAgainst.add(Integer.parseInt(game.getOpp_goals()));
                    }
                }
                Double avgGoalsFor = calculateAverage(goalsFor);
                Double avgGoalsAgainst = calculateAverage(goalsAgainst);
                return new String[]{String.format(Locale.US, "%.2f", avgGoalsFor), String.format(Locale.US, "%.2f", avgGoalsAgainst)};
            }
            catch(NumberFormatException e){
                Log.d(TAG, "avgGoalsWatcher: " + e);
                return new String[]{"0.00", "0.00"};
            }
        }else{
            return new String[]{"0.00", "0.00"};
        }

    }

    public static String getAvgGoalsString(WeekendLeague wl){
        return getAvgGoals(wl)[0] + "(" + getAvgGoals(wl)[1] + ")";
    }

    public static String[] getAvgTackles(WeekendLeague wl){
        if(wl.getWeekendLeague()!=null){
            try{

                ArrayList<Integer> tacklesFor = new ArrayList<>();
                ArrayList<Integer> tacklesAgainst = new ArrayList<>();
                for(Game game : wl.getWeekendLeague()){
                    if(!game.getGame_disconnected() && (game.getUser_tackles()!=null || game.getOpp_tackles()!=null)){
                        tacklesFor.add(Integer.parseInt(game.getUser_tackles()));
                        tacklesAgainst.add(Integer.parseInt(game.getOpp_tackles()));
                    }
                }
                Double avgTacklesFor = calculateAverage(tacklesFor);
                Double avgTacklesAgainst= calculateAverage(tacklesAgainst);
                return new String[]{String.format(Locale.US, "%.2f", avgTacklesFor), String.format(Locale.US, "%.2f", avgTacklesAgainst)};
            }
            catch(NumberFormatException e){
                Log.d(TAG, "avgTacklesatcher: " + e);
                return new String[]{"0.00", "0.00"};
            }
        }else{
            return new String[]{"0.00", "0.00"};
        }

    }

    public static String getAvgTacklesString(WeekendLeague wl){
        return getAvgTackles(wl)[0] + "(" + getAvgTackles(wl)[1] + ")";
    }

    public static String getAvgTeamRating(WeekendLeague wl){
        if(wl.getWeekendLeague()!=null){
            try{

                ArrayList<Integer> teamRating = new ArrayList<>();
                for(Game game : wl.getWeekendLeague()){
                    if(!game.getGame_disconnected() && game.getOpp_team_rating()!=null){
                        teamRating.add(Integer.parseInt(game.getOpp_team_rating()));
                    }
                }
                Double avgTeamRating = calculateAverage(teamRating);
                return String.format(Locale.US, "%.2f", avgTeamRating);
            }
            catch(NumberFormatException e){
                Log.d(TAG, "avgTeamRatingWatcher: " + e);
                return "0.00";
            }
        }else{
            return "0.00";
        }

    }

    public static String getDisconnectTotal(WeekendLeague weekendLeague){
        int gamesDisconnected = 0;
        if(weekendLeague.getWeekendLeague().size()>0){
            int i = 0;
            while(i<weekendLeague.getWeekendLeague().size()){
                Game currGame = weekendLeague.getWeekendLeague().get(i);
                if(currGame.getGame_disconnected()){
                    gamesDisconnected++;
                }
                i++;
            }
        }
        return Integer.toString(gamesDisconnected);
    }


    public static String getQuitTotal(WeekendLeague weekendLeague){
        int totalQuits = 0;
        if(weekendLeague.getWeekendLeague().size()>0){
            int i = 0;
            while(i<weekendLeague.getWeekendLeague().size()){
                Game currGame = weekendLeague.getWeekendLeague().get(i);
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

package com.futchampionsstats.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.futchampionsstats.BR;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

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

    public static String getTotalWLs(AllWeekendLeagues allWeekendLeagues){
        return String.valueOf(allWeekendLeagues.getAllWeekendLeagues().size());
    }

    public static boolean showWLsDeleteBtn(AllWeekendLeagues allWeekendLeagues){
        return allWeekendLeagues.getAllWeekendLeagues().size()>0;
    }

    public static String getTotalGamesWon(AllWeekendLeagues allWeekendLeagues){
        int gamesWon = 0;
        if(allWeekendLeagues.getAllWeekendLeagues()!=null){

            for (WeekendLeague weekendLeague : allWeekendLeagues.getAllWeekendLeagues()) {
                gamesWon += Integer.parseInt(WeekendLeague.getWinTotal(weekendLeague));
            }

        }
        return String.valueOf(gamesWon);
    }

    public static String getTotalGamesPlayed(AllWeekendLeagues allWeekendLeagues){
        int gamesPlayed = 0;
        if(allWeekendLeagues.getAllWeekendLeagues()!=null){

            for (WeekendLeague weekendLeague : allWeekendLeagues.getAllWeekendLeagues()) {
                gamesPlayed += weekendLeague.getWeekendLeague().size();
            }

        }
        return String.valueOf(gamesPlayed);
    }

    public static String[] getAvgShotsFor(AllWeekendLeagues allWeekendLeagues){
        Double shotsFor = 0.0;
        Double shotsOnG = 0.0;
        if(allWeekendLeagues.getAllWeekendLeagues()!=null && allWeekendLeagues.getAllWeekendLeagues().size()>0) {

            try {

                for (WeekendLeague weekendLeague : allWeekendLeagues.getAllWeekendLeagues()) {
                    shotsFor += Double.parseDouble(WeekendLeague.getAvgShotsFor(weekendLeague)[0]);
                    shotsOnG += Double.parseDouble(WeekendLeague.getAvgShotsFor(weekendLeague)[1]);
                }
                shotsFor = shotsFor / allWeekendLeagues.getAllWeekendLeagues().size();
                shotsOnG = shotsOnG / allWeekendLeagues.getAllWeekendLeagues().size();

                return new String[]{String.format(Locale.US, "%.2f", shotsFor), String.format(Locale.US, "%.2f", shotsOnG)};

            } catch (NumberFormatException e) {
                return new String[]{"0.00", "0.00"};
            }
        }else{
            return new String[]{"0.00", "0.00"};
        }

    }

    public static String getAvgShotsForString(AllWeekendLeagues allWeekendLeagues){
        return getAvgShotsFor(allWeekendLeagues)[0]+ "(" + getAvgShotsFor(allWeekendLeagues)[1]+")";
    }

    public static String[] getAvgShotsAgainst(AllWeekendLeagues allWeekendLeagues){
        Double shotsAgainst = 0.0;
        Double shotsAgainstOnG = 0.0;
        if(allWeekendLeagues.getAllWeekendLeagues()!=null && allWeekendLeagues.getAllWeekendLeagues().size()>0){
            try{


                for (WeekendLeague weekendLeague : allWeekendLeagues.getAllWeekendLeagues()) {
                    shotsAgainst += Double.parseDouble(WeekendLeague.getAvgShotsAgainst(weekendLeague)[0]);
                    shotsAgainstOnG += Double.parseDouble(WeekendLeague.getAvgShotsAgainst(weekendLeague)[1]);
                }

                shotsAgainst = shotsAgainst / allWeekendLeagues.getAllWeekendLeagues().size();
                shotsAgainstOnG = shotsAgainstOnG / allWeekendLeagues.getAllWeekendLeagues().size();

                return new String[]{String.format(Locale.US, "%.2f", shotsAgainst), String.format(Locale.US, "%.2f", shotsAgainstOnG)};
            }
            catch (NumberFormatException e){
                return new String[]{"0.00", "0.00"};
            }
        }
        else{
            return new String[]{"0.00", "0.00"};
        }
    }

    public static String getAvgShotsAgainstString(AllWeekendLeagues allWeekendLeagues){
        return getAvgShotsAgainst(allWeekendLeagues)[0] + "(" + getAvgShotsAgainst(allWeekendLeagues)[1] + ")";
    }


    public static String[] getAvgGoals(AllWeekendLeagues allWeekendLeagues){
        Double goalsFor = 0.0;
        Double goalsAgainst = 0.0;
        if(allWeekendLeagues.getAllWeekendLeagues()!=null && allWeekendLeagues.getAllWeekendLeagues().size()>0){

            try {

                for (WeekendLeague weekendLeague : allWeekendLeagues.getAllWeekendLeagues()) {
                    goalsFor += Double.parseDouble(WeekendLeague.getAvgGoals(weekendLeague)[0]);
                    goalsAgainst += Double.parseDouble(WeekendLeague.getAvgGoals(weekendLeague)[1]);
                }

                goalsFor = goalsFor / allWeekendLeagues.getAllWeekendLeagues().size();
                goalsAgainst = goalsAgainst / allWeekendLeagues.getAllWeekendLeagues().size();

                return new String[]{String.format(Locale.US, "%.2f", goalsFor), String.format(Locale.US, "%.2f", goalsAgainst)};
            }
            catch (NumberFormatException e){

                return new String[]{"0.00", "0.00"};
            }
        }
        else{
            return new String[]{"0.00", "0.00"};
        }
    }

    public static String getAvgGoalsString(AllWeekendLeagues allWeekendLeagues){
        return getAvgGoals(allWeekendLeagues)[0] + "(" + getAvgGoals(allWeekendLeagues)[1] + ")";
    }

    public static String[] getAvgPassAcc(AllWeekendLeagues allWeekendLeagues){
        Double passAcc = 0.0;
        Double passAccAgainst= 0.0;
        if(allWeekendLeagues.getAllWeekendLeagues()!=null && allWeekendLeagues.getAllWeekendLeagues().size()>0){

            try {

                for (WeekendLeague weekendLeague : allWeekendLeagues.getAllWeekendLeagues()) {
                    passAcc += Double.parseDouble(WeekendLeague.getAvgPassAcc(weekendLeague)[0]);
                    passAccAgainst += Double.parseDouble(WeekendLeague.getAvgPassAcc(weekendLeague)[1]);
                }

                passAcc = passAcc / allWeekendLeagues.getAllWeekendLeagues().size();
                passAccAgainst = passAccAgainst / allWeekendLeagues.getAllWeekendLeagues().size();

                return new String[]{String.format(Locale.US, "%.2f", passAcc), String.format(Locale.US, "%.2f", passAccAgainst)};
            }
            catch (NumberFormatException e){

                return new String[]{"0.00", "0.00"};
            }
        }
        else{
            return new String[]{"0.00", "0.00"};
        }
    }

    public static String getAvgPassAccString(AllWeekendLeagues allWeekendLeagues){
        return getAvgPassAcc(allWeekendLeagues)[0] + "%(" + getAvgPassAcc(allWeekendLeagues)[1] + "%)";
    }


    public static String getAvgPoss(AllWeekendLeagues allWeekendLeagues){
        Double possFor = 0.0;
        Double possAgainst = 0.0;
        if(allWeekendLeagues.getAllWeekendLeagues()!=null && allWeekendLeagues.getAllWeekendLeagues().size()>0){

            for (WeekendLeague weekendLeague : allWeekendLeagues.getAllWeekendLeagues()) {
                possFor += Double.parseDouble(WeekendLeague.getAvgPoss(weekendLeague)[0]);
                possAgainst += Double.parseDouble(WeekendLeague.getAvgPoss(weekendLeague)[1]);
            }
            possFor = possFor / allWeekendLeagues.getAllWeekendLeagues().size();
            possAgainst = possAgainst / allWeekendLeagues.getAllWeekendLeagues().size();

        }
        return String.format(Locale.US, "%.2f", possFor) + "%("+String.format(Locale.US, "%.2f", possAgainst) + "%)";
    }

    public static String getAvgTackles(AllWeekendLeagues allWeekendLeagues){
        Double tacklesFor = 0.0;
        Double tacklesAgainst = 0.0;
        if(allWeekendLeagues.getAllWeekendLeagues()!=null && allWeekendLeagues.getAllWeekendLeagues().size()>0){

            for (WeekendLeague weekendLeague : allWeekendLeagues.getAllWeekendLeagues()) {
                tacklesFor += Double.parseDouble(WeekendLeague.getAvgTackles(weekendLeague)[0]);
                tacklesAgainst += Double.parseDouble(WeekendLeague.getAvgTackles(weekendLeague)[1]);
            }

            tacklesFor = tacklesFor / allWeekendLeagues.getAllWeekendLeagues().size();
            tacklesAgainst = tacklesAgainst / allWeekendLeagues.getAllWeekendLeagues().size();

        }
        return String.format(Locale.US, "%.2f", tacklesFor) + "("+String.format(Locale.US, "%.2f", tacklesAgainst) + ")";
    }

    public static String getAvgTeamRating(AllWeekendLeagues allWeekendLeagues){
        Double teamRating = 0.0;
        if(allWeekendLeagues.getAllWeekendLeagues()!=null && allWeekendLeagues.getAllWeekendLeagues().size()>0){

            for (WeekendLeague weekendLeague : allWeekendLeagues.getAllWeekendLeagues()) {
                teamRating += Double.parseDouble(WeekendLeague.getAvgTeamRating(weekendLeague));
            }

            teamRating = teamRating / allWeekendLeagues.getAllWeekendLeagues().size();

        }
        return String.format(Locale.US, "%.2f", teamRating);
    }

    public static String getDisconnectTotals(AllWeekendLeagues allWeekendLeagues){
        int dcs = 0;
        if(allWeekendLeagues.getAllWeekendLeagues()!=null){

            for (WeekendLeague weekendLeague : allWeekendLeagues.getAllWeekendLeagues()) {
                dcs += Integer.parseInt(WeekendLeague.getDisconnectTotal(weekendLeague));
            }

        }
        return String.valueOf(dcs);
    }

    public static String getQuitTotals(AllWeekendLeagues allWeekendLeagues){
        int quits = 0;
        if(allWeekendLeagues.getAllWeekendLeagues()!=null){

            for (WeekendLeague weekendLeague : allWeekendLeagues.getAllWeekendLeagues()) {
                quits += Integer.parseInt(WeekendLeague.getQuitTotal(weekendLeague));
            }

        }
        return String.valueOf(quits);
    }

    public static String getAvgGoalPerShotString(AllWeekendLeagues allWeekendLeagues){
        Double userGoalsAvg = Double.parseDouble(getAvgGoals(allWeekendLeagues)[0]);
        Double userSOGAvg = Double.parseDouble(getAvgShotsFor(allWeekendLeagues)[1]);
        Double userAvgGPSOG =  userGoalsAvg / userSOGAvg;

        Double oppGoalsAvg = Double.parseDouble(getAvgGoals(allWeekendLeagues)[1]);
        Double oppSOGAvg = Double.parseDouble(getAvgShotsAgainst(allWeekendLeagues)[1]);
        Double oppAvgGPSOG =  oppGoalsAvg / oppSOGAvg;


        if(!userAvgGPSOG.isNaN() && !oppAvgGPSOG.isNaN()){
            return String.format(Locale.US, "%.2f", userAvgGPSOG) + "(" + String.format(Locale.US, "%.2f", oppAvgGPSOG) + ")";
        }
        else{
            return "0.00(0.00)";
        }

    }

    public void clearPastWLs(){
        this.allWeekendLeagues.clear();
        notifyPropertyChanged(BR._all);
    }

}

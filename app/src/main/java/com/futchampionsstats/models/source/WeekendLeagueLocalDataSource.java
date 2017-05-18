package com.futchampionsstats.models.source;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.futchampionsstats.models.AllWeekendLeagues;
import com.futchampionsstats.models.Game;
import com.futchampionsstats.models.WeekendLeague;
import com.futchampionsstats.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by yiannitzan on 3/15/17.
 */

@Singleton
public class WeekendLeagueLocalDataSource implements WeekendLeagueDataSource{

    public static final String TAG = WeekendLeagueLocalDataSource.class.getSimpleName();
    private static WeekendLeagueLocalDataSource INSTANCE;

    private SharedPreferences mSharedPrefs;

    @Inject
    public WeekendLeagueLocalDataSource(@NonNull Context context){
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static WeekendLeagueLocalDataSource getInstance(@NonNull Context context){
        if (INSTANCE == null){
            INSTANCE = new WeekendLeagueLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getCurrentWeekendLeague(@NonNull GetWeekendLeagueCallback callback) {
        Gson gson = new Gson();
        String json = mSharedPrefs.getString(Constants.CURRENT_WL, null);
        Type type = new TypeToken<WeekendLeague>() {}.getType();
        WeekendLeague wl = gson.fromJson(json, type);
        if (wl!=null && wl.getWeekendLeague()!=null){
            Log.d(TAG, "getCurrentWeekendLeague: " + new Gson().toJson(wl.getWeekendLeague()));
        }
        callback.onWeekendLeagueLoaded(wl);

    }

    @Override
    public void clearWeekendLeague(WeekendLeague weekendLeague) {
        Log.d(TAG, "clearWeekendLeague: ");
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putString(Constants.CURRENT_WL, null);
        editor.apply();
    }

    @Override
    public void saveWeekendLeague(WeekendLeague wl, OnWeekendLeagueSavedCallBack callBack) {

        if(wl!=null){
            Answers.getInstance().logCustom(new CustomEvent("Save Weekend League"));

            Gson gson = new Gson();
            String json = mSharedPrefs.getString(Constants.ALL_WLS, null);
            Type type = new TypeToken<AllWeekendLeagues>() {}.getType();
            AllWeekendLeagues all_wls = gson.fromJson(json, type);

            if(all_wls!=null && all_wls.getAllWeekendLeagues()!=null){
                ArrayList<WeekendLeague> curr_wls = all_wls.getAllWeekendLeagues();
                curr_wls.add(wl);

                all_wls.setAllWeekendLeagues(curr_wls);
            }
            else{
                all_wls = new AllWeekendLeagues();
                ArrayList<WeekendLeague> wls = new ArrayList<>();
                wls.add(wl);

                all_wls.setAllWeekendLeagues(wls);
            }


            SharedPreferences.Editor editor = mSharedPrefs.edit();
            String json2 = gson.toJson(all_wls);
            Log.d(TAG, "saveWeekendLeague: " + json2);

            editor.putString(Constants.ALL_WLS, json2);
            editor.apply();

            callBack.onWeekendLeagueSaved();
        }
        else{
            callBack.onWeekendLeagueSavedError();
        }
    }

    @Override
    public void refreshWeekendLeague() {

    }

    @Override
    public void setNewWeekendLeague() {
        WeekendLeague newWeekendLeague = new WeekendLeague();
        ArrayList<Game> new_wl = new ArrayList<>();
        newWeekendLeague.setWeekendLeague(new_wl);

        SharedPreferences.Editor editor = mSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(newWeekendLeague);
        Log.d(TAG, "setNewWeekendLeague: " + json);

        editor.putString(Constants.CURRENT_WL, json);
        editor.apply();
    }

    @Override
    public void saveNewGame(Game game, OnGameSavedCallback callback) {
        Gson gson = new Gson();

        String json = mSharedPrefs.getString(Constants.CURRENT_WL, null);
        Type type = new TypeToken<WeekendLeague>() {}.getType();
        WeekendLeague wl = gson.fromJson(json, type);

        if(wl!=null && wl.getWeekendLeague()!=null){
            wl.getWeekendLeague().add(game);

            SharedPreferences.Editor editor = mSharedPrefs.edit();
            String json2 = gson.toJson(wl);

            editor.putString(Constants.CURRENT_WL, json2);
            editor.apply();

            Answers.getInstance().logCustom(new CustomEvent("Saved New Game"));

            callback.onGameSaved(wl);
        }
        else{
            callback.onGameSaveError(wl);
        }
    }

    @Override
    public void saveEditGame(Game game, int gamePosition, OnGameSavedCallback callback) {
        Gson gson = new Gson();
        String json = mSharedPrefs.getString(Constants.CURRENT_WL, null);
        Type type = new TypeToken<WeekendLeague>() {}.getType();
        WeekendLeague wl = gson.fromJson(json, type);

        if(wl!=null && wl.getWeekendLeague()!=null){
            Log.d(TAG, "onResume savegame: " + new Gson().toJson(wl.getWeekendLeague().size()));

            Log.d(TAG, "onEditGameFragmentInteraction: " + gamePosition + " size:" + wl.getWeekendLeague().size());
            if(game!=null && gamePosition !=-1){
                try{
                    if(gamePosition != wl.getWeekendLeague().size()){
                        wl.getWeekendLeague().set(gamePosition, game);
                    }
                }
                catch (IndexOutOfBoundsException e){
                    Log.d(TAG, "onEditGameFragmentInteraction: " + e);
                }

                SharedPreferences.Editor editor = mSharedPrefs.edit();
                String json2 = gson.toJson(wl);

                editor.putString(Constants.CURRENT_WL, json2);
                editor.apply();

                callback.onGameSaved(wl);
            }
        }
        else{
            callback.onGameSaveError(wl);
        }
    }

    @Override
    public void getAllWeekendLeagues(GetAllWeekendLeaguesCallback callback) {

        Gson gson = new Gson();
        String json = mSharedPrefs.getString(Constants.ALL_WLS, null);
        Type type = new TypeToken<AllWeekendLeagues>() {}.getType();
        AllWeekendLeagues all_wl = gson.fromJson(json, type);

        if(all_wl!=null && all_wl.getAllWeekendLeagues()!=null){
            Log.d(TAG, "getAllWeekendLeagues: " + new Gson().toJson(all_wl.getAllWeekendLeagues()));
        }
        callback.onAllWeekendLeaguesLoaded(all_wl);

    }

    @Override
    public void clearAllWeekendLeagues() {
        Log.d(TAG, "clearWeekendLeague: ");
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putString(Constants.ALL_WLS, null);
        editor.apply();
    }
}

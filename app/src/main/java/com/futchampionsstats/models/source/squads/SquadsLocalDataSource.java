package com.futchampionsstats.models.source.squads;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.futchampionsstats.models.Squad;
import com.futchampionsstats.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by yiannitzan on 3/24/17.
 */

public class SquadsLocalDataSource implements SquadsDataSource{

    public static final String TAG = SquadsLocalDataSource.class.getSimpleName();
    private static SquadsLocalDataSource INSTANCE;

    private SharedPreferences mSharedPrefs;

    private SquadsLocalDataSource(@NonNull Context context){
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SquadsLocalDataSource getInstance(@NonNull Context context){
        if(INSTANCE == null){
            INSTANCE = new SquadsLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getSquads(@NonNull GetSquadsCallback callback) {
        Gson gson = new Gson();
        String json = mSharedPrefs.getString(Constants.SAVED_SQUADS, null);
        Type type = new TypeToken<ArrayList<Squad>>() {}.getType();
        ArrayList<Squad> user_squads = gson.fromJson(json, type);

        if(user_squads!=null){
            Log.d(TAG, "getSquads: " + new Gson().toJson(user_squads));
        }
        callback.onSquadsLoaded(user_squads);
    }

    @Override
    public void deleteSquad(int squadIndex) {

        Gson gson = new Gson();
        String json = mSharedPrefs.getString(Constants.SAVED_SQUADS, null);
        Type type = new TypeToken<ArrayList<Squad>>() {}.getType();
        ArrayList<Squad> saved_squads = gson.fromJson(json, type);

        if(saved_squads!=null){
            saved_squads.remove(squadIndex);
        }

        SharedPreferences.Editor editor = mSharedPrefs.edit();
        String json2 = gson.toJson(saved_squads);
        Log.d(TAG, "delete squad: " + saved_squads);

        editor.putString(Constants.SAVED_SQUADS, json2);
        editor.apply();
    }

    @Override
    public void editSquad(Squad squad, int squadIndex) {

        Gson gson = new Gson();
        String json = mSharedPrefs.getString(Constants.SAVED_SQUADS, null);
        Type type = new TypeToken<ArrayList<Squad>>() {}.getType();
        ArrayList<Squad> saved_squads = gson.fromJson(json, type);

        if(saved_squads!=null){
            saved_squads.set(squadIndex, squad);
        }

        SharedPreferences.Editor editor = mSharedPrefs.edit();
        String json2 = gson.toJson(saved_squads);
        Log.d(TAG, "editSquad: " + json2);

        editor.putString(Constants.SAVED_SQUADS, json2);
        editor.apply();
    }

    @Override
    public void saveNewSquad(Squad squad) {
        Gson gson = new Gson();
        String json = mSharedPrefs.getString(Constants.SAVED_SQUADS, null);
        Type type = new TypeToken<ArrayList<Squad>>() {}.getType();
        ArrayList<Squad> saved_squads = gson.fromJson(json, type);

        if(saved_squads!=null){
            saved_squads.add(squad);
        }
        else{
            saved_squads = new ArrayList<>();
            saved_squads.add(squad);
        }

        SharedPreferences.Editor editor = mSharedPrefs.edit();
        String json2 = gson.toJson(saved_squads);
        Log.d(TAG, "saveSquad: " + saved_squads);

        editor.putString(Constants.SAVED_SQUADS, json2);
        editor.apply();
    }
}

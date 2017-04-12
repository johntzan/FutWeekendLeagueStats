package com.futchampionsstats.models;

import android.support.annotation.NonNull;

import com.futchampionsstats.models.source.WeekendLeagueDataSource;
import com.futchampionsstats.models.source.WeekendLeagueLocalDataSource;

import java.io.Serializable;

/**
 * Created by yiannitzan on 3/15/17.
 */

public class WeekendLeagueRepository implements Serializable, WeekendLeagueDataSource {

    private static WeekendLeagueRepository INSTANCE = null;

    private final WeekendLeagueLocalDataSource mWeekendLeagueLocalDataSource;

    private WeekendLeagueRepository(@NonNull WeekendLeagueLocalDataSource weekendLeagueLocalDataSource){
        mWeekendLeagueLocalDataSource = weekendLeagueLocalDataSource;
    }

    public static WeekendLeagueRepository getInstance(WeekendLeagueLocalDataSource weekendLeagueLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new WeekendLeagueRepository(weekendLeagueLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }



    @Override
    public void getCurrentWeekendLeague(@NonNull final GetWeekendLeagueCallback callback) {
        mWeekendLeagueLocalDataSource.getCurrentWeekendLeague(new GetWeekendLeagueCallback() {
            @Override
            public void onWeekendLeagueLoaded(WeekendLeague weekendLeague) {
                callback.onWeekendLeagueLoaded(weekendLeague);
            }
        });
    }

    @Override
    public void clearWeekendLeague(WeekendLeague weekendLeague) {
        mWeekendLeagueLocalDataSource.clearWeekendLeague(weekendLeague);
    }

    @Override
    public void setNewWeekendLeague() {
        mWeekendLeagueLocalDataSource.setNewWeekendLeague();
    }

    @Override
    public void saveNewGame(Game game, final OnGameSavedCallback callback) {
        mWeekendLeagueLocalDataSource.saveNewGame(game, new OnGameSavedCallback() {
            @Override
            public void onGameSaved(WeekendLeague weekendLeague) {
                callback.onGameSaved(weekendLeague);
            }

            @Override
            public void onGameSaveError(WeekendLeague weekendLeague) {
                callback.onGameSaveError(weekendLeague);
            }
        });
    }

    @Override
    public void saveEditGame(Game game, int gamePosition, final OnGameSavedCallback callback) {
        mWeekendLeagueLocalDataSource.saveEditGame(game, gamePosition, new OnGameSavedCallback() {
            @Override
            public void onGameSaved(WeekendLeague weekendLeague) {
                callback.onGameSaved(weekendLeague);
            }

            @Override
            public void onGameSaveError(WeekendLeague weekendLeague) {
                callback.onGameSaveError(weekendLeague);
            }
        });
    }

    @Override
    public void saveWeekendLeague(WeekendLeague weekendLeague, final OnWeekendLeagueSavedCallBack callBack) {
        mWeekendLeagueLocalDataSource.saveWeekendLeague(weekendLeague, new OnWeekendLeagueSavedCallBack() {
            @Override
            public void onWeekendLeagueSaved() {
                callBack.onWeekendLeagueSaved();
            }

            @Override
            public void onWeekendLeagueSavedError() {
                callBack.onWeekendLeagueSavedError();
            }
        });
    }

    @Override
    public void refreshWeekendLeague() {

    }
}


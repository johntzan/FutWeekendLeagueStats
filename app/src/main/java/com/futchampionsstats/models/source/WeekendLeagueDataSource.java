package com.futchampionsstats.models.source;

import android.support.annotation.NonNull;

import com.futchampionsstats.models.Game;
import com.futchampionsstats.models.WeekendLeague;

/**
 * Created by yiannitzan on 3/15/17.
 */

public interface WeekendLeagueDataSource {

    interface GetWeekendLeagueCallback{
        void onWeekendLeagueLoaded(WeekendLeague weekendLeague);
    }

    interface OnGameSavedCallback{
        void onGameSaved(WeekendLeague weekendLeague);
        void onGameSaveError(WeekendLeague weekendLeague);
    }

    interface OnWeekendLeagueSavedCallBack{
        void onWeekendLeagueSaved();
        void onWeekendLeagueSavedError();
    }

    void getCurrentWeekendLeague(@NonNull GetWeekendLeagueCallback callback);

    void clearWeekendLeague(WeekendLeague weekendLeague);

    void setNewWeekendLeague();

    void saveNewGame(Game game, OnGameSavedCallback callback);

    void saveEditGame(Game game, int gamePosition, OnGameSavedCallback callback);

    void saveWeekendLeague(WeekendLeague weekendLeague, OnWeekendLeagueSavedCallBack callBack);

    void refreshWeekendLeague();
}

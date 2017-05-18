package com.futchampionsstats.models.source.squads;

import android.support.annotation.NonNull;

import com.futchampionsstats.models.Squad;

import java.util.ArrayList;

/**
 * Created by yiannitzan on 3/24/17.
 */

public interface SquadsDataSource {

    interface GetSquadsCallback{
        void onSquadsLoaded(ArrayList<Squad> squads);
    }

    void getSquads(@NonNull GetSquadsCallback callback);
    void editSquad(Squad squad, int squadIndex);
    void deleteSquad(int squadIndex);
    void saveNewSquad(Squad squad);

}

package com.futchampionsstats.models;

import android.support.annotation.NonNull;
import android.util.Log;

import com.futchampionsstats.models.source.squads.SquadsDataSource;
import com.futchampionsstats.models.source.squads.SquadsLocalDataSource;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by yiannitzan on 3/24/17.
 */
@Singleton
public class SquadRepository implements SquadsDataSource{

    private static SquadRepository INSTANCE = null;
    private final SquadsLocalDataSource mSquadsLocalDataSource;

    @Inject
    SquadRepository(@NonNull SquadsLocalDataSource squadsLocalDataSource){
        Log.d("SquadRepository: ", "injecting Squad repository");
        mSquadsLocalDataSource = squadsLocalDataSource;
    }

    public static SquadRepository getInstance(SquadsLocalDataSource squadsLocalDataSource){
        if (INSTANCE == null) {
            INSTANCE = new SquadRepository(squadsLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getSquads(@NonNull final GetSquadsCallback callback) {
        mSquadsLocalDataSource.getSquads(new GetSquadsCallback() {
            @Override
            public void onSquadsLoaded(ArrayList<Squad> squads) {
                callback.onSquadsLoaded(squads);
            }
        });
    }

    @Override
    public void deleteSquad(int squadIndex) {
        mSquadsLocalDataSource.deleteSquad(squadIndex);
    }

    @Override
    public void editSquad(Squad squad, int squadindex) {
        mSquadsLocalDataSource.editSquad(squad, squadindex);
    }

    @Override
    public void saveNewSquad(Squad squad) {
        mSquadsLocalDataSource.saveNewSquad(squad);
    }
}

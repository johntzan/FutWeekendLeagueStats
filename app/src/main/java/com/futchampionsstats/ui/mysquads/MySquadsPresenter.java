package com.futchampionsstats.ui.mysquads;

import android.support.annotation.NonNull;
import android.util.Log;

import com.futchampionsstats.models.Squad;
import com.futchampionsstats.models.SquadRepository;
import com.futchampionsstats.models.source.squads.SquadsDataSource;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by yiannitzan on 4/11/17.
 */

public class MySquadsPresenter implements MySquadsContract.Presenter {

    public static final String TAG = MySquadsPresenter.class.getSimpleName();
    private SquadRepository mSquadRepository;
    private MySquadsContract.View mMySquadsView;
    private ArrayList<Squad> mSquads;

    @Inject
    public MySquadsPresenter(@NonNull SquadRepository squadRepository, @NonNull MySquadsContract.View view){
        mSquadRepository = squadRepository;
        mMySquadsView = view;
        Log.d(TAG, "MySquadsPresenter: new");
        mMySquadsView.setPresenter(this);
    }

    @Inject
    void setupListeners() {
        mMySquadsView.setPresenter(this);
    }

    @Override
    public void start() {
        setSquads();
    }

    @Override
    public void setSquads() {
        mSquadRepository.getSquads(new SquadsDataSource.GetSquadsCallback() {
            @Override
            public void onSquadsLoaded(ArrayList<Squad> squads) {
                if (squads!=null && squads.size()>0){
                    Log.d(TAG, "onSquadsLoaded: > 0");
                    mSquads = squads;
                    mMySquadsView.setSquads(squads);
                }
                else{
                    Log.d(TAG, "onSquadsLoaded: null");
                    mMySquadsView.showEmptySquads();
                }
            }
        });
    }

    @Override
    public void addNewSquad(Squad squad) {
        mSquadRepository.saveNewSquad(squad);
        setSquads();
    }

    @Override
    public void setSquadForEdit(int squadIndex) {
        mMySquadsView.showEditSquad(mSquads.get(squadIndex), squadIndex);
    }

    @Override
    public void editSquad(Squad squad, int squadIndex) {
        mSquadRepository.editSquad(squad, squadIndex);
        setSquads();
    }

    @Override
    public void deleteSquad(int squadIndex) {
        mSquadRepository.deleteSquad(squadIndex);
        setSquads();
    }
}

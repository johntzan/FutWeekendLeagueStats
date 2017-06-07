package com.futchampionsstats.ui.wl;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.futchampionsstats.models.Game;
import com.futchampionsstats.models.WeekendLeague;
import com.futchampionsstats.models.WeekendLeagueRepository;
import com.futchampionsstats.models.source.WeekendLeagueDataSource;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by yiannitzan on 3/15/17.
 */

public class WeekendLeagueDetailPresenter implements WeekendLeagueDetailContract.Presenter {

    public static final String TAG = WeekendLeagueDetailPresenter.class.getSimpleName();

    private final WeekendLeagueDetailContract.View mWeekendLeagueDetailView;
    private WeekendLeagueRepository mWeekendLeagueRepository;
    private WeekendLeague mCurrentWeekendLeague;

    public WeekendLeagueDetailPresenter(@NonNull WeekendLeagueRepository weekendLeagueRepository, @NonNull WeekendLeagueDetailContract.View view){
        mWeekendLeagueDetailView = checkNotNull(view, "View should not be null");
        mWeekendLeagueRepository = checkNotNull(weekendLeagueRepository, "Repository should not be null");

        mWeekendLeagueDetailView.setPresenter(this);
    }

    @Override
    public void start() {
        getCurrentWeekendLeague();
    }

    @Override
    public void getCurrentWeekendLeague() {

         mWeekendLeagueRepository.getCurrentWeekendLeague(new WeekendLeagueDataSource.GetWeekendLeagueCallback() {
             @Override
             public void onWeekendLeagueLoaded(WeekendLeague weekendLeague) {
                 // The view may not be able to handle UI updates anymore
                 mCurrentWeekendLeague = weekendLeague;
                 if (!mWeekendLeagueDetailView.isActive()) {
                     return;
                 }
                 if (weekendLeague != null &&  weekendLeague.getWeekendLeague()!=null) {
                     Log.d(TAG, "onWeekendLeagueLoaded: wl not null");
                     mWeekendLeagueDetailView.showCurrentWeekendLeague(weekendLeague);
                 } else {
                     Log.d(TAG, "onWeekendLeagueLoaded: wl is NULL");
                     //WL is null, create new and send to view
                     weekendLeague = new WeekendLeague();
                     ArrayList<Game> new_wl = new ArrayList<>();
                     weekendLeague.setWeekendLeague(new_wl);

                     mWeekendLeagueRepository.setNewWeekendLeague();
                     mWeekendLeagueDetailView.showEmptyWeekendLeague(weekendLeague);
                 }
             }

         });
    }

    @Override
    public void clearWeekendLeague() {
        mWeekendLeagueRepository.getCurrentWeekendLeague(new WeekendLeagueDataSource.GetWeekendLeagueCallback() {
            @Override
            public void onWeekendLeagueLoaded(WeekendLeague weekendLeague) {
                // The view may not be able to handle UI updates anymore
                if (!mWeekendLeagueDetailView.isActive()) {
                    return;
                }
                if (weekendLeague != null &&  weekendLeague.getWeekendLeague()!=null) {
                    //clear old, set new below
                    mWeekendLeagueRepository.clearWeekendLeague(weekendLeague);

                    getCurrentWeekendLeague();
                }
            }

        });
    }

    @Override
    public void addNewGame(View v) {
        Log.d(TAG, "addNewGame: ");
        mWeekendLeagueRepository.getCurrentWeekendLeague(new WeekendLeagueDataSource.GetWeekendLeagueCallback() {
            @Override
            public void onWeekendLeagueLoaded(WeekendLeague weekendLeague) {
                // The view may not be able to handle UI updates anymore
                if (!mWeekendLeagueDetailView.isActive()) {
                    return;
                }
                if (weekendLeague != null &&  weekendLeague.getWeekendLeague()!=null) {
                    Log.d(TAG, "addNewGame: wl not null");
                    if (weekendLeague.getWeekendLeague().size()< 40){
                        mWeekendLeagueDetailView.showNewGame();
                    }
                    else{
                        mWeekendLeagueDetailView.showSaveWlFromNewGameDialog();
                    }
                }
                else{
                    Log.d(TAG, "onWeekendLeagueLoaded: wl null");
                }
            }

        });
    }


    @Override
    public void saveWeekendLeague(String date) {
        mCurrentWeekendLeague.setDateOfWL(date);
        mWeekendLeagueRepository.saveWeekendLeague(mCurrentWeekendLeague, new WeekendLeagueDataSource.OnWeekendLeagueSavedCallBack() {
            @Override
            public void onWeekendLeagueSaved() {
                //save successful clear out old. set new wl up
                clearWeekendLeague();
            }

            @Override
            public void onWeekendLeagueSavedError() {
                //Error handling
            }
        });
    }

}

package com.futchampionsstats.ui.wl.view_games;

import android.support.annotation.NonNull;
import android.util.Log;

import com.futchampionsstats.models.WeekendLeague;
import com.futchampionsstats.models.WeekendLeagueRepository;
import com.futchampionsstats.models.source.WeekendLeagueDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by yiannitzan on 4/5/17.
 */

public class ViewGamesPresenter implements ViewGamesContract.Presenter{

    public static final String TAG = ViewGamesPresenter.class.getSimpleName();
    private WeekendLeagueRepository mWeekendLeagueRepository;
    private ViewGamesContract.View mViewGamesView;
    private WeekendLeague currentWeekendLeague;


    public ViewGamesPresenter(@NonNull WeekendLeagueRepository weekendLeagueRepository, @NonNull ViewGamesContract.View view) {
        mWeekendLeagueRepository = checkNotNull(weekendLeagueRepository);
        mViewGamesView = checkNotNull(view);
        Log.d(TAG, "ViewGamesPresenter: new");
        mViewGamesView.setPresenter(this);
    }

    @Override
    public void start() {
        setGames();
    }

    @Override
    public void setGames() {

        mWeekendLeagueRepository.getCurrentWeekendLeague(new WeekendLeagueDataSource.GetWeekendLeagueCallback() {
            @Override
            public void onWeekendLeagueLoaded(WeekendLeague weekendLeague) {
                // The view may not be able to handle UI updates anymore
                if (!mViewGamesView.isActive()) {
                    return;
                }
                if (weekendLeague != null &&  weekendLeague.getWeekendLeague()!=null) {
                    Log.d(TAG, "onWeekendLeagueLoaded: wl not null");
                    currentWeekendLeague = weekendLeague;
                    if(weekendLeague.getWeekendLeague().size()>0){
                        mViewGamesView.setGames(weekendLeague);
                    }
                    else{
                        mViewGamesView.showEmptyWeekendLeague();
                    }
                } else {
                    Log.d(TAG, "onWeekendLeagueLoaded: wl is NULL");
                    //WL is null, show empty wl
                    mViewGamesView.showEmptyWeekendLeague();
                }
            }

        });

    }

    @Override
    public void updateGames() {
        setGames();
    }

    @Override
    public void editGame(int gamePosition) {
        if(mViewGamesView.isActive()) mViewGamesView.showEditGame(currentWeekendLeague.getWeekendLeague().get(gamePosition), gamePosition);
    }
}

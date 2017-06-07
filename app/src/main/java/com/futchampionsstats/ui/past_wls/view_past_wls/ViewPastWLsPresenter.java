package com.futchampionsstats.ui.past_wls.view_past_wls;

import android.support.annotation.NonNull;
import android.util.Log;

import com.futchampionsstats.models.AllWeekendLeagues;
import com.futchampionsstats.models.Game;
import com.futchampionsstats.models.WeekendLeague;
import com.futchampionsstats.models.WeekendLeagueRepository;
import com.futchampionsstats.models.source.WeekendLeagueDataSource;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by yiannitzan on 4/18/17.
 */

public class ViewPastWLsPresenter implements ViewPastWLsContract.Presenter {

    public static final String TAG = ViewPastWLsPresenter.class.getSimpleName();
    private ViewPastWLsContract.View mViewPastWLsView;
    private WeekendLeagueRepository mWeekendLeagueRepository;
    private AllWeekendLeagues mAllWeekendLeagues;
    private ArrayList<Game> mAllGames;

    public ViewPastWLsPresenter(@NonNull WeekendLeagueRepository weekendLeagueRepository, @NonNull  ViewPastWLsContract.View view) {
        mViewPastWLsView = checkNotNull(view);
        mWeekendLeagueRepository = checkNotNull(weekendLeagueRepository);

        mAllWeekendLeagues = new AllWeekendLeagues();
        mAllGames = new ArrayList<>();

        mViewPastWLsView.setPresenter(this);
    }

    @Override
    public void start() {
        getAllWeekendLeagues();
    }

    @Override
    public void getAllWeekendLeagues() {
        mWeekendLeagueRepository.getAllWeekendLeagues(new WeekendLeagueDataSource.GetAllWeekendLeaguesCallback() {
            @Override
            public void onAllWeekendLeaguesLoaded(AllWeekendLeagues weekendLeagues) {
                mAllWeekendLeagues = weekendLeagues;
                if(!mViewPastWLsView.isActive()){
                    return;
                }
                if(mAllWeekendLeagues !=null && mAllWeekendLeagues.getAllWeekendLeagues()!=null){
                    Log.d(TAG, "onAllWeekendLeaguesLoaded: show past");
                    mViewPastWLsView.showPastWeekendLeagues(mAllWeekendLeagues);
                }
                else{
                    Log.d(TAG, "onAllWeekendLeaguesLoaded: is null");
                    weekendLeagues = new AllWeekendLeagues();
                    ArrayList<WeekendLeague> new_wls = new ArrayList<>();
                    weekendLeagues.setAllWeekendLeagues(new_wls);

                    mAllWeekendLeagues = weekendLeagues;

                    mViewPastWLsView.showEmptyWeekendLeagues(mAllWeekendLeagues);
                }
            }
        });
        getAllGames();
    }

    @Override
    public void getAllGames() {

        mAllGames = new ArrayList<>();

        if(mAllWeekendLeagues!=null && mAllWeekendLeagues.getAllWeekendLeagues()!=null){

            ArrayList<Game> temp = new ArrayList<>();
            for(WeekendLeague weekendLeague : mAllWeekendLeagues.getAllWeekendLeagues()){
                for(Game game : weekendLeague.getWeekendLeague()){
                    temp.add(game);
                }
            }
            if(temp.size()>mAllGames.size()){
                mAllGames = temp;
                if(mViewPastWLsView.isActive()) mViewPastWLsView.showAllGamesView(mAllGames);
            }
        }
    }

    @Override
    public void getWeekendLeagueForDetail(int position) {
        if(mAllWeekendLeagues.getAllWeekendLeagues().size()>0){
            if(mViewPastWLsView.isActive()) mViewPastWLsView.showWeekendLeagueDetail(mAllWeekendLeagues.getAllWeekendLeagues().get(position), position);
        }
    }

    @Override
    public void getGameForDetail(int position) {
        if(mAllGames!=null && mAllGames.size()>0){
            if(mViewPastWLsView.isActive()) mViewPastWLsView.showGameDetail(mAllGames.get(position), position);
        }
    }

    @Override
    public void updateGamesList() {

    }
}

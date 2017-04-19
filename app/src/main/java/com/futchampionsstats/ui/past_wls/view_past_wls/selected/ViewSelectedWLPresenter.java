package com.futchampionsstats.ui.past_wls.view_past_wls.selected;

import android.support.annotation.NonNull;

import com.futchampionsstats.models.WeekendLeague;

/**
 * Created by yiannitzan on 4/19/17.
 */

public class ViewSelectedWLPresenter implements ViewSelectedWLContract.Presenter {

    private WeekendLeague mWeekendLeague;
    private ViewSelectedWLContract.View mViewSelectedWLView;

    public ViewSelectedWLPresenter(@NonNull WeekendLeague weekendLeague, ViewSelectedWLContract.View view) {
        mViewSelectedWLView = view;
        mWeekendLeague = weekendLeague;

        mViewSelectedWLView.setPresenter(this);
    }

    @Override
    public void start() {
        getWeekendLeague();
    }

    @Override
    public void getWeekendLeague() {
        if(mViewSelectedWLView.isActive()) mViewSelectedWLView.showWeekendLeague(mWeekendLeague);
    }

    @Override
    public void getWeekendLeagueGames() {
        if(mViewSelectedWLView.isActive()) mViewSelectedWLView.showWeekendLeagueGames(mWeekendLeague);
    }
}

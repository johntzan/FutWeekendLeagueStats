package com.futchampionsstats.ui.past_wls.view_past_wls.selected;

import android.support.annotation.NonNull;

import com.futchampionsstats.models.WeekendLeague;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by yiannitzan on 4/19/17.
 */

public class ViewSelectedWLPresenter implements ViewSelectedWLContract.Presenter {

    private WeekendLeague mWeekendLeague;
    private ViewSelectedWLContract.View mViewSelectedWLView;

    public ViewSelectedWLPresenter(@NonNull WeekendLeague weekendLeague, @NonNull ViewSelectedWLContract.View view) {
        mViewSelectedWLView = checkNotNull(view);
        mWeekendLeague = checkNotNull(weekendLeague);

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

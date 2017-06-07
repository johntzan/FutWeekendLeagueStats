package com.futchampionsstats.ui.past_wls.view_past_wls.games;

import android.support.annotation.NonNull;

import com.futchampionsstats.models.WeekendLeague;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by yiannitzan on 4/19/17.
 */

public class ViewPastWLGamesPresenter implements ViewPastWLGamesContract.Presenter{

    private WeekendLeague mWeekendLeague;
    private ViewPastWLGamesContract.View mViewPastWLGamesView;

    public ViewPastWLGamesPresenter(@NonNull WeekendLeague weekendLeague, @NonNull ViewPastWLGamesContract.View view) {
        mViewPastWLGamesView = checkNotNull(view);
        mWeekendLeague = checkNotNull(weekendLeague);

        mViewPastWLGamesView.setPresenter(this);
    }

    @Override
    public void start() {
        getWeekendLeague();
    }

    @Override
    public void getWeekendLeague() {
        if(mViewPastWLGamesView.isActive()) mViewPastWLGamesView.showWeekendLeague(mWeekendLeague);
    }

    @Override
    public void getWeekendLeagueGameSelected(int position) {
        if(mViewPastWLGamesView.isActive()) mViewPastWLGamesView.showWeekendLeagueGame(mWeekendLeague.getWeekendLeague().get(position));
    }
}

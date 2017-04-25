package com.futchampionsstats.ui.wl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yiannitzan on 4/21/17.
 */

@Module
public class WeekendLeagueDetailPresenterModule {

    private final WeekendLeagueDetailContract.View mView;

    public WeekendLeagueDetailPresenterModule(WeekendLeagueDetailContract.View view){
        mView = view;
    }

    @Provides
    WeekendLeagueDetailContract.View provideWeekendLeagueDetailContractView(){
        return mView;
    }
}

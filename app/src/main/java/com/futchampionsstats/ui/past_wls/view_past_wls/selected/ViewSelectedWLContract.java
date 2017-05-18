package com.futchampionsstats.ui.past_wls.view_past_wls.selected;

import com.futchampionsstats.BasePresenter;
import com.futchampionsstats.BaseView;
import com.futchampionsstats.models.WeekendLeague;

/**
 * Created by yiannitzan on 4/19/17.
 */

public interface ViewSelectedWLContract {

    interface View extends BaseView<Presenter> {

        void showWeekendLeague(WeekendLeague weekendLeague);
        void showWeekendLeagueGames(WeekendLeague weekendLeague);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void getWeekendLeague();
        void getWeekendLeagueGames();

    }
}

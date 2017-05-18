package com.futchampionsstats.ui.past_wls.view_past_wls.games;

import com.futchampionsstats.BasePresenter;
import com.futchampionsstats.BaseView;
import com.futchampionsstats.models.Game;
import com.futchampionsstats.models.WeekendLeague;

/**
 * Created by yiannitzan on 4/19/17.
 */

public interface ViewPastWLGamesContract {

    interface View extends BaseView<Presenter> {

        void showWeekendLeague(WeekendLeague weekendLeague);
        void showWeekendLeagueGame(Game game);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void getWeekendLeague();
        void getWeekendLeagueGameSelected(int position);

    }

}

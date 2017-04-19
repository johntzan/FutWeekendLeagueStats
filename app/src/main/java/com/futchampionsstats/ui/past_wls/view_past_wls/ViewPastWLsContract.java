package com.futchampionsstats.ui.past_wls.view_past_wls;

import com.futchampionsstats.BasePresenter;
import com.futchampionsstats.BaseView;
import com.futchampionsstats.models.AllWeekendLeagues;
import com.futchampionsstats.models.Game;
import com.futchampionsstats.models.WeekendLeague;

import java.util.ArrayList;

/**
 * Created by yiannitzan on 4/18/17.
 */

public interface ViewPastWLsContract {


    interface View extends BaseView<Presenter> {

        void showPastWeekendLeagues(AllWeekendLeagues allWeekendLeagues);
        void showEmptyWeekendLeagues(AllWeekendLeagues allWeekendLeagues);

        void showAllGamesView(ArrayList<Game> allGames);
        void showWeekendLeagueDetail(WeekendLeague weekendLeague, int position);
        void showGameDetail(Game game, int position);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void getAllWeekendLeagues();
        void getWeekendLeagueForDetail(int position);
        void getGameForDetail(int position);

        void getAllGames();
        void updateGamesList();


    }
}

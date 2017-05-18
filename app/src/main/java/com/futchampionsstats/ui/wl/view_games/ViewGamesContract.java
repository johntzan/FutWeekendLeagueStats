package com.futchampionsstats.ui.wl.view_games;

import com.futchampionsstats.BasePresenter;
import com.futchampionsstats.BaseView;
import com.futchampionsstats.models.Game;
import com.futchampionsstats.models.WeekendLeague;

/**
 * Created by yiannitzan on 4/5/17.
 */

public class ViewGamesContract {

    interface View extends BaseView<Presenter> {

        //initiate view
        void setGames(WeekendLeague weekendLeague);
        void showEmptyWeekendLeague();

        void showEditGame(Game game, int position);

        boolean isActive();

    }

    interface Presenter extends BasePresenter {

        //game related
        void setGames();
        void updateGames();
        void editGame(int position);

    }
}

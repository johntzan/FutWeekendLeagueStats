package com.futchampionsstats.ui.wl;

import com.futchampionsstats.BasePresenter;
import com.futchampionsstats.BaseView;
import com.futchampionsstats.models.WeekendLeague;

/**
 * Created by yiannitzan on 3/15/17.
 */

public interface WeekendLeagueDetailContract {

    interface View extends BaseView<Presenter> {

        //setting view
        void showCurrentWeekendLeague(WeekendLeague weekendLeague);
        void showEmptyWeekendLeague(WeekendLeague weekendLeague);

        //Dialogs
        void showSaveWLDialog();
        void showSaveWlFromNewGameDialog();
        void showClearWLDialog();

        //view change
        void showNewGame();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void getCurrentWeekendLeague();

        void addNewGame(android.view.View v);
        void saveWeekendLeague(String date);
        void clearWeekendLeague();

    }

}

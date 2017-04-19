package com.futchampionsstats.ui.past_wls.past_wl_view_games;

import com.futchampionsstats.BasePresenter;
import com.futchampionsstats.BaseView;
import com.futchampionsstats.models.Game;

/**
 * Created by yiannitzan on 4/19/17.
 */

public interface PastWLViewGameContract {

    interface View extends BaseView<Presenter> {

        void showGame(Game game);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void getGame();

    }
}

package com.futchampionsstats.ui.leaderboards.consoles_fragments;

import com.futchampionsstats.BasePresenter;
import com.futchampionsstats.BaseView;
import com.futchampionsstats.models.leaderboards.Top100;

/**
 * Created by yiannitzan on 5/12/17.
 */

public class XboxLeaderboardsContract {

    interface View extends BaseView<Presenter> {

        void setXboxLeaderboards(Top100 top100);
        void setMonths(String[] months);
        void showLoading();
        void hideLoading();
        void showError();
        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void getXboxLeaderboards(String month, String region);
        void getMonths();

    }
}

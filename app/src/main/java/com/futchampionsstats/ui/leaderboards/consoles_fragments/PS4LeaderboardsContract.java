package com.futchampionsstats.ui.leaderboards.consoles_fragments;

import com.futchampionsstats.BasePresenter;
import com.futchampionsstats.BaseView;
import com.futchampionsstats.models.leaderboards.Top100;

/**
 * Created by yiannitzan on 5/11/17.
 */

public class PS4LeaderboardsContract {

    interface View extends BaseView<Presenter> {

        void setPS4Leaderboards(Top100 top100);
        void setMonths(String[] months);
        void showLoading();
        void hideLoading();
        void showError();
        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void getPS4Leaderboards(String month, String region);
        void getMonths();

    }

}

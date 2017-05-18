package com.futchampionsstats.ui.past_wls.past_wl_detail;

import com.futchampionsstats.BasePresenter;
import com.futchampionsstats.BaseView;
import com.futchampionsstats.models.AllWeekendLeagues;

/**
 * Created by yiannitzan on 4/12/17.
 */

public interface PastWLDetailContract {

    interface View extends BaseView<Presenter> {

        void showClearAllWLsDialog(android.view.View v);
        void showPastWeekendLeagues(AllWeekendLeagues allWeekendLeagues);
        void showEmptyWeekendLeagues(AllWeekendLeagues allWeekendLeagues);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void getAllWeekendLeagues();
        void clearAllWeekendLeagues();

    }

}

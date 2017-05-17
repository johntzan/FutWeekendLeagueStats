package com.futchampionsstats.ui.leaderboards;

import com.futchampionsstats.BasePresenter;
import com.futchampionsstats.BaseView;
import com.futchampionsstats.models.leaderboards.SearchSuggestions;
import com.futchampionsstats.models.leaderboards.User;

import java.util.List;

/**
 * Created by yiannitzan on 5/4/17.
 */

public class LeaderboardsContract {

    interface View extends BaseView<Presenter> {

        void showSearchSuggestions(List<SearchSuggestions> searchResults);
        void showUserProfile(User user);
        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void getSearchSuggestions(String query);
        void getResultFromSuggestion(String suggestion);

    }
}

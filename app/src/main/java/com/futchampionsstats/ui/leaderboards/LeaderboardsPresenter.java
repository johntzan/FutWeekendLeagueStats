package com.futchampionsstats.ui.leaderboards;

import android.support.annotation.NonNull;
import android.util.Log;

import com.futchampionsstats.models.leaderboards.SearchResults;
import com.futchampionsstats.models.leaderboards.SearchSuggestions;
import com.futchampionsstats.models.leaderboards.User;
import com.futchampionsstats.service.Service;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by yiannitzan on 5/4/17.
 */

public class LeaderboardsPresenter implements LeaderboardsContract.Presenter{

    public static final String TAG = LeaderboardsPresenter.class.getSimpleName();

    private LeaderboardsContract.View mLeaderboardsView;
    private Service mService;
    private SearchResults[] mSearchResults;

    public LeaderboardsPresenter(@NonNull Service service, @NonNull LeaderboardsContract.View view) {
        mService = checkNotNull(service);
        mLeaderboardsView = checkNotNull(view);

        mLeaderboardsView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void getSearchSuggestions(String query) {
        mService.getUserSearchResults(query, new Service.GetSearchResultsCallback() {
            @Override
            public void onSuccess(SearchResults[] searchResults) {
                Log.d(TAG, "onSuccess: " + new Gson().toJson(searchResults));
                mSearchResults = searchResults;
                List<SearchSuggestions> suggestions = new ArrayList<>();
                for (SearchResults result : searchResults){
                    Log.d(TAG, "showSearchSuggestions: " + result.getUsername());
                    suggestions.add(new SearchSuggestions(result.getUsername(), result.getConsole()));
                }
                if(mLeaderboardsView.isActive()) mLeaderboardsView.showSearchSuggestions(suggestions);
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }

    @Override
    public void getResultFromSuggestion(String suggestion) {
        final String user = suggestion.replaceAll("\\(.*\\)", "").trim();
        SearchResults userProfile = new SearchResults();
        Log.d(TAG, "getResultFromSuggestion: " + user);

        for (SearchResults result : mSearchResults) {
            if (result.getUsername().equals(user)) {
                Log.d(TAG, "getResultFromSuggestion: " + result.getId() + "/" + result.getUsername());
                userProfile = result;
            }
        }

        if(userProfile.getId()!=null){
            mService.getUserProfile(userProfile.getUsername(), userProfile.getId(), new Service.GetUserCallback() {
                @Override
                public void onSuccess(User userResponse) {
                    Log.d(TAG, "onSuccess: " + new Gson().toJson(userResponse));
                    if(mLeaderboardsView.isActive()) mLeaderboardsView.showUserProfile(userResponse);
                }

                @Override
                public void onError(Throwable error) {
                    error.printStackTrace();
                }
            });
        }
    }
}

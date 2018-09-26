package com.futchampionsstats.service;

import android.util.Log;

import com.futchampionsstats.models.leaderboards.SearchResults;
import com.futchampionsstats.models.leaderboards.Top100;
import com.futchampionsstats.models.leaderboards.User;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yiannitzan on 5/3/17.
 */

public class Service {

    public static final String TAG = Service.class.getSimpleName();

    private final WeekendLeagueService weekendLeagueService;

    public Service(WeekendLeagueService weekendLeagueService){
        this.weekendLeagueService = weekendLeagueService;
    }

    public void getUserProfile(String username, String id, final GetUserCallback callback){

        weekendLeagueService.getUserProfile(username, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<User>() {
                    @Override
                    public void onSuccess(User user) {
                        callback.onSuccess(user);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onNext: " + e.getMessage());
                        callback.onError(e);
                    }
                });
    }

    public void getTop100(String month, String region, String console, final GetTop100Callback callback){

        weekendLeagueService.getTop100(month.toLowerCase(), region.toLowerCase(), console)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retry(2)
                .subscribe(new DisposableSingleObserver<Top100>() {

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onNext: " + e.getMessage());
                        callback.onError(e);
                    }

                    @Override
                    public void onSuccess(Top100 top100) {

                        callback.onSuccess(top100);
                    }
                });
    }


    public void getUserSearchResults(String query, final GetSearchResultsCallback callback){

        weekendLeagueService.getUserSearchResults(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<SearchResults[]>() {

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onNext: " + e.getMessage());
                        callback.onError(e);
                    }

                    @Override
                    public void onSuccess(SearchResults[] searchResults) {

                        callback.onSuccess(searchResults);
                    }
                });
    }

    public void getMonths(final GetMonthsCallback callback){
        weekendLeagueService.getMonthsAvailable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<String[]>() {
                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e);
                    }

                    @Override
                    public void onSuccess(String[] response) {
                        callback.onSuccess(response);
                    }
                });
    }

    public interface GetUserCallback{
        void onSuccess(User userResponse);
        void onError(Throwable error);
    }

    public interface GetTop100Callback{
        void onSuccess(Top100 top100);
        void onError(Throwable error);
    }

    public interface GetSearchResultsCallback{
        void onSuccess(SearchResults[] searchResults);
        void onError(Throwable error);
    }

    public interface GetMonthsCallback{
        void onSuccess(String[] months);
        void onError(Throwable error);
    }
}

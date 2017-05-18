package com.futchampionsstats.service;

import android.util.Log;

import com.futchampionsstats.models.leaderboards.SearchResults;
import com.futchampionsstats.models.leaderboards.Top100;
import com.futchampionsstats.models.leaderboards.User;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yiannitzan on 5/3/17.
 */

public class Service {

    public static final String TAG = Service.class.getSimpleName();

    private final WeekendLeagueService weekendLeagueService;

    public Service(WeekendLeagueService weekendLeagueService){
        this.weekendLeagueService = weekendLeagueService;
    }

    public Subscription getUserProfile(String username, String id, final GetUserCallback callback){

        return weekendLeagueService.getUserProfile(username, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onNext: " + e.getMessage());
                        callback.onError(e);
                    }

                    @Override
                    public void onNext(User user) {

                        callback.onSuccess(user);
                    }
                });
    }

    public Subscription getTop100(String month, String region, String console, final GetTop100Callback callback){

        return weekendLeagueService.getTop100(month.toLowerCase(), region.toLowerCase(), console)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Top100>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onNext: " + e.getMessage());
                        callback.onError(e);
                    }

                    @Override
                    public void onNext(Top100 top100) {

                        callback.onSuccess(top100);
                    }
                });
    }


    public Subscription getUserSearchResults(String query, final GetSearchResultsCallback callback){

        return weekendLeagueService.getUserSearchResults(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SearchResults[]>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onNext: " + e.getMessage());
                        callback.onError(e);
                    }

                    @Override
                    public void onNext(SearchResults[] searchResults) {

                        callback.onSuccess(searchResults);
                    }
                });
    }

    public Subscription getMonths(final GetMonthsCallback callback){
        return weekendLeagueService.getMonthsAvailable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String[]>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e);
                    }

                    @Override
                    public void onNext(String[] response) {
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

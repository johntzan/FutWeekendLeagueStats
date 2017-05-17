package com.futchampionsstats.service;

import com.futchampionsstats.models.leaderboards.SearchResults;
import com.futchampionsstats.models.leaderboards.Top100;
import com.futchampionsstats.models.leaderboards.User;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by yiannitzan on 5/3/17.
 */

public interface WeekendLeagueService {

    @GET("api/user/{username}/{id}")
    Observable<User> getUserProfile(@Path("username") String username, @Path("id") String id);

    @GET("api/top100/{month}/{region}/{console}")
    Observable<Top100> getTop100(@Path("month") String month, @Path("region") String region, @Path("console") String console);

    @GET("api/search/{query}")
    Observable<SearchResults[]> getUserSearchResults(@Path("query") String query);

    @GET("api/months")
    Observable<String[]> getMonthsAvailable();

}

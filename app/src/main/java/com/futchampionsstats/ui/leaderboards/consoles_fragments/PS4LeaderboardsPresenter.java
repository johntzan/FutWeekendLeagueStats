package com.futchampionsstats.ui.leaderboards.consoles_fragments;

import android.util.Log;

import com.futchampionsstats.models.leaderboards.Top100;
import com.futchampionsstats.service.Service;
import com.google.gson.Gson;

/**
 * Created by yiannitzan on 5/11/17.
 */

public class PS4LeaderboardsPresenter implements PS4LeaderboardsContract.Presenter{

    public static final String TAG = PS4LeaderboardsPresenter.class.getSimpleName();
    private PS4LeaderboardsContract.View mPS4LeaderboardsView;
    private Service mService;
    private Top100 mTop100;
    private String[] mMonths;

    public PS4LeaderboardsPresenter(Service service, PS4LeaderboardsContract.View view) {
        this.mService = service;
        this.mPS4LeaderboardsView = view;

        this.mPS4LeaderboardsView.setPresenter(this);
    }

    @Override
    public void start() {
            getPS4Leaderboards("current", "all");
            getMonths();
    }

    @Override
    public void getPS4Leaderboards(String month, String region) {
        month = month.replaceAll("\\s+","");
        if(region.equals("Rest of World")){
            region = "row";
        }
        mPS4LeaderboardsView.showLoading();
        mService.getTop100(month, region, "ps4", new Service.GetTop100Callback() {
            @Override
            public void onSuccess(Top100 top100) {
                Log.d(TAG, "onSuccess: " + new Gson().toJson(top100));
                mPS4LeaderboardsView.hideLoading();
                mTop100 = top100;
                mPS4LeaderboardsView.setPS4Leaderboards(top100);
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
                mPS4LeaderboardsView.showError();
            }
        });
    }

    @Override
    public void getMonths() {
        mService.getMonths(new Service.GetMonthsCallback() {
            @Override
            public void onSuccess(String[] months) {
                mMonths = months;
                mPS4LeaderboardsView.setMonths(months);
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }
}

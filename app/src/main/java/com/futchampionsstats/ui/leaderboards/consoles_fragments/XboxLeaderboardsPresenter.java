package com.futchampionsstats.ui.leaderboards.consoles_fragments;

import android.util.Log;

import com.futchampionsstats.models.leaderboards.Top100;
import com.futchampionsstats.service.Service;
import com.google.gson.Gson;

/**
 * Created by yiannitzan on 5/12/17.
 */

public class XboxLeaderboardsPresenter implements XboxLeaderboardsContract.Presenter{

    public static final String TAG = XboxLeaderboardsPresenter.class.getSimpleName();

    private XboxLeaderboardsContract.View mXboxLeaderboardsView;
    private Service mService;

    private Top100 mTop100;
    private String[] mMonths;

    public XboxLeaderboardsPresenter(Service service, XboxLeaderboardsContract.View view) {
        this.mService = service;
        this.mXboxLeaderboardsView = view;

        this.mXboxLeaderboardsView.setPresenter(this);

    }

    @Override
    public void start() {
            getXboxLeaderboards("current", "all");
            getMonths();
    }

    @Override
    public void getXboxLeaderboards(String month, String region) {
        month = month.replaceAll("\\s+","");
        if(region.equals("Rest of World")){
            region = "row";
        }
        mXboxLeaderboardsView.showLoading();
        mService.getTop100(month, region.toLowerCase(), "xbox", new Service.GetTop100Callback() {
            @Override
            public void onSuccess(Top100 top100) {
                Log.d(TAG, "onSuccess: " + new Gson().toJson(top100));
                mXboxLeaderboardsView.hideLoading();
                mTop100 = top100;
                mXboxLeaderboardsView.setXboxLeaderboards(top100);
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }

    @Override
    public void getMonths() {
        mService.getMonths(new Service.GetMonthsCallback() {
            @Override
            public void onSuccess(String[] months) {
                mMonths = months;
                mXboxLeaderboardsView.setMonths(months);
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }
}

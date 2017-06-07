package com.futchampionsstats.ui.leaderboards.consoles_fragments;

import android.support.annotation.NonNull;
import android.util.Log;

import com.futchampionsstats.models.leaderboards.Top100;
import com.futchampionsstats.service.Service;
import com.google.gson.Gson;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by yiannitzan on 5/11/17.
 */

public class PS4LeaderboardsPresenter implements PS4LeaderboardsContract.Presenter{

    public static final String TAG = PS4LeaderboardsPresenter.class.getSimpleName();
    private PS4LeaderboardsContract.View mPS4LeaderboardsView;
    private Service mService;
    private Top100 mTop100;
    private String[] mMonths;

    public PS4LeaderboardsPresenter(@NonNull Service service, @NonNull PS4LeaderboardsContract.View view) {
        mService = checkNotNull(service);
        mPS4LeaderboardsView = checkNotNull(view);

        mPS4LeaderboardsView.setPresenter(this);
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
        if(mPS4LeaderboardsView.isActive()) mPS4LeaderboardsView.showLoading();
        mService.getTop100(month, region, "ps4", new Service.GetTop100Callback() {
            @Override
            public void onSuccess(Top100 top100) {
                Log.d(TAG, "onSuccess: " + new Gson().toJson(top100));
                if(mPS4LeaderboardsView.isActive()) mPS4LeaderboardsView.hideLoading();
                mTop100 = top100;
                if(mPS4LeaderboardsView.isActive()) mPS4LeaderboardsView.setPS4Leaderboards(top100);
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
                if(mPS4LeaderboardsView.isActive()) mPS4LeaderboardsView.showError();
            }
        });
    }

    @Override
    public void getMonths() {
        mService.getMonths(new Service.GetMonthsCallback() {
            @Override
            public void onSuccess(String[] months) {
                mMonths = months;
                if(mPS4LeaderboardsView.isActive()) mPS4LeaderboardsView.setMonths(months);
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }
}

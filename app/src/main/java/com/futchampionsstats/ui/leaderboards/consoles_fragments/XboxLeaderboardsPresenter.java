package com.futchampionsstats.ui.leaderboards.consoles_fragments;

import android.support.annotation.NonNull;
import android.util.Log;

import com.futchampionsstats.models.leaderboards.Top100;
import com.futchampionsstats.service.Service;
import com.google.gson.Gson;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by yiannitzan on 5/12/17.
 */

public class XboxLeaderboardsPresenter implements XboxLeaderboardsContract.Presenter{

    public static final String TAG = XboxLeaderboardsPresenter.class.getSimpleName();

    private XboxLeaderboardsContract.View mXboxLeaderboardsView;
    private Service mService;

    private Top100 mTop100;
    private String[] mMonths;

    public XboxLeaderboardsPresenter(@NonNull Service service, @NonNull XboxLeaderboardsContract.View view) {
        mService = checkNotNull(service);
        mXboxLeaderboardsView = checkNotNull(view);

        mXboxLeaderboardsView.setPresenter(this);

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
        if(mXboxLeaderboardsView.isActive()) mXboxLeaderboardsView.showLoading();
        mService.getTop100(month, region, "xbox", new Service.GetTop100Callback() {
            @Override
            public void onSuccess(Top100 top100) {
                Log.d(TAG, "onSuccess: " + new Gson().toJson(top100));
                if(mXboxLeaderboardsView.isActive()) mXboxLeaderboardsView.hideLoading();
                mTop100 = top100;
                if(mXboxLeaderboardsView.isActive()) mXboxLeaderboardsView.setXboxLeaderboards(top100);
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
                if(mXboxLeaderboardsView.isActive()) mXboxLeaderboardsView.setMonths(months);
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }
}

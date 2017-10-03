package com.futchampionsstats;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.stetho.Stetho;
import com.futchampionsstats.models.DaggerSquadRepositoryComponent;
import com.futchampionsstats.models.DaggerWeekendLeagueRepositoryComponent;
import com.futchampionsstats.models.SquadRepositoryComponent;
import com.futchampionsstats.models.WeekendLeagueRepositoryComponent;
import com.futchampionsstats.service.DaggerServiceComponent;
import com.futchampionsstats.service.ServiceComponent;

import io.fabric.sdk.android.Fabric;

/**
 * Created by yiannitzan on 1/9/17.
 */
public class FutChampsApplication extends Application{

    public static final String TAG = FutChampsApplication.class.getSimpleName();

    private WeekendLeagueRepositoryComponent mWeekendLeagueRepository;
    private SquadRepositoryComponent mSquadRepositoryComponent;
    private ServiceComponent mServiceComponent;

    private ConnectionDetector cd;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Log.d(TAG, "onCreate: onApp create");
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }

        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(this);

        mWeekendLeagueRepository = DaggerWeekendLeagueRepositoryComponent.builder()
                .applicationModule(new ApplicationModule(getApplicationContext()))
                .build();

        mSquadRepositoryComponent = DaggerSquadRepositoryComponent.builder()
                .applicationModule(new ApplicationModule(getApplicationContext()))
                .build();

        mServiceComponent = DaggerServiceComponent.builder()
                .applicationModule(new ApplicationModule(getApplicationContext()))
                .build();

        cd = new ConnectionDetector(this);
    }

    public static FutChampsApplication getInstance(Context context) {
        return (FutChampsApplication)context.getApplicationContext();
    }


    public boolean isInternetAvailable() {
        // get Internet status
        return cd.isConnectingToInternet();
    }

    public WeekendLeagueRepositoryComponent getWeekendLeagueRepository(){
        return mWeekendLeagueRepository;
    }

    public SquadRepositoryComponent getSquadRepositoryComponent(){
        return mSquadRepositoryComponent;
    }

    public ServiceComponent getServiceComponent(){
        return mServiceComponent;
    }

}

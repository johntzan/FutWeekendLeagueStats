package com.futchampionsstats;

import android.app.Application;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
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

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Log.d(TAG, "onCreate: onApp create");
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }

        mWeekendLeagueRepository = DaggerWeekendLeagueRepositoryComponent.builder()
                .applicationModule(new ApplicationModule(getApplicationContext()))
                .build();

        mSquadRepositoryComponent = DaggerSquadRepositoryComponent.builder()
                .applicationModule(new ApplicationModule(getApplicationContext()))
                .build();

        mServiceComponent = DaggerServiceComponent.builder()
                .applicationModule(new ApplicationModule(getApplicationContext()))
                .build();

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

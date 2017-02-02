package com.futchampionsstats;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;

import io.fabric.sdk.android.Fabric;

/**
 * Created by yiannitzan on 1/9/17.
 */
public class FutChampsApplication extends Application{

    public static final String TAG = FutChampsApplication.class.getSimpleName();

    private static FutChampsApplication ourInstance = new FutChampsApplication();

    public static FutChampsApplication getInstance(Context ctx) {
        return (FutChampsApplication) ctx.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Log.d(TAG, "onCreate: onApp create");
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }


}

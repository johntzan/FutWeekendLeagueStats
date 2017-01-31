package com.futchampionsstats;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.facebook.stetho.Stetho;

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
        Log.d(TAG, "onCreate: onApp create");
        Stetho.initializeWithDefaults(this);

    }


}

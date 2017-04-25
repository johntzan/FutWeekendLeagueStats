package com.futchampionsstats;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yiannitzan on 4/21/17.
 */

@Module
public class ApplicationModule {

    private final Context mContext;

    ApplicationModule(Context context) {
        mContext = context;
    }

    @Provides
    Context provideContext() {
        return mContext;
    }

}

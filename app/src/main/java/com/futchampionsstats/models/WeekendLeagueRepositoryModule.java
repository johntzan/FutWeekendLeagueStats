package com.futchampionsstats.models;

import android.content.Context;
import android.support.annotation.NonNull;

import com.futchampionsstats.models.source.WeekendLeagueLocalDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yiannitzan on 4/21/17.
 */

@Module
public class WeekendLeagueRepositoryModule {

    @Singleton
    @Provides
    WeekendLeagueLocalDataSource provideWeekendLeagueLocalDataSource(Context context) {
        return new WeekendLeagueLocalDataSource(context);
    }

    @Provides
    @Singleton
    WeekendLeagueRepository provideWeekendLeagueRepository(@NonNull Context context) {
        return WeekendLeagueRepository.getInstance(WeekendLeagueLocalDataSource.getInstance(context));
    }
}

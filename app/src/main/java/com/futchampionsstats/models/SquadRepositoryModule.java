package com.futchampionsstats.models;

import android.content.Context;
import android.support.annotation.NonNull;

import com.futchampionsstats.models.source.squads.SquadsLocalDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yiannitzan on 4/25/17.
 */
@Module
public class SquadRepositoryModule {

    @Singleton
    @Provides
    SquadsLocalDataSource provideSquadsLocalDataSource(Context context) {
        return new SquadsLocalDataSource(context);
    }

    @Provides
    @Singleton
    SquadRepository provideSquadRepository(@NonNull Context context) {
        return SquadRepository.getInstance(SquadsLocalDataSource.getInstance(context));
    }
}

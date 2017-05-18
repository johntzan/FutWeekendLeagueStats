package com.futchampionsstats.models;

import com.futchampionsstats.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yiannitzan on 4/25/17.
 */
@Singleton
@Component(modules = {SquadRepositoryModule.class, ApplicationModule.class})
public interface SquadRepositoryComponent {

    SquadRepository getSquadRepository();
}

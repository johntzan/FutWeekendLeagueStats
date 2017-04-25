package com.futchampionsstats.models;

import com.futchampionsstats.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yiannitzan on 4/21/17.
 */

@Singleton
@Component(modules = {WeekendLeagueRepositoryModule.class, ApplicationModule.class})
public interface WeekendLeagueRepositoryComponent {

    WeekendLeagueRepository getWeekendLeagueRepository();
}

package com.futchampionsstats.ui.wl;

import com.futchampionsstats.models.WeekendLeagueRepositoryComponent;
import com.futchampionsstats.utils.FragmentScoped;

import dagger.Component;

/**
 * Created by yiannitzan on 4/21/17.
 */
@FragmentScoped
@Component(dependencies = WeekendLeagueRepositoryComponent.class, modules = WeekendLeagueDetailPresenterModule.class)
public interface WeekendLeagueComponent {

    void inject(WeekendLeagueDetailFragment fragment);
}

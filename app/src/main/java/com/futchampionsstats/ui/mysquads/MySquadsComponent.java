package com.futchampionsstats.ui.mysquads;

import com.futchampionsstats.models.SquadRepositoryComponent;
import com.futchampionsstats.utils.FragmentScoped;

import dagger.Component;

/**
 * Created by yiannitzan on 4/25/17.
 */
@FragmentScoped
@Component(dependencies = SquadRepositoryComponent.class, modules = MySquadsPresenterModule.class)
public interface MySquadsComponent {

    void inject(MySquadsFragment fragment);

}

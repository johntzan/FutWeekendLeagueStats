package com.futchampionsstats.service;

import com.futchampionsstats.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yiannitzan on 5/3/17.
 */

@Singleton
@Component(modules = {ServiceModule.class, ApplicationModule.class})
public interface ServiceComponent {

    Service getService();
}

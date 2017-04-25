package com.futchampionsstats.ui.mysquads;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yiannitzan on 4/25/17.
 */

@Module
public class MySquadsPresenterModule {

    private final MySquadsContract.View mView;

    public MySquadsPresenterModule(MySquadsContract.View view){
        mView = view;
    }

    @Provides
    MySquadsContract.View provideMySquadsContractView(){
        return mView;
    }
}

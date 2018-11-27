package com.clicktech.snsktv.module_home.inject.module;


import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.module_home.contract.PracticingSingContract;
import com.clicktech.snsktv.module_home.model.PracticingSingModel;

import dagger.Module;
import dagger.Provides;


@Module
public class PracticingSingModule {
    private PracticingSingContract.View view;

    /**
     * 构建PracticingSingModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public PracticingSingModule(PracticingSingContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    PracticingSingContract.View providePracticingSingView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    PracticingSingContract.Model providePracticingSingModel(PracticingSingModel model) {
        return model;
    }
}
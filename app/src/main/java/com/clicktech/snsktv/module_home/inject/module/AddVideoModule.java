package com.clicktech.snsktv.module_home.inject.module;


import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.module_home.contract.AddVideoContract;
import com.clicktech.snsktv.module_home.model.AddVideoModel;

import dagger.Module;
import dagger.Provides;


@Module
public class AddVideoModule {
    private AddVideoContract.View view;

    /**
     * 构建AddVideoModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public AddVideoModule(AddVideoContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    AddVideoContract.View provideAddVideoView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    AddVideoContract.Model provideAddVideoModel(AddVideoModel model) {
        return model;
    }
}
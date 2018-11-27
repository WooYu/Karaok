package com.clicktech.snsktv.module_home.inject.module;

import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.module_home.contract.OtherShareContract;
import com.clicktech.snsktv.module_home.model.OtherShareModel;

import dagger.Module;
import dagger.Provides;


@Module
public class OtherShareModule {
    private OtherShareContract.View view;

    /**
     * 构建OtherShareModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public OtherShareModule(OtherShareContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    OtherShareContract.View provideOtherShareView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    OtherShareContract.Model provideOtherShareModel(OtherShareModel model) {
        return model;
    }
}
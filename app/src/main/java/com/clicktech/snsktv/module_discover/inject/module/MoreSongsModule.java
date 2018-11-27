package com.clicktech.snsktv.module_discover.inject.module;

import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.module_discover.contract.MoreSongsContract;
import com.clicktech.snsktv.module_discover.model.MoreSongsModel;

import dagger.Module;
import dagger.Provides;


@Module
public class MoreSongsModule {
    private MoreSongsContract.View view;

    /**
     * 构建MoreSongsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MoreSongsModule(MoreSongsContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MoreSongsContract.View provideMoreSongsView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MoreSongsContract.Model provideMoreSongsModel(MoreSongsModel model) {
        return model;
    }
}
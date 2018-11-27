package com.clicktech.snsktv.module_enter.inject.module;


import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.module_enter.contract.PreviewWorkContract;
import com.clicktech.snsktv.module_enter.model.PreviewWorkModel;

import dagger.Module;
import dagger.Provides;


@Module
public class PreviewWorkModule {
    private PreviewWorkContract.View view;

    /**
     * 构建PreviewWorkModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public PreviewWorkModule(PreviewWorkContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    PreviewWorkContract.View providePreviewWorkView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    PreviewWorkContract.Model providePreviewWorkModel(PreviewWorkModel model) {
        return model;
    }
}
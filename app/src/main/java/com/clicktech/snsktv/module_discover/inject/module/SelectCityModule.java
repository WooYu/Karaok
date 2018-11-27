package com.clicktech.snsktv.module_discover.inject.module;

import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.module_discover.contract.SelectCityContract;
import com.clicktech.snsktv.module_discover.model.SelectCityModel;

import dagger.Module;
import dagger.Provides;


@Module
public class SelectCityModule {
    private SelectCityContract.View view;

    /**
     * 构建SelectCityModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public SelectCityModule(SelectCityContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    SelectCityContract.View provideSelectCityView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    SelectCityContract.Model provideSelectCityModel(SelectCityModel model) {
        return model;
    }
}
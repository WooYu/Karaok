package com.clicktech.snsktv.module_discover.inject.component;

import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.module_discover.inject.module.SelectCityModule;
import com.clicktech.snsktv.module_discover.ui.activity.SelectCityActivity;

import dagger.Component;

@ActivityScope
@Component(modules = SelectCityModule.class, dependencies = AppComponent.class)
public interface SelectCityComponent {
    void inject(SelectCityActivity activity);
}
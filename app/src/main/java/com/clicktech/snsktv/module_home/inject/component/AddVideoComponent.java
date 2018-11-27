package com.clicktech.snsktv.module_home.inject.component;


import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.module_home.inject.module.AddVideoModule;
import com.clicktech.snsktv.module_home.ui.activity.AddVideoActivity;

import dagger.Component;

@ActivityScope
@Component(modules = AddVideoModule.class, dependencies = AppComponent.class)
public interface AddVideoComponent {
    void inject(AddVideoActivity activity);
}
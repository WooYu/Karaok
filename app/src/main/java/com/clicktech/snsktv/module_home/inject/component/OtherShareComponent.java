package com.clicktech.snsktv.module_home.inject.component;

import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.module_home.inject.module.OtherShareModule;
import com.clicktech.snsktv.module_home.ui.activity.OtherShareActivity;

import dagger.Component;

@ActivityScope
@Component(modules = OtherShareModule.class, dependencies = AppComponent.class)
public interface OtherShareComponent {
    void inject(OtherShareActivity activity);
}
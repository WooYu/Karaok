package com.clicktech.snsktv.module_home.inject.component;


import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.module_home.inject.module.PracticingSingModule;
import com.clicktech.snsktv.module_home.ui.activity.PracticingSingActivity;

import dagger.Component;

@ActivityScope
@Component(modules = PracticingSingModule.class, dependencies = AppComponent.class)
public interface PracticingSingComponent {
    void inject(PracticingSingActivity activity);
}
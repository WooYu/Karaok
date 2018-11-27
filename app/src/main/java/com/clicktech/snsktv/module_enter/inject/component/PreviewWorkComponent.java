package com.clicktech.snsktv.module_enter.inject.component;


import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.module_enter.inject.module.PreviewWorkModule;
import com.clicktech.snsktv.module_enter.ui.activity.PreviewWorkActivity;

import dagger.Component;

@ActivityScope
@Component(modules = PreviewWorkModule.class, dependencies = AppComponent.class)
public interface PreviewWorkComponent {
    void inject(PreviewWorkActivity activity);
}
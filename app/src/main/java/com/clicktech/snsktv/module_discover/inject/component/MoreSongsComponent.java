package com.clicktech.snsktv.module_discover.inject.component;

import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.module_discover.inject.module.MoreSongsModule;
import com.clicktech.snsktv.module_discover.ui.activity.MoreSongsActivity;

import dagger.Component;

@ActivityScope
@Component(modules = MoreSongsModule.class, dependencies = AppComponent.class)
public interface MoreSongsComponent {
    void inject(MoreSongsActivity activity);
}
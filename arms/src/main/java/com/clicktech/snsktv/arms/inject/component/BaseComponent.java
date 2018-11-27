package com.clicktech.snsktv.arms.inject.component;

import com.clicktech.snsktv.arms.base.BaseApplication;
import com.clicktech.snsktv.arms.inject.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {AppModule.class})
public interface BaseComponent {
    void inject(BaseApplication application);
}

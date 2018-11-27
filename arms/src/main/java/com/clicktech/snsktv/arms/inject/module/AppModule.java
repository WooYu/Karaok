package com.clicktech.snsktv.arms.inject.module;

import android.app.Application;
import android.media.MediaPlayer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * 提供第三方类或者全局的类实例：Application、Gson实例
 */
@Module
public class AppModule {
    private Application mApplication;

    public AppModule(Application application) {
        this.mApplication = application;
    }

    @Singleton
    @Provides
    public Application provideApplication() {
        return mApplication;
    }

    @Singleton
    @Provides
    public Gson provideGson() {
        return new GsonBuilder().setLenient().create();
    }

    @Singleton
    @Provides
    public MediaPlayer provideMediaPlayer() {
        return new MediaPlayer();
    }
}

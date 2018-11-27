package com.clicktech.snsktv.arms.inject.module;

import com.clicktech.snsktv.arms.widget.imageloader.BaseImageLoaderStrategy;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageLoaderStrategy;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class ImageModule {

    @Singleton
    @Provides
    public BaseImageLoaderStrategy provideImageLoaderStrategy() {
        return new GlideImageLoaderStrategy();
    }

}

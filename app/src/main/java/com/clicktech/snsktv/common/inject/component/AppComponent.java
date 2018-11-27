package com.clicktech.snsktv.common.inject.component;

import android.app.Application;
import android.media.MediaPlayer;

import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.inject.module.AppModule;
import com.clicktech.snsktv.arms.inject.module.ClientModule;
import com.clicktech.snsktv.arms.inject.module.GlobeConfigModule;
import com.clicktech.snsktv.arms.inject.module.ImageModule;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.cache.CacheManager;
import com.clicktech.snsktv.common.inject.module.CacheModule;
import com.clicktech.snsktv.common.inject.module.ServiceModule;
import com.clicktech.snsktv.common.net.ServiceManager;
import com.clicktech.snsktv.entity.AuthEntity;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;


@Singleton
@Component(modules = {AppModule.class, ClientModule.class, ServiceModule.class,
        ImageModule.class, CacheModule.class, GlobeConfigModule.class})
public interface AppComponent {
    Application Application();

    //服务管理器,retrofitApi
    ServiceManager serviceManager();

    //缓存管理器
    CacheManager cacheManager();

    //Rxjava错误处理管理类
    RxErrorHandler rxErrorHandler();

    OkHttpClient okHttpClient();

    //图片管理器,用于加载图片的管理类,默认使用glide,使用策略模式,可替换框架
    ImageLoader imageLoader();

    //gson
    Gson gson();

    //用于管理所有activity
    AppManager appManager();

    //接口请求时获取Auth信息
    AuthEntity authManager();

    //MediaPlayer
    MediaPlayer mediaplayer();

}

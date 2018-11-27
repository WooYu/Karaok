package com.clicktech.snsktv.arms.base;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.clicktech.snsktv.arms.inject.component.DaggerBaseComponent;
import com.clicktech.snsktv.arms.inject.module.AppModule;
import com.clicktech.snsktv.arms.inject.module.ClientModule;
import com.clicktech.snsktv.arms.inject.module.GlobeConfigModule;
import com.clicktech.snsktv.arms.inject.module.ImageModule;

import javax.inject.Inject;

import static com.clicktech.snsktv.arms.utils.Preconditions.checkNotNull;

/**
 * 本项目由
 * mvp
 * +dagger2
 * +retrofit
 * +rxjava
 * +androideventbus
 * +butterknife组成
 */
public abstract class BaseApplication extends MultiDexApplication {
    static private BaseApplication mApplication;
    protected final String TAG = this.getClass().getSimpleName();
    @Inject
    protected AppManager mAppManager;
    @Inject
    protected ActivityLifecycle mActivityLifecycle;
    private ClientModule mClientModule;
    private AppModule mAppModule;
    private ImageModule mImagerModule;
    private GlobeConfigModule mGlobeConfigModule;

    /**
     * 返回上下文
     *
     * @return
     */
    public static Context getContext() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        this.mAppModule = new AppModule(this);//提供application
        DaggerBaseComponent
                .builder()
                .appModule(mAppModule)
                .build()
                .inject(this);
        this.mImagerModule = new ImageModule();//图片加载框架默认使用glide
        this.mClientModule = new ClientModule(mAppManager);//用于提供okhttp和retrofit的单例
        this.mGlobeConfigModule = checkNotNull(getGlobeConfigModule(), "lobeConfigModule is required");
        registerActivityLifecycleCallbacks(mActivityLifecycle);
    }

    /**
     * 程序终止的时候执行
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mClientModule != null)
            this.mClientModule = null;
        if (mAppModule != null)
            this.mAppModule = null;
        if (mImagerModule != null)
            this.mImagerModule = null;
        if (mActivityLifecycle != null) {
            unregisterActivityLifecycleCallbacks(mActivityLifecycle);
        }
        if (mAppManager != null) {//释放资源
            this.mAppManager.release();
            this.mAppManager = null;
        }
        if (mApplication != null)
            this.mApplication = null;
    }

    /**
     * 将app的全局配置信息封装进module(使用Dagger注入到需要配置信息的地方)
     *
     * @return
     */
    protected abstract GlobeConfigModule getGlobeConfigModule();

    public ClientModule getClientModule() {
        return mClientModule;
    }

    public AppModule getAppModule() {
        return mAppModule;
    }

    public ImageModule getImageModule() {
        return mImagerModule;
    }

    public AppManager getAppManager() {
        return mAppManager;
    }

}

package com.clicktech.snsktv.module_home.model;

import android.app.Application;

import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BaseModel;
import com.clicktech.snsktv.common.cache.CacheManager;
import com.clicktech.snsktv.common.net.ServiceManager;
import com.clicktech.snsktv.module_home.contract.OtherShareContract;
import com.google.gson.Gson;

import javax.inject.Inject;


@ActivityScope
public class OtherShareModel extends BaseModel<ServiceManager, CacheManager> implements
        OtherShareContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public OtherShareModel(ServiceManager serviceManager, CacheManager cacheManager, Gson gson, Application application) {
        super(serviceManager, cacheManager);
        this.mGson = gson;
        this.mApplication = application;
    }

    @Override
    public void onDestory() {
        super.onDestory();
        this.mGson = null;
        this.mApplication = null;
    }

}
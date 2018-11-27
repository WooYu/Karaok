package com.clicktech.snsktv.module_home.model;

import android.app.Application;

import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BaseModel;
import com.clicktech.snsktv.common.cache.CacheManager;
import com.clicktech.snsktv.common.net.ServiceManager;
import com.clicktech.snsktv.module_home.contract.AddVideoContract;
import com.google.gson.Gson;

import javax.inject.Inject;


@ActivityScope
public class AddVideoModel extends BaseModel implements AddVideoContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public AddVideoModel(ServiceManager serviceManager, CacheManager cacheManager, Gson gson, Application application) {
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
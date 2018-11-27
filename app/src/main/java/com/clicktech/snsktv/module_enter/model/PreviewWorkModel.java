package com.clicktech.snsktv.module_enter.model;

import android.app.Application;

import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BaseModel;
import com.clicktech.snsktv.common.cache.CacheManager;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.common.net.ServiceManager;
import com.clicktech.snsktv.entity.HomeShowResponse;
import com.clicktech.snsktv.module_enter.contract.PreviewWorkContract;
import com.google.gson.Gson;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;


@ActivityScope
public class PreviewWorkModel extends BaseModel<ServiceManager, CacheManager> implements PreviewWorkContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public PreviewWorkModel(ServiceManager serviceManager, CacheManager cacheManager, Gson gson, Application application) {
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

    @Override
    public Observable<HomeShowResponse> getHomeShowList(String userid) {
        Map<String, String> map = RequestParams_Maker.getHome(userid);
        return mServiceManager.getCommonService().getHome(map);
    }
}
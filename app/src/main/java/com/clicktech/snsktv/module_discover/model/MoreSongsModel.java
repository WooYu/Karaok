package com.clicktech.snsktv.module_discover.model;

import android.app.Application;

import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BaseModel;
import com.clicktech.snsktv.common.cache.CacheManager;
import com.clicktech.snsktv.common.net.ServiceManager;
import com.clicktech.snsktv.entity.ListOfWorksResponse;
import com.clicktech.snsktv.module_discover.contract.MoreSongsContract;
import com.google.gson.Gson;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;


@ActivityScope
public class MoreSongsModel extends BaseModel<ServiceManager, CacheManager> implements MoreSongsContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public MoreSongsModel(ServiceManager serviceManager, CacheManager cacheManager, Gson gson, Application application) {
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
    public Observable<ListOfWorksResponse> getMoreSingerOfTheSong(Map<String, String> info) {
        return mServiceManager.getCommonService().getSingerSongWorksList(info);
    }
}
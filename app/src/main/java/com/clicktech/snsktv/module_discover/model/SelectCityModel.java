package com.clicktech.snsktv.module_discover.model;

import android.app.Application;
import android.support.annotation.NonNull;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BaseModel;
import com.clicktech.snsktv.common.cache.CacheManager;
import com.clicktech.snsktv.common.net.ServiceManager;
import com.clicktech.snsktv.entity.CityEntity;
import com.clicktech.snsktv.module_discover.contract.SelectCityContract;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


@ActivityScope
public class SelectCityModel extends BaseModel implements SelectCityContract.Model {

    private Gson mGson;
    private Application mApplication;

    @Inject
    public SelectCityModel(ServiceManager serviceManager, CacheManager cacheManager,
                           Gson gson, Application application) {
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
    public List<CityEntity> getJapanCities() {
        return getCityEntities(0);
    }

    @Override
    public List<CityEntity> getUnJapanCities() {
        return getCityEntities(1);
    }

    @NonNull
    private List<CityEntity> getCityEntities(int type) {
        List<CityEntity> province = new ArrayList<>();
        String[] str;
        if (type == 1) {
            str = mApplication.getResources().getStringArray(R.array.province);
        } else {
            str = mApplication.getResources().getStringArray(R.array.japanprovince);
        }

        for (int i = 0; i < str.length; i++) {
            CityEntity cityEntity = new CityEntity();
            cityEntity.setName(str[i]);
            province.add(cityEntity);
        }
        return province;
    }

}
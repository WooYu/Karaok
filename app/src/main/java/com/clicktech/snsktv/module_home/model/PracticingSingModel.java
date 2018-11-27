package com.clicktech.snsktv.module_home.model;

import android.app.Application;

import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BaseModel;
import com.clicktech.snsktv.arms.utils.FileUtils;
import com.clicktech.snsktv.common.cache.CacheManager;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.common.net.ServiceManager;
import com.clicktech.snsktv.entity.SongInfoWithSongIDResponse;
import com.clicktech.snsktv.module_home.contract.PracticingSingContract;
import com.google.gson.Gson;
import com.library.multimedia.lyricscontrols.parser.DefaultLrcParser;
import com.library.multimedia.lyricscontrols.parser.LrcRow;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;


@ActivityScope
public class PracticingSingModel extends BaseModel<ServiceManager, CacheManager> implements PracticingSingContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public PracticingSingModel(ServiceManager serviceManager, CacheManager cacheManager, Gson gson, Application application) {
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
    public Observable<SongInfoWithSongIDResponse> getKSingInfo(String userid, String singid, int is_practice) {
        return mServiceManager.getCommonService()
                .getSingerSongToKGe(RequestParams_Maker.getSingerSongToKGe(userid, singid, is_practice));
    }

    @Override
    public List<LrcRow> parseLrc(String lrcpath) {
        List<LrcRow> rows = null;
        String charsetName = "UTF-8";
        rows = DefaultLrcParser.getIstance().getLrcRows(FileUtils.readFile2String(lrcpath, charsetName));
        return rows;
    }
}
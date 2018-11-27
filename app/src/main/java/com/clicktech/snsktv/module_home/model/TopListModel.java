package com.clicktech.snsktv.module_home.model;

import android.app.Application;

import com.clicktech.snsktv.arms.mvp.BaseModel;
import com.clicktech.snsktv.common.cache.CacheManager;
import com.clicktech.snsktv.common.net.ServiceManager;
import com.clicktech.snsktv.entity.PastSpecialEntity;
import com.clicktech.snsktv.entity.TopListEntity;
import com.clicktech.snsktv.module_home.contract.TopListContract;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */

/**
 * Created by Administrator on 2017/1/13.
 */

public class TopListModel extends BaseModel<ServiceManager, CacheManager> implements TopListContract.Model {
    private Gson mGson;
    private Application mApplication;

    public TopListModel(ServiceManager serviceManager, CacheManager cacheManager, Gson gson, Application application) {
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
    public List<TopListEntity> getTopList() {
        List<TopListEntity> lists = new ArrayList<TopListEntity>();
        for (int i = 0; i < 15; i++) {
            lists.add(new TopListEntity());
        }
        return lists;
    }

    @Override
    public List<PastSpecialEntity> getPastSpecialList() {
        List<PastSpecialEntity> lists = new ArrayList<PastSpecialEntity>();
        for (int i = 0; i < 15; i++) {
            lists.add(new PastSpecialEntity());
        }
        return lists;
    }
}
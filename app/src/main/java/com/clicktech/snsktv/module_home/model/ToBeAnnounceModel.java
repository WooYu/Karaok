package com.clicktech.snsktv.module_home.model;

import android.app.Application;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BaseModel;
import com.clicktech.snsktv.common.cache.CacheManager;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.common.net.ServiceManager;
import com.clicktech.snsktv.entity.SignCosResponse;
import com.clicktech.snsktv.entity.SoundEffectEntity;
import com.clicktech.snsktv.entity.SoundReverbEntity;
import com.clicktech.snsktv.module_home.contract.ToBeAnnounceContract;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;


/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */

/**
 * Created by Administrator on 2017/3/14.
 */

@ActivityScope
public class ToBeAnnounceModel extends BaseModel<ServiceManager, CacheManager> implements
        ToBeAnnounceContract.Model {
    private static final int[] reverbNames = new int[]{
            R.string.tobeanno_reverb_recordroom, R.string.tobeanno_reverb_ktv,
            R.string.tobeanno_reverb_warmth, R.string.tobeanno_reverb_magnetism
    };
    private static final int[] inflexionNames = new int[]{
            R.string.tobeanno_inflexion_original, R.string.tobeanno_inflexion_emusic,
            R.string.tobeanno_inflexion_croaking, R.string.tobeanno_inflexion_male,
            R.string.tobeanno_inflexion_female
    };
    private static final int[] reverbImgs = new int[]{
            R.mipmap.tobeanno_reverb_ktv, R.mipmap.tobeanno_reverb_recordroom,
            R.mipmap.tobeanno_reverb_ktv, R.mipmap.tobeanno_reverb_recordroom,
            R.mipmap.tobeanno_reverb_ktv, R.mipmap.tobeanno_reverb_recordroom,
            R.mipmap.tobeanno_reverb_ktv, R.mipmap.tobeanno_reverb_recordroom
    };
    private static final int[] inflexionImgs = new int[]{
            R.drawable.selector_bg_original, R.drawable.selector_bg_emusic,
            R.drawable.selector_bg_croaking, R.drawable.selector_bg_malevoice,
            R.drawable.selector_bg_femalevoice
    };
    private Gson mGson;
    private Application mApplication;

    @Inject
    public ToBeAnnounceModel(ServiceManager serviceManager, CacheManager cacheManager,
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
    public List<SoundReverbEntity> getReverbData() {
        List<SoundReverbEntity> reverbList = new ArrayList();
        for (int i = 0; i < reverbNames.length; i++) {
            SoundReverbEntity entity = new SoundReverbEntity();
            entity.setEffectName(mApplication.getString(reverbNames[i]));
            entity.setEffectDesc(mApplication.getString(reverbNames[i]));
            entity.setResid(reverbImgs[i]);
            reverbList.add(entity);
        }
        return reverbList;
    }

    @Override
    public List<SoundEffectEntity> getInflexionData() {
        List<SoundEffectEntity> inflexions = new ArrayList();
        for (int i = 0; i < inflexionNames.length; i++) {
            SoundEffectEntity entity = new SoundEffectEntity();
            entity.setEffectname(mApplication.getString(inflexionNames[i]));
            entity.setResid(inflexionImgs[i]);
            entity.setSelected(i == 0);
            inflexions.add(entity);
        }
        return inflexions;
    }

    @Override
    public Observable<SignCosResponse> getSignCOSParams(String userid) {
        return mServiceManager.getCommonService().signCOS(RequestParams_Maker.getSignCOS(userid));
    }
}
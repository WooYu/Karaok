package com.clicktech.snsktv.module_mine.presenter;

import android.app.Application;

import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.entity.ChrousSongs_ListResponse;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_mine.contract.Mine_MyChorusSongContract;
import com.clicktech.snsktv.module_mine.ui.adapter.Mine_MyChrousSongsListAdapter;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.clicktech.snsktv.rxerrorhandler.handler.ErrorHandleSubscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */


/**
 * Created by Administrator on 2017/3/23.
 */

@ActivityScope
public class Mine_MyChorusSongPresenter extends BasePresenter<Mine_MyChorusSongContract.Model, Mine_MyChorusSongContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private List<SongInfoBean> mSingers = new ArrayList<>();
    private Mine_MyChrousSongsListAdapter mAdapter;

    @Inject
    public Mine_MyChorusSongPresenter(Mine_MyChorusSongContract.Model model, Mine_MyChorusSongContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
        mAdapter = new Mine_MyChrousSongsListAdapter(mSingers);
        mRootView.setRecyclerView(mAdapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
        this.mAdapter = null;
        this.mSingers = null;
    }

    public void requestData() {
        String userid = ((KtvApplication) mApplication).getUserID();
        mModel.getSongList(RequestParams_Maker.getMyChorusList(userid))
                .compose(RxUtils.<ChrousSongs_ListResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<ChrousSongs_ListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(ChrousSongs_ListResponse response) {
                        mSingers = response.getList();
                        mAdapter.setmInfos(mSingers);
                    }

                });
    }
}

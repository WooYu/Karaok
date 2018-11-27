package com.clicktech.snsktv.module_enter.presenter;

import android.app.Application;

import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.HomeShowResponse;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_enter.contract.PreviewWorkContract;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.clicktech.snsktv.rxerrorhandler.handler.ErrorHandleSubscriber;

import java.util.List;

import javax.inject.Inject;


@ActivityScope
public class PreviewWorkPresenter extends BasePresenter<PreviewWorkContract.Model, PreviewWorkContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private List<SongInfoBean> homeList;

    @Inject
    public PreviewWorkPresenter(PreviewWorkContract.Model model, PreviewWorkContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    // 获取首页歌曲信息
    public void requestHomeData() {
        String userid = ((KtvApplication) mApplication).getUserID();
        mModel.getHomeShowList(userid)
                .compose(RxUtils.<HomeShowResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<HomeShowResponse>(mErrorHandler) {
                    @Override
                    public void onNext(HomeShowResponse singInfo) {
                        homeList = singInfo.getWorksInfoList();
                        mRootView.setPlayList(homeList);
                    }

                });
    }

}
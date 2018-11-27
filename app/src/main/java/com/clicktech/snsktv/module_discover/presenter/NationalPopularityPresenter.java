package com.clicktech.snsktv.module_discover.presenter;

import android.app.Application;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.entity.MusicHallHomeResponse;
import com.clicktech.snsktv.entity.PopularSingerEntity;
import com.clicktech.snsktv.entity.RankingResponse;
import com.clicktech.snsktv.module_discover.contract.NationalPopularityContract;
import com.clicktech.snsktv.module_discover.ui.adapter.NationPopularAdapter;
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
 * Created by Administrator on 2017/1/20.
 */

@ActivityScope
public class NationalPopularityPresenter extends BasePresenter<NationalPopularityContract.Model,
        NationalPopularityContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private List<PopularSingerEntity> mRankList = new ArrayList<>();
    private NationPopularAdapter mRankAdapter;

    @Inject
    public NationalPopularityPresenter(NationalPopularityContract.Model model, NationalPopularityContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;

        mRankAdapter = new NationPopularAdapter(mRankList);
        mRootView.setNationPopRecyclerView(mRankAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
        this.mRankAdapter = null;
        this.mRankList = null;
    }

    public void requestData(String city) {
        int mCurPage = mApplication.getResources().getInteger(R.integer.paging_startindex);
        int mPageSize = mApplication.getResources().getInteger(R.integer.paging_size);

        mModel.getPopularList(RequestParams_Maker.getNationalPopularList(mCurPage, mPageSize, city))
                .compose(RxUtils.<RankingResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<RankingResponse>(mErrorHandler) {
                    @Override
                    public void onNext(RankingResponse response) {
                        mRankList = response.getPopularSingerInCountry();
                        mRankAdapter.setmInfos(mRankList);
                    }

                });
    }

    //获取背景图
    public void requestData_BackGround() {
        String userid = KtvApplication.ktvApplication.getUserID();
        int type = mApplication.getResources().getInteger(R.integer.wheeltype_nation);
        mModel.getBanners(userid, type)
                .compose(RxUtils.<MusicHallHomeResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<MusicHallHomeResponse>(mErrorHandler) {
                    @Override
                    public void onNext(MusicHallHomeResponse singInfo) {
                        mRootView.setTopBanners(singInfo.getCarouselFigureList());
                    }

                });
    }
}
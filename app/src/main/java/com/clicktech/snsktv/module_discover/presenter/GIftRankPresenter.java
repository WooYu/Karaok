package com.clicktech.snsktv.module_discover.presenter;

import android.app.Application;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.base.BaseResponse;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.entity.AlbumDetailResponse;
import com.clicktech.snsktv.entity.AlbumGiftListEntity;
import com.clicktech.snsktv.entity.GIftRankResponse;
import com.clicktech.snsktv.entity.GiftListResponse;
import com.clicktech.snsktv.module_discover.contract.GIftRankContract;
import com.clicktech.snsktv.module_discover.ui.adapter.GIftRankAdapter;
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
 * Created by Administrator on 2017/6/9.
 */

@ActivityScope
public class GIftRankPresenter extends BasePresenter<GIftRankContract.Model, GIftRankContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private GIftRankAdapter mGiftRankAdapter;
    private List<AlbumGiftListEntity> mRankList = new ArrayList<>();

    private String mUserID;//作者的用户id
    private String mWorksID;//单曲id
    private String mAlbumID;//专辑id
    private GiftListResponse mGiftInfo;//礼物信息

    @Inject
    public GIftRankPresenter(GIftRankContract.Model model, GIftRankContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;

        mGiftRankAdapter = new GIftRankAdapter(mRankList);
        mRootView.setRecyclerView(mGiftRankAdapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public void setData(String userid, String worksid, String albumid) {
        this.mUserID = userid;
        this.mWorksID = worksid;
        this.mAlbumID = albumid;
        requestRankDetail();
    }

    //请求排行详情
    public void requestRankDetail() {
        if (EmptyUtils.isNotEmpty(mWorksID)) {
            requestGiftRank();
        } else if (EmptyUtils.isNotEmpty(mAlbumID)) {
            requestAlbumDetail();
        }
    }

    //送花
    public void sendFlowers(String userid, int flowernum) {
        if (EmptyUtils.isNotEmpty(mAlbumID)) {
            sendingAlbumFlowers(userid, flowernum);
        } else if (EmptyUtils.isNotEmpty(mWorksID)) {
            sendingSingleFlowers(userid, flowernum);
        }
    }

    //送礼物
    public void sendGifts(String userid, String giftid, int giftnum) {
        if (EmptyUtils.isNotEmpty(mAlbumID)) {
            sendingAlbumGifts(userid, giftid, giftnum);
        } else if (EmptyUtils.isNotEmpty(mWorksID)) {
            sendingSingleGifts(userid, giftid, giftnum);
        }
    }

    //专辑-请求专辑详情
    private void requestAlbumDetail() {
        if (EmptyUtils.isEmpty(mUserID) || EmptyUtils.isEmpty(mAlbumID)) {
            return;
        }

        mModel.getAlbumDetail(RequestParams_Maker.getAlbumDetail(mUserID, mAlbumID))
                .compose(RxUtils.<AlbumDetailResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<AlbumDetailResponse>(mErrorHandler) {

                    @Override
                    public void onNext(AlbumDetailResponse response) {

                        mRootView.fillDataToView_Album(response);

                        mRankList = response.getAlbumGiftList();
                        mGiftRankAdapter.setmInfos(mRankList);
                    }

                });
    }

    //单曲-请求礼物排行榜
    private void requestGiftRank() {
        if (EmptyUtils.isEmpty(mUserID) || EmptyUtils.isEmpty(mWorksID)) {
            return;
        }

        int mCurPage = mApplication.getResources().getInteger(R.integer.paging_startindex);
        int mPageSize = mApplication.getResources().getInteger(R.integer.paging_size);

        mModel.getGiftRank(RequestParams_Maker.getRankOfGifts(mUserID, mWorksID, mCurPage, mPageSize))
                .compose(RxUtils.<GIftRankResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<GIftRankResponse>(mErrorHandler) {
                    @Override
                    public void onNext(GIftRankResponse response) {

                        mRootView.fillDataToView_Single(response);

                        mRankList = response.getWorksGiftList();
                        mGiftRankAdapter.setmInfos(mRankList);

                    }

                });
    }

    //请求礼物列表
    public void requesGiftList(String userid) {
        if (null != mGiftInfo) {
            mRootView.showPopupOfPresent(mGiftInfo);
            return;
        }
        mModel.getGiftList(RequestParams_Maker.getGistList(userid))
                .compose(RxUtils.<GiftListResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<GiftListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(GiftListResponse response) {
                        mGiftInfo = response;
                        mRootView.showPopupOfPresent(mGiftInfo);
                    }

                });
    }

    //专辑-送礼物
    private void sendingAlbumGifts(String userid, String giftid, int giftnum) {
        if (EmptyUtils.isEmpty(mAlbumID)) {
            return;
        }

        mModel.sendingAlbumGifts(RequestParams_Maker.getSendGiftsOfAlbum(userid, mAlbumID, giftid, giftnum))
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        mRootView.showMessage(mApplication.getString(R.string.tip_sendgifts));
                        mRootView.givingGifsSuccess();
                    }

                });
    }

    //专辑- 送花
    private void sendingAlbumFlowers(String userid, int flowernum) {
        if (EmptyUtils.isEmpty(mAlbumID)) {
            return;
        }

        mModel.sendingAlbumFlowers(RequestParams_Maker.getSendFlowersOfAlbum(userid, mAlbumID, flowernum))
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        mRootView.showMessage(mApplication.getString(R.string.tip_sendflowers));
                        mRootView.givingGifsSuccess();
                    }

                });
    }


    //单曲-送礼物
    private void sendingSingleGifts(String userid, String giftid, int giftnum) {
        if (EmptyUtils.isEmpty(mWorksID)) {
            return;
        }

        mModel.sendingSingleGifts(RequestParams_Maker.getSendGifts(userid, mWorksID, giftid, giftnum))
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        mRootView.showMessage(mApplication.getString(R.string.tip_sendgifts));
                        mRootView.givingGifsSuccess();
                    }

                });
    }

    //单曲 - 送花
    private void sendingSingleFlowers(String userid, int flowernum) {
        if (EmptyUtils.isEmpty(mWorksID)) {
            return;
        }

        mModel.sendingSingleFlowers(RequestParams_Maker.getSendFlowers(userid, mWorksID, flowernum))
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        mRootView.showMessage(mApplication.getString(R.string.tip_sendflowers));
                        mRootView.givingGifsSuccess();
                    }

                });
    }

}

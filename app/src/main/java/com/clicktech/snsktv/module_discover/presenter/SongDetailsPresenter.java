package com.clicktech.snsktv.module_discover.presenter;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;

import com.clicktech.snsktv.BuildConfig;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.base.BaseResponse;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.FileUtils;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.ConstantConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.entity.ArtworksDetailResponse;
import com.clicktech.snsktv.entity.GiftListResponse;
import com.clicktech.snsktv.entity.PresentBean;
import com.clicktech.snsktv.entity.ResponseAttentonCollectResponse;
import com.clicktech.snsktv.module_discover.contract.SongDetailsContract;
import com.clicktech.snsktv.module_enter.ui.activity.LoginActivity;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.clicktech.snsktv.rxerrorhandler.handler.ErrorHandleSubscriber;
import com.clicktech.snsktv.widget.dialog.SendGiftsDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */


/**
 * Created by Administrator on 2017/4/21.
 */

@ActivityScope
public class SongDetailsPresenter extends BasePresenter<SongDetailsContract.Model, SongDetailsContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private String mWorksId;
    private String mSongDetailDirs;
    private String mLyricPath;//歌词路径
    private String mUserID;
    private GiftListResponse mGiftInfo;//礼物信息

    @Inject
    public SongDetailsPresenter(SongDetailsContract.Model model, SongDetailsContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;

        //创建歌曲详情的目录
        mSongDetailDirs = FileUtils.getCacheDirectory(mApplication, Environment.DIRECTORY_MUSIC)
                + ConstantConfig.Dirs_Lyric;
        FileUtils.createOrExistsDir(mSongDetailDirs);

        mUserID = KtvApplication.ktvApplication.getUserID();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);

        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public void requestData(String workid) {
        this.mWorksId = workid;
        requestArtworkDetails();
    }

    //请求作品详情
    private void requestArtworkDetails() {
        if (EmptyUtils.isEmpty(mWorksId)) {
            return;
        }
        String userid = ((KtvApplication) mApplication).getUserID();

        mModel.listenToReleaseWorks(RequestParams_Maker.getDiscoverWorkInfo(userid, mWorksId, ""))
                .compose(RxUtils.<ArtworksDetailResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<ArtworksDetailResponse>(mErrorHandler) {
                    @Override
                    public void onNext(ArtworksDetailResponse artworksDetailResponse) {
                        if (null == artworksDetailResponse || null == artworksDetailResponse.getWorksDetail()) {
                            return;
                        }
                        mRootView.initUIInterface(artworksDetailResponse);
                        downloadLyric(artworksDetailResponse.getWorksDetail().getLyric_url());
                    }
                });
    }

    /**
     * 下载歌词
     *
     * @param lyricUrl
     */
    private void downloadLyric(String lyricUrl) {
        if (EmptyUtils.isEmpty(lyricUrl)) {
            Timber.e("歌词下载路径异常");
            return;
        }

        //如果本地存在文件就不下载
        mLyricPath = mSongDetailDirs + lyricUrl;
        Timber.d("歌词路径：" + mLyricPath);
        if (FileUtils.isFileExists(mLyricPath)) {
            mRootView.downloadTheLyrics(mLyricPath);
            return;
        }

        //歌词不存在就下载
        OkGo.<File>get(BuildConfig.APP_DOMAIN_File + lyricUrl)
                .tag(this)
                .execute(new FileCallback(mSongDetailDirs, lyricUrl) {
                    @Override
                    public void onSuccess(Response<File> response) {
                        Timber.d("歌词下载完成");
                        mRootView.downloadTheLyrics(mLyricPath);
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        Timber.e("歌词下载失败!");
                        FileUtils.deleteFile(mLyricPath);
                        if (null != mRootView) {
                            mRootView.showMessage(mApplication.getString(R.string.error_lyricdownfail));
                        }
                    }
                });
    }

    //点击收藏
    public void clickCollect(boolean collectstatus) {
        if (((KtvApplication) mApplication).loggingStatus()) {
            if (collectstatus) {
                requestCancelCollect();
            } else {
                requestAddCollect();
            }
        } else {
            mRootView.launchActivity(new Intent(mApplication, LoginActivity.class));
        }
    }

    //点击关注
    public void clickAttent(boolean attentstatus) {
        if (((KtvApplication) mApplication).loggingStatus()) {
            if (attentstatus) {
                requestCancelAttention();
            } else {
                requestAddAttention();
            }
        } else {
            mRootView.launchActivity(new Intent(mApplication, LoginActivity.class));
        }
    }

    //发布评论
    public void requestReleaseComment(String worksid, String parentid, String content) {
        if (EmptyUtils.isEmpty(worksid)) {
            return;
        }

        String userid = ((KtvApplication) mApplication).getUserID();
        if (EmptyUtils.isEmpty(content)) {
            mRootView.showMessage(mApplication.getString(R.string.error_nocomment));
        }
        mModel.addAComment(RequestParams_Maker.getUserReviews(userid, worksid, parentid, content, ""))
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        mRootView.showMessage(mApplication.getString(R.string.tip_comment));
                        requestArtworkDetails();
                    }
                });
    }

    //添加关注
    private void requestAddAttention() {
        if (EmptyUtils.isEmpty(mWorksId)) {
            return;
        }
        String userid = ((KtvApplication) mApplication).getUserID();
        mModel.addAttention(RequestParams_Maker.getAddAndCancleAttention(userid, mWorksId))
                .compose(RxUtils.<ResponseAttentonCollectResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<ResponseAttentonCollectResponse>(mErrorHandler) {
                    @Override
                    public void onNext(ResponseAttentonCollectResponse response) {
                        mRootView.showMessage(mApplication.getString(R.string.tip_attent));
                        mRootView.updateStatusOfAttention(true);
                    }

                });
    }

    //取消关注
    private void requestCancelAttention() {
        if (EmptyUtils.isEmpty(mWorksId)) {
            return;
        }
        String userid = ((KtvApplication) mApplication).getUserID();
        mModel.delAttention(RequestParams_Maker.getAddAndCancleAttention(userid, mWorksId))
                .compose(RxUtils.<ResponseAttentonCollectResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<ResponseAttentonCollectResponse>(mErrorHandler) {
                    @Override
                    public void onNext(ResponseAttentonCollectResponse response) {
                        mRootView.showMessage(mApplication.getString(R.string.tip_attent_cancel));
                        mRootView.updateStatusOfAttention(false);
                    }

                });
    }

    //取消收藏
    private void requestCancelCollect() {
        if (EmptyUtils.isEmpty(mWorksId)) {
            return;
        }
        String userid = ((KtvApplication) mApplication).getUserID();
        mModel.cancelWorksStore(RequestParams_Maker.getAddAndCancleCollection(userid, mWorksId,
                mApplication.getResources().getInteger(R.integer.collectworkstype_sing)))
                .compose(RxUtils.<ResponseAttentonCollectResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<ResponseAttentonCollectResponse>(mErrorHandler) {
                    @Override
                    public void onNext(ResponseAttentonCollectResponse response) {
                        mRootView.showMessage(mApplication.getString(R.string.tip_collect_cancel));
                        mRootView.updateStatusOfCollect(false);
                    }
                });
    }

    //添加收藏
    private void requestAddCollect() {
        if (EmptyUtils.isEmpty(mWorksId)) {
            return;
        }
        String userid = ((KtvApplication) mApplication).getUserID();
        mModel.addWorksStore(RequestParams_Maker.getAddAndCancleCollection(userid, mWorksId,
                mApplication.getResources().getInteger(R.integer.collectworkstype_sing)))
                .compose(RxUtils.<ResponseAttentonCollectResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<ResponseAttentonCollectResponse>(mErrorHandler) {
                    @Override
                    public void onNext(ResponseAttentonCollectResponse response) {
                        mRootView.showMessage(mApplication.getString(R.string.tip_collect));
                        mRootView.updateStatusOfCollect(true);
                    }
                });
    }

    //送礼物
    private void requestGiveGifts(String giftId, int num) {
        if (EmptyUtils.isEmpty(mWorksId)) {
            return;
        }
        mModel.givingGift(RequestParams_Maker.getSendGifts(mUserID, mWorksId, giftId, num))
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        mRootView.showMessage(mApplication.getString(R.string.tip_sendgifts));
                    }
                });
    }

    //送花
    private void requestGiveFlowers(int num) {
        if (EmptyUtils.isEmpty(mWorksId)) {
            return;
        }
        mModel.givingFlowers(RequestParams_Maker.getSendFlowers(mUserID, mWorksId, num))
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        mRootView.showMessage(mApplication.getString(R.string.tip_sendflowers));
                    }

                });
    }

    //获取礼物列表
    public void requestGiftList(final FragmentManager fragmentManager) {
        if (null != mGiftInfo) {
            showPopupOfPresent(fragmentManager, mGiftInfo);
            return;
        }
        String userid = KtvApplication.ktvApplication.getUserID();
        mModel.getGiftList(RequestParams_Maker.getGistList(userid))
                .compose(RxUtils.<GiftListResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<GiftListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(GiftListResponse response) {
                        mGiftInfo = response;
                        showPopupOfPresent(fragmentManager, response);
                    }

                });
    }

    //展示礼物弹窗
    private void showPopupOfPresent(FragmentManager fragmentManager, GiftListResponse response) {

        Bundle bundle = new Bundle();
        if (EmptyUtils.isNotEmpty(response.getGiftList())) {
            ArrayList<PresentBean> tempList = new ArrayList<>();
            tempList.addAll(response.getGiftList());
            bundle.putParcelableArrayList("giftlist", tempList);
        }
        GiftListResponse.WalletBean walletBean = response.getWallet();
        if (null != walletBean) {
            bundle.putInt("wallet_coin", walletBean.getWallet_coin());
            bundle.putInt("wallet_flower", walletBean.getWallet_flower());
        }
        SendGiftsDialog sendGiftsDialog = new SendGiftsDialog();
        sendGiftsDialog.setArguments(bundle);
        sendGiftsDialog.setOnEvent_ClickRequest(new SendGiftsDialog.OnEvent_ClickReqeust() {
            @Override
            public void buyClicked() {

            }

            @Override
            public void sendFlowersClicked(int flowernum) {
                requestGiveFlowers(flowernum);
            }

            @Override
            public void sendGiftsClicked(long giftid, int giftnum) {
                requestGiveGifts(String.valueOf(giftid), giftnum);
            }
        });
        sendGiftsDialog.show(fragmentManager, "com.clicktech.snsktv.widget.dialog.SendGiftsDialog");
    }

}

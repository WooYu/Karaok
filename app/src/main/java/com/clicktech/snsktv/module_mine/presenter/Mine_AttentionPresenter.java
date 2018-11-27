package com.clicktech.snsktv.module_mine.presenter;

import android.app.Application;
import android.widget.PopupWindow;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.base.BaseResponse;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.entity.Att_ListResponse;
import com.clicktech.snsktv.entity.MineAttentEntity;
import com.clicktech.snsktv.module_mine.contract.Mine_AttentionContract;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.clicktech.snsktv.rxerrorhandler.handler.ErrorHandleSubscriber;

import java.util.Map;

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
 * Created by Administrator on 2017/3/17.
 */

@ActivityScope
public class Mine_AttentionPresenter extends BasePresenter<Mine_AttentionContract.Model, Mine_AttentionContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;
    private PopupWindow mPopupWindow;

    private String mUserID;

    @Inject
    public Mine_AttentionPresenter(Mine_AttentionContract.Model model, Mine_AttentionContract.View rootView
            , RxErrorHandler handler, final Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;

        mUserID = ((KtvApplication) mApplication).getUserID();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    //请求关注的数据
    public void requestDataOfAttention(int page) {
        KtvApplication ktvApplication = (KtvApplication) mApplication;
        if (!ktvApplication.loggingStatus()) {
            return;
        }

        String userid = ktvApplication.getUserID();
        int pagesize = mApplication.getResources().getInteger(R.integer.paging_size);

        mModel.getMine_AttentionList(RequestParams_Maker.getMine_AttentionList(userid, page, pagesize))
                .compose(RxUtils.<Att_ListResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<Att_ListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(Att_ListResponse response) {
                        if (EmptyUtils.isEmpty(response.getList())) {
                            return;
                        }
                        mRootView.updateData(response.getList());
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        mRootView.requestCompletion();
                    }
                });
    }

    /**
     * 评论
     *
     * @param info
     */
    public void userReviews(Map<String, String> info) {
        mModel.userReviews(info)
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse singInfo) {
                        mRootView.showMessage(mApplication.getString(R.string.tip_comment));
                        mRootView.doCommentSuccess();
                    }
                });
    }

    //送花
    public void sendFlowers(MineAttentEntity attentEntity, int flowernum) {

        if (EmptyUtils.isEmpty(attentEntity.getWorkAlbum())) {
            //送作品
            sendingSingleFlowers(attentEntity.getWorkInfo().getId(), flowernum);
        } else {
            //送专辑
            sendingAlbumFlowers(attentEntity.getWorkAlbum().getId(), flowernum);
        }

    }

    //送礼物
    public void sendGifts(MineAttentEntity attentEntity, String giftid, int giftnum) {
        if (EmptyUtils.isEmpty(attentEntity.getWorkAlbum())) {
            //送作品
            sendingSingleGifts(attentEntity.getWorkInfo().getId(), giftid, giftnum);
        } else {
            //送专辑
            sendingAlbumGifts(attentEntity.getWorkAlbum().getId(), giftid, giftnum);
        }
    }

    //专辑-送礼物
    private void sendingAlbumGifts(String albumid, String giftid, int giftnum) {
        if (EmptyUtils.isEmpty(albumid)) {
            return;
        }

        mModel.sendingAlbumGifts(RequestParams_Maker.getSendGiftsOfAlbum(mUserID, albumid, giftid, giftnum))
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        mRootView.showMessage(mApplication.getString(R.string.tip_sendgifts));
                    }

                });
    }

    //专辑- 送花
    private void sendingAlbumFlowers(String albumid, int flowernum) {
        if (EmptyUtils.isEmpty(albumid)) {
            return;
        }

        mModel.sendingAlbumFlowers(RequestParams_Maker.getSendFlowersOfAlbum(mUserID, albumid, flowernum))
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        mRootView.showMessage(mApplication.getString(R.string.tip_sendflowers));
                    }

                });
    }

    //单曲-送礼物
    private void sendingSingleGifts(String workid, String giftid, int giftnum) {
        if (EmptyUtils.isEmpty(workid)) {
            return;
        }

        mModel.sendingSingleGifts(RequestParams_Maker.getSendGifts(mUserID, workid, giftid, giftnum))
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        mRootView.showMessage(mApplication.getString(R.string.tip_sendgifts));
                    }

                });
    }

    //单曲 - 送花
    private void sendingSingleFlowers(String workid, int flowernum) {
        if (EmptyUtils.isEmpty(workid)) {
            return;
        }

        mModel.sendingSingleFlowers(RequestParams_Maker.getSendFlowers(mUserID, workid, flowernum))
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        mRootView.showMessage(mApplication.getString(R.string.tip_sendflowers));
                    }

                });
    }

}

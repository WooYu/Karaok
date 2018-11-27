package com.clicktech.snsktv.module_mine.presenter;

import android.app.Application;
import android.os.Bundle;
import android.os.Message;
import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.base.BaseResponse;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.EventBusTag;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.entity.AlbumDetailResponse;
import com.clicktech.snsktv.entity.AlbumGiftListEntity;
import com.clicktech.snsktv.entity.GiftListResponse;
import com.clicktech.snsktv.entity.ResponseAttentonCollectResponse;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_mine.contract.AlbumDetailContract;
import com.clicktech.snsktv.module_mine.ui.adapter.AlbumCommentAdapter;
import com.clicktech.snsktv.module_mine.ui.adapter.AlbumGiftAdapter;
import com.clicktech.snsktv.module_mine.ui.adapter.AlbumMusicAdapter;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.clicktech.snsktv.rxerrorhandler.handler.ErrorHandleSubscriber;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;

import cn.jzvd.JZVideoPlayer;
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
 * Created by Administrator on 2017/5/24.
 */

@ActivityScope
public class AlbumDetailPresenter extends BasePresenter<AlbumDetailContract.Model, AlbumDetailContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private ArrayList<SongInfoBean> mList = new ArrayList<>();
    private ArrayList<AlbumGiftListEntity> gList = new ArrayList<>();
    private ArrayList<AlbumDetailResponse.AlbumCommentListBean> cList = new ArrayList<>();
    private AlbumMusicAdapter musicAdapter;
    private AlbumGiftAdapter giftAdapter;
    private AlbumCommentAdapter commentAdapter;
    private GiftListResponse mGiftInfo;//礼物信息
    private ArrayList<SongInfoBean> mSingleList;//存放播放列表的数据
    private int mPositionOfPlay;//存放当前播放的位置

    @Inject
    public AlbumDetailPresenter(AlbumDetailContract.Model model, AlbumDetailContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;

        musicAdapter = new AlbumMusicAdapter(mList);
        mRootView.setWorkRecyclerview(musicAdapter);
        giftAdapter = new AlbumGiftAdapter(gList);
        giftAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data, int position) {
                mRootView.turn2GiftRank();
            }
        });
        mRootView.setGiftRecyclerview(giftAdapter);
        commentAdapter = new AlbumCommentAdapter(cList);
        mRootView.setCommentRecyclerview(commentAdapter);

    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    //获取专辑详情
    public void getAlbumDetail(Map<String, String> info) {

        mModel.getAlbumDetail(info)
                .compose(RxUtils.<AlbumDetailResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<AlbumDetailResponse>(mErrorHandler) {
                    @Override
                    public void onNext(AlbumDetailResponse response) {

                        mRootView.setupToUi(response);

                        mList = response.getAlbumWorksList();
                        gList = response.getAlbumGiftList();
                        cList = response.getAlbumCommentList();

                        mRootView.showEmpetyGiftRecyclerview(EmptyUtils.isEmpty(gList));

                        musicAdapter.setmInfos(mList);
                        giftAdapter.setmInfos(gList);
                        commentAdapter.setmInfos(cList);
                    }

                });
    }

    //添加收藏
    public void addCollect(String workId) {
        String userid = ((KtvApplication) mApplication).getUserID();


        if (EmptyUtils.isEmpty(workId)) {
            Timber.e("workId is null !");
            return;
        }

        mModel.addWorksStore(RequestParams_Maker.getAddAndCancleCollection(userid, workId, 1))
                .compose(RxUtils.<ResponseAttentonCollectResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<ResponseAttentonCollectResponse>(mErrorHandler) {
                    @Override
                    public void onNext(ResponseAttentonCollectResponse response) {

                        mRootView.addCollcet(response);
                    }
                });
    }

    //取消收藏
    public void cancelCollect(String workId) {
        String userid = ((KtvApplication) mApplication).getUserID();


        if (EmptyUtils.isEmpty(workId)) {
            return;
        }

        mModel.cancelWorksStore(RequestParams_Maker.getAddAndCancleCollection(userid, workId, 0))
                .compose(RxUtils.<ResponseAttentonCollectResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<ResponseAttentonCollectResponse>(mErrorHandler) {
                    @Override
                    public void onNext(ResponseAttentonCollectResponse response) {
                        mRootView.showMessage(mApplication.getString(R.string.tip_collect_cancel));
                        mRootView.cancleCollcet(response);
                    }
                });
    }

    //用户评论
    public void saveAlbumComment(String userid, String albumid, String parentid, String commentcontent) {
        mModel.saveAlbumComment(RequestParams_Maker.getAlbumReview(userid, albumid, parentid, commentcontent))
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        mRootView.showMessage(mApplication.getString(R.string.tip_comment));
                        mRootView.userReviewsSuccess();
                    }

                });
    }

    //送礼物
    public void sendingAlbumGifts(String userid, String albumid, String giftid, int giftnum) {
        mModel.sendingAlbumGifts(RequestParams_Maker.getSendGiftsOfAlbum(userid, albumid, giftid, giftnum))
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        mRootView.showMessage(mApplication.getString(R.string.tip_sendgifts));
                        mRootView.givingGifsSuccess();
                    }

                });
    }

    //送花
    public void sendingAlbumFlowers(String userid, String albumid, int flowernum) {
        mModel.sendingAlbumFlowers(RequestParams_Maker.getSendFlowersOfAlbum(userid, albumid, flowernum))
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        mRootView.showMessage(mApplication.getString(R.string.tip_sendflowers));
                        mRootView.givingGifsSuccess();
                    }

                });
    }

    //获取礼物列表
    public void requestGiftList(String userid) {

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

    //播放全部单曲
    public void playAllSingleClicked() {
        if (EmptyUtils.isEmpty(mList)) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putInt("type", 2);
        bundle.putParcelableArrayList("songlist", mList);
        Message message = new Message();
        message.setData(bundle);
        EventBus.getDefault().post(message, EventBusTag.PLAYLIST_ADDSONG);

        if (EmptyUtils.isNotEmpty(mSingleList)) {
            mSingleList.get(mPositionOfPlay).setbPlayStatus(false);
        }
        mSingleList = new ArrayList<>();
        mSingleList.addAll(mList);
        if (EmptyUtils.isEmpty(mSingleList)) {
            return;
        }
        mPositionOfPlay = 0;
        mSingleList.get(mPositionOfPlay).setbPlayStatus(true);
        mRootView.showPlayList(mSingleList);
    }

    @Subscriber(tag = EventBusTag.PLAYLIST_DELSONG, mode = ThreadMode.MAIN)
    public void eventPlayListDeleteElement(Message message) {
        Bundle bundle = message.getData();
        int type = bundle.getInt("type");
        if (type == 1) {//删除单首
            int delPosition = bundle.getInt("position");
            int newPositionPlay;
            if (mPositionOfPlay < delPosition) {
                newPositionPlay = mPositionOfPlay;
            } else if (mPositionOfPlay == delPosition) {
                newPositionPlay = (delPosition + 1) >= mSingleList.size() ? 0 : delPosition;
            } else {
                newPositionPlay = (mPositionOfPlay - 1) < 0 ? 0 : (mPositionOfPlay - 1);
            }

            mSingleList.get(mPositionOfPlay).setbPlayStatus(false);
            mSingleList.remove(delPosition);
            if (EmptyUtils.isNotEmpty(mSingleList)) {
                mSingleList.get(newPositionPlay).setbPlayStatus(true);
                mPositionOfPlay = newPositionPlay;
            }
            mRootView.updatePlayList(mSingleList);

        } else if (type == 2) {//删除全部
            if (EmptyUtils.isNotEmpty(mSingleList)) {
                mSingleList.get(mPositionOfPlay).setbPlayStatus(false);
                mSingleList.clear();
            }
        }

        if (EmptyUtils.isEmpty(mSingleList)) {
            JZVideoPlayer.releaseAllVideos();
            EventBus.getDefault().post(new Message(), EventBusTag.MINIPLAYER_HIDE);
        }

    }

    @Subscriber(tag = EventBusTag.PLAYLIST_TOGGLESONG, mode = ThreadMode.MAIN)
    public void eventPlayListToggleSong(Message message) {
        Bundle bundle = message.getData();
        int newPositionPlay = bundle.getInt("position");
        mSingleList.get(mPositionOfPlay).setbPlayStatus(false);
        mSingleList.get(newPositionPlay).setbPlayStatus(true);
        mPositionOfPlay = newPositionPlay;
        mRootView.updatePlayList(mSingleList);
    }
}

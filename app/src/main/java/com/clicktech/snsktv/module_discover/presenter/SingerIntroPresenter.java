package com.clicktech.snsktv.module_discover.presenter;

import android.app.Application;
import android.os.Bundle;
import android.os.Message;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.EventBusTag;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.entity.AlbumBean;
import com.clicktech.snsktv.entity.OthersAlbumResponse;
import com.clicktech.snsktv.entity.ResponseAttentonCollectResponse;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.contract.SingerIntroContract;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.clicktech.snsktv.rxerrorhandler.handler.ErrorHandleSubscriber;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;

import javax.inject.Inject;

import cn.jzvd.JZVideoPlayer;


/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */


/**
 * Created by Administrator on 2017/1/22.
 */

@ActivityScope
public class SingerIntroPresenter extends BasePresenter<SingerIntroContract.Model, SingerIntroContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private ArrayList<SongInfoBean> mSingleList;
    private int mPositionOfPlay;

    @Inject
    public SingerIntroPresenter(SingerIntroContract.Model model, SingerIntroContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;

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

    //获取用户信息
    public void requestUserInfo(String userid) {
        int mCurPage = mApplication.getResources().getInteger(R.integer.paging_startindex);
        int mPageSize = mApplication.getResources().getInteger(R.integer.paging_size);
        String loginUserid = ((KtvApplication) mApplication).getUserID();

        mModel.indexOtherUser(RequestParams_Maker.getPersonInfoOfOtherUser(mCurPage, mPageSize,
                userid, loginUserid, mApplication.getResources().getInteger(R.integer.ispublish_open)
        ))
                .compose(RxUtils.<OthersAlbumResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<OthersAlbumResponse>(mErrorHandler) {

                    @Override
                    public void onNext(OthersAlbumResponse response) {
                        mRootView.setDataToTopUI(response);
                        mRootView.setAlbumPart((ArrayList<AlbumBean>) response.getAlbums());
                        mRootView.setSinglePart((ArrayList<SongInfoBean>) response.getWorks());
                    }

                });
    }

    //取消关注
    public void requestData_CancleAtten(String attentid) {
        KtvApplication ktvApplication = (KtvApplication) mApplication;
        String loginUserid = ktvApplication.getUserID();
        mModel.getCancleAtten(RequestParams_Maker.getAddAndCancleAttention(loginUserid, String.valueOf(attentid)))
                .compose(RxUtils.<ResponseAttentonCollectResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<ResponseAttentonCollectResponse>(mErrorHandler) {
                    @Override
                    public void onNext(ResponseAttentonCollectResponse response) {

                        mRootView.showMessage(mApplication.getString(R.string.tip_attent_cancel));
                        mRootView.resultOfAttent(false);
                        EventBus.getDefault().post(new Message(), EventBusTag.ANNOUNCESUCCESS);
                    }
                });
    }

    //添加关注
    public void requestData_Addtten(String attentid) {
        KtvApplication ktvApplication = (KtvApplication) mApplication;
        String loginUserid = ktvApplication.getUserID();
        mModel.getAddAtten(RequestParams_Maker.getAddAndCancleAttention(loginUserid, String.valueOf(attentid)))
                .compose(RxUtils.<ResponseAttentonCollectResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<ResponseAttentonCollectResponse>(mErrorHandler) {
                    @Override
                    public void onNext(ResponseAttentonCollectResponse response) {
                        mRootView.showMessage(mApplication.getString(R.string.tip_attent));
                        mRootView.resultOfAttent(true);
                        EventBus.getDefault().post(new Message(), EventBusTag.ANNOUNCESUCCESS);
                    }

                });
    }

    /**
     * 转换等级到图片
     *
     * @param grade
     */
    public int convertGradeToImage(int grade) {
        if (1 <= grade && grade <= 3) {
            return R.mipmap.grade_a;
        } else if (4 <= grade && grade <= 6) {
            return R.mipmap.grade_b;
        } else if (7 <= grade && grade <= 9) {
            return R.mipmap.grade_c;
        } else if (10 <= grade && grade <= 12) {
            return R.mipmap.grade_d;
        } else if (13 <= grade && grade <= 15) {
            return R.mipmap.grade_e;
        } else if (16 <= grade && grade <= 18) {
            return R.mipmap.grade_f;
        } else if (19 <= grade && grade <= 21) {
            return R.mipmap.grade_g;
        } else {
            return R.mipmap.grade_a;
        }
    }

    @Subscriber(tag = EventBusTag.PLAYLIST_ADDSONG, mode = ThreadMode.MAIN)
    public void eventPlayListAddElement(Message message) {
        Bundle bundle = message.getData();
        int type = bundle.getInt("type");
        if (type != 2) {
            return;
        }

        if (EmptyUtils.isNotEmpty(mSingleList)) {
            mSingleList.get(mPositionOfPlay).setbPlayStatus(false);
        }
        mSingleList = new ArrayList<>();
        ArrayList<SongInfoBean> songList = bundle.getParcelableArrayList("songlist");
        mSingleList.addAll(songList);
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
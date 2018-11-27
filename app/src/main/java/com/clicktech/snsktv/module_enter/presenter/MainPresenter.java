package com.clicktech.snsktv.module_enter.presenter;

import android.app.Application;
import android.os.Bundle;
import android.os.Message;

import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.EventBusTag;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.entity.VersionMessageResponse;
import com.clicktech.snsktv.module_enter.contract.MainContract;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.clicktech.snsktv.rxerrorhandler.handler.ErrorHandleSubscriber;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Map;

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
 * Created by Administrator on 2016/12/21.
 */

@ActivityScope
public class MainPresenter extends BasePresenter<MainContract.Model, MainContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;


    private ArrayList<SongInfoBean> mSongList;
    private int mPositionOfPlay = 0;

    @Inject
    public MainPresenter(MainContract.Model model, MainContract.View rootView
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

    public void getVersionMessage(Map<String, String> info) {
        mModel.getVsersionMessage(info)
                .compose(RxUtils.<VersionMessageResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<VersionMessageResponse>(mErrorHandler) {
                    @Override
                    public void onNext(VersionMessageResponse message) {
                    }
                });
    }

    @Subscriber(tag = EventBusTag.MINIPLAYER_CLOSEPROGRESS, mode = ThreadMode.MAIN)
    public void eventClosePlayProgressMonitor(Message message) {
        mRootView.stopMonitorService();
    }

    @Subscriber(tag = EventBusTag.MINIPLAYER_OPENPROGRESS, mode = ThreadMode.MAIN)
    public void eventOpenPlayProgressMonitor(Message message) {
        mRootView.openMonitorService();
    }

    @Subscriber(tag = EventBusTag.PLAYLIST_SHOW, mode = ThreadMode.MAIN)
    public void eventPlayListShow(Message message) {
        mRootView.showPlayList(mSongList);
    }

    @Subscriber(tag = EventBusTag.PLAYLIST_ADDSONG, mode = ThreadMode.MAIN)
    public void eventPlayListAddElement(Message message) {
        Bundle bundle = message.getData();
        mSongList = new ArrayList<>();

        int type = bundle.getInt("type");
        mPositionOfPlay = 0;
        if (type == 1) {//播放单首
            SongInfoBean songInfoBean = bundle.getParcelable("songinfo");
            if (null != songInfoBean) {
                songInfoBean.setbPlayStatus(true);
                mSongList.add(songInfoBean);
            }
        } else if (type == 2) {//播放全部
            ArrayList<SongInfoBean> songList = bundle.getParcelableArrayList("songlist");
            if (EmptyUtils.isNotEmpty(songList)) {
                songList.get(mPositionOfPlay).setbPlayStatus(true);
            }
            mSongList.addAll(songList);

            showMiniPlay();
        }

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
                newPositionPlay = (delPosition + 1) >= mSongList.size() ? 0 : delPosition;
            } else {
                newPositionPlay = (mPositionOfPlay - 1) < 0 ? 0 : (mPositionOfPlay - 1);
            }

            mSongList.get(mPositionOfPlay).setbPlayStatus(false);
            mSongList.remove(delPosition);
            if (EmptyUtils.isNotEmpty(mSongList)) {
                mSongList.get(newPositionPlay).setbPlayStatus(true);
                mPositionOfPlay = newPositionPlay;
            }
            mRootView.updatePlayList(mSongList);
            showMiniPlay();

        } else if (type == 2) {//删除全部
            if (EmptyUtils.isNotEmpty(mSongList)) {
                mSongList.get(mPositionOfPlay).setbPlayStatus(false);
                mSongList.clear();
            }
        }

        if (EmptyUtils.isEmpty(mSongList)) {
            JZVideoPlayer.releaseAllVideos();
            EventBus.getDefault().post(new Message(), EventBusTag.MINIPLAYER_HIDE);
        }

    }

    @Subscriber(tag = EventBusTag.PLAYLIST_TOGGLESONG, mode = ThreadMode.MAIN)
    public void eventPlayListToggleSong(Message message) {
        Bundle bundle = message.getData();
        int newPositionPlay = bundle.getInt("position");
        mSongList.get(mPositionOfPlay).setbPlayStatus(false);
        mSongList.get(newPositionPlay).setbPlayStatus(true);
        mPositionOfPlay = newPositionPlay;
        mRootView.updatePlayList(mSongList);
        showMiniPlay();
    }

    //展示迷你播放器
    private void showMiniPlay() {
        mRootView.action_playMusic(mSongList, mPositionOfPlay);
        action_playStatus(true);
    }

    //改变播放状态
    public void action_playStatus(boolean state) {
        if (EmptyUtils.isEmpty(mSongList)) {
            return;
        }
        SongInfoBean songInfoBean = mSongList.get(mPositionOfPlay);
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putParcelable("songinfo", songInfoBean);
        bundle.putBoolean("status", state);
        bundle.putBoolean("newwork", false);
        message.setData(bundle);
        EventBus.getDefault().post(message, EventBusTag.MINIPLAYER_SHOW);
    }

}
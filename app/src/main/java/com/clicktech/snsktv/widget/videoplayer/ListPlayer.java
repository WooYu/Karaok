package com.clicktech.snsktv.widget.videoplayer;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.common.EventBusTag;

import org.simple.eventbus.EventBus;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import timber.log.Timber;

/**
 * Created by wy201 on 2018-01-18.
 * 列表中的播放器：
 * 无全屏按钮
 */

public class ListPlayer extends JZVideoPlayerStandard {

    private ClickStartBtnCallBack mListener;

    public ListPlayer(Context context) {
        super(context);
    }

    public ListPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_video_recyclerview;
    }

    @Override
    public void setUp(Object[] dataSourceObjects, int defaultUrlMapIndex, int screen, Object... objects) {
        super.setUp(dataSourceObjects, defaultUrlMapIndex, screen, objects);
    }

    @Override
    public void startVideo() {
        Timber.d(hashCode() + "startVideo() ");
        super.startVideo();
        JZVideoPlayer.setVideoImageDisplayType(JZVideoPlayer.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP);
    }

    @Override
    public void startWindowTiny() {
    }

    @Override
    public void onStatePreparing() {
        Timber.d(hashCode() + "onStatePreparing() ");
        super.onStatePreparing();
        if (null != mListener) {
            mListener.onStatePreparing();
        }
    }

    @Override
    public void onStatePlaying() {
        Timber.d(hashCode() + "onStatePlaying() ");
        super.onStatePlaying();
        openScheduleMonitoring();
        if (null != mListener) {
            mListener.onStatePlaying();
        }
    }

    @Override
    public void onStatePause() {
        Timber.d(hashCode() + "onStatePause() ");
        super.onStatePause();
        closeScheduleMonitoring();
        if (null != mListener) {
            mListener.onStatePause();
        }
    }

    @Override
    public void onStateAutoComplete() {
        Timber.d(hashCode() + "onStateAutoComplete() ");
        super.onStateAutoComplete();
        closeScheduleMonitoring();
        if (null != mListener) {
            mListener.onStateAutoComplete();
        }
    }

    @Override
    public void onStateError() {
        Timber.d(hashCode() + "onStateError() ");
        super.onStateError();
        closeScheduleMonitoring();
        if (null != mListener) {
            mListener.onStateError();
        }
    }

    @Override
    public void onCompletion() {
        Timber.d(hashCode() + "onCompletion() ");
        closeScheduleMonitoring();
        super.onCompletion();
        if (null != mListener) {
            mListener.onCompletion();
        }
    }

    //设置状态回调监听
    public void setmListener(ClickStartBtnCallBack callBack) {
        this.mListener = callBack;
    }

    //开启进度监听
    private void openScheduleMonitoring() {
        EventBus.getDefault().post(new Message(), EventBusTag.MINIPLAYER_OPENPROGRESS);
    }

    //关闭进度监听
    private void closeScheduleMonitoring() {
        EventBus.getDefault().post(new Message(), EventBusTag.MINIPLAYER_CLOSEPROGRESS);
    }

    public interface ClickStartBtnCallBack {
        void onStatePreparing();//点击播放按钮

        void onStatePlaying();//开始播放

        void onStatePause();//暂停状态

        void onStateAutoComplete();//视频播放完成状态

        void onStateError();//错误状态

        void onCompletion();//完成状态
    }
}

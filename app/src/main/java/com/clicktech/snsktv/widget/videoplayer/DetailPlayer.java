package com.clicktech.snsktv.widget.videoplayer;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.EventBusTag;
import com.clicktech.snsktv.common.KtvApplication;

import org.simple.eventbus.EventBus;

import java.util.LinkedHashMap;

import cn.jzvd.JZMediaManager;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerManager;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Administrator on 2018/1/14.
 * 作品详情的播放器
 * 特征：
 * 全屏、无UI、连续播放
 */

public class DetailPlayer extends JZVideoPlayerStandard {
    private WorkPlayerCallBack mCallBack;

    public DetailPlayer(Context context) {
        super(context);
    }

    public DetailPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_video_jucevlc;
    }

    @Override
    public void setAllControlsVisiblity(int topCon, int bottomCon, int startBtn,
                                        int loadingPro, int thumbImg, int bottomPro, int retryLayout) {
        topContainer.setVisibility(INVISIBLE);
        bottomContainer.setVisibility(INVISIBLE);
        startButton.setVisibility(INVISIBLE);
        loadingProgressBar.setVisibility(loadingPro);
        thumbImageView.setVisibility(thumbImg);
        bottomProgressBar.setVisibility(INVISIBLE);
        mRetryLayout.setVisibility(retryLayout);
    }

    @Override
    public void updateStartImage() {
    }

    @Override
    public void dissmissControlView() {
    }

    @Override
    public void startVideo() {
        super.startVideo();
        JZVideoPlayer.setVideoImageDisplayType(JZVideoPlayer.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP);
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
        if (null != mCallBack) {
            mCallBack.onStatePreparing();
        }
    }

    @Override
    public void onStateAutoComplete() {
        closeScheduleMonitoring();
        super.onStateAutoComplete();
        if (null != mCallBack) {
            mCallBack.onStateAutoComplete();
        }
    }

    @Override
    public void onStatePlaying() {
        openScheduleMonitoring();
        super.onStatePlaying();
        if (null != mCallBack) {
            mCallBack.onStatePlaying();
        }
    }

    @Override
    public void onStatePause() {
        closeScheduleMonitoring();
        super.onStatePause();
        if (null != mCallBack) {
            mCallBack.onStatePause();
        }
    }

    @Override
    public void onStateError() {
        closeScheduleMonitoring();
        super.onStateError();
        if (null != mCallBack) {
            mCallBack.onStateError();
        }
    }

    @Override
    public void onCompletion() {
        closeScheduleMonitoring();
        super.onCompletion();
        if (null != mCallBack) {
            mCallBack.onCompletion();
        }
    }

    public void setUpParams(String videoUrl, String thumbUrl, boolean connectionplay) {
        LinkedHashMap hashMap = new LinkedHashMap();
        hashMap.put(JZVideoPlayer.URL_KEY_DEFAULT, videoUrl);
        Object[] objects = new Object[2];
        objects[0] = hashMap;
        objects[1] = true;
        setUp(objects, 0, JZVideoPlayer.SCREEN_WINDOW_NORMAL, "");

        KtvApplication.ktvApplication.getAppComponent().imageLoader().loadImage(getContext(),
                GlideImageConfig.builder()
                        .url(thumbUrl)
                        .placeholder(R.mipmap.def_square_large)
                        .errorPic(R.mipmap.def_square_large)
                        .imageView(thumbImageView)
                        .build());

        if (null != JZVideoPlayerManager.getCurrentJzvd()) {
            JZVideoPlayerManager.getCurrentJzvd().textureViewContainer.removeView(JZMediaManager.textureView);

            if (connectionplay) {
                setState(JZVideoPlayerManager.getCurrentJzvd().currentState);
                addTextureView();
                JZVideoPlayerManager.setSecondFloor(this);
            } else {
                startVideo();
            }
        } else {
            startVideo();
        }
    }

    public void setVideoPlayCallBack(WorkPlayerCallBack workPlayerCallBack) {
        this.mCallBack = workPlayerCallBack;
    }

    //开启进度监听
    private void openScheduleMonitoring() {
        EventBus.getDefault().post(new Message(), EventBusTag.MINIPLAYER_OPENPROGRESS);
    }

    //关闭进度监听
    private void closeScheduleMonitoring() {
        EventBus.getDefault().post(new Message(), EventBusTag.MINIPLAYER_CLOSEPROGRESS);
    }

    public interface WorkPlayerCallBack {
        void onStatePreparing();//点击播放按钮

        void onStatePlaying();//开始播放

        void onStatePause();//暂停状态

        void onStateAutoComplete();//视频播放完成状态

        void onStateError();//错误状态

        void onCompletion();//完成状态
    }

}

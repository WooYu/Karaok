package com.clicktech.snsktv.widget.videoplayer;

import android.content.Context;
import android.util.AttributeSet;

import com.clicktech.snsktv.BuildConfig;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Administrator on 2018/1/14.
 * 预览作品的播放器
 * 特征：
 * 全屏、无UI、连续播放
 */

public class JuceVLCPlayer extends JZVideoPlayerStandard {

    private boolean bPause = false;

    public JuceVLCPlayer(Context context) {
        super(context);
    }

    public JuceVLCPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_video_jucevlc;
    }

    @Override
    public void setUp(String url, int screen, Object... objects) {
        super.setUp(url, screen, objects);
        startVideo();
        JZVideoPlayer.setVideoImageDisplayType(JZVideoPlayer.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP);
    }

    @Override
    public void setAllControlsVisiblity(int topCon, int bottomCon, int startBtn,
                                        int loadingPro, int thumbImg, int bottomPro, int retryLayout) {
        topContainer.setVisibility(INVISIBLE);
        bottomContainer.setVisibility(INVISIBLE);
        startButton.setVisibility(INVISIBLE);
        loadingProgressBar.setVisibility(INVISIBLE);
        thumbImageView.setVisibility(INVISIBLE);
        bottomProgressBar.setVisibility(INVISIBLE);
        mRetryLayout.setVisibility(INVISIBLE);
    }

    @Override
    public void onStateError() {
        super.onStateError();
        thumbImageView.setVisibility(VISIBLE);
    }

    @Override
    public void onCompletion() {
        super.onCompletion();
        bPause = true;
    }

    @Override
    public void updateStartImage() {
    }

    @Override
    public void dissmissControlView() {
    }

    public void switchNextVideo(String videoUrl, String thumbUrl) {
        onAutoCompletion();
        setUpParams(videoUrl, thumbUrl);
    }

    public void setUpParams(String videoUrl, String thumbUrl) {
        setUp(BuildConfig.APP_DOMAIN_File + videoUrl, JZVideoPlayer.SCREEN_WINDOW_NORMAL,
                "");
        KtvApplication.ktvApplication.getAppComponent().imageLoader().loadImage(getContext(),
                GlideImageConfig.builder()
                        .url(thumbUrl)
                        .placeholder(R.mipmap.def_square_large)
                        .errorPic(R.mipmap.def_square_large)
                        .imageView(thumbImageView)
                        .build());
    }

    public void onResume() {
        if (bPause) {
            startVideo();
            bPause = false;
        }
    }

}

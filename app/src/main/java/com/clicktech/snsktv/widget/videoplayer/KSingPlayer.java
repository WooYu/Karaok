package com.clicktech.snsktv.widget.videoplayer;

import android.content.Context;
import android.util.AttributeSet;

import com.clicktech.snsktv.R;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Administrator on 2018/1/14.
 * 录制作品时的播放器
 * 特征：
 * 全屏、无UI、可以暂停和播放
 */

public class KSingPlayer extends JZVideoPlayerStandard {

    public KSingPlayer(Context context) {
        super(context);
    }

    public KSingPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_video_jucevlc;
    }

    @Override
    public void setUp(String url, int screen, Object... objects) {
        super.setUp(url, screen, objects);
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
    public void updateStartImage() {
    }

    @Override
    public void dissmissControlView() {
    }

}

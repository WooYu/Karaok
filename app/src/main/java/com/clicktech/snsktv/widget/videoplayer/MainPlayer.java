package com.clicktech.snsktv.widget.videoplayer;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;

import com.clicktech.snsktv.BuildConfig;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.common.EventBusTag;
import com.clicktech.snsktv.entity.SongInfoBean;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Administrator on 2018/1/14.
 * 作品详情的播放器
 * 特征：
 * 全屏、无UI、连续播放
 */

public class MainPlayer extends JZVideoPlayerStandard {
    private WorkPlayerCallBack mCallBack;
    private ArrayList<SongInfoBean> mSongList;
    private int mPlayPosition;

    public MainPlayer(Context context) {
        super(context);
    }

    public MainPlayer(Context context, AttributeSet attrs) {
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
        turn2NextSong();
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

    public void setUpParams(ArrayList<SongInfoBean> list, int position) {
        if (EmptyUtils.isEmpty(list)) {
            return;
        }
        mSongList = list;
        mPlayPosition = position;
        SongInfoBean songInfoBean = mSongList.get(mPlayPosition);
        String workurl = BuildConfig.APP_DOMAIN_File + songInfoBean.getWorks_url();

        if (mSongList.size() == 1) {
            LinkedHashMap hashMap = new LinkedHashMap();
            hashMap.put(JZVideoPlayer.URL_KEY_DEFAULT, workurl);
            Object[] objects = new Object[2];
            objects[0] = hashMap;
            objects[1] = true;
            setUp(objects, 0, JZVideoPlayer.SCREEN_WINDOW_NORMAL, "");
            startVideo();
        } else {
            setUp(workurl, JZVideoPlayer.SCREEN_WINDOW_NORMAL, "");
        }

    }

    private void turn2NextSong() {
        if (EmptyUtils.isEmpty(mSongList)) {
            return;
        }
        int nextPosition = (mPlayPosition + 1) >= mSongList.size() ? 0 : (mPlayPosition + 1);
        String workurl = BuildConfig.APP_DOMAIN_File + mSongList.get(nextPosition).getWorks_url();
        setUp(workurl, JZVideoPlayer.SCREEN_WINDOW_NORMAL, "");
        mPlayPosition = nextPosition;
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

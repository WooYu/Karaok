package com.clicktech.snsktv.module_mine.ui.service;


import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.clicktech.snsktv.common.EventBusTag;

import org.simple.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerManager;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by wy201 on 2018-02-11.
 */

public class MiniPlayerService extends Service {

    public static final String PARAMS_TYPE = "params_type";
    public static final String PROGRESS_CURRENT = "progress_current";
    public static final String PROGRESSS_TOTAL = "progresss_total";
    private Subscription mPlayProgressSubscription;//播放进度定时器

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);//注册到事件主线
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int type = intent.getIntExtra(PARAMS_TYPE, 0);
        switch (type) {
            case 1://开启播放器进度更新
                openScheduleMonitoring();
                break;
            case 2://关闭播放器进度更新
                closeScheduleMonitoring();
                break;
            default:
                break;
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        closeScheduleMonitoring();
    }

    //开启进度监听
    private void openScheduleMonitoring() {
        if (null == mPlayProgressSubscription || mPlayProgressSubscription.isUnsubscribed()) {
            mPlayProgressSubscription =
                    Observable.interval(0, 100, TimeUnit.MILLISECONDS)
                            .onBackpressureDrop()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<Long>() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onNext(Long aLong) {
                                    JZVideoPlayer jzVideoPlayer = JZVideoPlayerManager.getCurrentJzvd();
                                    if (null == jzVideoPlayer) {
                                        return;
                                    }

                                    if (jzVideoPlayer.getCurrentPositionWhenPlaying() < 0 ||
                                            jzVideoPlayer.getCurrentPositionWhenPlaying() > jzVideoPlayer.getDuration()) {
                                        return;
                                    }

                                    Message message = new Message();
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(PROGRESS_CURRENT, (int) Math.floor(jzVideoPlayer.getCurrentPositionWhenPlaying()));
                                    bundle.putInt(PROGRESSS_TOTAL, (int) Math.floor(jzVideoPlayer.getDuration()));
                                    message.setData(bundle);
                                    EventBus.getDefault().post(message, EventBusTag.MINIPLAYER_UPDATEPROGRESS);
                                }

                            });
        }
    }

    //关闭进度监听
    private void closeScheduleMonitoring() {
        if (null != mPlayProgressSubscription && !mPlayProgressSubscription.isUnsubscribed())
            mPlayProgressSubscription.unsubscribe();
    }
}

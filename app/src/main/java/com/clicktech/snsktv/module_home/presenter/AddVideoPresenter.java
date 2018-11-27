package com.clicktech.snsktv.module_home.presenter;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.FileUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.ConstantConfig;
import com.clicktech.snsktv.entity.KSingRGVEntity;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_home.contract.AddVideoContract;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.flyco.animation.BounceEnter.BounceEnter;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;

import org.wysaid.view.CameraRecordGLSurfaceView;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import nano.karaoksound.ncKaraokSound;
import nano.karaoksound.ncKaraokSoundPlayer;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;


@ActivityScope
public class AddVideoPresenter extends BasePresenter<AddVideoContract.Model, AddVideoContract.View> {
    public static final int MSGCODE_COUNTDOWN = 0x0008;
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;
    //路径
    private String mDirOfCacheRec;//添加视频的缓存目录
    private String mPathOfRecMV;//录制的mv路径
    private String mRecordPath;//录音路径
    private SongInfoBean mSongInfo;//歌曲信息的实体
    private KSingRGVEntity mKSingRGVInfo;//mv的相关信息
    private boolean status_recordprepare;//录制是否准备好
    private ncKaraokSoundPlayer mSoundPlayer;//歌曲播放器
    private Subscription subscribe;//定时器
    private MaterialDialog mTipDialog;//提示对话框
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (null == mRootView) {
                return;
            }

            switch (msg.what) {
                case MSGCODE_COUNTDOWN:
                    int count = (int) msg.obj;
                    mRootView.updateCountDown(count);

                    if (count > 0) {
                        Message msg1 = obtainMessage();
                        msg1.what = MSGCODE_COUNTDOWN;
                        msg1.obj = count - 1;
                        sendMessageDelayed(msg1, 1000);
                    } else {
                        record_kSingAction();
                    }
                    break;
            }
        }
    };

    @Inject
    public AddVideoPresenter(AddVideoContract.Model model, AddVideoContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;

        initPath();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mTipDialog) {
            mTipDialog.superDismiss();
            mTipDialog = null;
        }

        record_endAudio();

        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    //初始化路径
    private void initPath() {
        String rootDir = FileUtils.getCacheDirectory(mApplication,
                Environment.DIRECTORY_MUSIC).getAbsolutePath()
                + ConstantConfig.Dirs_HasVideo;
        mDirOfCacheRec = rootDir + "addvideo" + File.separator;
        FileUtils.createOrDeleteOldDir(mDirOfCacheRec);

        mPathOfRecMV = mDirOfCacheRec + "rec_addmovie.mp4";

    }

    //设置数据
    public void setData(SongInfoBean singInfoBeanEntity, KSingRGVEntity kSingRGVEntity) {
        this.mSongInfo = singInfoBeanEntity;
        this.mKSingRGVInfo = kSingRGVEntity;
        this.mRecordPath = kSingRGVEntity.getRecordaudiopath();
    }

    //倒计时
    public void startCountDown() {
        Message msg = mHandler.obtainMessage();
        msg.what = MSGCODE_COUNTDOWN;
        msg.obj = mApplication.getResources().getInteger(R.integer.cantata_countdown);
        mHandler.sendMessageDelayed(msg, 1);
    }

    //K歌开始
    public void record_kSingAction() {
        record_recodMediaFile();
        record_playMediaFile();
    }

    //开始录制
    private void record_recodMediaFile() {
        CameraRecordGLSurfaceView.StartRecordingCallback startCallBack =
                new CameraRecordGLSurfaceView.StartRecordingCallback() {
                    @Override
                    public void startRecordingOver(boolean success) {
                        if (success) {
                            status_recordprepare = true;
                            Timber.d("开始录制成功");
                        } else {
                            mRootView.killMyself();
                        }
                    }
                };

        mRootView.startMVRecord(mPathOfRecMV, startCallBack, null);
    }

    //播放媒体文件
    private void record_playMediaFile() {

        if (null == mSoundPlayer) {
            mSoundPlayer = new ncKaraokSoundPlayer();
            mSoundPlayer.setup(mRecordPath);
        }
        mRootView.setMaxValueOfPlayProgress(Math.round(mSoundPlayer.getDurationSec()) * 1000);
        mSoundPlayer.start();

        openAuditionProgressListener();
    }

    //获取播放状态
    private boolean record_getPlayStatus() {
        if (null == mSoundPlayer) {
            return false;
        }

        boolean status_play = ncKaraokSound.PLAYING == mSoundPlayer.getStatus();
        Timber.tag("ncKaraokSoundRecording").d("录制状态：" + mSoundPlayer.getStatus());
        return status_play;
    }

    //获取录制的准备状态
    public boolean record_getPrepareStatus() {
        return status_recordprepare;
    }


    // 暂停/开始录音
    public void record_pauseAndPlayRecord(boolean status) {
        //音频播放器-播放暂停
        if (null == mSoundPlayer) {
            return;
        }

        if (status) {
            mSoundPlayer.pause();
        } else {
            mSoundPlayer.resume();
        }

    }

    //停止录制和播放视频
    private void record_reset() {
        //停止音频
        record_endAudio();

        //停止视频
        mRootView.stopMVRecord(new CameraRecordGLSurfaceView.EndRecordingCallback() {
            @Override
            public void endRecordingOK() {
                status_recordprepare = false;
                initPath();
                record_kSingAction();
            }
        });
    }

    //结束录音
    private void record_endAudio() {
        if (null != mSoundPlayer) {
            mSoundPlayer.end();
            mSoundPlayer = null;
        }

        closeAuditionProgressListener();
    }

    //录制结束
    public void record_kSingEnd(final boolean reset) {
        //停止音频
        record_endAudio();

        //停止视频
        mRootView.stopMVRecord(new CameraRecordGLSurfaceView.EndRecordingCallback() {
            @Override
            public void endRecordingOK() {
                status_recordprepare = false;
                if (reset) {
                    initPath();
                    record_kSingAction();
                } else {
                    mRootView.turn2ToBeAnnounce();
                }

            }
        });
    }

    // 添加试听音效的播放进度监听
    public void openAuditionProgressListener() {
        if (null == subscribe || subscribe.isUnsubscribed()) {
            subscribe = Observable.interval(0, 50, TimeUnit.MILLISECONDS)
                    .onBackpressureDrop()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Long>() {
                        @Override
                        public void onCompleted() {
                            Timber.d("进度监听：onCompleted()");
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(Long aLong) {
                            if (null == mRootView)
                                return;

                            mRootView.updatePlayProgress(Math.round(mSoundPlayer.getPositionSec()) * 1000);
                        }
                    });
        }

    }

    //关闭试听音效播放进度监听
    public void closeAuditionProgressListener() {
        if (null != subscribe && !subscribe.isUnsubscribed())
            subscribe.unsubscribe();
    }

    //界面到后台
    public void onInterfaceToBackground() {
        if (null != mHandler) {
            mHandler.removeMessages(MSGCODE_COUNTDOWN);
        }

        if (null != mSoundPlayer) {
            mSoundPlayer.end();
            mSoundPlayer = null;
        }

        closeAuditionProgressListener();
    }

    //界面到前台
    public void onInterfaceToForeground() {
        startCountDown();
    }

    // 展示提示框
    public void showTipDialog(Context context, String content, final int type) {

        mTipDialog = new MaterialDialog(context);
        mTipDialog.content(content)//
                .btnText(mApplication.getString(R.string.dialog_cancel),
                        mApplication.getString(R.string.dialog_sure))//
                .showAnim(new BounceEnter())//
                .dismissAnim(new FadeExit())//
                .show();
        mTipDialog.setCancelable(false);
        mTipDialog.setCanceledOnTouchOutside(false);

        mTipDialog.setOnBtnClickL(
                new OnBtnClickL() {//left btn click listener
                    @Override
                    public void onBtnClick() {
                        mTipDialog.dismiss();
                    }
                },
                new OnBtnClickL() {//right btn click listener
                    @Override
                    public void onBtnClick() {
                        mTipDialog.dismiss();
                        if (type == 1) {
                            record_kSingEnd(true);
                        } else {
                            mRootView.killMyself();
                        }
                    }
                }
        );

    }

}

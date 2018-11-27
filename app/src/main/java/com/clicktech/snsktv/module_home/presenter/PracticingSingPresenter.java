package com.clicktech.snsktv.module_home.presenter;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.view.Gravity;

import com.clicktech.snsktv.BuildConfig;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.FileUtils;
import com.clicktech.snsktv.arms.utils.PermissionUtil;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.ConstantConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.entity.SongInfoWithSongIDResponse;
import com.clicktech.snsktv.module_home.contract.PracticingSingContract;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.clicktech.snsktv.rxerrorhandler.handler.ErrorHandleSubscriber;
import com.flyco.animation.BounceEnter.BounceEnter;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.flyco.dialog.widget.NormalDialog;
import com.library.multimedia.lyricscontrols.parser.LrcRow;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import nano.karaoksound.ncKaraokSound;
import nano.karaoksound.ncKaraokSoundMix;
import nano.karaoksound.ncKaraokSoundRecording;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import timber.log.Timber;


@ActivityScope
public class PracticingSingPresenter extends BasePresenter<PracticingSingContract.Model,
        PracticingSingContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    //路径
    private String mDirOfPractice;//练唱缓存目录
    private String mLyricPath;//歌词保存路径
    private String mAccompanyPath;//伴奏保存路径
    private String mRecordPath;//录制路径

    //数据
    private SongInfoBean mSongInfo;
    private List<LrcRow> mLrcRowList;//歌词数据
    private MaterialDialog mTipDialog;//提示对话框

    //状态
    private boolean status_tipDialog;//提示对话框是否展示
    private boolean status_downlyric;//下载歌词的状态
    private boolean status_downaccompany;//下载伴奏的状态
    private boolean status_audio;//试听的状态

    //录制
    private ncKaraokSoundRecording mSoundRecord;//录音器
    private Subscription mPlayProgressSubscription;//播放进度定时器

    //试听
    private ncKaraokSoundMix mSoundMix;

    @Inject
    public PracticingSingPresenter(PracticingSingContract.Model model, PracticingSingContract.View rootView
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

        OkGo.getInstance().cancelTag(this);

        if (null != mTipDialog) {
            mTipDialog.superDismiss();
        }

        record_end();

        audio_end();

        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;

    }

    //初始化项目路径
    private void initPath() {
        String rootDir = FileUtils.getCacheDirectory(mApplication, Environment.DIRECTORY_MUSIC)
                + ConstantConfig.Dirs_NoVideo;
        mDirOfPractice = rootDir + "practice" + File.separator;
        FileUtils.createOrDeleteOldDir(mDirOfPractice);

        status_downaccompany = false;
        status_downlyric = false;
        status_tipDialog = false;
        status_audio = false;
    }

    //请求数据
    public void requestdata(final SongInfoBean singInfoBeanEntity) {

        PermissionUtil.recordaudio(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                mSongInfo = singInfoBeanEntity;
                request_downloadLrcData();
                request_downloadSongData();
            }

            @Override
            public void onRequestPermissionRefuse() {
                showMessage(R.string.permiss_recordaudio);
                mRootView.killMyself();
            }
        }, mRootView.getRxPermissions(), mErrorHandler);
    }

    //下载歌词
    private void request_downloadLrcData() {

        if (null == mSongInfo || EmptyUtils.isEmpty(mSongInfo.getLyric_url()))
            return;

        String lyric_url = mSongInfo.getLyric_url();
        //如果本地存在文件就不下载
        mLyricPath = FileUtils.getCacheDirectory(mApplication,
                Environment.DIRECTORY_MUSIC).getAbsolutePath() + ConstantConfig.Dirs_Lyric + lyric_url;
        if (FileUtils.isFileExists(mLyricPath)) {
            status_downlyric = true;
            mLrcRowList = mModel.parseLrc(mLyricPath);
            mRootView.updateLrc(mLrcRowList);
            record_prepared();
            return;
        }
        //歌词不存在就下载
        OkGo.<File>get(BuildConfig.APP_DOMAIN_File + lyric_url)
                .tag(this)
                .execute(new FileCallback(FileUtils.getDirName(mLyricPath), lyric_url) {
                    @Override
                    public void onSuccess(Response<File> response) {
                        Timber.d("歌词下载完成");
                        status_downlyric = true;
                        mLrcRowList = mModel.parseLrc(mLyricPath);
                        mRootView.updateLrc(mLrcRowList);
                        record_prepared();
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        Timber.e("歌词下载失败!");
                        FileUtils.deleteFile(mLyricPath);
                        showMessage(R.string.error_lyricdownfail);
                    }
                });
    }

    //下载歌曲
    private void request_downloadSongData() {

        if (null == mSongInfo)
            return;

        String original_url = EmptyUtils.isEmpty(mSongInfo.getWorks_url()) ? mSongInfo.getAccompany_url() :
                mSongInfo.getWorks_url();

        //如果本地存在文件就不下载
        mAccompanyPath = FileUtils.getCacheDirectory(mApplication,
                Environment.DIRECTORY_MUSIC).getAbsolutePath() + ConstantConfig.Dirs_Song + original_url;

        if (FileUtils.isFileExists(mAccompanyPath)) {
            status_downaccompany = true;
            record_prepared();
            return;
        }

        //歌曲不存在就下载
        mRootView.showNumberDialog();
        OkGo.<File>get(BuildConfig.APP_DOMAIN_File + original_url)
                .tag(this)
                .execute(new FileCallback(FileUtils.getDirName(mAccompanyPath), original_url) {
                    @Override
                    public void onSuccess(Response<File> response) {
                        Timber.d("歌曲下载完成");
                        status_downaccompany = true;
                        record_prepared();
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        mRootView.updateNumberDialog(progress.fraction * 100);
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        Timber.e("歌曲下载失败!");
                        FileUtils.deleteFile(mAccompanyPath);
                        showMessage(R.string.error_downloadfailed_song);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (null != mRootView) {
                            mRootView.dismissNumberDialog();
                        }
                    }
                });
    }

    private void showMessage(int resid) {
        if (null != mApplication) {
            UiUtils.SnackbarText(mApplication.getString(resid));
        }
    }

    // 展示提示框
    public void showTipDialog(Context context, String content, final int type) {
        if (status_tipDialog) {
            return;
        }
        status_tipDialog = true;

        mTipDialog = new MaterialDialog(context);
        mTipDialog.content(content)//
                .btnText(mApplication.getString(R.string.dialog_cancel),
                        mApplication.getString(R.string.dialog_sure))//
                .showAnim(new BounceEnter())//
                .dismissAnim(new FadeExit())
                .show();
        mTipDialog.setCancelable(false);
        mTipDialog.setCanceledOnTouchOutside(false);

        mTipDialog.setOnBtnClickL(
                new OnBtnClickL() {//left btn click listener
                    @Override
                    public void onBtnClick() {
                        mTipDialog.dismiss();
                        status_tipDialog = false;
                    }
                },
                new OnBtnClickL() {//right btn click listener
                    @Override
                    public void onBtnClick() {
                        mTipDialog.dismiss();
                        status_tipDialog = false;
                        handlesPopupEvents(type);
                    }
                }
        );

    }

    //处理弹窗事件
    private void handlesPopupEvents(int type) {
        switch (type) {
            case 0://返回键
                mRootView.killMyself();
                break;
            case 1://播放 （录制完的部分播放）
                status_audio = true;
                record_kSingEnd();
                break;
            case 2://重录
                status_audio = false;
                record_resetRecord();
                break;
            case 3://试听状态下重录
                status_audio = false;
                audio_end();
                record_kSingAction();
                break;
        }
    }

    //获取录制准备状态
    public boolean getStatusOfPrepared() {
        return status_downaccompany && status_downlyric;
    }

    //获取试听状态
    public boolean getStatusOfAudio() {
        return status_audio;
    }

    //获取播放状态
    public boolean record_getPlayStatus() {
        if (null == mSoundRecord) {
            return false;
        }

        return ncKaraokSound.PLAYING == mSoundRecord.getStatus() || 1065353216 == mSoundRecord.getStatus();
    }

    //录制准备完毕
    private synchronized void record_prepared() {
        if (!getStatusOfPrepared()) {
            return;
        }

        showMessage(R.string.dialog_practice_ready);
    }

    //K歌开始
    public void record_kSingAction() {

        if (null != mSoundRecord) {
            record_pauseAndPlayRecord();
            return;
        }

        mRecordPath = mDirOfPractice + "recordvoice";
        FileUtils.createFileByDeleteOldFile(mRecordPath);
        mSoundRecord = new ncKaraokSoundRecording();
        mSoundRecord.setup(mAccompanyPath, mRecordPath);
        mSoundRecord.start();
        openAuditionProgressListener();//设置播放器进度监听
    }

    //暂停/开始录音
    private void record_pauseAndPlayRecord() {
        if (null == mSoundRecord) {
            return;
        }
        mSoundRecord.togglePlayback();
        Timber.tag("ncKaraokSoundRecording").d("录制状态：" + mSoundRecord.getStatus());
    }

    //重新录制
    private void record_resetRecord() {
        if (null != mSoundRecord) {
            mSoundRecord.restart();
        }
    }

    //结束录音
    private void record_end() {
        if (null != mSoundRecord) {
            mSoundRecord.end();
            mSoundRecord = null;
        }

        closeAuditionProgressListener();
    }

    //录制结束
    private void record_kSingEnd() {
        record_end();
        audio_start();
    }

    //录制部分试听
    private void audio_start() {
        //初始化声音合成器
        if (null == mSoundMix) {
            mSoundMix = new ncKaraokSoundMix();
            mSoundMix.setupPath(mAccompanyPath, mRecordPath + ".wav", 0);
            //初始化音量
            mSoundMix.setVolumeMusic(1.0f);
            mSoundMix.setVolumeVocal(2.0f);
            //初始化音效
            mSoundMix.setRoomEffect(0);
            mSoundMix.setVocalEffect(0);
        }
        mSoundMix.start();

        //开启播放进度监听
        openAuditionProgressListener();
    }

    //结束试听
    private void audio_end() {
        if (null != mSoundMix) {
            mSoundMix.end();
            mSoundMix = null;
        }

        closeAuditionProgressListener();
    }

    // 开启歌曲进度监听
    private void openAuditionProgressListener() {

        if (null == mPlayProgressSubscription || mPlayProgressSubscription.isUnsubscribed()) {
            mPlayProgressSubscription =
                    Observable.interval(0, 30, TimeUnit.MILLISECONDS)
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

                                    if (null != mSoundRecord) {
                                        mRootView.updatePlayProgress(mSoundRecord.getPositionSec(),
                                                mSoundRecord.getDurationSec());
                                    } else if (null != mSoundMix) {
                                        mRootView.updatePlayProgress(mSoundMix.getPositionSec(),
                                                mSoundMix.getDurationSec());
                                    }

                                }

                            });
        }
    }

    // 关闭试听音效播放进度监听
    private void closeAuditionProgressListener() {
        if (null != mPlayProgressSubscription && !mPlayProgressSubscription.isUnsubscribed())
            mPlayProgressSubscription.unsubscribe();
    }
}
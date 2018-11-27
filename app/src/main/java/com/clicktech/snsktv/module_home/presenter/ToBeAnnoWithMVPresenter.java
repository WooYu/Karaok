package com.clicktech.snsktv.module_home.presenter;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Environment;
import android.view.View;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferType;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.clicktech.snsktv.BuildConfig;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.base.BaseResponse;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.DeviceUtils;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.FileUtils;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.ConstantConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.common.net.CommonService;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.common.net.okgo.JsonCallback;
import com.clicktech.snsktv.entity.KSingRGVEntity;
import com.clicktech.snsktv.entity.PublishInforEntity;
import com.clicktech.snsktv.entity.ScoreResultEntity;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.entity.SoundEffectEntity;
import com.clicktech.snsktv.module_home.contract.ToBeAnnoWithMVContract;
import com.clicktech.snsktv.module_home.ui.adapter.SoundEffectAdapter;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.clicktech.snsktv.util.StringHelper;
import com.clicktech.snsktv.util.amazonaws.Util;
import com.coremedia.iso.boxes.Container;
import com.flyco.animation.BounceEnter.BounceEnter;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AACTrackImpl;
import com.library.multimedia.audioprocess.AudioCodec;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import cn.jzvd.JZVideoPlayer;
import nano.karaoksound.ncKaraokSound;
import nano.karaoksound.ncKaraokSoundMix;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@ActivityScope
public class ToBeAnnoWithMVPresenter extends BasePresenter<ToBeAnnoWithMVContract.Model,
        ToBeAnnoWithMVContract.View> {

    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;
    private SongInfoBean mSingInfoBean;//歌曲信息
    private KSingRGVEntity mkSingRGVEntity;//录制信息
    private ScoreResultEntity mScoreResult;//打分结果
    private MaterialDialog mTipDialog;
    //音效
    private SoundEffectAdapter mInflexionAdapter;
    private List<SoundEffectEntity> mInflexionList;
    private ncKaraokSoundMix mSoundMix;
    private Subscription subscribe;//定时器
    private int mVoiceOffset = 0;//声音偏移
    //路径
    private String toannounceDir;
    private String mSoundMixPath;//声音合成目录
    private String mPulishWorkPath;//发布作品路径

    //发布
    private PublishInforEntity publishInforEntity;//发布或者保存时作品信息
    private boolean mSaveActions;//发布或者保存

    //亚马逊
    private int idOfTransfer;//亚马逊传输的id
    private TransferUtility transferUtility;//亚马逊传输实体

    @Inject
    public ToBeAnnoWithMVPresenter(ToBeAnnoWithMVContract.Model model, ToBeAnnoWithMVContract.View rootView
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

        if (null != transferUtility) {
            transferUtility.cancelAllWithType(TransferType.UPLOAD);
        }

        OkGo.getInstance().cancelTag(this);

        //关闭播放进度监听
        closeAuditionProgressListener();

        if (null != mTipDialog) {
            mTipDialog.superDismiss();
            mTipDialog = null;
        }

        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
        this.mInflexionAdapter = null;
        this.mInflexionList = null;

    }

    private void initPath() {
        toannounceDir = FileUtils.getCacheDirectory(mApplication,
                Environment.DIRECTORY_MUSIC).getAbsolutePath() + ConstantConfig.Dirs_HasVideo +
                "dispose" + File.separator;
        FileUtils.createOrDeleteOldDir(toannounceDir);
    }

    public void requestData(SongInfoBean singInfoBeanEntity, KSingRGVEntity kSingRGVEntity,
                            ScoreResultEntity scoreResult) {
        this.mSingInfoBean = singInfoBeanEntity;
        this.mkSingRGVEntity = kSingRGVEntity;
        this.mScoreResult = scoreResult;

        requestEffectData();
        audition_start();
    }

    // 获取音效数据
    private void requestEffectData() {
        //设置混响效果
        mRootView.setReverbList(mModel.getReverbData());
        mInflexionList = mModel.getInflexionData();
        mInflexionAdapter = new SoundEffectAdapter(mInflexionList);

        //点击变音
        mInflexionAdapter.setOnItemClickListener(
                new DefaultAdapter.OnRecyclerViewItemClickListener<SoundEffectEntity>() {
                    @Override
                    public void onItemClick(View view, SoundEffectEntity data, int position) {
                        audition_switchInflexionEffect(position);
                    }
                });
        mRootView.setInflexionAdapter(mInflexionAdapter);
    }

    //开始试听
    private void audition_start() {
        if (null == mSoundMix) {
            mSoundMix = new ncKaraokSoundMix();
            String musicpath = EmptyUtils.isNotEmpty(mkSingRGVEntity.getAccompanypath()) ?
                    mkSingRGVEntity.getAccompanypath() : mkSingRGVEntity.getChorusvideopath();
            mSoundMix.setupPath(musicpath,
                    mkSingRGVEntity.getRecordaudiopath(), mVoiceOffset);
        }

        //初始化音量
        mSoundMix.setVolumeMusic(1.0f);
        mSoundMix.setVolumeVocal(2.0f);

        //初始化音效
        mSoundMix.setRoomEffect(0);
        mSoundMix.setVocalEffect(0);

        //初始化总时长
        Timber.tag("ncKaraokSoundMix").d("录音文件时长：" + mSoundMix.getDurationSec());
        mRootView.setMaxValueOfPlayProgress(Math.round(mSoundMix.getDurationSec() * 1000));

        mSoundMix.start();

        //开启播放进度监听
        openAuditionProgressListener();
    }

    //结束试听
    private void audition_finish() {
        if (null != mSoundMix) {
            mSoundMixPath = toannounceDir + "out_mix_" + System.currentTimeMillis() + ".mp3";
            mSoundMix.write(mSoundMixPath);
            mSoundMix.end();
            mSoundMix = null;
        }

        closeAuditionProgressListener();

        JZVideoPlayer.goOnPlayOnPause();
    }

    //切换混响音效
    public void audition_switchReverbEffect(int position) {
        if (null != mSoundMix) {
            mSoundMix.setRoomEffect(position);
        }
    }

    // 切换变音效果
    private void audition_switchInflexionEffect(int position) {
        for (int i = 0; i < mInflexionList.size(); i++) {
            mInflexionList.get(i).setSelected(i == position);
        }
        mInflexionAdapter.setmInfos(mInflexionList);

        if (null == mSoundMix)
            return;

        mSoundMix.setVocalEffect(position);
    }

    //播放、暂停
    public void audio_pauseAndPlay() {
        //音频播放暂停
        if (null == mSoundMix) {
            return;
        }

        mSoundMix.togglePlayback();

        //视频播放暂停
        if (audition_getPlayStatus()) {
            JZVideoPlayer.goOnPlayOnPause();
        } else {
            JZVideoPlayer.goOnPlayOnResume();
        }
    }

    //切换到后台
    public void onPause() {
        if (null != mSoundMix) {
            mSoundMix.pause();
        }
        if (null != transferUtility) {
            transferUtility.pause(idOfTransfer);
        }

        try {
            JZVideoPlayer.goOnPlayOnPause();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //切换到前台
    public void onResume() {
        if (null != mSoundMix) {
            mSoundMix.resume();
        }
        if (null != transferUtility) {
            transferUtility.resume(idOfTransfer);
        }

        try {
            JZVideoPlayer.goOnPlayOnResume();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //获取播放状态
    public boolean audition_getPlayStatus() {
        if (null == mSoundMix)
            return false;
//        Timber.tag("ncKaraokSoundMix").d("录制状态：" + mSoundMix.getStatus());
        return mSoundMix.getStatus() == ncKaraokSound.PLAYING;
    }

    //跳转到指定位置
    public void audition_jumpToSpecifiedLocation(int position) {
        if (null == mSoundMix)
            return;

        mSoundMix.setPositionSec(position);
    }

    // 设置人声音量
    public void setAuditionVolume(float volume) {
        if (null != mSoundMix) {
            Timber.tag("ncKaraokSoundMix").d("人声音量：" + mSoundMix.getVolumeVocal() + "，设置的人声音量：" + volume);
            mSoundMix.setVolumeVocal(volume);
        }
    }

    // 设置背景音乐声音
    public void setBgmusicVolume(float volume) {
        if (null != mSoundMix) {
            mSoundMix.setVolumeMusic(volume);
        }
    }

    //控制人声移动
    public void setVoiceOffset(int offset) {
        if (null != mSoundMix) {
            mSoundMix.setVocalDelay(offset);
        }
    }

    // 添加试听音效的播放进度监听
    private void openAuditionProgressListener() {
        if (null == subscribe || subscribe.isUnsubscribed()) {
            subscribe = Observable.interval(0, 50, TimeUnit.MILLISECONDS)
                    .onBackpressureDrop()
                    .compose(RxUtils.<Long>bindToLifecycle(mRootView))
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
                            if (null == mRootView || null == mSoundMix)
                                return;

                            float positionSec = mSoundMix.getPositionSec();
//                            Timber.tag("ncKaraokSoundMix").d("positionSec = " + positionSec);
                            mRootView.updatePlayProgress(positionSec);
                        }
                    });
        }

    }

    // 关闭试听音效播放进度监听
    private void closeAuditionProgressListener() {
        if (null != subscribe && !subscribe.isUnsubscribed())
            subscribe.unsubscribe();
    }

    /**
     * 发布或者保存
     * 1.结束试听
     * 2.转换音频格式
     * 3.视频和音频文件合成
     * 4.拼接参数
     * 发布：
     * 5.跳转到发布界面
     * 保存
     * 5.上传亚马逊
     * 6.上传后台服务器
     */
    private void publishAndSave(boolean saveActions) {
        mRootView.showNumberDialog();

        mSaveActions = saveActions;
        final long startTime = System.currentTimeMillis();

        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                audition_finish();
                subscriber.onNext(true);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {
                        long timeMs = System.currentTimeMillis() - startTime;
                        Timber.tag("upload").d("1.结束试听，等待耗时：" + timeMs);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Timber.tag("upload").d("发布或保存异常！");
                        if (null != mRootView) {
                            mRootView.hideNumberDialog();
                        }
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        upload_convertMp3toAac(mSoundMixPath);
                    }
                });
    }

    //转换音频格式：mp3->aac
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void upload_convertMp3toAac(String mp3path) {
        if (mSaveActions) {
            mRootView.updateNumberDialog((int) (Math.random() * 10));//0%~10%
        } else {
            mRootView.updateNumberDialog((int) (Math.random() * 30));//<0%~30%
        }

        final AudioCodec audioCodec = AudioCodec.newInstance();
        audioCodec.setEncodeType(MediaFormat.MIMETYPE_AUDIO_AAC);
        final String aacpath = toannounceDir + "temp_audio.aac";
        FileUtils.createFileByDeleteOldFile(aacpath);
        audioCodec.setIOPath(mp3path, aacpath);
        audioCodec.prepare();
        audioCodec.startAsync();
        audioCodec.setOnCompleteListener(new AudioCodec.OnCompleteListener() {
            @Override
            public void completed() {
                Timber.tag("upload").d("2.转换音频格式结束");
                audioCodec.release();
                upload_synthesisOfMVWithMp4Parser(aacpath, mkSingRGVEntity.getRecordmvpath());
            }
        });

    }

    //将视频和音频合成为mv
    private void upload_synthesisOfMVWithMp4Parser(final String inputfile_audio,
                                                   final String inputfile_video) {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                if (mSaveActions) {
                    mRootView.updateNumberDialog((int) ((Math.random() + 1) * 10));//10%~20%
                } else {
                    mRootView.updateNumberDialog((int) ((Math.random() + 5) * 10));//50%~60%
                }

                subscriber.onNext(0);
                subscriber.onCompleted();

            }
        }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .map(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) {
                        try {
                            //输出的mv路径
                            mPulishWorkPath = toannounceDir + "outfile_mv_" + System.currentTimeMillis() + ".mp4";
                            FileUtils.createFileByDeleteOldFile(mPulishWorkPath);
                            Movie countVideo = MovieCreator.build(inputfile_video);
                            AACTrackImpl mp3Track = new AACTrackImpl(new FileDataSourceImpl(inputfile_audio));
                            countVideo.addTrack(mp3Track);
                            Container out = new DefaultMp4Builder().build(countVideo);
                            FileOutputStream fos;

                            // 保存的位置
                            fos = new FileOutputStream(new File(mPulishWorkPath));
                            out.writeContainer(fos.getChannel());
                            fos.close();
                            Timber.tag("upload").d("3.视频和音频文件合成结束");
                            return 0;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return -1;
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Timber.tag("upload").e("3.视频和音频文件合成异常!");
                        if (null != mRootView) {
                            mRootView.hideNumberDialog();
                        }
                    }

                    @Override
                    public void onNext(Integer integer) {
                        if (-1 == integer) {
                            return;
                        }

                        if (((KtvApplication) mApplication).loggingStatus()) {
                            upload_stitchingParameters();
                        } else {
                            mRootView.hideNumberDialog();
                            mRootView.turn2Login();
                        }
                    }
                });
    }

    //拼接发布参数
    public void upload_stitchingParameters() {
        if (mSaveActions) {
            mRootView.updateNumberDialog((int) ((Math.random() + 2) * 10));//20%~30%
        } else {
            mRootView.updateNumberDialog((int) ((Math.random() + 8) * 10));//80%~90%
        }

        if (null == mSingInfoBean) {
            return;
        }

        String userid = ((KtvApplication) mApplication).getUserID();
        Resources resources = mApplication.getResources();
        publishInforEntity = new PublishInforEntity();
//      userId	是	long	用户ID
        publishInforEntity.setUserId(userid);
//        song_id	是	long	歌曲ID
        publishInforEntity.setSong_id(mSingInfoBean.getSong_id());
//      TODO  album_id	是	long	专辑ID
//      works_second	是	int	时长 秒
        publishInforEntity.setWorks_second(String.valueOf(mkSingRGVEntity.getRecordtime() / 1000));
//      works_size	是	int	大小 KB
        publishInforEntity.setWorks_size(FileUtils.getFileSizeNoUnit(mPulishWorkPath));
//      works_score	是	int	得分
        if (null != mScoreResult) {
            publishInforEntity.setWorks_score(String.valueOf((int) mScoreResult.getTotal_score()));
        } else {
            publishInforEntity.setWorks_score("0");
        }
//      TODO  works_level	是	String	级别 A B S SSS等
        publishInforEntity.setWorks_level("A");
//      works_name	是	String	作品名
        publishInforEntity.setWorks_name(StringHelper.getLau_With_J_U_C(
                mSingInfoBean.getSong_name_jp(), mSingInfoBean.getSong_name_us(), mSingInfoBean.getSong_name_cn()));
//      works_desc	是	String	作品描述
        publishInforEntity.setWorks_desc(mApplication.getString(R.string.announce_leaveaword_hint));
//      works_type	是	int	类型：0默认 1合唱第一人 2合唱第二人 3清唱
        publishInforEntity.setWorks_type(mkSingRGVEntity.getWorkstype());
//      ######  is_publish	是	int	私密 1私密 2发布
        publishInforEntity.setIs_publish(resources.getInteger(R.integer.ispublish_secret));
//       phone_type	是	String	手机类型
        publishInforEntity.setPhone_type(DeviceUtils.getPhoneType());
//      chorus_with	是	long	如果不是合唱传0，如果是合唱，传合唱的作品id
        if (resources.getInteger(R.integer.workstype_common) == mkSingRGVEntity.getWorkstype()
                || resources.getInteger(R.integer.workstype_chrousother) == mkSingRGVEntity.getWorkstype()) {
            publishInforEntity.setChorus_with("0");
        } else {
            publishInforEntity.setChorus_with(mSingInfoBean.getWorks_id());
        }
        //待发布作品路径
        publishInforEntity.setWorks_url(mPulishWorkPath);
        //歌曲图片
        publishInforEntity.setWorks_photo(mSingInfoBean.getSong_image());

        Timber.tag("upload").d("4.拼接参数结束");
        if (mSaveActions) {
            upload_amazonServers();
        } else {
            mRootView.hideNumberDialog();
            mRootView.turn2Announce(publishInforEntity);
        }
    }

    //亚马逊上传
    private void upload_amazonServers() {

        mRootView.updateNumberDialog((int) (Math.random() + 3) * 10);//30%~40%

        if (!FileUtils.isFileExists(mPulishWorkPath)) {
            Timber.tag("upload").e("5.上传亚马逊失败！");
            mRootView.hideNumberDialog();
            return;
        }

        transferUtility = Util.getTransferUtility(mApplication);
        final File file = new File(mPulishWorkPath);
        TransferObserver observer = transferUtility.upload(BuildConfig.BUCKET_NAME, file.getName(),
                file);
        idOfTransfer = observer.getId();
        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                Timber.tag("upload").d("onStateChanged: " + id + ", " + state);
                if (state == TransferState.COMPLETED) {
                    publishInforEntity.setWorks_url(file.getName());
                    upload_appServers();
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                Timber.tag("upload").d("onProgressChanged:" + id + "," +
                        " total:" + bytesTotal + ", current:" + bytesCurrent);
                int percentage = (int) (bytesCurrent * 100 / bytesTotal);
                if (null != mRootView) {
                    mRootView.updateNumberDialog(percentage > 40 ? percentage : 40);
                }
            }

            @Override
            public void onError(int id, Exception ex) {
                ex.printStackTrace();
                Timber.tag("upload").e("Error during upload: " + id);
                if (null != mRootView) {
                    mRootView.hideNumberDialog();
                }
            }
        });

    }

    // 调用服务器接口上传
    private void upload_appServers() {
        Timber.tag("upload").d("5.上传亚马逊结束");

        // album_id	是	long	专辑ID
        publishInforEntity.setAlbum_id("0");

        String auth = RequestParams_Maker.getAuthString();
        String sign = RequestParams_Maker.getSignString(auth);
        String info = ((KtvApplication) mApplication).getAppComponent().gson().toJson(publishInforEntity);
        Timber.d("info = " + info);

        PostRequest<BaseResponse> postRequest = OkGo.<BaseResponse>post(BuildConfig.APP_DOMAIN + CommonService.PUBLISHWORKS)
                .tag(this)
                .params("auth", auth)
                .params("sign", sign)
                .params("info", info);

        if (EmptyUtils.isNotEmpty(publishInforEntity.getPhoto())) {
            postRequest.params("photo", new File(publishInforEntity.getPhoto()));
        }

        postRequest.execute(new JsonCallback<BaseResponse>() {
            @Override
            public void onSuccess(Response<BaseResponse> response) {
                if (null == mRootView) {
                    return;
                }
                BaseResponse baseResponse = response.body();
                if (baseResponse.getErrCode() == 0) {
                    mRootView.turn2MainActivity();
                } else {
                    mRootView.showMessage(baseResponse.getMsg());
                }
            }

            @Override
            public void onError(Response<BaseResponse> response) {
                super.onError(response);
                response.getException().printStackTrace();
                Timber.tag("upload").e("6.上传服务器后台异常！");
                if (null != mRootView) {
                    mRootView.hideNumberDialog();
                }

            }

            @Override
            public void onFinish() {
                super.onFinish();
                Timber.tag("upload").d("6.上传服务器后台结束");
                mRootView.hideNumberDialog();
            }
        });

    }

    //展示提示框
    public void showTipDialog(Context context, String content, final int type) {

        mTipDialog = new MaterialDialog(context);
        mTipDialog.content(content)//
                .btnText(mApplication.getString(R.string.dialog_cancel),
                        mApplication.getString(R.string.dialog_sure))//
                .showAnim(new BounceEnter())//
                .dismissAnim(new FadeExit())//
                .show();

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
            case 1://重录
                mRootView.resetRecord();
                break;
            case 2://发布
                publishAndSave(false);
                break;
            case 3://保存
                publishAndSave(true);
                break;
        }

    }

}

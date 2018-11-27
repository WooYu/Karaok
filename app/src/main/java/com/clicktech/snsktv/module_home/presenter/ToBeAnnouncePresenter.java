package com.clicktech.snsktv.module_home.presenter;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
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
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.ConstantConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.common.net.CommonService;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.common.net.okgo.JsonCallback;
import com.clicktech.snsktv.entity.PublishInforEntity;
import com.clicktech.snsktv.entity.ScoreResultEntity;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.entity.SoundEffectEntity;
import com.clicktech.snsktv.module_home.contract.ToBeAnnounceContract;
import com.clicktech.snsktv.module_home.ui.adapter.SoundEffectAdapter;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.clicktech.snsktv.util.StringHelper;
import com.clicktech.snsktv.util.amazonaws.Util;
import com.flyco.animation.BounceEnter.BounceEnter;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import nano.karaoksound.ncKaraokSound;
import nano.karaoksound.ncKaraokSoundMix;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@ActivityScope
public class ToBeAnnouncePresenter extends BasePresenter<ToBeAnnounceContract.Model,
        ToBeAnnounceContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private SongInfoBean mSingInfoBean;//歌曲信息
    private ScoreResultEntity mScoreResult;//打分结果
    private int mWorkstype;//参与者类型
    private float mTimeOfWork;//作品时长

    //音效
    private SoundEffectAdapter mInflexionAdapter;//变音Adapter
    private List<SoundEffectEntity> mInflexionList;//变音集合
    private ncKaraokSoundMix mSoundMix;
    private int mVoiceOffset = 0;//声音移动
    private Subscription subscribe;//定时器

    //路径
    private String mRecordPath;//录音路径
    private String mAccomPath;//伴奏路径
    private String toannounceDir;//歌曲编辑目录
    private String mSoundMixPath;//声音合成目录

    //发布或者保存
    private boolean mSaveActions;//发布或者保存
    private PublishInforEntity publishInforEntity;//发布或者保存时作品信息
    private int idOfTransfer;//亚马逊传输的id
    private TransferUtility transferUtility;//亚马逊传输实体

    private MaterialDialog mTipDialog;//提示弹窗

    @Inject
    public ToBeAnnouncePresenter(ToBeAnnounceContract.Model model, ToBeAnnounceContract.View rootView
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

        closeAuditionProgressListener();

        if (null != transferUtility) {
            transferUtility.cancelAllWithType(TransferType.UPLOAD);
        }

        if (null != mTipDialog) {
            mTipDialog.superDismiss();
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
                Environment.DIRECTORY_MUSIC).getAbsolutePath() + ConstantConfig.Dirs_NoVideo +
                "dispose" + File.separator;
        FileUtils.createOrDeleteOldDir(toannounceDir);
    }

    public void setData(SongInfoBean singInfoBeanEntity, ScoreResultEntity scoreResultEntity,
                        int workstype, String recordpath, String accompanypath) {
        this.mSingInfoBean = singInfoBeanEntity;
        this.mScoreResult = scoreResultEntity;
        this.mWorkstype = workstype;
        this.mRecordPath = recordpath;
        this.mAccomPath = accompanypath;

        requestEffectData();
        startAudition();
    }


    /**
     * 获取混响和变音的效果
     */
    private void requestEffectData() {

        //设置混响效果
        mRootView.setReverbList(mModel.getReverbData());

        //设置变音效果
        mInflexionList = mModel.getInflexionData();
        mInflexionAdapter = new SoundEffectAdapter(mInflexionList);

        //点击变音
        mInflexionAdapter.setOnItemClickListener(
                new DefaultAdapter.OnRecyclerViewItemClickListener<SoundEffectEntity>() {
                    @Override
                    public void onItemClick(View view, SoundEffectEntity data, int position) {
                        switchInflexionEffect(position);
                    }
                });
        mRootView.setInflexionAdapter(mInflexionAdapter);
    }

    //开始试听
    private void startAudition() {
        //初始化声音合成器
        if (null == mSoundMix) {
            mSoundMix = new ncKaraokSoundMix();
            mSoundMix.setupPath(mAccomPath, mRecordPath, mVoiceOffset);
        }

        //初始化音量
        mSoundMix.setVolumeMusic(1.0f);
        mSoundMix.setVolumeVocal(2.0f);
        //初始化音效
        mSoundMix.setRoomEffect(0);
        mSoundMix.setVocalEffect(0);
        //初始化录音时长
        mTimeOfWork = mSoundMix.getDurationSec();
        Timber.tag("ncKaraokSoundMix").d("录音文件时长：" + mTimeOfWork);
        mRootView.setMaxValueOfPlayProgress(Math.round(mTimeOfWork * 1000));

        mSoundMix.start();

        //开启播放进度监听
        openAuditionProgressListener();
    }

    //结束试听(子线程中进行)
    private void endAudiotion() {

        if (null != mSoundMix) {
            mSoundMixPath = toannounceDir + "out_mix_" + System.currentTimeMillis() + ".mp3";
            mSoundMix.write(mSoundMixPath);
            mSoundMix.end();
            mSoundMix = null;
        }

        closeAuditionProgressListener();
    }

    //获取播放状态
    public boolean getPlayStatus() {
        return ncKaraokSound.PLAYING == mSoundMix.getStatus();
    }

    //切换混响音效
    public void switchReverbEffect(int position) {
        if (null != mSoundMix) {
            mSoundMix.setRoomEffect(position);
        }
    }

    // 切换变音效果
    private void switchInflexionEffect(int position) {
        for (int i = 0; i < mInflexionList.size(); i++) {
            mInflexionList.get(i).setSelected(i == position);
        }
        mInflexionAdapter.setmInfos(mInflexionList);

        if (null == mSoundMix)
            return;

        mSoundMix.setVocalEffect(position);
    }

    //播放、暂停
    public void pauseAndPlayRecord() {
        if (null == mSoundMix) {
            return;
        }

        mSoundMix.togglePlayback();
    }

    public void onPause() {
        if (null != mSoundMix) {
            mSoundMix.pause();
        }

        if (null != transferUtility) {
            transferUtility.pause(idOfTransfer);
        }

    }

    public void onResume() {
        if (null != mSoundMix) {
            mSoundMix.resume();
        }
        if (null != transferUtility) {
            transferUtility.resume(idOfTransfer);
        }
    }

    //跳转到指定位置
    public void jumpToSpecifiedLocation(int position) {
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
//                            Timber.tag("ncKaraokSoundMix").d("positionsec = " + positionSec);
                            mRootView.updatePlayProgress(positionSec);
                        }
                    });
        }

    }

    //关闭试听音效播放进度监听
    private void closeAuditionProgressListener() {
        if (null != subscribe && !subscribe.isUnsubscribed())
            subscribe.unsubscribe();
    }

    //跳转之前先处理音频
    private void turn2AddVideo() {
        mRootView.showLoading();
        final long startTime = System.currentTimeMillis();
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                endAudiotion();
                subscriber.onNext(true);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        long timeMs = System.currentTimeMillis() - startTime;
                        Timber.tag("upload").d("音效处理完成，等待耗时：" + timeMs);
                        mRootView.hideLoading();
                        mRootView.turn2MVPreview(mSoundMixPath);
                    }
                });
    }

    /**
     * 发布或保存
     * 1.结束试听；关闭进度监听
     * 2.拼接上传参数
     * 发布
     * 3.跳转到发布界面
     * 保存
     * 3.上传亚马逊
     * 4.上传后台服务器
     * 5.跳转到发布界面
     */
    public void publishAndSave(boolean action_save) {
        mSaveActions = action_save;
        mRootView.showLoading();
        final long startTime = System.currentTimeMillis();
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                endAudiotion();
                subscriber.onNext(true);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {
                        long timeMs = System.currentTimeMillis() - startTime;
                        Timber.tag("upload").d("1.音效处理完成，等待耗时：" + timeMs);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.dismissNumberDialog();
                        e.printStackTrace();
                        Timber.tag("upload").d("保存作品异常！");
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        mRootView.hideLoading();
                        if (((KtvApplication) mApplication).loggingStatus()) {
                            upload_stitchingParameters();
                        } else {
                            mRootView.turn2Login();
                        }
                    }
                });

    }

    //拼接发布参数
    public void upload_stitchingParameters() {

        if (null == mSingInfoBean || EmptyUtils.isEmpty(mSoundMixPath)) {
            return;
        }

        Resources resources = mApplication.getResources();
        String userid = ((KtvApplication) mApplication).getUserID();
        publishInforEntity = new PublishInforEntity();
//      userId	是	long	用户ID
        publishInforEntity.setUserId(userid);
//      song_id	是	long	歌曲ID
        publishInforEntity.setSong_id(mSingInfoBean.getSong_id());
//      #######  album_id	是	long	专辑ID
//      works_second	是	int	时长 秒
        publishInforEntity.setWorks_second(String.valueOf(Math.round(mTimeOfWork)));
//      works_size	是	int	大小 KB
        publishInforEntity.setWorks_size(FileUtils.getFileSizeNoUnit(mSoundMixPath));
//      works_score	是	int	得分
        if (null != mScoreResult) {
            publishInforEntity.setWorks_score(String.valueOf((int) mScoreResult.getTotal_score()));
        } else {
            publishInforEntity.setWorks_score("0");
        }
//      TODO  works_level	是	String	级别 A B S SSS等
        publishInforEntity.setWorks_level("sss");
//      works_name	是	String	作品名
        publishInforEntity.setWorks_name(StringHelper.getLau_With_J_U_C(
                mSingInfoBean.getSong_name_jp(), mSingInfoBean.getSong_name_us(), mSingInfoBean.getSong_name_cn()));
//      works_desc	是	String	作品描述
        publishInforEntity.setWorks_desc(mApplication.getString(R.string.announce_leaveaword_hint));
//      works_type	是	int	类型：0普通K歌; 1合唱第一人唱A; 2合唱第一人唱B; 3合唱第二人; 4清唱K歌
        publishInforEntity.setWorks_type(mWorkstype);
//      #####  is_publish	是	int	私密 1私密 2发布
        publishInforEntity.setIs_publish(resources.getInteger(R.integer.ispublish_secret));
//      phone_type	是	String	手机类型
        publishInforEntity.setPhone_type(DeviceUtils.getPhoneType());
//      chorus_with	是	long	如果不是合唱传0，如果是合唱，传合唱的作品id
        if (resources.getInteger(R.integer.workstype_common) == mWorkstype
                || resources.getInteger(R.integer.workstype_cantata) == mWorkstype) {
            publishInforEntity.setChorus_with("0");
        } else {
            publishInforEntity.setChorus_with(mSingInfoBean.getWorks_id());
        }
        //待发布作品路径
        publishInforEntity.setWorks_url(mSoundMixPath);
        //歌曲图片
        publishInforEntity.setWorks_photo(mSingInfoBean.getSong_image());

        Timber.tag("upload").d("2.拼接上传参数结束");
        if (mSaveActions) {
            upload_amazonServer();
        } else {
            mRootView.turn2Announce(publishInforEntity);
        }

    }

    //上传亚马逊服务器
    private void upload_amazonServer() {
        mRootView.showNumberDialog();

        transferUtility = Util.getTransferUtility(mApplication);
        final File file = new File(publishInforEntity.getWorks_url());
        TransferObserver observer = transferUtility.upload(BuildConfig.BUCKET_NAME, file.getName(),
                file);
        idOfTransfer = observer.getId();
        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                Timber.tag("upload").d(TAG, "onStateChanged: " + id + ", " + state);
                if(state == TransferState.COMPLETED){
                    publishInforEntity.setWorks_url(file.getName());
                    upload_appServer();
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                Timber.tag("upload").d("onProgressChanged:" + id + "," +
                        " total:" + bytesTotal + ", current:" + bytesCurrent);
                int percentage = (int) (bytesCurrent * 100 / bytesTotal);
                if (null != mRootView) {
                    mRootView.updateNumberDialog(percentage);
                }
            }

            @Override
            public void onError(int id, Exception ex) {
                ex.printStackTrace();
                Timber.tag("upload").e("Error during upload: " + id);
                if (null != mRootView) {
                    mRootView.dismissNumberDialog();
                }
            }

        });

    }

    //上传到后台
    private  void upload_appServer() {

        Timber.tag("upload").d("3.上传亚马逊结束");
        if (null != mRootView) {
            mRootView.dismissNumberDialog();
        }

        // album_id	是	long	专辑ID
        publishInforEntity.setAlbum_id("0");

        String auth = RequestParams_Maker.getAuthString();
        String sign = RequestParams_Maker.getSignString(auth);
        String info = ((KtvApplication) mApplication).getAppComponent().gson().toJson(publishInforEntity);
        Timber.tag("upload").d("info = " + info);

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
                Timber.tag("upload").e("4.上传后台服务器异常！");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                Timber.tag("upload").d("4.上传后台服务器");
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
            case 0://重新录制
                mRootView.turn2PrevActivity();
                break;
            case 1://加视频
                turn2AddVideo();
                break;
            case 2://发布
                publishAndSave(false);
                break;
            case 3://保存
                if (mWorkstype == mApplication.getResources().getInteger(R.integer.workstype_cantata)) {
                    mRootView.showEditSongNameDialog();
                } else {
                    publishAndSave(true);
                }
                break;
            case 4:
                mRootView.killMyself();
                break;
        }
    }

}

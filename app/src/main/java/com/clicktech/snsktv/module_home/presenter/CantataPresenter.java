package com.clicktech.snsktv.module_home.presenter;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.FileUtils;
import com.clicktech.snsktv.arms.utils.PermissionUtil;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.ConstantConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.entity.UserInfoBean;
import com.clicktech.snsktv.module_home.contract.CantataContract;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.flyco.animation.BounceEnter.BounceEnter;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.flyco.dialog.widget.popup.BubblePopup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import nano.karaoksound.ncKaraokSound;
import nano.karaoksound.ncKaraokSoundRecording;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */


/**
 * Created by Administrator on 2017-06-30.
 */

@ActivityScope
public class CantataPresenter extends BasePresenter<CantataContract.Model, CantataContract.View> {
    public static final int MSGCODE_COUNTDOWN = 0x0002;
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;
    private String mDirOfCantata;//清唱目录
    private String mCantataRecordPath;//清唱保存路径
    private String mAccompanypath;//伴奏路径
    private ncKaraokSoundRecording mSoundRecord;
    private Subscription mPlayProgressSubscription;//定时器
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
                        startRecording();
                    }
                    break;
            }
        }
    };
    private MaterialDialog mTipDialog;

    @Inject
    public CantataPresenter(CantataContract.Model model, CantataContract.View rootView
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
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;

        if (null != mTipDialog) {
            mTipDialog.superDismiss();
        }

        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * 初始化项目路径
     */
    private void initPath() {
        String rootDir = FileUtils.getCacheDirectory(mApplication, Environment.DIRECTORY_MUSIC).getAbsolutePath()
                + ConstantConfig.Dirs_NoVideo;
        mDirOfCantata = rootDir + "cantata" + File.separator;
        FileUtils.createOrDeleteOldDir(mDirOfCantata);
    }

    /**
     * 检查权限
     */
    public void examinePermission() {
        PermissionUtil.recordaudio(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                startCountDown();
            }

            @Override
            public void onRequestPermissionRefuse() {
                mRootView.showMessage(mApplication.getString(R.string.permiss_recordaudio));
                mRootView.killMyself();
            }
        }, mRootView.getRxPermissions(), mErrorHandler);
    }

    //倒计时
    public void startCountDown() {
        Message msg = mHandler.obtainMessage();
        msg.what = MSGCODE_COUNTDOWN;
        msg.obj = mApplication.getResources().getInteger(R.integer.cantata_countdown);
        mHandler.sendMessageDelayed(msg, 1);
    }

    /**
     * 开始录音
     */
    private void startRecording() {
        mCantataRecordPath = mDirOfCantata + "recordvoice";
        FileUtils.createFileByDeleteOldFile(mCantataRecordPath);

        mAccompanypath = mDirOfCantata + "cantata_accompany.mp3";
        FileUtils.createFileByDeleteOldFile(mAccompanypath);
        try {
            InputStream is = mApplication.getAssets().open("10min_silent.mp3");
            FileOutputStream fos = new FileOutputStream(new File(mAccompanypath));
            byte[] buffer = new byte[1024];
            int byteCount = 0;
            while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取
                // buffer字节
                fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
            }
            fos.flush();// 刷新缓冲区
            is.close();
            fos.close();

            mSoundRecord = new ncKaraokSoundRecording();
            mSoundRecord.setup(mAccompanypath, mCantataRecordPath);
            mSoundRecord.start();
            openAuditionProgressListener();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    /**
     * 开启歌曲进度监听
     */
    private void openAuditionProgressListener() {
        if (null == mPlayProgressSubscription || mPlayProgressSubscription.isUnsubscribed()) {
            mPlayProgressSubscription =
                    Observable.interval(0, 200, TimeUnit.MILLISECONDS)
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

                                    mRootView.updateRecodingTime(Math.round(mSoundRecord.getPositionSec() * 1000));
                                }
                            });
        }
    }

    /**
     * 关闭试听音效播放进度监听
     */
    private void closeAuditionProgressListener() {
        if (null != mPlayProgressSubscription && !mPlayProgressSubscription.isUnsubscribed())
            mPlayProgressSubscription.unsubscribe();
    }

    /**
     * 播放、暂停
     */
    public void playPauseRecording() {
        if (null == mSoundRecord) {
            return;
        }
        mSoundRecord.togglePlayback();
        Timber.tag("ncKaraokSoundRecording").d("录制状态：" + mSoundRecord.getStatus());
        mRootView.updateReordingStatus(ncKaraokSound.PLAYING == mSoundRecord.getStatus());
    }

    /**
     * 重新录制
     */
    private void resetRecording() {
        if (null == mSoundRecord) {
            return;
        }
        mSoundRecord.restart();
    }

    /**
     * 停止录音
     */
    public void stopRecording() {
        if (null == mSoundRecord) {
            return;
        }
        mSoundRecord.end();
        closeAuditionProgressListener();
        mRootView.turn2Publish(mCantataRecordPath + ".wav", mAccompanypath);
    }

    //暂停录制
    public void onPauseRecord() {
        if (null == mSoundRecord) {
            return;
        }
        mSoundRecord.pause();
    }

    //继续录制
    public void onResumeRecord() {
        if (null == mSoundRecord) {
            return;
        }
        mSoundRecord.resume();
    }

    //设置升降调
    public void setRisingFallingTone(Context context, View anchorView) {
        if (null == mSoundRecord) {
            return;
        }

        View inflate = View.inflate(mApplication, R.layout.layout_ksong_tone, null);
        ImageView ivToneAdd = (ImageView) inflate.findViewById(R.id.iv_tone_add);
        ImageView ivToneDel = (ImageView) inflate.findViewById(R.id.iv_tone_del);
        final TextView tvTone = (TextView) inflate.findViewById(R.id.tv_tone);
        tvTone.setText(String.valueOf(getRisingFallingTone()));
        ivToneAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tone = getRisingFallingTone();
                if (tone > 1) {
                    return;
                }
                mSoundRecord.setPitchShift(tone + 1);
                tvTone.setText(String.valueOf(tone + 1));
            }
        });
        ivToneDel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int tone = getRisingFallingTone();
                if (tone == 0) {
                    return;
                }
                mSoundRecord.setPitchShift(tone - 1);
                tvTone.setText(String.valueOf(tone - 1));
            }
        });
        new BubblePopup(context, inflate)
                .anchorView(anchorView)
                .showAnim(null)
                .dismissAnim(null)
                .show();
    }

    //获取升降调
    public int getRisingFallingTone() {
        if (null == mSoundRecord) {
            return 0;
        }

        float pitchshift = mSoundRecord.getPitchShift();
        System.out.println(pitchshift);

        return (int) pitchshift;
    }

    //创建歌曲信息
    public SongInfoBean getSongInfoBean() {
        UserInfoBean userInfoBean = KtvApplication.ktvApplication.getUserInfoBean();
        SongInfoBean songInfoBean = new SongInfoBean();
        songInfoBean.setSong_id("0");
        songInfoBean.setSinger_name_cn(userInfoBean.getUser_realname());
        songInfoBean.setSinger_name_jp(userInfoBean.getUser_realname());
        songInfoBean.setSinger_name_us(userInfoBean.getUser_realname());
        songInfoBean.setUser_photo(userInfoBean.getUser_photo());
        songInfoBean.setUser_nickname(userInfoBean.getUser_nickname());
        return songInfoBean;
    }

    /**
     * 展示提示框
     *
     * @param context
     * @param content
     * @param type
     */
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
                        cantataOperationType(type);
                    }
                }
        );

    }

    /**
     * 清唱操作类型：暂停/播放、追加视频、重录、录制完成
     *
     * @param type
     */
    private void cantataOperationType(int type) {
        switch (type) {
            case 0://暂停播放
                playPauseRecording();
                break;
            case 1://
                break;
            case 2://重唱
                resetRecording();
                break;
            case 3://录制完成
                stopRecording();
                break;
            case 4://点击返回键
                mRootView.killMyself();
                break;
            default:
                break;
        }
    }

}

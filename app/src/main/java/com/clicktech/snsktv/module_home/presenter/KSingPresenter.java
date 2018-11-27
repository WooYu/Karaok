package com.clicktech.snsktv.module_home.presenter;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.annotation.IntRange;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicktech.snsktv.BuildConfig;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.DataHelper;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.FileUtils;
import com.clicktech.snsktv.arms.utils.PermissionUtil;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.ConstantConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.AverageScoreResponse;
import com.clicktech.snsktv.entity.ScoreResultEntity;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_home.contract.KSingContract;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.clicktech.snsktv.rxerrorhandler.handler.ErrorHandleSubscriber;
import com.clicktech.snsktv.widget.dialog.SwitchModeDialog;
import com.flyco.animation.BounceEnter.BounceEnter;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.flyco.dialog.widget.popup.BubblePopup;
import com.library.multimedia.lyricscontrols.parser.LrcRow;
import com.library.multimedia.soundercontrols.GradeBean;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import nano.karaoksound.ncKaraokSound;
import nano.karaoksound.ncKaraokSoundRecording;
import nano.karaoksound.ncKaraokSoundScoring;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
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
 * Created by Administrator on 2017/3/9.
 */

@ActivityScope
public class KSingPresenter extends BasePresenter<KSingContract.Model, KSingContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private SongInfoBean mSongInfo;//歌曲信息
    private ScoreResultEntity mAverageScore;//全国平均分
    private MaterialDialog mTipDialog;//提示对话框

    //路径
    private String mDirOfRecCache;//录歌缓存目录
    private String mDirOfMid;//mid的缓存目录
    private String mRecordPath;//录音保存路径
    private String mLyricPath;//歌词保存路径
    private String mAccompanyPath;//伴奏保存路径
    private String mMidPath;//打分文件路径

    //状态
    private boolean status_downloadmid = false;//下载mid的状态
    private boolean status_downaccompany = false;//下载伴奏的状态
    private boolean status_recordprepare = false;//录音准备状态
    private boolean status_tipDialog;//提示对话框是否展示

    //打分
    private final float OFFSET_MID = 0.2f;
    private int mStartEndPosition;//打分数据距离左右的宽度
    private int mWidthOfPitchView;//显示打分数据的宽度
    private int mNumOfPitchLine;//音调线数量
    private Subscription mPlayProgressSubscription;//播放进度定时器
    private Subscription mRealTimeScaleSubscription;//实时打分的定时器
    private ncKaraokSoundRecording mSoundRecord;//录音器
    private ncKaraokSoundScoring mSoundScore;//音准器
    private SparseArray<List<GradeBean>> mMidDataHashMap;//整首歌的mid数据
    private List<Integer> mStartNoList;//每句歌词打分开始的no
    private List<LrcRow> mLrcRowList;//歌词数据
    private int mIndexOfGrade = 0;//当前打分开始的no
    private int mCurRowsOfLyrics = 0;//当前歌词行数
    private float mSoundSizeOfMid = 0f;//mid的声音大小
    private ScoreResultEntity mCurrentScore;//总打分结果

    @Inject
    public KSingPresenter(KSingContract.Model model, KSingContract.View rootView
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
        }

        record_endAudio();

        OkGo.getInstance().cancelTag(this);

        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;

    }

    /**
     * 初始化项目路径
     */
    private void initPath() {
        String rootDirOfKSing = FileUtils.getCacheDirectory(mApplication, Environment.DIRECTORY_MUSIC)
                + ConstantConfig.Dirs_NoVideo;
        mDirOfRecCache = rootDirOfKSing + "ksing" + File.separator;
        FileUtils.createOrDeleteOldDir(mDirOfRecCache);

        mDirOfMid = FileUtils.getCacheDirectory(mApplication,
                Environment.DIRECTORY_MUSIC).getAbsolutePath()
                + ConstantConfig.Dirs_Mid;
    }

    /**
     * 请求数据
     *
     * @param singInfoBeanEntity
     */
    public void requestdata(final SongInfoBean singInfoBeanEntity) {

        PermissionUtil.recordaudio(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                mSongInfo = singInfoBeanEntity;
                request_downloadLrcData();
                request_downloadGradeMid();
                request_downloadSongData();
                request_songAverageScore();
            }

            @Override
            public void onRequestPermissionRefuse() {
                showMessage(R.string.permiss_recordaudio);
                mRootView.killMyself();
            }
        }, mRootView.getRxPermissions(), mErrorHandler);
    }

    //获取歌曲全国平均分数
    private void request_songAverageScore() {
        mModel.getSongWorksAverageScore(((KtvApplication) mApplication).getUserID(), mSongInfo.getSong_id())
                .compose(RxUtils.<AverageScoreResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<AverageScoreResponse>(mErrorHandler) {
                    @Override
                    public void onNext(AverageScoreResponse averageScoreResponse) {
                        mAverageScore = averageScoreResponse.getSongAverageScore();
                    }
                });
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
            mLrcRowList = mModel.parseLrc(mLyricPath);
            mRootView.updateLrc(mLrcRowList);
            return;
        }
        //歌词不存在就下载
        OkGo.<File>get(BuildConfig.APP_DOMAIN_File + lyric_url)
                .tag(this)
                .execute(new FileCallback(FileUtils.getDirName(mLyricPath), lyric_url) {
                    @Override
                    public void onSuccess(Response<File> response) {
                        Timber.d("歌词下载完成");
                        mLrcRowList = mModel.parseLrc(mLyricPath);
                        mRootView.updateLrc(mLrcRowList);
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
            grade_intializerEvalutionInstrument();
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
                        grade_intializerEvalutionInstrument();
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

    //下载打分mid
    private void request_downloadGradeMid() {
        if (null == mSongInfo) {
            return;
        }

        if (EmptyUtils.isNotEmpty(mSongInfo.getGrade_file_url())) {
            mMidPath = mDirOfMid + mSongInfo.getGrade_file_url();
            if (FileUtils.isFileExists(mMidPath)) {
                status_downloadmid = true;
                grade_intializerEvalutionInstrument();
                return;
            }

            //Mid不存在就下载
            OkGo.<File>get(BuildConfig.APP_DOMAIN_File + mSongInfo.getGrade_file_url())
                    .tag(this)
                    .execute(new FileCallback(FileUtils.getDirName(mMidPath), mSongInfo.getGrade_file_url()) {

                        @Override
                        public void onSuccess(Response<File> response) {
                            Timber.d("评分下载完成");
                            status_downloadmid = true;
                            grade_intializerEvalutionInstrument();
                        }

                        @Override
                        public void onError(Response<File> response) {
                            super.onError(response);
                            Timber.e("评分文件下载失败!");
                            FileUtils.deleteFile(mMidPath);
                            showMessage(R.string.error_downfile_mid);
                        }
                    });

        } else {
            mMidPath = mDirOfMid + "m365mid.mid";
            Observable.create(new Observable.OnSubscribe<Boolean>() {
                @Override
                public void call(Subscriber<? super Boolean> subscriber) {
                    subscriber.onNext(FileUtils.readInputStream(mMidPath,
                            mApplication.getResources().openRawResource(R.raw.m365mid)));
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Boolean>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Timber.e("默认评分文件下载失败!");
                            FileUtils.deleteFile(mMidPath);
                            showMessage(R.string.error_downfile_mid);
                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            if (aBoolean) {
                                status_downloadmid = true;
                                grade_intializerEvalutionInstrument();
                            }
                        }

                    });
        }

    }

    //初始化音准器
    private void grade_intializerEvalutionInstrument() {
        if (!status_downloadmid || !status_downaccompany) {
            return;
        }

        mRootView.prepareDataOfGrade();

        if (null == mSoundScore) {
            mSoundScore = new ncKaraokSoundScoring();
            mSoundScore.setup(mMidPath, ncKaraokSound.NONE);
            mSoundScore.setVolumeGuideMelody(mSoundSizeOfMid);
        }
        int count = mSoundScore.getCount();
        Timber.tag("ncKaraokSoundScoring").d("count = %s", count);

        mSoundScore.getInfo();
        if (mSoundScore.info_valid) {
            Timber.tag("ncKaraokSoundScoring").d("count = %d , part = %d,note_range_low = %d,note_range_high = %d",
                    mSoundScore.count, mSoundScore.part, mSoundScore.note_range_low, mSoundScore.note_range_high);
        }

        grade_getMidDataOfTheWholeSong();

    }

    //设置打分的数据
    public void grade_setDataToBeGraded(int x, int width, int ynum) {
        this.mStartEndPosition = x;
        this.mWidthOfPitchView = width;
        this.mNumOfPitchLine = ynum;
    }

    //获取整首歌曲的mid数据
    private void grade_getMidDataOfTheWholeSong() {
        if (EmptyUtils.isEmpty(mLrcRowList)) {
            return;
        }

        mMidDataHashMap = new SparseArray<>();
        mStartNoList = new ArrayList<>();
        for (int i = 0; i < mLrcRowList.size(); i++) {
            int numOfMid = grade_setRatingInfomation(mLrcRowList.get(i));
            int startNoteNo = mSoundScore.getStartNoteNo();
            Timber.tag("ncKaraokSoundScoring").d("getStartNoteNo() : startNoteNo = %d ", startNoteNo);
            mStartNoList.add(startNoteNo);

            List<GradeBean> gradeList = new ArrayList<>();
            for (int j = startNoteNo; j < numOfMid + startNoteNo; j++) {
                mSoundScore.getNote(j);
                if (mSoundScore.note_valid) {
//                    Timber.tag("ncKaraokSoundScoring").d("getNote: j = %d,note = %d , from_sec = %f,to_sec = %f",
//                            j, mSoundScore.note, mSoundScore.from_sec, mSoundScore.to_sec);
                    GradeBean gradebean = new GradeBean();
                    gradebean.setScore(mSoundScore.note);
                    gradebean.setStarttime(mSoundScore.from_sec * 1000);
                    gradebean.setEndtime(mSoundScore.to_sec * 1000);
                    gradebean.setNoteX1(mSoundScore.getNoteX1(j));
                    gradebean.setNoteX2(mSoundScore.getNoteX2(j));
                    gradebean.setNoteY(mSoundScore.getNoteY(j));
                    gradeList.add(gradebean);
                }
            }

            mMidDataHashMap.put(i, gradeList);
        }

        if (EmptyUtils.isEmpty(mMidDataHashMap) || EmptyUtils.isEmpty(mStartNoList)) {
            Timber.tag("ncKaraokSoundScoring").e("没有打分数据");
            return;
        }

        mRootView.initIntonationIs(mMidDataHashMap);
        record_startRecordingDelay();

    }

    //设置打分信息
    private int grade_setRatingInfomation(LrcRow lrcRow) {
        float from_sec = lrcRow.getTime() * 1.0f / 1000;
        float to_sec = (lrcRow.getTime() + lrcRow.getTotalTime()) * 1.0f / 1000;
        return mSoundScore.setInfo(0, mWidthOfPitchView, mNumOfPitchLine, from_sec, to_sec);
    }

    //实时获取音准器竖线的位置
    private void grade_getRealPositionOfVerticalBar() {
        if (null == mSoundScore) {
            return;
        }
        mSoundScore.getNote(mIndexOfGrade);
        float positionSecX = mSoundScore.getPositionSecX();
        if (positionSecX > mWidthOfPitchView) {
            Timber.tag("ncKaraokSoundRecording").d("跳转到下一句歌词");
            record_jumpToTheNextPage();
            mRootView.turn2NextLyric();
            return;
        }
        mRootView.updateIntonationIs(null, positionSecX);
    }

    //获取实时的打分数据
    private void grade_getRealTimeRatingData() {

        if (null == mSoundScore) {
            return;
        }

        mSoundScore.getResultNote(mIndexOfGrade);
        if (mSoundScore.note_valid) {
            if (mSoundScore.note == 0) {//无效note
                ++mIndexOfGrade;
                return;
            }

            Timber.tag("ncKaraokSoundScoring").d("mIndexOfGrade = %d , getCount() = %d,from_sec = %f," +
                            "to_sec = %f,getNoteX1 = %f,getNoteX2 = %f,getNoteY = %d," +
                            "note_kobushi = %b,note_vibrato = %b,note_fall = %b,note_shakuri = %b",
                    mIndexOfGrade,
                    mSoundScore.getCount(), mSoundScore.from_sec, mSoundScore.to_sec, mSoundScore.getNoteX1(mIndexOfGrade),
                    mSoundScore.getNoteX2(mIndexOfGrade), mSoundScore.getNoteY(mIndexOfGrade),
                    mSoundScore.note_kobushi,mSoundScore.note_vibrato,mSoundScore.note_fall,mSoundScore.note_shakuri);
            GradeBean gradeBean = new GradeBean();
            gradeBean.setStarttime(mSoundScore.from_sec * 1000);
            gradeBean.setEndtime(mSoundScore.to_sec * 1000);
            gradeBean.setNoteX1(mSoundScore.getNoteX1(mIndexOfGrade));
            gradeBean.setNoteX2(mSoundScore.getNoteX2(mIndexOfGrade));
            gradeBean.setNoteY(mSoundScore.resultNoteToY(mIndexOfGrade, mSoundScore.note));

            if (mSoundScore.note_kobushi) {
                gradeBean.setIndexOfIcon(1);
            } else if (mSoundScore.note_vibrato) {
                gradeBean.setIndexOfIcon(2);
            } else if (mSoundScore.note_fall) {
                gradeBean.setIndexOfIcon(3);
            } else if (mSoundScore.note_shakuri) {
                gradeBean.setIndexOfIcon(4);
            }

            //12跟音乐有关
            int realtimeNote = mSoundScore.note % 12;

            mSoundScore.getNote(mIndexOfGrade);
            int noteOfOriginal = mSoundScore.note;
            int standartNote = noteOfOriginal % 12;
            gradeBean.setMatch(standartNote == realtimeNote);
            float positionSecX = mSoundScore.getPositionSecX();
            mRootView.updateIntonationIs(gradeBean, positionSecX);
            ++mIndexOfGrade;
        }

    }

    //跳转到下一句歌词
    public void record_jumpToTheNextPage() {
        ++mCurRowsOfLyrics;
        if (EmptyUtils.isEmpty(mStartNoList) || mCurRowsOfLyrics >= mStartNoList.size()) {
            return;
        }
        mIndexOfGrade = mStartNoList.get(mCurRowsOfLyrics);
        grade_setRatingInfomation(mLrcRowList.get(mCurRowsOfLyrics));
        Timber.tag("ncKaraokSoundRecording").d("第%d句歌词开始的no是%d", mCurRowsOfLyrics, mIndexOfGrade);
    }

    //延迟一段时间后开始录音
    private void record_startRecordingDelay() {
        mIndexOfGrade = mStartNoList.get(0);
        grade_setRatingInfomation(mLrcRowList.get(0));

        mRootView.startCountDown();
    }

    //设置升降调
    public void record_setRisingFallingTone(Context context, View anchorView) {
        if (null == mSoundRecord) {
            return;
        }

        View inflate = View.inflate(mApplication, R.layout.layout_ksong_tone, null);
        ImageView ivToneAdd = inflate.findViewById(R.id.iv_tone_add);
        ImageView ivToneDel = inflate.findViewById(R.id.iv_tone_del);
        final TextView tvTone = inflate.findViewById(R.id.tv_tone);
        tvTone.setText(String.valueOf(record_getRisingFallingTone()));
        ivToneAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int offset = record_getRisingFallingTone();
                Timber.d("offset = " + offset);
                if (offset > 11) {
                    return;
                }
                mSoundRecord.setPitchShift(offset + 1);
                tvTone.setText(String.valueOf(offset + 1));
            }
        });
        ivToneDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int offset = record_getRisingFallingTone();
                Timber.d("offset = " + offset);
                if (offset < -11)
                    return;
                mSoundRecord.setPitchShift(offset - 1);
                tvTone.setText(String.valueOf(offset - 1));
            }
        });
        new BubblePopup(context, inflate)
                .anchorView(anchorView)
                .showAnim(null)
                .dismissAnim(null)
                .show();

    }

    //获取升降调
    public int record_getRisingFallingTone() {
        if (null == mSoundRecord) {
            return 0;
        }

        return (int) mSoundRecord.getPitchShift();
    }

    //获取mid音量图片资源
    public int record_getVoicePictureOfMid() {
        if (null == mSoundScore) {
            mSoundSizeOfMid = DataHelper.getFloatSF(mApplication, ConstantConfig.VOLUMEGUIDEMELODY);
            if (mSoundSizeOfMid < 0) {
                mSoundSizeOfMid = 0.5f;
            }
        } else {
            mSoundSizeOfMid = mSoundScore.getVolumeGuideMelody();
        }

        int resid = 0;
        if (mSoundSizeOfMid == 0f) {
            resid = R.mipmap.midsound_0_normal;
        } else if (mSoundSizeOfMid == 0.5f) {
            resid = R.mipmap.midsound_1_normal;
        } else if (mSoundSizeOfMid == 0.75f) {
            resid = R.mipmap.midsound_2_normal;
        } else if (mSoundSizeOfMid == 1f) {
            resid = R.mipmap.midsound_3_normal;
        }
        return resid;

    }

    //调节Mid音量大小
    public void record_setVoiceOfMid() {
        if (null == mSoundScore) {
            return;
        }

        int resid = 0;
        if (mSoundSizeOfMid == 0f) {
            mSoundSizeOfMid = 0.5f;
        } else if (mSoundSizeOfMid == 0.5f) {
            mSoundSizeOfMid = 0.75f;
        } else if (mSoundSizeOfMid == 0.75f) {
            mSoundSizeOfMid = 1f;
        } else if (mSoundSizeOfMid == 1f) {
            mSoundSizeOfMid = 0f;
        }


        mSoundScore.setVolumeGuideMelody(mSoundSizeOfMid);
        DataHelper.setFloatSF(mApplication, ConstantConfig.VOLUMEGUIDEMELODY, mSoundSizeOfMid);
        mRootView.updatePictureOfMidVoice();
    }

    //K歌开始
    public void record_kSingAction() {
        if (null == mSoundRecord) {
            mRecordPath = mDirOfRecCache + "recordvoice";
            FileUtils.createFileByDeleteOldFile(mRecordPath);
            mSoundRecord = new ncKaraokSoundRecording();
            mSoundRecord.setup(mAccompanyPath, mRecordPath);
        }
        mSoundRecord.start();
        openAuditionProgressListener();//设置播放器进度监听
    }

    //暂停/开始录音
    public void record_pauseAndPlayRecord() {
        if (null == mSoundRecord) {
            return;
        }
        mSoundRecord.togglePlayback();
        Timber.tag("ncKaraokSoundRecording").d("录制状态：" + mSoundRecord.getStatus());
    }

    //暂停录制
    public void record_onPauseRecord() {
        if (null == mSoundRecord) {
            return;
        }
        mSoundRecord.pause();
    }

    //继续录制
    public void record_onResumeRecord() {
        if (null == mSoundRecord) {
            return;
        }
        mSoundRecord.resume();
    }

    //重新录制
    private void record_resetRecord() {
        if (null == mSoundRecord) {
            return;
        }
        mSoundRecord.restart();
    }

    //结束录音
    private void record_endAudio() {
        if (null != mSoundRecord) {
            mSoundRecord.end();
            mSoundRecord = null;
        }

        closeAuditionProgressListener();
    }

    // 停止录音
    public void record_kSingEnd() {
        setScoreResults();

        record_endAudio();

        //普通和清唱才展示打分结果
        if (null != mRootView) {
            mRootView.showScoreResult();
        }

    }

    //获取播放状态
    public boolean record_getPlayStatus() {
        if (null == mSoundRecord) {
            return false;
        }

        return ncKaraokSound.PLAYING == mSoundRecord.getStatus() || 1065353216 == mSoundRecord.getStatus();
    }

    // 开启歌曲进度监听
    private void openAuditionProgressListener() {
        status_recordprepare = true;

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
                                    if (null == mRootView || null == mSoundRecord)
                                        return;

                                    grade_getRealPositionOfVerticalBar();
                                    mRootView.updatePlayProgress(mSoundRecord.getPositionSec(),
                                            mSoundRecord.getDurationSec());
                                }

                            });
        }

        if (null == mRealTimeScaleSubscription || mRealTimeScaleSubscription.isUnsubscribed()) {
            mRealTimeScaleSubscription = Observable.interval(100, TimeUnit.MILLISECONDS)
                    .onBackpressureDrop()
                    .observeOn(Schedulers.newThread())
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
                            grade_getRealTimeRatingData();
                        }

                    });
        }
    }

    // 关闭试听音效播放进度监听
    private void closeAuditionProgressListener() {
        if (null != mPlayProgressSubscription && !mPlayProgressSubscription.isUnsubscribed())
            mPlayProgressSubscription.unsubscribe();
        if (null != mRealTimeScaleSubscription && mRealTimeScaleSubscription.isUnsubscribed())
            mRealTimeScaleSubscription.unsubscribe();
    }


    //设置打分结果
    public void setScoreResults() {
        if (null == mSoundScore) {
            return;
        }

        mSoundScore.getTotalScore();
        mCurrentScore = new ScoreResultEntity();
        mCurrentScore.setScore_valid(mSoundScore.score_valid);
        mCurrentScore.setResult_from_time_sec(mSoundScore.result_from_time_sec);
        mCurrentScore.setResult_to_time_sec(mSoundScore.result_to_time_sec);
        mCurrentScore.setTotal_score(mSoundScore.total_score);

        mCurrentScore.setPart_score(mSoundScore.part_score);
        mCurrentScore.setPitch(mSoundScore.pitch);
        mCurrentScore.setRythm(mSoundScore.rythm);
        mCurrentScore.setRelative_rythm(mSoundScore.relative_rythm);
        mCurrentScore.setExpression(mSoundScore.expression);
        mCurrentScore.setYokuyo(mSoundScore.yokuyo);
        mCurrentScore.setShakuri(mSoundScore.shakuri);
        mCurrentScore.setShakuri_count(mSoundScore.shakuri_count);
        mCurrentScore.setKobushi(mSoundScore.kobushi);
        mCurrentScore.setKobushi_count(mSoundScore.kobushi_count);
        mCurrentScore.setFall(mSoundScore.fall);
        mCurrentScore.setFall_count(mSoundScore.fall_count);
        mCurrentScore.setLong_tone(mSoundScore.long_tone);
        mCurrentScore.setStability(mSoundScore.stability);
        mCurrentScore.setVoice_range_low(mSoundScore.voice_range_low);
        mCurrentScore.setVoice_range_high(mSoundScore.voice_range_high);
        mCurrentScore.setGuide_melody_range_low(mSoundScore.guide_melody_range_low);
        mCurrentScore.setGuide_melody_range_high(mSoundScore.guide_melody_range_high);
        mCurrentScore.setOver_tone(mSoundScore.over_tone);
        mCurrentScore.setVibrato(mSoundScore.vibrato);
        mCurrentScore.setVibrato_times(mSoundScore.vibrato_times);
        mCurrentScore.setVibrato_length_sec(mSoundScore.vibrato_length_sec);
        mCurrentScore.setVibrato_and_long_tone(mSoundScore.vibrato_and_long_tone);
        mCurrentScore.setKey_count(mSoundScore.key_count);

        mCurrentScore.setPitch_all(mSoundScore.pitch_all);
        mCurrentScore.setPitch_part(mSoundScore.pitch_part);

        Timber.d("打分结果：" + KtvApplication.ktvApplication.getAppComponent().gson().toJson(mCurrentScore));
//        mCurrentScore.setScore_valid(true);
//        mCurrentScore.setTotal_score(26.385906f);
//        mCurrentScore.setPitch(4.150943f);
//        mCurrentScore.setRythm(10.943397f);
//        mCurrentScore.setRelative_rythm(-2);
//        mCurrentScore.setExpression(0.226069f);
//        mCurrentScore.setYokuyo(0.904277f);
//        mCurrentScore.setShakuri(0.000f);
//        mCurrentScore.setShakuri_count(0);
//        mCurrentScore.setKobushi(0.000f);
//        mCurrentScore.setKobushi_count(0);
//        mCurrentScore.setFall(0.000000f);
//        mCurrentScore.setFall_count(0);
//        mCurrentScore.setLong_tone(0.0000f);
//        mCurrentScore.setStability(35.062264f);
//        mCurrentScore.setVoice_range_low(46);
//        mCurrentScore.setVoice_range_high(54);
//        mCurrentScore.setGuide_melody_range_low(55);
//        mCurrentScore.setGuide_melody_range_high(70);
//        mCurrentScore.setOver_tone(0.000f);
//        mCurrentScore.setVibrato(4.1021192f);
//        mCurrentScore.setVibrato_times(3);
//        mCurrentScore.setVibrato_length_sec(0.909f);
//        mCurrentScore.setPitch_all(48f);
//        mCurrentScore.setPitch_part(new float[]{1, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
    }

    //获取打分结果
    public ScoreResultEntity getCurrentScore() {
        return mCurrentScore;
    }

    //获取打分结果的平均值
    public ScoreResultEntity getAverageScore() {
        return mAverageScore;
    }

    //获取录制路径
    public String getRecordPath() {
        return mRecordPath + ".wav";
    }

    //获取伴奏路径
    public String getAccompanyPath() {
        return mAccompanyPath;
    }

    /**
     * 展示提示框
     *
     * @param context
     * @param content
     * @param type
     */
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
            case 2:
                record_resetRecord();
                break;
            case 3:
                record_kSingEnd();
                break;
            case 4:
                mRootView.killMyself();
                break;
        }
    }

    //展示切换模式对话框
    public void showSwitchModeDialog(Context context) {
        record_pauseAndPlayRecord();
        SwitchModeDialog switchModeDialog = new SwitchModeDialog(context);
        switchModeDialog.show();
        switchModeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                record_pauseAndPlayRecord();
            }
        });
    }

    //获取录音的准备状态
    public boolean getStatusOfPrepare() {
        return status_recordprepare;
    }

    private void showMessage(int resid) {
        if (null != mApplication) {
            UiUtils.SnackbarText(mApplication.getString(resid));
        }
    }
}

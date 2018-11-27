package com.clicktech.snsktv.module_home.presenter;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ktv.KTVConvertCallback;
import com.clicktech.snsktv.BuildConfig;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.DataHelper;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.FileUtils;
import com.clicktech.snsktv.arms.utils.MediaFile;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.ConstantConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.AverageScoreResponse;
import com.clicktech.snsktv.entity.KSingRGVEntity;
import com.clicktech.snsktv.entity.ScoreResultEntity;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_home.contract.KSingWithMVContract;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.clicktech.snsktv.rxerrorhandler.handler.ErrorHandleSubscriber;
import com.clicktech.snsktv.widget.dialog.SwitchModeDialog;
import com.flyco.animation.BounceEnter.BounceEnter;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.flyco.dialog.widget.popup.BubblePopup;
import com.library.multimedia.lyricscontrols.parser.LrcRow;
import com.library.multimedia.mvprocess.CopyFileThread;
import com.library.multimedia.mvprocess.ICopyFileCallBack;
import com.library.multimedia.mvprocess.MVProcess;
import com.library.multimedia.mvprocess.VideoJointBean;
import com.library.multimedia.mvprocess.VideoJointInputStream;
import com.library.multimedia.mvprocess.VideoJointOutputStream;
import com.library.multimedia.soundercontrols.GradeBean;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

import org.wysaid.view.CameraRecordGLSurfaceView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import cn.jzvd.JZVideoPlayer;
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
 * Created by Administrator on 2017-05-24.
 */

@ActivityScope
public class KSingWithMVPresenter extends BasePresenter<KSingWithMVContract.Model, KSingWithMVContract.View> {

    private final int TIMEINTERVAl_VIDEOSTICHING = 10;//视频拼接的持续时间
    public boolean bNoProcessedOfRecordedVideo;//录制的视频是否不处理
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;
    //路径
    private String mDirOfCacheRec;//带视频录制的缓存根目录
    private String mDirOfMid;//mid目录
    private String mPathOfMid;//mid文件路径
    private String mPathOfAudio;//录制的音频目录
    //状态
    private boolean status_downAccompany;//下载伴奏的状态
    private boolean status_downChoursVideo;//下载合唱视频的状态
    private boolean status_recordprepare = false;//录音准备状态
    private boolean status_initmid;//mid的初始化状态
    private boolean status_recordStart;//录制是否开启
    private boolean status_recordComplete;//录制是否完成
    private boolean status_tipDialog;//提示对话框是否展示
    //
    private SongInfoBean mSingInfo;
    private KSingRGVEntity mKSingRGVInfo;
    private ScoreResultEntity mAverageScore;//全国平均分
    private Subscription mPlayProgressSubscription;//播放进度定时器
    private Subscription mRealTimeScaleSubscription;//实时打分的定时器
    private MaterialDialog mTipDialog;//提示对话框
    //录制处理MV部分
    private String mPathOfRecMV;//录制mv的路径
    private String videoChipDir;//视频拼接片段的目录
    private String mPathOfFirstVideoChip;//第一个视频片段路径
    private int yuvWidth;//帧宽度
    private int yuvHeight;//帧高度
    private int yuvSize;//帧大小
    private int numOfVideoJoint;//视频拼接的数量
    private double accumulateFrame;//积累的帧数（每段视频拼接的时间）
    private long acquisitionTime;//已采集帧数的时间（小于每段视频拼接时间）
    private long lastTime;//上次时间（计算帧率）
    private HashMap<Integer, VideoJointBean> mVideoJointMap;//存储带拼接的视频信息
    private FileOutputStream mYUVOutputStream;//yuv输入流
    private MVProcess mMVProcess;//视频拼接器
    private VideoJointInputStream mVideojointStream;//视频拼接流
    private String mp4IncisePath;//存储分割的伴唱mv路径
    private int numOfCopied = -1;//拷贝的ts文件数量
    private String silentMoviePath;//视频拼接路径和分割的视频路径（不带声音）

    //打分部分
    private int mStartEndPosition;//打分数据距离左右的宽度
    private int mWidthOfPitchView;//显示打分数据的宽度
    private int mNumOfPitchLine;//音调线数量
    private List<LrcRow> mLrcRowList;//歌词数据
    private SparseArray<List<GradeBean>> mMidDataHashMap;//整首歌的mid数据
    private List<Integer> mStartNoList;//每句歌词打分开始的no
    private int mIndexOfGrade = 0;//当前打分开始的no
    private int mCurRowsOfLyrics = 0;//当前歌词行数
    private float mSoundSizeOfMid = 0f;//mid的声音大小
    private ncKaraokSoundRecording mSoundRecord;//录音器
    private ncKaraokSoundScoring mSoundScore;//音准器
    private ScoreResultEntity mCurrentScore;//总打分结果


    @Inject
    public KSingWithMVPresenter(KSingWithMVContract.Model model, KSingWithMVContract.View rootView
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
        OkGo.getInstance().cancelAll();

        if (null != mMVProcess) {
            mMVProcess.cancleEffect();
            mMVProcess.releaseRecource();
            mMVProcess = null;
        }

        if (null != mYUVOutputStream) {
            try {
                mYUVOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mYUVOutputStream = null;
            }
        }

        if (null != mVideojointStream) {
            mVideojointStream.release();
            mVideojointStream = null;
        }

        if (null != mTipDialog) {
            mTipDialog.superDismiss();
        }

        record_endAudio();

        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;

    }

    private void initPath() {
        String rootDir = FileUtils.getCacheDirectory(mApplication,
                Environment.DIRECTORY_MUSIC).getAbsolutePath()
                + ConstantConfig.Dirs_HasVideo;
        mDirOfCacheRec = rootDir + "ksingwithmv" + File.separator;
        FileUtils.createOrDeleteOldDir(mDirOfCacheRec);

        mDirOfMid = FileUtils.getCacheDirectory(mApplication,
                Environment.DIRECTORY_MUSIC).getAbsolutePath()
                + ConstantConfig.Dirs_Mid;

        mPathOfRecMV = mDirOfCacheRec + "recordmovie.mp4";
        silentMoviePath = mDirOfCacheRec + "chorus_silentmovie.mp4";
        mp4IncisePath = mDirOfCacheRec + "chorus_mp4incise.mp4";

    }

    public void setData(SongInfoBean singInfoBeanEntity, KSingRGVEntity kSingRGVEntity) {
        this.mSingInfo = singInfoBeanEntity;
        this.mKSingRGVInfo = kSingRGVEntity;
        this.bNoProcessedOfRecordedVideo = mKSingRGVInfo.getLayouttype() == R.integer.chorustype_fullscreen;

        request_parselyricData(mKSingRGVInfo.getLyricpath());
        request_downloadGradeMid();
        request_songAverageScore();
        if (bNoProcessedOfRecordedVideo) {
            request_downloadAccompany();
        } else {
            request_downloadChorusVideo();
        }

    }

    //解析歌词数据
    private void request_parselyricData(String lyricpath) {
        mRootView.updateLrc(mLrcRowList = mModel.parseLrc(lyricpath));
    }

    //获取歌曲全国平均分数
    private void request_songAverageScore() {
        mModel.getSongWorksAverageScore(((KtvApplication) mApplication).getUserID(), mSingInfo.getSong_id())
                .compose(RxUtils.<AverageScoreResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<AverageScoreResponse>(mErrorHandler) {
                    @Override
                    public void onNext(AverageScoreResponse averageScoreResponse) {
                        mAverageScore = averageScoreResponse.getSongAverageScore();
                    }
                });
    }

    //下载打分mid
    private void request_downloadGradeMid() {
        if (null == mSingInfo) {
            return;
        }

        if (EmptyUtils.isNotEmpty(mSingInfo.getGrade_file_url())) {
            mPathOfMid = mDirOfMid + mSingInfo.getGrade_file_url();
            if (FileUtils.isFileExists(mPathOfMid)) {
                grade_intializerEvalutionInstrument();
                return;
            }

            //Mid不存在就下载
            OkGo.<File>get(BuildConfig.APP_DOMAIN_File + mSingInfo.getGrade_file_url())
                    .tag(this)
                    .execute(new FileCallback(FileUtils.getDirName(mPathOfMid), mSingInfo.getGrade_file_url()) {

                        @Override
                        public void onSuccess(Response<File> response) {
                            Timber.tag("ncKaraokSoundScoring").d("评分下载完成");
                            grade_intializerEvalutionInstrument();
                        }

                        @Override
                        public void onError(Response<File> response) {
                            super.onError(response);
                            Timber.tag("ncKaraokSoundScoring").e("评分下载失败!");
                            showMessage(R.string.error_downfile_mid);
                            FileUtils.deleteFile(mPathOfMid);
                        }
                    });

        } else {
            mPathOfMid = mDirOfMid + "m365mid.mid";
            Observable.create(new Observable.OnSubscribe<Boolean>() {
                @Override
                public void call(Subscriber<? super Boolean> subscriber) {
                    subscriber.onNext(FileUtils.readInputStream(mPathOfMid,
                            mApplication.getResources().openRawResource(R.raw.m365mid)));
                }
            }).subscribeOn(rx.schedulers.Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Boolean>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            showMessage(R.string.error_downfile_mid);
                            FileUtils.deleteFile(mPathOfMid);
                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            if (aBoolean) {
                                grade_intializerEvalutionInstrument();
                            }
                        }

                    });
        }

    }

    //下载伴奏
    private void request_downloadAccompany() {
        if (EmptyUtils.isEmpty(mSingInfo.getAccompany_url())) {
            mRootView.showMessage(mApplication.getString(R.string.error_accompany_notfound));
            return;
        }

        final String accompanysavepath = FileUtils.getCacheDirectory(mApplication,
                Environment.DIRECTORY_MUSIC).getAbsolutePath() + ConstantConfig.Dirs_Song +
                mSingInfo.getAccompany_url();
        if (FileUtils.isFileExists(accompanysavepath)) {
            mKSingRGVInfo.setAccompanypath(accompanysavepath);
            status_downAccompany = true;
            record_startRecordingDelay();
            return;
        }

        FileUtils.createOrExistsFile(accompanysavepath);
        mRootView.showNumberDialog();
        OkGo.<File>get(BuildConfig.APP_DOMAIN_File + mSingInfo.getAccompany_url())
                .tag(this)
                .execute(new FileCallback(FileUtils.getDirName(accompanysavepath), mSingInfo.getAccompany_url()) {
                    @Override
                    public void onSuccess(Response<File> response) {
                        Timber.d("歌曲下载完成");
                        mKSingRGVInfo.setAccompanypath(accompanysavepath);
                        status_downAccompany = true;
                        record_startRecordingDelay();
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        mRootView.updateNumberDialog((int) (progress.fraction * 100));
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        Timber.e("伴奏文件下载失败！");
                        FileUtils.deleteFile(accompanysavepath);
                        showMessage(R.string.error_downloadfailed_song);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (null != mRootView) {
                            mRootView.hideNumberDialog();
                        }
                    }
                });
    }

    // 下载合唱视频
    private void request_downloadChorusVideo() {
        if (EmptyUtils.isEmpty(mSingInfo.getWorks_url()) || !MediaFile.isVideoFileType(mSingInfo.getWorks_url())) {
            mRootView.showMessage(mApplication.getString(R.string.error_chorusvideo_notfound));
            return;
        }

        final String chorusvideoSavePath = FileUtils.getCacheDirectory(mApplication,
                Environment.DIRECTORY_MUSIC).getAbsolutePath() + ConstantConfig.Dirs_Video +
                mSingInfo.getWorks_url();
        if (FileUtils.isFileExists(chorusvideoSavePath)) {
            mKSingRGVInfo.setChorusvideopath(chorusvideoSavePath);
            status_downChoursVideo = true;
            record_startRecordingDelay();
            return;
        }

        FileUtils.createOrExistsFile(chorusvideoSavePath);
        mRootView.showNumberDialog();
        OkGo.<File>get(BuildConfig.APP_DOMAIN_File + mSingInfo.getWorks_url())
                .tag(this)
                .execute(new FileCallback(FileUtils.getDirName(chorusvideoSavePath), mSingInfo.getWorks_url()) {
                    @Override
                    public void onSuccess(Response<File> response) {
                        Timber.d("合唱视频下载完成");
                        mKSingRGVInfo.setChorusvideopath(chorusvideoSavePath);
                        status_downChoursVideo = true;
                        record_startRecordingDelay();
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        mRootView.updateNumberDialog((int) (progress.fraction * 100));
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);

                        Timber.e("合唱视频下载失败！");
                        showMessage(R.string.error_chorusvideo_downfail);
                        FileUtils.deleteFile(chorusvideoSavePath);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (null != mRootView) {
                            mRootView.hideNumberDialog();
                        }
                    }
                });

    }

    //初始化音准器
    private void grade_intializerEvalutionInstrument() {
        if (null == mRootView) {
            return;
        }

        mRootView.prepareDataOfGrade();

        if (null == mSoundScore) {
            mSoundScore = new ncKaraokSoundScoring();
            mSoundScore.setup(mPathOfMid, ncKaraokSound.NONE);
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
    public void grade_setSizeOfGradeView(int x, int width, int ynum) {
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
            Timber.tag("ncKaraokSoundScoring").e("没有打分数据!");
            return;
        }

        mRootView.initIntonationIs(mMidDataHashMap);
        status_initmid = true;
        record_startRecordingDelay();

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
            grade_jumpToTheNextPage();
            mRootView.turn2NextLyric();
            return;
        }
        mRootView.updateIntonationIs(null, positionSecX);
    }

    //设置打分信息
    private int grade_setRatingInfomation(LrcRow lrcRow) {
        float from_sec = lrcRow.getTime() * 1.0f / 1000;
        float to_sec = (lrcRow.getTime() + lrcRow.getTotalTime()) * 1.0f / 1000;
        return mSoundScore.setInfo(0, mWidthOfPitchView, mNumOfPitchLine, from_sec, to_sec);
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
            gradeBean.setNoteY(mSoundScore.getNoteY(mIndexOfGrade));
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
    private void grade_jumpToTheNextPage() {
        ++mCurRowsOfLyrics;
        if (EmptyUtils.isEmpty(mStartNoList) || mCurRowsOfLyrics >= mStartNoList.size()) {
            return;
        }

        LrcRow lrcRow = mLrcRowList.get(mCurRowsOfLyrics);
        mIndexOfGrade = mStartNoList.get(mCurRowsOfLyrics);
        whetherToSwitchRoles(lrcRow);
        grade_setRatingInfomation(lrcRow);
        Timber.tag("ncKaraokSoundRecording").d("第%d句歌词开始的no是%d", mCurRowsOfLyrics, mIndexOfGrade);
    }

    //延迟一段时间后开始录音
    public void record_startRecordingDelay() {
        if (!status_initmid) {
            return;
        }

        if (!(status_downAccompany || status_downChoursVideo)) {
            return;
        }

        if (status_recordStart) {
            return;
        }

        status_recordStart = true;

        mIndexOfGrade = mStartNoList.get(0);
        grade_setRatingInfomation(mLrcRowList.get(0));

        mRootView.startCountDown();
    }

    //K歌开始
    public void record_kSingAction() {
        record_videoRecording();
        record_playMediaFile();
    }

    //开始视频录制
    private void record_videoRecording() {
        Timber.tag("videoprocess").d("开始视频录制");
        if (!bNoProcessedOfRecordedVideo) {
            videoChipDir = mDirOfCacheRec + "chip" + File.separator;
            FileUtils.createOrDeleteOldDir(videoChipDir);
            mPathOfFirstVideoChip = videoChipDir + "inputchip_0.yuv";
            FileUtils.createFileByDeleteOldFile(mPathOfFirstVideoChip);
            try {
                mYUVOutputStream = new FileOutputStream(mPathOfFirstVideoChip);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        CameraRecordGLSurfaceView.StartRecordingCallback startCallBack =
                new CameraRecordGLSurfaceView.StartRecordingCallback() {
                    @Override
                    public void startRecordingOver(boolean success) {
                        if (!success) {
                            Timber.tag("videoprocess").e("视频开始录制失败！");
                            if (null != mRootView) {
                                mRootView.killMyself();
                            }
                        }
                    }
                };
        CameraRecordGLSurfaceView.YUVCallback yuvCallback = null;
        if (!bNoProcessedOfRecordedVideo) {
            yuvCallback = new CameraRecordGLSurfaceView.YUVCallback() {
                @Override
                public void YUVcallback(byte[] bytes, int length, int width, int height) {
                    //如果暂停中，那么不处理数据
                    if (!record_getPlayStatus()) {
                        return;
                    }

                    //存储yuv数据
                    if (null != mYUVOutputStream) {
                        try {
                            mYUVOutputStream.write(bytes);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    //每隔一段时间计算一次帧率
                    if (lastTime == 0) {
                        Timber.tag("videoprocess").d("YUV数据开始返回: length = %d , width = %d , height = %d", length, width, height);
                        yuvSize = length;
                        yuvWidth = width;
                        yuvHeight = height;
                        lastTime = System.currentTimeMillis();
                        mVideoJointMap = new HashMap<>();
                    }
                    long currentTimestamp = System.currentTimeMillis();
                    ++accumulateFrame;
                    acquisitionTime += currentTimestamp - lastTime;
                    lastTime = currentTimestamp;
                    if (acquisitionTime >= TIMEINTERVAl_VIDEOSTICHING * 1000) {
                        if (null != mYUVOutputStream) {
                            try {
                                mYUVOutputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                mYUVOutputStream = null;
                            }
                        }

                        double averageFrameRate = accumulateFrame * 1000 / acquisitionTime;
                        Timber.tag("videoprocess").d("每" + TIMEINTERVAl_VIDEOSTICHING + "秒的平均帧率：" + averageFrameRate);
                        VideoJointBean videoJointBean = new VideoJointBean();
                        videoJointBean.setFirstVideoBeginTime(numOfVideoJoint * TIMEINTERVAl_VIDEOSTICHING);
                        videoJointBean.setFirstVideoTimeLength(TIMEINTERVAl_VIDEOSTICHING + 1);
                        videoJointBean.setSampleRate(averageFrameRate);
                        String yuvpath = videoChipDir + "inputchip_" + numOfVideoJoint + ".yuv";
                        videoJointBean.setYuvInputPath(yuvpath);
                        FileUtils.createOrExistsFile(yuvpath);
                        videoJointBean.setVideoOutputPath(videoChipDir + "outputchip_" + numOfVideoJoint + ".ts");
                        mVideoJointMap.put(numOfVideoJoint, videoJointBean);
                        mvProcess_videoJoint(numOfVideoJoint);

                        ++numOfVideoJoint;
                        String nextYuvPath = videoChipDir + "inputchip_" + numOfVideoJoint + ".yuv";
                        try {
                            mYUVOutputStream = new FileOutputStream(nextYuvPath);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        acquisitionTime %= TIMEINTERVAl_VIDEOSTICHING * 1000;
                        accumulateFrame = 0;
                    }

                }
            };
        }


        mRootView.startMVRecord(mPathOfRecMV, startCallBack, yuvCallback);
    }

    //将两个视频合成一个
    private void mvProcess_videoJoint(final int curChip) {

        if (status_recordComplete) {
            VideoJointBean videoJointBean = new VideoJointBean();
            videoJointBean.setFirstVideoBeginTime(curChip * TIMEINTERVAl_VIDEOSTICHING);
            videoJointBean.setFirstVideoTimeLength((int) acquisitionTime / 1000 + 1);
            videoJointBean.setSampleRate(accumulateFrame * 1000 / acquisitionTime);
            String yuvpath = videoChipDir + "inputchip_" + numOfVideoJoint + ".yuv";
            videoJointBean.setYuvInputPath(yuvpath);
            FileUtils.createOrExistsFile(yuvpath);
            videoJointBean.setVideoOutputPath(videoChipDir + "outputchip_" + numOfVideoJoint + ".ts");
            mVideoJointMap.put(numOfVideoJoint, videoJointBean);
        }

        if (bNoProcessedOfRecordedVideo || null == mKSingRGVInfo || null == mVideoJointMap)
            return;

        final VideoJointBean videoJointBean = mVideoJointMap.get(curChip);
        if (null == videoJointBean)
            return;

        mMVProcess = MVProcess.getInstance();
        mVideojointStream = new VideoJointInputStream(yuvWidth, yuvHeight,
                videoJointBean.getSampleRate(), yuvSize,
                videoJointBean.getYuvInputPath());
        Timber.tag("videoprocess").d("第" + curChip + "个合成开始");

        KTVConvertCallback ktvConvertCallback = new KTVConvertCallback() {
            @Override
            public void success() {
                Timber.tag("videoprocess").d("第" + curChip + "个合成success()");
                FileUtils.deleteFile(videoJointBean.getYuvInputPath());
                if (status_recordComplete && curChip == numOfVideoJoint) {
                    mvProcess_combineVideoChips();
                }
            }

            @Override
            public void error() {
                Timber.tag("videoprocess").e("第" + curChip + "个合成error()");
                if (null != mRootView) {
                    mRootView.hideRequetDialog();
                }
            }
        };


        if (mKSingRGVInfo.getLayouttype() == R.integer.chorustype_leftright) {
            VideoJointOutputStream videoJointOutputStream = new VideoJointOutputStream(mKSingRGVInfo.getLeftwidth() / 2,
                    mKSingRGVInfo.getLeftheight() / 2, mKSingRGVInfo.getRightwidth() / 2, mKSingRGVInfo.getRightheight() / 2,
                    mKSingRGVInfo.getInsidebottomRightx(), mKSingRGVInfo.getInsidebottomRightY(), videoJointBean.getVideoOutputPath());

            mMVProcess.combineVideoLeftRight(mVideojointStream, videoJointBean.getFirstVideoBeginTime(),
                    videoJointBean.getFirstVideoTimeLength(), mp4IncisePath, mKSingRGVInfo.getChorusvideopath(),
                    videoJointOutputStream, ktvConvertCallback);

        } else if (mKSingRGVInfo.getLayouttype() == R.integer.chorustype_frontback) {
            Timber.tag("videoprocess").d("输出前后视频：insideWidth = %d, insideHeight = %d, outsideWidth = %d ,outsideHeight = %d",
                    mKSingRGVInfo.getInsidewidth() / 2, mKSingRGVInfo.getInsideheight() / 2,
                    mKSingRGVInfo.getOutsidewidth() / 2, mKSingRGVInfo.getOutsideheight() / 2);

            VideoJointOutputStream videoJointOutputStream = new VideoJointOutputStream(
                    mKSingRGVInfo.getInsidewidth() / 2, mKSingRGVInfo.getInsidewidth() / 2,
                    mKSingRGVInfo.getOutsidewidth() / 2, mKSingRGVInfo.getOutsideheight() / 2,
                    mKSingRGVInfo.getInsidebottomRightx(),
                    mKSingRGVInfo.getInsidebottomRightY(), videoJointBean.getVideoOutputPath());
            mMVProcess.combineVideoFrontBack(videoJointBean.getFirstVideoBeginTime(),
                    videoJointBean.getFirstVideoTimeLength(),
                    mp4IncisePath, mKSingRGVInfo.getChorusvideopath(),
                    mVideojointStream,
                    videoJointOutputStream, ktvConvertCallback);
        }
    }

    //将多个合成的视频拼接到一起
    private void mvProcess_combineVideoChips() {
        Timber.tag("videoprocess").d("将多个ts文件合成一个视频");

        final String combinePath = mDirOfCacheRec + "chorus_combine.ts";
        long curtCopyFileLength = 0;
        for (int i = 0; i <= numOfVideoJoint; i++) {
            synchronized (this) {
                VideoJointBean videoJointBean = mVideoJointMap.get(i);
                if (null == videoJointBean) {
                    continue;
                }
                long fileLength = FileUtils.getFileLength(videoJointBean.getVideoOutputPath());
                new CopyFileThread(i, videoJointBean.getVideoOutputPath(), combinePath, curtCopyFileLength,
                        fileLength,
                        new ICopyFileCallBack() {
                            @Override
                            public void onSuccess() {
                                ++numOfCopied;
                                mvProcess_convertVideoFormat(combinePath);
                            }

                            @Override
                            public void onFailure() {
                                if (null != mRootView) {
                                    mRootView.hideRequetDialog();
                                }
                            }
                        }).start();
                curtCopyFileLength += (fileLength + 1);
            }
        }
    }

    // 转换视频格式:ts->mp4
    private void mvProcess_convertVideoFormat(String inputpath) {
        if (numOfCopied != numOfVideoJoint)
            return;

        Timber.tag("videoprocess").d("转换视频格式");
        mMVProcess = MVProcess.getInstance();
        mMVProcess.videoFormatConversion(inputpath, silentMoviePath, new KTVConvertCallback() {
            @Override
            public void success() {
                Timber.tag("videoprocess").d("视频格式转换成功");
                if (null != mRootView) {
                    mRootView.hideRequetDialog();
                    mRootView.showScoreResult();
                }

            }

            @Override
            public void error() {
                Timber.tag("videoprocess").e("视频格式转换失败!");
                if (null != mRootView) {
                    mRootView.hideRequetDialog();
                }
            }
        });
    }

    //播放媒体文件
    private void record_playMediaFile() {
        if (null == mKSingRGVInfo)
            return;

        if (bNoProcessedOfRecordedVideo) {
            playMediaFile_Audio(mKSingRGVInfo.getAccompanypath());
        } else {
            playMediaFile_Video();
            playMediaFile_Audio(mKSingRGVInfo.getChorusvideopath());
        }
    }

    //播放下载的合唱的mv（静音）
    private void playMediaFile_Video() {
        if (null == mRootView) {
            return;
        }
        mRootView.playChorusVideo();
    }

    //播放媒体文件（配合mid获取打分）
    private void playMediaFile_Audio(String accompanypath) {

        if (null == mSoundRecord) {
            mPathOfAudio = mDirOfCacheRec + "recordvoice";
            mSoundRecord = new ncKaraokSoundRecording();
            mSoundRecord.setup(accompanypath, mPathOfAudio);
        }
        mSoundRecord.start();

        openAuditionProgressListener();//设置播放器进度监听
    }

    //获取播放状态
    public boolean record_getPlayStatus() {
        if (null == mSoundRecord) {
            return false;
        }

        boolean status_play = (ncKaraokSound.PLAYING == mSoundRecord.getStatus()
                || 1065353216 == mSoundRecord.getStatus());
//        Timber.tag("ncKaraokSoundRecording").d("录制状态：" + mSoundRecord.getStatus());
        return status_play;
    }

    // 暂停/开始录音
    public void record_pauseAndPlayRecord() {
        //音频播放器-播放暂停
        if (null == mSoundRecord || !status_recordStart) {
            return;
        }
        mSoundRecord.togglePlayback();

        //视频播放器-播放暂停
        if (record_getPlayStatus()) {
            JZVideoPlayer.goOnPlayOnPause();
        } else {
            JZVideoPlayer.goOnPlayOnResume();
        }

        //录制-播放暂停

    }

    //设置升降调
    public void record_setRisingFallingTone(Context context, View anchorView) {
        if (null == mSoundRecord || !status_recordStart) {
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
    private int record_getRisingFallingTone() {
        if (null == mSoundRecord) {
            return 0;
        }

        float pitchshift = mSoundRecord.getPitchShift();

        return (int) pitchshift;
    }

    //调节Mid音量大小
    public void record_setVoiceOfMid() {
        if (null == mSoundScore || !status_recordStart) {
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

    //获取mid音量图片资源
    public int getVoicePictureOfMid() {
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

    //重新录制
    private void record_resetRecord() {
        if (!status_recordStart) {
            return;
        }

        //停止视频处理
        if (null != mMVProcess) {
            mMVProcess.cancleEffect();
        }

        //停止打分
        record_endAudio();

        //停止录制和播放视频
        mRootView.stopMVRecord(new CameraRecordGLSurfaceView.EndRecordingCallback() {
            @Override
            public void endRecordingOK() {
                status_recordStart = false;
                initPath();
                record_startRecordingDelay();
            }
        }, bNoProcessedOfRecordedVideo);
    }

    /**
     * K歌结束
     * 停止录制
     * 停止播放音频或者视频
     * 停止播放进度监听
     */
    public void record_kSingEnd() {
        if (status_recordComplete || null == mRootView)
            return;

        mRootView.showRequestDialog();
        mRootView.stopMVRecord(new CameraRecordGLSurfaceView.EndRecordingCallback() {
            @Override
            public void endRecordingOK() {
                Timber.tag("ncKaraokSoundRecording").d("录制结束");
                status_recordComplete = true;

                setScoreResults();

                record_endAudio();

                if (!bNoProcessedOfRecordedVideo && acquisitionTime >= 1000) {
                    mvProcess_videoJoint(numOfVideoJoint);
                } else {
                    --numOfVideoJoint;
                }

                if (bNoProcessedOfRecordedVideo && null != mRootView) {
                    mRootView.showScoreResult();
                }
            }
        }, bNoProcessedOfRecordedVideo);
    }

    //结束录音
    public void record_endAudio() {
        if (null != mSoundRecord) {
            mSoundRecord.end();
            mSoundRecord = null;
        }

        closeAuditionProgressListener();
    }

    /**
     * 开启歌曲进度监听
     * 开启一个轮询器每隔1秒钟查询一次播放进度
     */
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
                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onNext(Long aLong) {
                                    if (null == mRootView || null == mSoundRecord) {
                                        return;
                                    }
//                                    Timber.tag("ncKaraokSoundRecording").d("getPositionSec = %f," +
//                                                    "getDurationSec = %f",
//                                            mSoundRecord.getPositionSec(), mSoundRecord.getDurationSec());
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

    /**
     * 关闭试听音效播放进度监听
     */
    private void closeAuditionProgressListener() {
        status_recordprepare = false;
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
    }

    //获取打分结果
    public ScoreResultEntity getCurrentScore() {
        return mCurrentScore;
    }

    //获取打分结果的平均值
    public ScoreResultEntity getAverageScore() {
        return mAverageScore;
    }

    //获取录制的视频路径
    public String getRecordVideoPath() {
        return bNoProcessedOfRecordedVideo ? mPathOfRecMV : silentMoviePath;
    }

    //获取录制的音频路径
    public String getRecordAudioPath() {
        return mPathOfAudio + ".wav";
    }

    //获取录音的准备状态
    public boolean getStatusOfPrepare() {
        return status_recordprepare;
    }


    //展示提示框
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
                .dismissAnim(new FadeExit())//
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
                        dialogOperationType(type);
                        status_tipDialog = false;
                    }
                }
        );
    }

    /**
     * 清唱操作类型：关闭界面(0)、重录(1)、录制完成(2)
     */
    private void dialogOperationType(int type) {
        switch (type) {
            case 0:
                if (null != mMVProcess) {
                    mMVProcess.cancleEffect();
                }
                mRootView.killMyself();
                break;
            case 1:
                record_resetRecord();
                break;
            case 2:
                mRootView.record_checkTime();
                break;
            default:
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

    private void showMessage(int resid) {
        if (null != mApplication) {
            UiUtils.SnackbarText(mApplication.getString(resid));
        }
    }

    //判断是否要切换角色
    private void whetherToSwitchRoles(LrcRow lrcRow) {
        if (bNoProcessedOfRecordedVideo) {
            return;
        }

        boolean switchRole = lrcRow.isSwitch();
        if (!switchRole) {
            return;
        }

        if (lrcRow.getRoletype() == 1) {
            mRootView.switchRoleA();
        } else if (lrcRow.getRoletype() == 2) {
            mRootView.switchRoleB();
        } else if (lrcRow.getRoletype() == 3) {
            mRootView.switchChorus();
        }

    }
}

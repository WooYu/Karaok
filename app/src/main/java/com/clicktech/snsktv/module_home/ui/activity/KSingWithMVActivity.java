package com.clicktech.snsktv.module_home.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.ConvertUtils;
import com.clicktech.snsktv.arms.utils.DeviceUtils;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.TimeUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.EventBusTag;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.entity.KSingRGVEntity;
import com.clicktech.snsktv.entity.MVConfig;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.ui.helper.TemplateAndFilterType;
import com.clicktech.snsktv.module_discover.ui.helper.TemplateFilterTypeHelper;
import com.clicktech.snsktv.module_home.contract.KSingWithMVContract;
import com.clicktech.snsktv.module_home.inject.component.DaggerKSingWithMVComponent;
import com.clicktech.snsktv.module_home.inject.module.KSingWithMVModule;
import com.clicktech.snsktv.module_home.presenter.KSingWithMVPresenter;
import com.clicktech.snsktv.module_home.ui.fragment.ScoreResultFragment;
import com.clicktech.snsktv.util.StringHelper;
import com.clicktech.snsktv.widget.animaoftiming.DotCountDownView;
import com.clicktech.snsktv.widget.dialog.DownloadDialog;
import com.clicktech.snsktv.widget.dialog.NetworkDialog;
import com.clicktech.snsktv.widget.titlebar.HeaderView;
import com.clicktech.snsktv.widget.videoplayer.KSingPlayer;
import com.jaeger.library.StatusBarUtil;
import com.library.multimedia.lyricscontrols.parser.LrcRow;
import com.library.multimedia.lyricscontrols.view.LyricOfChorusImpl;
import com.library.multimedia.lyricscontrols.view.LyricOfSingleImpl;
import com.library.multimedia.soundercontrols.GradeBean;
import com.library.multimedia.soundercontrols.IntonationView;

import org.simple.eventbus.EventBus;
import org.wysaid.camera.CameraInstance;
import org.wysaid.view.CameraGLSurfaceView;
import org.wysaid.view.CameraRecordGLSurfaceView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;
import timber.log.Timber;

import static com.clicktech.snsktv.arms.utils.Preconditions.checkNotNull;

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
 * MV、视频合唱
 */

public class KSingWithMVActivity extends WEActivity<KSingWithMVPresenter> implements
        KSingWithMVContract.View, HeaderView.OnCustomTileListener, View.OnClickListener {

    @BindView(R.id.headerview)
    HeaderView headerview;
    @BindView(R.id.tv_curtime)
    TextView tvCurtime;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_totaltime)
    TextView tvTotaltime;
    @BindView(R.id.intonationview)
    IntonationView mIntonationView;
    @BindView(R.id.dotView)
    DotCountDownView mDotView;
    @BindView(R.id.lyricofchorus)
    LyricOfChorusImpl mLyricOfChorus;
    @BindView(R.id.lyricofsingle)
    LyricOfSingleImpl mLyricOfSingle;
    @BindView(R.id.previewview)
    CameraRecordGLSurfaceView previewview;
    @BindView(R.id.playview)
    KSingPlayer playview;
    @BindView(R.id.focusbox)
    View focusBox;
    @BindView(R.id.rl_mv)
    RelativeLayout rlMv;
    @BindView(R.id.tv_playpause)
    TextView tvPlaypause;
    @BindView(R.id.tv_tone)
    TextView mTvTone;
    @BindView(R.id.tv_reset)
    TextView tvReset;
    @BindView(R.id.tv_over)
    TextView tvOver;
    @BindView(R.id.tv_midvoice)
    TextView mTvMidVoice;
    @BindView(R.id.rl_lyric)
    RelativeLayout mRlLyric;
    @BindView(R.id.iv_topbg)
    ImageView mIvTopBg;
    @BindView(R.id.view_expandbg)
    View mViewExpandBg;
    @BindView(R.id.view_control_expandfold)
    View mViewExpandFoldControl;

    private boolean bStatusOfLyric;//歌词状态：true展开；false折叠
    private boolean bTheActorRoleA = false;//录制者的角色是否为A
    private SongInfoBean mSongInfo;//歌曲信息
    private TemplateAndFilterType filterType;//滤镜信息
    private MVConfig mvConfig;//录制效果、布局配置
    private KSingRGVEntity mKSingRGVEntity;//录制mv的配置信息
    private boolean bAChorusOfRecording;//录制的是否为合唱
    private int mMVSize;//mv尺寸
    private int mFullMVWidth;//只有预览视图时的宽(单个视频)
    private int mFullMVHeight;//只有预览视图时的高（单个视频）
    private int mLeftWidth;//左边录制视频的宽
    private Bitmap mLoginUserBitmap;//登录用户的bitmap
    private Bitmap mSponsorBitmap;//合唱发起者bitmap
    private DownloadDialog downloadDialog;
    private NetworkDialog networkDialog;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerKSingWithMVComponent
                .builder()
                .appComponent(appComponent)
                .kSingWithMVModule(new KSingWithMVModule(this)) //请将KSingWithMVModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_ksingwithmv2, null, false);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this,
                getResources().getInteger(R.integer.statusalpha_a), headerview);
    }

    @Override
    protected void initData() {
        JZVideoPlayer.releaseAllVideos();
        EventBus.getDefault().post(new Message(), EventBusTag.MINIPLAYER_HIDE);
        headerview.setTitleClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (null == bundle)
            return;

        mSongInfo = bundle.getParcelable("songinfo");
        filterType = (TemplateAndFilterType) bundle.getSerializable("mvfilter");
        mvConfig = TemplateFilterTypeHelper.getMvConfig(filterType);
        mKSingRGVEntity = bundle.getParcelable("ksingrgv");

        if (null == mSongInfo || null == mvConfig || null == mKSingRGVEntity)
            return;

        bAChorusOfRecording = mKSingRGVEntity.getWorkstype() == getResources().getInteger(R.integer.workstype_chrousa)
                || mKSingRGVEntity.getWorkstype() == getResources().getInteger(R.integer.workstype_chrousb)
                || mKSingRGVEntity.getWorkstype() == getResources().getInteger(R.integer.workstype_chrousother);

        networkDialog = new NetworkDialog(this);
        init_lrcView();
        init_fillingData();
        mPresenter.setData(mSongInfo, mKSingRGVEntity);

    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showRequestDialog() {
        if (null != networkDialog) {
            networkDialog.showNetWorkDialog();
        }
    }

    @Override
    public void hideRequetDialog() {
        if (null != networkDialog) {
            networkDialog.dismissNetWorkDialog();
        }
    }

    @Override
    public void showNumberDialog() {
        if (null == downloadDialog) {
            downloadDialog = new DownloadDialog();
            downloadDialog.setDialogDismissListener(new DownloadDialog.DialogDismissListener() {
                @Override
                public void onDismiss() {
                    setTitleLeftClick();
                }
            });
        }
        downloadDialog.show(getFragmentManager(), "com.clicktech.snsktv.widget.dialog.DownloadDialog");
    }

    @Override
    public void hideNumberDialog() {
        if (null != downloadDialog) {
            downloadDialog.dismiss();
            downloadDialog = null;
        }
    }

    @Override
    public void updateNumberDialog(int progress) {
        if (null != downloadDialog && progress <= 100) {
            downloadDialog.setTvProgress(progress);
        }
    }

    @Override
    public void showMessage(String message) {
        checkNotNull(message);
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(Intent intent) {
        checkNotNull(intent);
        UiUtils.startActivity(this, intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        layout_measure();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideRequetDialog();
        hideNumberDialog();

        record_closeMediaResource();
        mPresenter.record_endAudio();
        killMyself();
    }

    @Override
    protected void onDestroy() {

        mLoginUserBitmap = null;
        mSponsorBitmap = null;

        mDotView.endCountDown();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        setTitleLeftClick();
    }

    @Override
    public void setTitleLeftClick() {
        mPresenter.showTipDialog(this, getString(R.string.dialog_cantata_back), 0);
    }

    @Override
    public void setTitleRightClick() {
    }

    @OnClick({R.id.tv_playpause, R.id.tv_tone, R.id.tv_midvoice, R.id.tv_reset, R.id.tv_over,
            R.id.view_control_expandfold, R.id.rl_mv})
    public void onClick(View v) {
        if (!mPresenter.getStatusOfPrepare()) {
            return;
        }

        switch (v.getId()) {
            case R.id.tv_playpause://切换模式
                onTvPlaypauseClicked();
                break;
            case R.id.tv_tone://音调
                onTvToneClicked();
                break;
            case R.id.tv_midvoice://mid音量
                onTvMidVoiceClicked();
                break;
            case R.id.tv_reset://重唱
                onTvResetClicked();
                break;
            case R.id.tv_over://结束
                onTvOverClicked();
                break;
            case R.id.view_control_expandfold://歌词展开折叠
                lyricExpandingAndCollapsing();
                break;
            case R.id.rl_mv:
                if (mPresenter.record_getPlayStatus()) {
                    showMessage(getString(R.string.tip_touchlyric));
                }
                mPresenter.record_pauseAndPlayRecord();
                break;
        }
    }

    public void onTvPlaypauseClicked() {
        mPresenter.showSwitchModeDialog(this);
    }

    public void onTvToneClicked() {
        mPresenter.record_setRisingFallingTone(this, mTvTone);
    }

    public void onTvMidVoiceClicked() {
        mPresenter.record_setVoiceOfMid();
    }

    public void onTvResetClicked() {
        mPresenter.showTipDialog(this, getString(R.string.dialog_rerecord), 1);
    }

    public void onTvOverClicked() {
        mPresenter.showTipDialog(this, getString(R.string.dialog_finishrecord), 2);
    }

    @Override
    public void startCountDown() {
        mDotView.setTimingListener(new DotCountDownView.TimingListener() {
            @Override
            public void endingTheTimer() {
                mDotView.setVisibility(View.GONE);
                mPresenter.record_kSingAction();
            }
        });

        mDotView.startCountDown();
    }

    @Override
    public void updateLrc(List<LrcRow> lrcRowList) {
        if (EmptyUtils.isEmpty(lrcRowList))
            return;

        if (bAChorusOfRecording) {
            mLyricOfChorus.setLrcRows(lrcRowList);
        } else {
            mLyricOfSingle.setLrcRows(lrcRowList);
        }
    }

    @Override
    public void prepareDataOfGrade() {
        mPresenter.grade_setSizeOfGradeView(mIntonationView.getStartingPositionOfVLine(),
                mIntonationView.getWidthOfIntonationView(),
                mIntonationView.getNumOfPitchLine());
    }

    @Override
    public void turn2NextLyric() {
        mIntonationView.jumpToTheNextSentence();
    }

    @Override
    public void switchRoleA() {
        Timber.tag("switchRole").d("角色A");
        if (mKSingRGVEntity.getLayouttype() == R.integer.chorustype_frontback) {
            layout_switchSingingTheRole_FrontBack(bTheActorRoleA);
        } else if (mKSingRGVEntity.getLayouttype() == R.integer.chorustype_leftright) {
            mLeftWidth = bTheActorRoleA ? mMVSize * 2 / 3 : mMVSize / 3;
            layout_switchSingingTheRole_LeftRight();
        }
    }

    @Override
    public void switchRoleB() {
        Timber.tag("switchRole").d("角色B");
        if (mKSingRGVEntity.getLayouttype() == R.integer.chorustype_frontback) {
            layout_switchSingingTheRole_FrontBack(!bTheActorRoleA);
        } else if (mKSingRGVEntity.getLayouttype() == R.integer.chorustype_leftright) {
            mLeftWidth = bTheActorRoleA ? mMVSize / 3 : mMVSize * 2 / 3;
            layout_switchSingingTheRole_LeftRight();
        }
    }

    @Override
    public void switchChorus() {
        Timber.tag("switchRole").d("合唱");
        if (mKSingRGVEntity.getLayouttype() == R.integer.chorustype_frontback) {

        } else if (mKSingRGVEntity.getLayouttype() == R.integer.chorustype_leftright) {
            mLeftWidth = mMVSize / 2;
            layout_switchSingingTheRole_LeftRight();
        }
    }

    @Override
    public void initIntonationIs(SparseArray<List<GradeBean>> hashMap) {
        mIntonationView.setData(hashMap);
    }

    @Override
    public void updateIntonationIs(final GradeBean gradeBean, final float positionSecX) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIntonationView.updateRatingData(gradeBean, positionSecX);
            }
        });

    }

    @Override
    public void updatePictureOfMidVoice() {
        Drawable drawable = getResources().getDrawable(mPresenter.getVoicePictureOfMid());
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mTvMidVoice.setCompoundDrawables(null, drawable, null, null);
    }

    @Override
    public void startMVRecord(String recordpath,
                              CameraRecordGLSurfaceView.StartRecordingCallback startRecordingCallback,
                              CameraRecordGLSurfaceView.YUVCallback yuvCallback) {
        previewview.startRecording(recordpath, startRecordingCallback, yuvCallback);
    }

    @Override
    public void playChorusVideo() {
        if (null == mKSingRGVEntity)
            return;

        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(mKSingRGVEntity.getChorusvideopath(),
                MediaStore.Images.Thumbnails.MINI_KIND);

        playview.setUp(mKSingRGVEntity.getChorusvideopath(), JZVideoPlayer.SCREEN_WINDOW_NORMAL, "");
        playview.thumbImageView.setImageBitmap(bitmap);
        playview.startButton.performClick();
    }

    @Override
    public void updatePlayProgress(float progress, float totalTime) {
        int curProgress = Math.round(progress) * 1000;
        int curTotalTime = Math.round(totalTime) * 1000;

        //调整歌词进度
        if (bAChorusOfRecording) {
            mLyricOfChorus.seekTo(curProgress, false, false);
        } else {
            mLyricOfSingle.seekTo(curProgress, false, false);
        }

        //调整时间进度
        progressBar.setMax(curTotalTime);
        progressBar.setProgress(curProgress);

        //设置歌曲时长
        String curTimeStr = TimeUtils.millis2String(curProgress, "mm:ss");
        String totalTimeStr = TimeUtils.millis2String(curTotalTime, "mm:ss");
        tvCurtime.setText(curTimeStr);
        tvTotaltime.setText(totalTimeStr);

        if (progress >= totalTime && progress > 0) {
            record_checkTime();
        }

    }

    @Override
    public void stopMVRecord(CameraRecordGLSurfaceView.EndRecordingCallback endRecordingCallback, boolean shouldSave) {
        previewview.endRecording(endRecordingCallback, shouldSave);
    }

    @Override
    public void record_checkTime() {
        String recordTimeStr = tvCurtime.getText().toString();
        String totalTimeStr = tvTotaltime.getText().toString();
        long recordTimeMillis = ConvertUtils.mmssString2millis(recordTimeStr);
        long totalTimeMillis = ConvertUtils.mmssString2millis(totalTimeStr);

        //个人演唱：至少30s
        //合唱第一人：整首
        //合唱第二人：至少30s
//        if ((mKSingRGVEntity.getWorkstype() == getResources().getInteger(R.integer.workstype_chrousa)
//                || mKSingRGVEntity.getWorkstype() == getResources().getInteger(R.integer.workstype_chrousb))
//                && recordTimeMillis < totalTimeMillis) {
//            showMessage(getString(R.string.error_recordall));
//            return;
//        } else if (recordTimeMillis < getResources().getInteger(R.integer.ksingwithmv_lowerlimit) * 1000) {
//            showMessage(getString(R.string.error_recordmvtime));
//            return;
//        }

        mPresenter.record_kSingEnd();
    }

    @Override
    public void showScoreResult() {
        int worktype = mKSingRGVEntity.getWorkstype();
        if (worktype != getResources().getInteger(R.integer.workstype_common)
                && worktype != getResources().getInteger(R.integer.workstype_cantata)) {
            turn2TobeAnnounce(mPresenter.getRecordVideoPath(), mPresenter.getRecordAudioPath());
            return;
        }
        ScoreResultFragment dialog = new ScoreResultFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("totalscore", mPresenter.getCurrentScore());
        bundle.putParcelable("averagescore", mPresenter.getAverageScore());
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "scoreresult");
        dialog.setEventListener(new ScoreResultFragment.ScoreResultEventListener() {
            @Override
            public void clickClose() {
                turn2TobeAnnounce(mPresenter.getRecordVideoPath(), mPresenter.getRecordAudioPath());
            }
        });
    }

    @Override
    public void turn2TobeAnnounce(String pathOfVideo, String pathOfAudio) {

        String totalTimeStr = tvCurtime.getText().toString();
        long totalTimeMillis = ConvertUtils.mmssString2millis(totalTimeStr);

        Intent tobeAnnoIntent = new Intent(this, ToBeAnnoWithMVActivity.class);
        mKSingRGVEntity.setRecordmvpath(pathOfVideo);
        mKSingRGVEntity.setRecordaudiopath(pathOfAudio);
        mKSingRGVEntity.setRecordtime((int) totalTimeMillis);

        Bundle bundle = new Bundle();
        bundle.putParcelable("songinfo", mSongInfo);
        bundle.putParcelable("ksingrgv", mKSingRGVEntity);
        bundle.putSerializable("mvfilter", filterType);
        bundle.putParcelable("scoreresult", mPresenter.getCurrentScore());
        tobeAnnoIntent.putExtras(bundle);
        launchActivity(tobeAnnoIntent);
        killMyself();
    }

    //初始化歌词
    private void init_lrcView() {
        //歌词背景设置透明
        mViewExpandBg.setBackgroundResource(R.color.black);
        mViewExpandBg.getBackground().mutate().setAlpha(0);
        //歌词
        mLyricOfChorus.setVisibility(bAChorusOfRecording ? View.VISIBLE : View.GONE);
        mLyricOfSingle.setVisibility(bAChorusOfRecording ? View.GONE : View.VISIBLE);

        //合成时需要用户头像，没有登录就返回，用默认头像
        if (!bAChorusOfRecording || getString(R.string.userid_notlogin).equals(mWeApplication.getUserID())) {
            return;
        }

        Glide.with(this)
                .load(StringHelper.getImageUrl(mWeApplication.getUserInfoBean().getUser_photo()))
                .asBitmap()
                .centerCrop()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mLoginUserBitmap = resource;
                        init_lyricAvatar();
                    }
                });

        Glide.with(this)
                .load(StringHelper.getImageUrl(mSongInfo.getUser_photo()))
                .asBitmap()
                .centerCrop()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mSponsorBitmap = resource;
                        init_lyricAvatar();
                    }
                });
    }

    //初始化歌词头像
    private void init_lyricAvatar() {
        int workstype = mKSingRGVEntity.getWorkstype();
        if (workstype == getResources().getInteger(R.integer.workstype_chrousa)) {
            //1合唱第一人唱A
            mLyricOfChorus.setRoleAAvatar(mLoginUserBitmap);
            bTheActorRoleA = true;

        } else if (workstype == getResources().getInteger(R.integer.workstype_chrousb)) {
            //2合唱第一人唱B
            mLyricOfChorus.setRoleBBitmap(mLoginUserBitmap);
        } else if (workstype == getResources().getInteger(R.integer.workstype_chrousother)) {
            //3合唱第二人
            int worksTypeOfSponsor = EmptyUtils.isEmpty(mSongInfo.getWorks_type())
                    ? getResources().getInteger(R.integer.workstype_chrousa)
                    : Integer.parseInt(mSongInfo.getWorks_type());

            if (worksTypeOfSponsor == getResources().getInteger(R.integer.workstype_chrousa)) {
                mLyricOfChorus.setRoleAAvatar(mSponsorBitmap);
                mLyricOfChorus.setRoleBBitmap(mLoginUserBitmap);
            } else if (worksTypeOfSponsor == getResources().getInteger(R.integer.workstype_chrousb)) {
                mLyricOfChorus.setRoleAAvatar(mLoginUserBitmap);
                mLyricOfChorus.setRoleBBitmap(mSponsorBitmap);
                bTheActorRoleA = true;
            }
        }
    }

    //填充数据
    private void init_fillingData() {
        //歌曲名称
        headerview.setTitleName(StringHelper.getLau_With_J_U_C(mSongInfo.getSong_name_jp()
                , mSongInfo.getSong_name_us(), mSongInfo.getSong_name_cn()));

        //mid音量
        updatePictureOfMidVoice();
    }

    //开启摄像头预览
    private void record_openCameraPreview() {
        previewview.presetCameraForward(!mKSingRGVEntity.isOpenFrontCamera());
        previewview.presetRecordingSize(480, 640);
        previewview.setZOrderOnTop(false);
        previewview.setZOrderMediaOverlay(true);
        previewview.setFitFullView(true);
        previewview.setOnCreateCallback(new CameraGLSurfaceView.OnCreateCallback() {
            @Override
            public void createOver(boolean success) {
                if (!success) {
                    Timber.e("Camera打开失败！！！");
                    killMyself();
                    return;
                }

                if (null != mvConfig)
                    previewview.setFilterWithConfig(mvConfig.getConfig());

                mPresenter.record_startRecordingDelay();
            }
        });

    }

    //关闭相机预览和播放器
    private void record_closeMediaResource() {
        if (playview.getVisibility() == View.VISIBLE) {
            JZVideoPlayer.releaseAllVideos();
        }

        CameraInstance.getInstance().stopCamera();
        previewview.release(null);
        previewview.onPause();
    }

    //测量布局
    private void layout_measure() {
        previewview.onResume();

        final int screenWidth = (int) DeviceUtils.getScreenWidth(this);
        final int screenHeight = (int) DeviceUtils.getScreenHeight(this);
        ViewTreeObserver viewTreeObserver = rlMv.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rlMv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int viewwidth = rlMv.getWidth();
                int viewheight = rlMv.getHeight();
                mMVSize = Math.min(viewwidth, viewheight);
                if (viewwidth < viewheight) {
                    mFullMVHeight = viewwidth;
                    mFullMVWidth = viewwidth * screenWidth / screenHeight;
                } else {
                    mFullMVHeight = viewheight;
                    mFullMVWidth = viewheight * screenWidth / screenHeight;
                }

                layout_changeMVSize();
            }
        });
    }

    //改变MV大小
    private void layout_changeMVSize() {
        switch (mKSingRGVEntity.getLayouttype()) {
            case R.integer.chorustype_leftright://左右
                layout_switch2LeftRightView();
                break;
            case R.integer.chorustype_fullscreen://独占
                layout_switch2FullView();
                break;
            case R.integer.chorustype_frontback://前后
                layout_switch2FrontBack();
                break;
            default:
                layout_setMVPartVisiable(true, false);
                break;
        }

        record_openCameraPreview();
    }

    /**
     * 设置预览或者播放视图是否可见
     *
     * @param showPreview
     * @param showPlayer
     */
    private void layout_setMVPartVisiable(boolean showPreview, boolean showPlayer) {
        previewview.setVisibility(showPreview ? View.VISIBLE : View.GONE);
        playview.setVisibility(showPlayer ? View.VISIBLE : View.GONE);
    }

    /**
     * 切换到左右
     */
    private void layout_switch2LeftRightView() {
        RelativeLayout.LayoutParams recordViewParams = (RelativeLayout.LayoutParams) previewview.getLayoutParams();
        recordViewParams.width = mMVSize * 2 / 3;
        recordViewParams.height = mMVSize;
        recordViewParams.addRule(RelativeLayout.CENTER_VERTICAL);
        recordViewParams.leftMargin = (int) ((DeviceUtils.getScreenWidth(this) - mMVSize) / 2);
        previewview.setLayoutParams(recordViewParams);

        RelativeLayout.LayoutParams playerViewParams = (RelativeLayout.LayoutParams) playview.getLayoutParams();
        playerViewParams.width = mMVSize / 3;
        playerViewParams.height = mMVSize;
        playerViewParams.addRule(RelativeLayout.CENTER_VERTICAL);
        playerViewParams.addRule(RelativeLayout.RIGHT_OF, previewview.getId());
        playview.setLayoutParams(playerViewParams);

        layout_setMVPartVisiable(true, true);
    }

    /**
     * 切换到前后
     */
    private void layout_switch2FrontBack() {
        RelativeLayout.LayoutParams recordViewParams = (RelativeLayout.LayoutParams) previewview.getLayoutParams();
        recordViewParams.width = mMVSize;
        recordViewParams.height = mMVSize;
        recordViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        previewview.setLayoutParams(recordViewParams);

        RelativeLayout.LayoutParams playerViewParams = (RelativeLayout.LayoutParams) playview.getLayoutParams();
        playerViewParams.width = mMVSize / 3;
        playerViewParams.height = mMVSize / 3;
        playerViewParams.addRule(RelativeLayout.ALIGN_RIGHT, previewview.getId());
        playerViewParams.addRule(RelativeLayout.ALIGN_BOTTOM, previewview.getId());
        playview.setLayoutParams(playerViewParams);

        layout_setMVPartVisiable(true, true);

    }

    /**
     * 切换到全屏
     */
    private void layout_switch2FullView() {

        RelativeLayout.LayoutParams recordViewParams = (RelativeLayout.LayoutParams) previewview.getLayoutParams();
        recordViewParams.width = mFullMVWidth;
        recordViewParams.height = mFullMVHeight;
        recordViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        previewview.setLayoutParams(recordViewParams);

        RelativeLayout.LayoutParams focusBoxParams = (RelativeLayout.LayoutParams) focusBox.getLayoutParams();
        focusBoxParams.width = mFullMVWidth;
        focusBoxParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        focusBox.setLayoutParams(focusBoxParams);

        layout_setMVPartVisiable(true, false);

        focusBox.setVisibility(View.VISIBLE);

    }

    //左右：切换演唱的角色
    private void layout_switchSingingTheRole_LeftRight() {
        int lastPreviewWidth = previewview.getWidth();
        int deff = mLeftWidth - lastPreviewWidth;
        ValueAnimator animator = ValueAnimator.ofInt(lastPreviewWidth, mLeftWidth);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (int) animation.getAnimatedValue();
                RelativeLayout.LayoutParams participantParams = (RelativeLayout.LayoutParams) previewview.getLayoutParams();
                participantParams.width = currentValue;
                previewview.setLayoutParams(participantParams);

                RelativeLayout.LayoutParams promulgatorParams = (RelativeLayout.LayoutParams) playview.getLayoutParams();
                promulgatorParams.width = mMVSize - currentValue;
                playview.requestLayout();
            }
        });
        animator.start();
    }

    //前后：切换演唱的角色
    //变化过程：小视频由mMVSize/3变成mMvSize，尺寸改变结束时，设置大小视频的布局规则。然后大视频由0变成mMVSize/3。
    private void layout_switchSingingTheRole_FrontBack(final boolean showChorusParticipant) {

        //小视频变化
        ValueAnimator smallVideoAnimator = ValueAnimator.ofInt(mMVSize / 3, mMVSize);
        smallVideoAnimator.setDuration(500);
        smallVideoAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (int) animation.getAnimatedValue();
                //判断当前演唱角色合唱参与者还是发布者
                if (showChorusParticipant) {
                    RelativeLayout.LayoutParams participantParams = (RelativeLayout.LayoutParams) previewview.getLayoutParams();
                    participantParams.width = currentValue;
                    participantParams.height = currentValue;
                    previewview.setLayoutParams(participantParams);
                } else {
                    RelativeLayout.LayoutParams promulgatorParams = (RelativeLayout.LayoutParams) playview.getLayoutParams();
                    promulgatorParams.width = currentValue;
                    promulgatorParams.height = currentValue;
                    playview.setLayoutParams(promulgatorParams);
                }

            }
        });
        //小视频动画监听
        smallVideoAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Timber.tag("switchRole").d("小视频变大结束");
                RelativeLayout.LayoutParams promulgatorParams = (RelativeLayout.LayoutParams) playview.getLayoutParams();
                RelativeLayout.LayoutParams participantParams = (RelativeLayout.LayoutParams) previewview.getLayoutParams();
                if (showChorusParticipant) {
                    participantParams.removeRule(RelativeLayout.ALIGN_RIGHT);
                    participantParams.removeRule(RelativeLayout.ALIGN_BOTTOM);
                    participantParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    previewview.setLayoutParams(participantParams);

                    promulgatorParams.removeRule(RelativeLayout.CENTER_IN_PARENT);
                    promulgatorParams.addRule(RelativeLayout.ALIGN_RIGHT, previewview.getId());
                    promulgatorParams.addRule(RelativeLayout.ALIGN_BOTTOM, previewview.getId());
                    promulgatorParams.width = 1;
                    promulgatorParams.height = 1;
                    playview.setLayoutParams(promulgatorParams);

                    playview.bringToFront();
                } else {
                    promulgatorParams.removeRule(RelativeLayout.ALIGN_RIGHT);
                    promulgatorParams.removeRule(RelativeLayout.ALIGN_BOTTOM);
                    promulgatorParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    playview.setLayoutParams(promulgatorParams);

                    participantParams.removeRule(RelativeLayout.CENTER_IN_PARENT);
                    participantParams.addRule(RelativeLayout.ALIGN_RIGHT, playview.getId());
                    participantParams.addRule(RelativeLayout.ALIGN_BOTTOM, playview.getId());
                    participantParams.width = 1;
                    participantParams.height = 1;
                    previewview.setLayoutParams(participantParams);

                    previewview.bringToFront();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        //当前大视频的动画
        ValueAnimator bigVideoAnimation = ValueAnimator.ofInt(1, mMVSize / 3);
        bigVideoAnimation.setDuration(500);
        bigVideoAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (int) animation.getAnimatedValue();
                //判断当前演唱角色合唱参与者还是发布者
                if (showChorusParticipant) {
                    RelativeLayout.LayoutParams promulgatorParams = (RelativeLayout.LayoutParams) playview.getLayoutParams();
                    promulgatorParams.width = currentValue;
                    promulgatorParams.height = currentValue;
                    playview.setLayoutParams(promulgatorParams);
                } else {
                    RelativeLayout.LayoutParams participantParams = (RelativeLayout.LayoutParams) previewview.getLayoutParams();
                    participantParams.width = currentValue;
                    participantParams.height = currentValue;
                    previewview.setLayoutParams(participantParams);
                }
            }
        });
        //将两个动画添加到动画集
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(smallVideoAnimator).before(bigVideoAnimation);
        animatorSet.start();
    }

    //歌词展开和折叠
    private void lyricExpandingAndCollapsing() {
        bStatusOfLyric = !bStatusOfLyric;

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mRlLyric.getLayoutParams();
        if (bStatusOfLyric) {
            mViewExpandBg.getBackground().mutate().setAlpha(180);
            layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, rlMv.getId());
        } else {
            mViewExpandBg.getBackground().mutate().setAlpha(0);
            layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, mIvTopBg.getId());
        }
        mRlLyric.setLayoutParams(layoutParams);
    }

}

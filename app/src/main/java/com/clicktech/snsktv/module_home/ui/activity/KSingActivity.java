package com.clicktech.snsktv.module_home.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.ConvertUtils;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.TimeUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.EventBusTag;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_home.contract.KSingContract;
import com.clicktech.snsktv.module_home.inject.component.DaggerKSingComponent;
import com.clicktech.snsktv.module_home.inject.module.KSingModule;
import com.clicktech.snsktv.module_home.presenter.KSingPresenter;
import com.clicktech.snsktv.module_home.ui.fragment.ScoreResultFragment;
import com.clicktech.snsktv.util.StringHelper;
import com.clicktech.snsktv.widget.animaoftiming.DotCountDownView;
import com.clicktech.snsktv.widget.dialog.DownloadDialog;
import com.clicktech.snsktv.widget.titlebar.HeaderView;
import com.jaeger.library.StatusBarUtil;
import com.library.multimedia.lyricscontrols.parser.LrcRow;
import com.library.multimedia.lyricscontrols.view.LyricOfChorusImpl;
import com.library.multimedia.lyricscontrols.view.LyricOfSingleImpl;
import com.library.multimedia.soundercontrols.GradeBean;
import com.library.multimedia.soundercontrols.IntonationView;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;

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
 * Created by Administrator on 2017/3/9.
 * k歌界面_分支
 */

public class KSingActivity extends WEActivity<KSingPresenter> implements KSingContract.View,
        View.OnClickListener, HeaderView.OnCustomTileListener {

    @BindView(R.id.headerview)
    HeaderView mHeaderView;
    @BindView(R.id.intonationview)
    IntonationView mIntonationView;
    @BindView(R.id.dotView)
    DotCountDownView mDotView;
    @BindView(R.id.lyricofchorus)
    LyricOfChorusImpl mLyricOfChorus;
    @BindView(R.id.lyricofsingle)
    LyricOfSingleImpl mLyricOfSingle;
    @BindView(R.id.iv_cover)
    ImageView mIvCover;
    @BindView(R.id.tv_singname)
    TextView mTvSingName;
    @BindView(R.id.tv_singername)
    TextView mTvSingerName;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.tv_curtime)
    TextView mCurTimeTv;
    @BindView(R.id.tv_totaltime)
    TextView mTotalTimeTv;
    @BindView(R.id.tv_mppaused)
    TextView mMppausedTv;
    @BindView(R.id.tv_tone)
    TextView mTvTone;
    @BindView(R.id.tv_midvoice)
    TextView mTvMidVoice;
    @BindView(R.id.rl_lyric)
    RelativeLayout mRlLyric;
    @BindView(R.id.rl_cover)
    RelativeLayout mRlCover;
    @BindView(R.id.iv_topbg)
    ImageView mIvTopBg;
    @BindView(R.id.view_expandbg)
    View mViewExpandBg;

    private RxPermissions mRxPermissions;
    private SongInfoBean mSongInfo;//歌曲信息
    private int mWorksType;
    private DownloadDialog downloadDialog;
    private boolean isChorus;//是合唱
    private Bitmap mLoginUserBitmap;//登录用户的bitmap
    private Bitmap mSponsorBitmap;//合唱发起者bitmap
    private boolean bStatusOfLyric;//歌词状态：true展开；false折叠

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions(this);
        DaggerKSingComponent
                .builder()
                .appComponent(appComponent)
                .kSingModule(new KSingModule(this)) //请将KSingModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_ksing2, null, false);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this,
                getResources().getInteger(R.integer.statusalpha_a), mHeaderView);
    }

    @Override
    protected void initData() {
        JZVideoPlayer.releaseAllVideos();
        EventBus.getDefault().post(new Message(), EventBusTag.MINIPLAYER_HIDE);
        Bundle arguments = getIntent().getExtras();
        if (null == arguments)
            return;

        mSongInfo = arguments.getParcelable("songinfo");
        mWorksType = arguments.getInt("workstype");

        if (null == mSongInfo)
            return;

        initLrcView();
        mPresenter.requestdata(mSongInfo);
        updateView(mSongInfo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.record_onResumeRecord();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.record_onPauseRecord();
    }

    private void updateView(SongInfoBean singInfo) {
        //歌词背景设置透明
        mViewExpandBg.setBackgroundResource(R.color.black);
        mViewExpandBg.getBackground().mutate().setAlpha(0);
        //歌曲、歌手名称
        mHeaderView.setTitleName(StringHelper.getLau_With_J_U_C(singInfo.getSong_name_jp()
                , singInfo.getSong_name_us(), singInfo.getSong_name_cn()));
        mHeaderView.setTitleClickListener(this);
        mTvSingName.setText(StringHelper.getLau_With_J_U_C(singInfo.getSong_name_jp()
                , singInfo.getSong_name_us(), singInfo.getSong_name_cn()));
        mTvSingerName.setText(StringHelper.getLau_With_J_U_C(singInfo.getSinger_name_jp()
                , singInfo.getSinger_name_us(), singInfo.getSinger_name_cn()));

        //歌曲图片
        if (EmptyUtils.isNotEmpty(singInfo.getSong_image())) {
            mWeApplication.getAppComponent().imageLoader().loadImage(this,
                    GlideImageConfig.builder()
                            .url(singInfo.getSong_image())
                            .imageView(mIvCover)

                            .build()
            );
        }

        //mid音量
        updatePictureOfMidVoice();

    }

    //初始化歌词
    private void initLrcView() {
        //歌词
        isChorus = mWorksType != getResources().getInteger(R.integer.workstype_common)
                && mWorksType != getResources().getInteger(R.integer.workstype_cantata);
        mLyricOfChorus.setVisibility(isChorus ? View.VISIBLE : View.GONE);
        mLyricOfSingle.setVisibility(isChorus ? View.GONE : View.VISIBLE);

        if (!isChorus || getString(R.string.userid_notlogin).equals(mWeApplication.getUserID())) {
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
        if (mWorksType == getResources().getInteger(R.integer.workstype_chrousa)) {
            //1合唱第一人唱A
            mLyricOfChorus.setRoleAAvatar(mLoginUserBitmap);
        } else if (mWorksType == getResources().getInteger(R.integer.workstype_chrousb)) {
            //2合唱第一人唱B
            mLyricOfChorus.setRoleBBitmap(mLoginUserBitmap);
        } else if (mWorksType == getResources().getInteger(R.integer.workstype_chrousother)) {
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
            }
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        UiUtils.startActivity(this, intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    protected void onDestroy() {
        mLoginUserBitmap = null;
        mSponsorBitmap = null;

        mDotView.endCountDown();
        super.onDestroy();
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
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
    public void dismissNumberDialog() {
        if (null != downloadDialog) {
            downloadDialog.dismiss();
            downloadDialog = null;
        }
    }

    @Override
    public void updateNumberDialog(float progress) {
        if (null != downloadDialog && progress <= 100) {
            downloadDialog.setTvProgress((int) progress);
        }
    }

    @Override
    public void updatePictureOfMidVoice() {
        Drawable drawable = getResources().getDrawable(mPresenter.record_getVoicePictureOfMid());
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mTvMidVoice.setCompoundDrawables(null, drawable, null, null);
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
        if (EmptyUtils.isEmpty(lrcRowList)) {
            return;
        }

        if (isChorus) {
            mLyricOfChorus.setLrcRows(lrcRowList);
        } else {
            mLyricOfSingle.setLrcRows(lrcRowList);
        }

    }

    @Override
    public void updatePlayProgress(float progress, float totalTime) {
        int curProgress = Math.round(progress) * 1000;
        int curTotalTime = Math.round(totalTime) * 1000;

        //调整歌词进度
        if (isChorus) {
            mLyricOfChorus.seekTo(curProgress, false, false);
        } else {
            mLyricOfSingle.seekTo(curProgress, false, false);
        }

        //调整时间进度
        mProgressBar.setMax(curTotalTime);
        mProgressBar.setProgress(curProgress);

        //设置歌曲时长
        String curTimeStr = TimeUtils.millis2String(curProgress, "mm:ss");
        String totalTimeStr = TimeUtils.millis2String(curTotalTime, "mm:ss");
        mCurTimeTv.setText(curTimeStr);
        mTotalTimeTv.setText(totalTimeStr);

        if (progress >= totalTime) {
            mPresenter.record_kSingEnd();
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
    public void prepareDataOfGrade() {
        mPresenter.grade_setDataToBeGraded(mIntonationView.getStartingPositionOfVLine(),
                mIntonationView.getWidthOfIntonationView(),
                mIntonationView.getNumOfPitchLine());
    }

    @Override
    public void turn2NextLyric() {
        mIntonationView.jumpToTheNextSentence();
    }

    @OnClick({R.id.tv_over, R.id.tv_tone, R.id.tv_mppaused, R.id.tv_reset, R.id.tv_midvoice,
            R.id.view_externallink, R.id.view_control_expandfold, R.id.view_expandbg})
    public void onClick(View v) {
        if (!mPresenter.getStatusOfPrepare()) {
            return;
        }

        switch (v.getId()) {
            case R.id.tv_mppaused://切换模式
                mPresenter.showSwitchModeDialog(KSingActivity.this);
                break;
            case R.id.tv_tone://声调
                mPresenter.record_setRisingFallingTone(this, mTvTone);
                break;
            case R.id.tv_reset://重置
                mPresenter.showTipDialog(this, getString(R.string.dialog_rerecord), 2);
                break;
            case R.id.tv_over://结束录制
                onTvOverClicked();
                break;
            case R.id.tv_midvoice://mid音量
                mPresenter.record_setVoiceOfMid();
                break;
            case R.id.view_externallink://跳转到itunes
                turn2ExternalLink();
                break;
            case R.id.view_control_expandfold://歌词展开和折叠
                lyricExpandingAndCollapsing();
                break;
            case R.id.view_expandbg://播放、暂停
                if (mPresenter.record_getPlayStatus()) {
                    showMessage(getString(R.string.tip_touchlyric));
                }
                mPresenter.record_pauseAndPlayRecord();
                break;
        }
    }

    @Override
    public void setTitleLeftClick() {
        mPresenter.showTipDialog(this, getString(R.string.dialog_cantata_back), 4);
    }

    @Override
    public void setTitleRightClick() {
    }

    @Subscriber(tag = EventBusTag.SWITCHMODE, mode = ThreadMode.MAIN)
    public void onSwitchModeEvent(Message message) {
        killMyself();
    }

    //结束录制
    private void onTvOverClicked() {
        String recordtimeStr = mCurTimeTv.getText().toString();
        long recordtime = ConvertUtils.mmssString2millis(recordtimeStr);
        if (recordtime < getResources().getInteger(R.integer.singtime) * 1000) {
            showMessage(getResources().getString(R.string.error_recordtime));
            return;
        }

        mPresenter.showTipDialog(this, getString(R.string.dialog_finishrecord), 3);
    }

    @Override
    public void showScoreResult() {
        if (mWorksType != getResources().getInteger(R.integer.workstype_common)
                && mWorksType != getResources().getInteger(R.integer.workstype_cantata)) {
            turn2Publish();
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
                turn2Publish();
            }
        });
    }

    @Override
    public void turn2Publish() {
        Intent tobeAnnoIntent = new Intent(this, ToBeAnnounceActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("songinfo", mSongInfo);
        bundle.putInt("workstype", mWorksType);
        bundle.putString("audiopath", mPresenter.getRecordPath());
        bundle.putString("accompanypath", mPresenter.getAccompanyPath());
        bundle.putParcelable("scoreresult", mPresenter.getCurrentScore());
        tobeAnnoIntent.putExtras(bundle);
        launchActivity(tobeAnnoIntent);
        killMyself();
    }

    //跳转到外部链接
    private void turn2ExternalLink() {
        if (EmptyUtils.isEmpty(mSongInfo.getMusic_google_com())) {
            return;
        }

        Uri uri = Uri.parse(mSongInfo.getMusic_google_com());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    //歌词展开和折叠
    private void lyricExpandingAndCollapsing() {
        bStatusOfLyric = !bStatusOfLyric;

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mRlLyric.getLayoutParams();
        if (bStatusOfLyric) {
            mViewExpandBg.getBackground().mutate().setAlpha(180);
            layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, mRlCover.getId());
        } else {
            mViewExpandBg.getBackground().mutate().setAlpha(0);
            layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, mIvTopBg.getId());
        }
        mRlLyric.setLayoutParams(layoutParams);
    }

}

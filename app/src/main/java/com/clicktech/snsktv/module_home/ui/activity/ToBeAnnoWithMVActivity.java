package com.clicktech.snsktv.module_home.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.TimeUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.recyclerview.FullyGridLayoutManager;
import com.clicktech.snsktv.common.EventBusTag;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.entity.KSingRGVEntity;
import com.clicktech.snsktv.entity.PublishInforEntity;
import com.clicktech.snsktv.entity.ScoreResultEntity;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.entity.SoundReverbEntity;
import com.clicktech.snsktv.module_discover.ui.helper.TemplateAndFilterType;
import com.clicktech.snsktv.module_enter.ui.activity.LoginActivity;
import com.clicktech.snsktv.module_enter.ui.activity.MainActivity;
import com.clicktech.snsktv.module_home.contract.ToBeAnnoWithMVContract;
import com.clicktech.snsktv.module_home.inject.component.DaggerToBeAnnoWithMVComponent;
import com.clicktech.snsktv.module_home.inject.module.ToBeAnnoWithMVModule;
import com.clicktech.snsktv.module_home.presenter.ToBeAnnoWithMVPresenter;
import com.clicktech.snsktv.module_home.ui.adapter.SoundEffectAdapter;
import com.clicktech.snsktv.module_home.ui.holder.ReverbHolderView;
import com.clicktech.snsktv.widget.dialog.DownloadDialog;
import com.clicktech.snsktv.widget.dialog.NetworkDialog;
import com.clicktech.snsktv.widget.seekbar.IssueSeekBar;
import com.clicktech.snsktv.widget.titlebar.HeaderView;
import com.clicktech.snsktv.widget.videoplayer.TobeAnnoPlayer;
import com.clicktech.snsktv.widget.voiceoffset.VoiceOffsetView;
import com.jaeger.library.StatusBarUtil;
import com.jakewharton.rxbinding.widget.RxSeekBar;

import org.simple.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;
import rx.functions.Action1;

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
 * Created by Administrator on 2017-05-25.
 * MV录制（单人）
 */

public class ToBeAnnoWithMVActivity extends WEActivity<ToBeAnnoWithMVPresenter> implements
        ToBeAnnoWithMVContract.View, View.OnClickListener, HeaderView.OnCustomTileListener,
        ViewPager.OnPageChangeListener {

    public static final int RequestCode_Login = 101;
    @BindView(R.id.cbanner_reverb)
    ConvenientBanner mReverbBanner;
    @BindView(R.id.cb_status)
    CheckBox mCbStatus;
    @BindView(R.id.sBar_playprogress)
    IssueSeekBar mPlayProgress;
    @BindView(R.id.tv_playprogress)
    TextView mTvPlayProgress;
    @BindView(R.id.sbar_voicevol)
    SeekBar mVoiceVol;
    @BindView(R.id.sbar_accvol)
    SeekBar mAccVol;
    @BindView(R.id.rv_inflexion)
    RecyclerView mRvInflexion;
    @BindView(R.id.tv_remake)
    TextView mTvRemake;
    @BindView(R.id.headerview)
    HeaderView mHeaderView;
    @BindView(R.id.playerSurfaceView)
    TobeAnnoPlayer playerGLSurfaceView;
    @BindView(R.id.ll_voicemove)
    LinearLayout mLLVoiceMove;
    @BindView(R.id.iv_leftmove)
    ImageView mIvLeftMove;
    @BindView(R.id.iv_rightmove)
    ImageView mIvRightMove;
    @BindView(R.id.view_voiceoffset)
    VoiceOffsetView mViewVoiceOffset;
    @BindView(R.id.rl_video)
    RelativeLayout rlVideo;

    private ScoreResultEntity mScoreResult;//打分结果
    private SongInfoBean mSingInfo;//歌曲信息
    private KSingRGVEntity mKSingRGVEntity;//录制的信息
    private TemplateAndFilterType filterType;
    private boolean mAddVideo;//是否为追加视频的跳转

    private DownloadDialog downloadDialog;
    private NetworkDialog networkDialog;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerToBeAnnoWithMVComponent
                .builder()
                .appComponent(appComponent)
                .toBeAnnoWithMVModule(new ToBeAnnoWithMVModule(this)) //请将ToBeAnnoWithMVModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this,
                getResources().getInteger(R.integer.statusalpha_a), mHeaderView);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_tobeannounce_withmv, null, false);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null == bundle)
            return;

        mSingInfo = bundle.getParcelable("songinfo");
        mKSingRGVEntity = bundle.getParcelable("ksingrgv");
        filterType = (TemplateAndFilterType) bundle.getSerializable("mvfilter");
        mAddVideo = bundle.getBoolean("addvideo");
        mScoreResult = bundle.getParcelable("scoreresult");

        if (null == mSingInfo || null == mKSingRGVEntity)
            return;

        networkDialog = new NetworkDialog(this);
        setInitView();
        mPresenter.requestData(mSingInfo, mKSingRGVEntity, mScoreResult);
    }

    //设置初始化界面
    private void setInitView() {
        //标题点击监听
        mHeaderView.setTitleClickListener(this);
        //播放/暂停
        mCbStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.audio_pauseAndPlay();
            }
        });

        //播放进度条、时间进度
        if (mKSingRGVEntity.getRecordtime() > 0) {
            mPlayProgress.setMax(mKSingRGVEntity.getRecordtime());
            String totalTimeStr = TimeUtils.millis2String(mKSingRGVEntity.getRecordtime(), "mm:ss");
            mTvPlayProgress.setText(totalTimeStr);
        }

        mPlayProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPresenter.audition_jumpToSpecifiedLocation(seekBar.getProgress());
            }
        });

        //人声音量
        RxSeekBar.userChanges(mVoiceVol)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer progress) {
                        float volume = progress / 10.0f;
                        mPresenter.setAuditionVolume(volume);
                    }
                });

        //伴奏音量
        RxSeekBar.userChanges(mAccVol)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        float progress = integer / 10.0f;
                        mPresenter.setBgmusicVolume(progress);
                    }
                });

        //视频播放器
        init_measurePlayerWide();
    }

    @Override
    public void showLoading() {
        showRequestDialog();
    }

    @Override
    public void hideLoading() {
        hideRequestDialog();
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
    public void setTitleLeftClick() {
        mPresenter.showTipDialog(this, getString(R.string.dialog_cantata_back), 0);
    }

    @Override
    public void setTitleRightClick() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(mRvInflexion);
        JZVideoPlayer.releaseAllVideos();

        hideRequestDialog();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RequestCode_Login == requestCode && mWeApplication.loggingStatus()) {
            mPresenter.upload_stitchingParameters();
        }
    }

    @OnClick({R.id.tv_save, R.id.tv_remake, R.id.tv_publish,
            R.id.iv_leftmove, R.id.iv_rightmove})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_remake://重录
                mPresenter.showTipDialog(this, getString(R.string.dialog_rerecord), 1);
                break;
            case R.id.tv_publish://发布
                mPresenter.showTipDialog(this, getString(R.string.dialog_publishworks), 2);
                break;
            case R.id.tv_save://保存
                mPresenter.showTipDialog(this, getString(R.string.dialog_savework), 3);
                break;
            case R.id.iv_leftmove://左移
                mViewVoiceOffset.setVoiceLeftMove();
                mPresenter.setVoiceOffset(mViewVoiceOffset.getVoiceMove());
                break;
            case R.id.iv_rightmove://右移
                mViewVoiceOffset.setVoiceRightMove();
                mPresenter.setVoiceOffset(mViewVoiceOffset.getVoiceMove());
                break;
        }
    }

    @Override
    public void setReverbList(List<SoundReverbEntity> reverbEntityList) {
        mReverbBanner.setPages(new CBViewHolderCreator<ReverbHolderView>() {
            @Override
            public ReverbHolderView createHolder() {
                return new ReverbHolderView();
            }
        }, reverbEntityList)
                .setPageIndicator(new int[]{R.mipmap.spot_normal, R.mipmap.spot_selcet})
                .setOnPageChangeListener(this);
    }

    @Override
    public void setInflexionAdapter(SoundEffectAdapter adapter) {
        FullyGridLayoutManager fullyGridLayoutManager = new FullyGridLayoutManager(this, 5);
        mRvInflexion.setLayoutManager(fullyGridLayoutManager);
        mRvInflexion.setAdapter(adapter);
    }

    @Override
    public void setMaxValueOfPlayProgress(int max) {
        mPlayProgress.setMax(max);
    }

    @Override
    public void updatePlayProgress(float progress) {
        mCbStatus.setChecked(mPresenter.audition_getPlayStatus());
        int curProgress = Math.round(progress) * 1000;

        //调整时间进度
        mPlayProgress.setProgress(curProgress);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mPresenter.audition_switchReverbEffect(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void showRequestDialog() {
        if (null != networkDialog) {
            networkDialog.showNetWorkDialog();
        }
    }

    @Override
    public void hideRequestDialog() {
        if (null != networkDialog) {
            networkDialog.dismissNetWorkDialog();
        }
    }

    @Override
    public void showNumberDialog() {
        if (null == downloadDialog) {
            downloadDialog = new DownloadDialog();
            downloadDialog.show(getFragmentManager(), "com.clicktech.snsktv.widget.dialog.DownloadDialog");
        }
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
    public void resetRecord() {
        if (mAddVideo) {
            turn2KSingActivity();
        } else {
            turn2KSingWithMVActivity();
        }
    }

    //准备视频播放器宽和高
    private void init_measurePlayerWide() {
        rlVideo.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rlVideo.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = rlVideo.getWidth() - rlVideo.getPaddingLeft() - rlVideo.getPaddingRight();
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerGLSurfaceView.getLayoutParams();
                if (mKSingRGVEntity.getWorkstype() == getResources().getInteger(R.integer.workstype_chrousother)) {
                    //合唱显示成正方形
                    params.width = width;
                    params.height = width;
                } else {
                    //非合唱显示长方形。屏幕宽高比
                    params.width = width * mKSingRGVEntity.getInsidewidth() / mKSingRGVEntity.getInsideheight();
                    params.height = width;
                }
                playerGLSurfaceView.setLayoutParams(params);

                playerGLSurfaceView.setUpParams(mKSingRGVEntity.getRecordmvpath(),
                        EmptyUtils.isEmpty(mSingInfo.getWorks_image()) ? mSingInfo.getSong_image() : mSingInfo.getWorks_image());
            }
        });
    }

    @Override
    public void turn2Announce(PublishInforEntity entity) {
        hideLoading();
        Intent intent = new Intent(this, AnnounceActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("publishinfo", entity);
        bundle.putParcelable("scoreresult", mScoreResult);
        intent.putExtras(bundle);
        launchActivity(intent);
    }

    @Override
    public void turn2Login() {
        showMessage(getString(R.string.error_notlogin));
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, RequestCode_Login);
    }

    @Override
    public void turn2MainActivity() {
        hideLoading();
        EventBus.getDefault().post(new Message(), EventBusTag.ANNOUNCESUCCESS);

        Intent mainIntent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("announcefinish", true);
        mainIntent.putExtras(bundle);
        launchActivity(mainIntent);
        killMyself();
    }

    //返回录制mv界面
    public void turn2KSingWithMVActivity() {
//        Intent resetIntent = new Intent(this, KSingWithMVActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("songinfo", mSingInfo);
//        bundle.putParcelable("ksingrgv", mKSingRGVEntity);
//        bundle.putSerializable("mvfilter", filterType);
//        resetIntent.putExtras(bundle);
//        launchActivity(resetIntent);
        killMyself();
    }

    //追加视频返回录制音频界面
    public void turn2KSingActivity() {
//        Intent resetIntent = new Intent(this, KSingActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("songinfo", mSingInfo);
//        bundle.putInt("workstype", mKSingRGVEntity.getWorkstype());
//        resetIntent.putExtras(bundle);
//        launchActivity(resetIntent);
        killMyself();
    }

}

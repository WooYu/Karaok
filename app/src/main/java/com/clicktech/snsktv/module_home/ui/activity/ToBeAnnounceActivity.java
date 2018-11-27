package com.clicktech.snsktv.module_home.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.clicktech.snsktv.entity.PublishInforEntity;
import com.clicktech.snsktv.entity.ScoreResultEntity;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.entity.SoundReverbEntity;
import com.clicktech.snsktv.module_discover.ui.activity.MvPreviewFirstActivity;
import com.clicktech.snsktv.module_enter.ui.activity.LoginActivity;
import com.clicktech.snsktv.module_enter.ui.activity.MainActivity;
import com.clicktech.snsktv.module_home.contract.ToBeAnnounceContract;
import com.clicktech.snsktv.module_home.inject.component.DaggerToBeAnnounceComponent;
import com.clicktech.snsktv.module_home.inject.module.ToBeAnnounceModule;
import com.clicktech.snsktv.module_home.presenter.ToBeAnnouncePresenter;
import com.clicktech.snsktv.module_home.ui.adapter.SoundEffectAdapter;
import com.clicktech.snsktv.module_home.ui.fragment.CantataEditSongNameFragment;
import com.clicktech.snsktv.module_home.ui.holder.ReverbHolderView;
import com.clicktech.snsktv.widget.dialog.DownloadDialog;
import com.clicktech.snsktv.widget.dialog.NetworkDialog;
import com.clicktech.snsktv.widget.seekbar.IssueSeekBar;
import com.clicktech.snsktv.widget.titlebar.HeaderView;
import com.clicktech.snsktv.widget.voiceoffset.VoiceOffsetView;
import com.jaeger.library.StatusBarUtil;
import com.jakewharton.rxbinding.widget.RxSeekBar;

import org.simple.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
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
 * Created by Administrator on 2017/3/14.
 * 待发布界面
 */

public class ToBeAnnounceActivity extends WEActivity<ToBeAnnouncePresenter> implements
        ToBeAnnounceContract.View, View.OnClickListener, HeaderView.OnCustomTileListener,
        ViewPager.OnPageChangeListener {

    public static final int RequestCode_Login = 100;
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
    @BindView(R.id.tv_addVideo)
    TextView mTvAddVideo;
    @BindView(R.id.tv_remake)
    TextView mTvRemake;
    @BindView(R.id.tv_save)
    TextView mTvSave;
    @BindView(R.id.headerview)
    HeaderView mHeaderView;
    @BindView(R.id.ll_accvol)
    LinearLayout mLLAccvol;
    @BindView(R.id.ll_voicemove)
    LinearLayout mLLVoiceMove;
    @BindView(R.id.iv_leftmove)
    ImageView mIvLeftMove;
    @BindView(R.id.iv_rightmove)
    ImageView mIvRightMove;
    @BindView(R.id.view_voiceoffset)
    VoiceOffsetView mViewVoiceOffset;

    private String mRecordPath;//录音路径
    private SongInfoBean mSingInfo;//歌曲信息
    private String mAccompanyPath;//伴奏路径
    private ScoreResultEntity mScoreResult;//打分结果
    private int mWorksType;
    private NetworkDialog mNetworkDialog;
    private DownloadDialog mDownloadDialog;//下载进度框

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerToBeAnnounceComponent
                .builder()
                .appComponent(appComponent)
                .toBeAnnounceModule(new ToBeAnnounceModule(this)) //请将ToBeAnnounceModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_tobeannounce, null, false);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this,
                getResources().getInteger(R.integer.statusalpha_a), mHeaderView);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null == bundle)
            return;
        mSingInfo = bundle.getParcelable("songinfo");
        mScoreResult = bundle.getParcelable("scoreresult");
        mWorksType = bundle.getInt("workstype");
        mRecordPath = bundle.getString("audiopath");
        mAccompanyPath = bundle.getString("accompanypath");

        if (null == mSingInfo || EmptyUtils.isEmpty(mRecordPath) || EmptyUtils.isEmpty(mAccompanyPath))
            return;

        setInitView();
        mPresenter.setData(mSingInfo, mScoreResult, mWorksType, mRecordPath, mAccompanyPath);
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

    /**
     * 设置初始化界面
     */
    private void setInitView() {
        //是否隐藏追加视频:清唱和合唱第二人不显示追加视频
        if (mWorksType == getResources().getInteger(R.integer.workstype_cantata)
                || mWorksType == getResources().getInteger(R.integer.workstype_chrousother)) {
            mTvAddVideo.setVisibility(View.GONE);
        } else {
            mTvAddVideo.setVisibility(View.VISIBLE);
        }

        //创建对话框
        mNetworkDialog = new NetworkDialog(this);
        //标题点击监听
        mHeaderView.setTitleClickListener(this);
        //播放/暂停
        mCbStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.pauseAndPlayRecord();
            }
        });

        //播放进度条
        mPlayProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPresenter.jumpToSpecifiedLocation(seekBar.getProgress());
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

        //清唱隐藏伴奏音量调节和人声移动
        if (mWorksType == getResources().getInteger(R.integer.workstype_cantata)) {
            mLLAccvol.setVisibility(View.GONE);
            mLLVoiceMove.setVisibility(View.GONE);
        }
    }

    @Override
    public void showLoading() {
        if (null != mNetworkDialog)
            mNetworkDialog.showNetWorkDialog();
    }

    @Override
    public void hideLoading() {
        if (null != mNetworkDialog)
            mNetworkDialog.dismissNetWorkDialog();
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
        mRvInflexion.setLayoutManager(new GridLayoutManager(this,5));
        mRvInflexion.setAdapter(adapter);
    }

    @Override
    public void setMaxValueOfPlayProgress(int max) {
        mPlayProgress.setMax(max);
        mTvPlayProgress.setText(TimeUtils.millis2String(max, "mm:ss"));
    }

    @Override
    public void updatePlayProgress(float progress) {
        mCbStatus.setChecked(mPresenter.getPlayStatus());
        int curProgress = Math.round(progress) * 1000;
        //调整时间进度
        mPlayProgress.setProgress(curProgress);
    }

    @OnClick({R.id.tv_save, R.id.tv_addVideo, R.id.tv_remake, R.id.tv_publish,
            R.id.iv_leftmove, R.id.iv_rightmove})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_remake://重录
                mPresenter.showTipDialog(this, getString(R.string.dialog_rerecord), 0);
                break;
            case R.id.tv_addVideo://追加视频
                mPresenter.showTipDialog(this, getString(R.string.dialog_addvideo), 1);
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
    public void setTitleLeftClick() {
        mPresenter.showTipDialog(this, getString(R.string.dialog_cantata_back), 4);
    }

    @Override
    public void setTitleRightClick() {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mPresenter.switchReverbEffect(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RequestCode_Login == requestCode && mWeApplication.loggingStatus()) {
            mPresenter.upload_stitchingParameters();
        }
    }

    @Override
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(mRvInflexion);
        hideLoading();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        mPresenter.showTipDialog(this, getString(R.string.dialog_cantata_back), 4);
        return;
    }

    @Override
    public void turn2MVPreview(String mixpath) {
        if (mWorksType == getResources().getInteger(R.integer.workstype_cantata)
                || mWorksType == getResources().getInteger(R.integer.workstype_chrousother)) {
            return;
        }

        Intent intent = new Intent(this, MvPreviewFirstActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("songinfo", mSingInfo);
        bundle.putInt("workstype", mWorksType);
        bundle.putString("audiopath", mixpath);
        bundle.putString("accompanypath", mAccompanyPath);
        bundle.putParcelable("scoreresult", mScoreResult);
        intent.putExtras(bundle);
        launchActivity(intent);
        killMyself();
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
        killMyself();
    }

    @Override
    public void turn2Login() {
        showMessage(getString(R.string.error_notlogin));
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, RequestCode_Login);
    }

    @Override
    public void turn2PrevActivity() {
        Intent resetIntent;
        if (mWorksType == getResources().getInteger(R.integer.workstype_cantata)) {
            //清唱
            resetIntent = new Intent(this, CantataActivity.class);
        } else {
            resetIntent = new Intent(this, KSingActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("songinfo", mSingInfo);
            bundle.putInt("workstype", mWorksType);
            resetIntent.putExtras(bundle);
        }
        launchActivity(resetIntent);
        killMyself();
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


    @Override
    public void showNumberDialog() {
        if (null == mDownloadDialog) {
            mDownloadDialog = new DownloadDialog();
            mDownloadDialog.show(getFragmentManager(), "com.clicktech.snsktv.widget.dialog.DownloadDialog");
        }
    }

    @Override
    public void dismissNumberDialog() {
        if (null != mDownloadDialog) {
            mDownloadDialog.dismiss();
            mDownloadDialog = null;
        }
    }

    @Override
    public void updateNumberDialog(int progress) {
        if (null != mDownloadDialog && progress <= 100) {
            mDownloadDialog.setTvProgress(progress);
        }
    }

    @Override
    public void showEditSongNameDialog() {
        CantataEditSongNameFragment fragment = new CantataEditSongNameFragment();
        fragment.show(getSupportFragmentManager(), getClass().getSimpleName());
        fragment.setmCallBack(new CantataEditSongNameFragment.EditSongNameCallBack() {
            @Override
            public void getSongName(String name) {
                mSingInfo.setSong_name_jp(name);
                mSingInfo.setSong_name_cn(name);
                mSingInfo.setSong_name_us(name);
                mPresenter.publishAndSave(true);
            }
        });
    }


}

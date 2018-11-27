package com.clicktech.snsktv.module_home.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.TimeUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_home.contract.PracticingSingContract;
import com.clicktech.snsktv.module_home.inject.component.DaggerPracticingSingComponent;
import com.clicktech.snsktv.module_home.inject.module.PracticingSingModule;
import com.clicktech.snsktv.module_home.presenter.PracticingSingPresenter;
import com.clicktech.snsktv.util.StringHelper;
import com.clicktech.snsktv.widget.dialog.DownloadDialog;
import com.clicktech.snsktv.widget.titlebar.HeaderView;
import com.jaeger.library.StatusBarUtil;
import com.library.multimedia.lyricscontrols.parser.LrcRow;
import com.library.multimedia.lyricscontrols.view.LyricOfSingleImpl;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

import static com.clicktech.snsktv.arms.utils.Preconditions.checkNotNull;


public class PracticingSingActivity extends WEActivity<PracticingSingPresenter> implements
        PracticingSingContract.View, HeaderView.OnCustomTileListener {

    @BindView(R.id.headerview)
    HeaderView headerview;
    @BindView(R.id.tv_curtime)
    TextView tvCurtime;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_totaltime)
    TextView tvTotaltime;
    @BindView(R.id.lrcview)
    LyricOfSingleImpl lrcview;
    @BindView(R.id.tv_mppaused)
    TextView tvMppaused;
    @BindView(R.id.tv_reset)
    TextView tvReset;

    private RxPermissions mRxPermissions;
    private SongInfoBean mSongInfo;//歌曲信息
    private DownloadDialog downloadDialog;

    private long mTimeOfStartClick;//开始点击时间

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions(this);
        DaggerPracticingSingComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .practicingSingModule(new PracticingSingModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_practicing_sing, null, false);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucent(this, getResources().getInteger(R.integer.statusalpha_a));
    }

    @Override
    protected void initData() {
        headerview.setTitleClickListener(this);

        Bundle arguments = getIntent().getExtras();
        if (null == arguments)
            return;

        mSongInfo = arguments.getParcelable("songinfo");

        if (null == mSongInfo)
            return;

        mPresenter.requestdata(mSongInfo);
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
    public void setTitleLeftClick() {
        mPresenter.showTipDialog(this, getString(R.string.dialog_cantata_back), 0);
    }

    @Override
    public void setTitleRightClick() {

    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @OnClick({R.id.lrcview, R.id.tv_mppaused, R.id.tv_reset})
    public void onClick(View v) {
        if (!mPresenter.getStatusOfPrepared()) {
            return;
        }

        if (System.currentTimeMillis() - mTimeOfStartClick < 2000) {
            return;
        }

        mTimeOfStartClick = System.currentTimeMillis();

        switch (v.getId()) {
            case R.id.lrcview://轻触暂停，播放
                if(mPresenter.getStatusOfAudio()){
                    return;
                }

                if (mPresenter.record_getPlayStatus()) {
                    showMessage(getString(R.string.tip_touchlyric));
                }
                mPresenter.record_kSingAction();
                break;
            case R.id.tv_mppaused://播放录制的作品
                if(mPresenter.getStatusOfAudio()){
                    return;
                }

                mPresenter.showTipDialog(this, getString(R.string.dialog_practice_audition), 1);
                break;
            case R.id.tv_reset://重录
                if(mPresenter.getStatusOfAudio()){
                    mPresenter.showTipDialog(this, getString(R.string.dialog_practice_reset), 3);
                }else{
                    mPresenter.showTipDialog(this, getString(R.string.dialog_rerecord), 2);
                }

                break;
        }
    }

    @Override
    public void updateLrc(List<LrcRow> lrcRowList) {
        if (EmptyUtils.isEmpty(lrcRowList)) {
            return;
        }

        lrcview.setLrcRows(lrcRowList);
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
    public void updatePlayProgress(float progress, float totalTime) {
        int curProgress = Math.round(progress) * 1000;
        int curTotalTime = Math.round(totalTime) * 1000;

        lrcview.seekTo(curProgress, false, false);

        //调整时间进度
        progressBar.setMax(curTotalTime);
        progressBar.setProgress(curProgress);

        //设置歌曲时长
        String curTimeStr = TimeUtils.millis2String(curProgress, "mm:ss");
        String totalTimeStr = TimeUtils.millis2String(curTotalTime, "mm:ss");
        tvCurtime.setText(curTimeStr);
        tvTotaltime.setText(totalTimeStr);

    }

}

package com.clicktech.snsktv.module_home.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.ConvertUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.entity.KSingRGVEntity;
import com.clicktech.snsktv.entity.MVConfig;
import com.clicktech.snsktv.entity.ScoreResultEntity;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.ui.helper.TemplateAndFilterType;
import com.clicktech.snsktv.module_discover.ui.helper.TemplateFilterTypeHelper;
import com.clicktech.snsktv.module_home.contract.AddVideoContract;
import com.clicktech.snsktv.module_home.inject.component.DaggerAddVideoComponent;
import com.clicktech.snsktv.module_home.inject.module.AddVideoModule;
import com.clicktech.snsktv.module_home.presenter.AddVideoPresenter;
import com.clicktech.snsktv.util.StringHelper;
import com.clicktech.snsktv.widget.titlebar.HeaderView;
import com.jaeger.library.StatusBarUtil;

import org.wysaid.camera.CameraInstance;
import org.wysaid.view.CameraGLSurfaceView;
import org.wysaid.view.CameraRecordGLSurfaceView;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

import static com.clicktech.snsktv.arms.utils.Preconditions.checkNotNull;


public class AddVideoActivity extends WEActivity<AddVideoPresenter> implements AddVideoContract.View
        , HeaderView.OnCustomTileListener {

    @BindView(R.id.glsurfaceview)
    CameraRecordGLSurfaceView glsurfaceview;
    @BindView(R.id.titleview)
    HeaderView titleview;
    @BindView(R.id.tv_curtime)
    TextView tvCurtime;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_totaltime)
    TextView tvTotaltime;
    @BindView(R.id.cb_playpause)
    CheckBox cbPlaypause;
    @BindView(R.id.iv_reset)
    ImageView ivReset;
    @BindView(R.id.tv_countdown)
    TextView tvCountDown;

    private SongInfoBean mSongInfo;//歌曲信息
    private ScoreResultEntity mScoreResult;//打分结果
    private TemplateAndFilterType filterType;
    private MVConfig mvConfig;//录制效果、布局配置
    private KSingRGVEntity mKSingRGVEntity;//录制mv的配置信息
    private String mPathOfMV;//视频路径

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAddVideoComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .addVideoModule(new AddVideoModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this,
                getResources().getInteger(R.integer.statusalpha_a), titleview);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_addvideo, null, false);
    }

    @Override
    protected void initData() {
        titleview.setTitleClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (null == bundle) {
            return;
        }

        mSongInfo = bundle.getParcelable("songinfo");
        mScoreResult = bundle.getParcelable("scoreresult");
        filterType = (TemplateAndFilterType) bundle.getSerializable("mvfilter");
        mvConfig = TemplateFilterTypeHelper.getMvConfig(filterType);
        mKSingRGVEntity = bundle.getParcelable("ksingrgv");

        if (null == mSongInfo || null == mScoreResult || null == mKSingRGVEntity) {
            return;
        }

        titleview.setTitleName(StringHelper.getLau_With_J_U_C(mSongInfo.getSong_name_jp(),
                mSongInfo.getSong_name_us(), mSongInfo.getSong_name_cn()));
        mPresenter.setData(mSongInfo, mKSingRGVEntity);
        openCameraPreview();
        cbPlaypause.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!mPresenter.record_getPrepareStatus()) {
                    return;
                }

                //true 暂停，false 播放
                mPresenter.record_pauseAndPlayRecord(isChecked);
                if (isChecked) {
                    glsurfaceview.stopPreview();
                } else {
                    glsurfaceview.resumePreview();
                }
            }
        });
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

    @OnClick(R.id.iv_reset)
    public void onViewClicked() {
        mPresenter.showTipDialog(this, getString(R.string.dialog_addvideo_reset), 1);
    }

    @Override
    public void setTitleLeftClick() {
        mPresenter.showTipDialog(this, getString(R.string.dialog_cantata_back), 0);
    }

    @Override
    public void setTitleRightClick() {
        glsurfaceview.switchCamera();
    }

    //开启摄像头预览
    private void openCameraPreview() {
        glsurfaceview.presetCameraForward(!mKSingRGVEntity.isOpenFrontCamera());
        glsurfaceview.presetRecordingSize(480, 640);
        glsurfaceview.setZOrderOnTop(false);
        glsurfaceview.setZOrderMediaOverlay(true);
        glsurfaceview.setFitFullView(true);
        glsurfaceview.setOnCreateCallback(new CameraGLSurfaceView.OnCreateCallback() {
            @Override
            public void createOver(boolean success) {
                if (success) {
                    if (null != mvConfig)
                        glsurfaceview.setFilterWithConfig(mvConfig.getConfig());
                    mPresenter.startCountDown();

                } else {
                    Timber.e("Camera打开失败！！！");
                    killMyself();
                }

            }
        });

    }

    @Override
    public void updateCountDown(int count) {
        if (0 == count) {
            tvCountDown.setVisibility(View.GONE);
            return;
        }
        tvCountDown.setText(String.valueOf(count));
    }

    @Override
    public void startMVRecord(String recordpath, CameraRecordGLSurfaceView.StartRecordingCallback
            startRecordingCallback, CameraRecordGLSurfaceView.YUVCallback callback) {
        this.mPathOfMV = recordpath;
        glsurfaceview.startRecording(recordpath, startRecordingCallback, callback);
    }

    @Override
    public void setMaxValueOfPlayProgress(final int max) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setMax(max);
                tvTotaltime.setText(ConvertUtils.millis2mmss(max));
            }
        });

    }

    @Override
    public void updatePlayProgress(int progress) {
        progressBar.setProgress(progress);
        tvCurtime.setText(ConvertUtils.millis2mmss(progress));

        if (progress > 0 && progress == progressBar.getMax()) {
            mPresenter.record_kSingEnd(false);
        }
    }

    @Override
    public void stopMVRecord(CameraRecordGLSurfaceView.EndRecordingCallback endRecordingCallback) {
        glsurfaceview.endRecording(endRecordingCallback, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        glsurfaceview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        record_closeMediaResource();

        mPresenter.onInterfaceToBackground();
        killMyself();
    }


    //关闭相机预览和播放器
    private void record_closeMediaResource() {
        CameraInstance.getInstance().stopCamera();
        glsurfaceview.release(null);
        glsurfaceview.onPause();
    }

    @Override
    public void turn2ToBeAnnounce() {

        String totalTimeStr = tvTotaltime.getText().toString();
        long totalTimeMillis = ConvertUtils.mmssString2millis(totalTimeStr);

        Intent tobeAnnoIntent = new Intent(this, ToBeAnnoWithMVActivity.class);
        mKSingRGVEntity.setRecordmvpath(mPathOfMV);
        mKSingRGVEntity.setRecordtime((int) totalTimeMillis);

        Bundle bundle = new Bundle();
        bundle.putParcelable("songinfo", mSongInfo);
        bundle.putParcelable("ksingrgv", mKSingRGVEntity);
        bundle.putSerializable("mvfilter", filterType);
        bundle.putParcelable("scoreresult", mScoreResult);
        bundle.putBoolean("addvideo", true);
        tobeAnnoIntent.putExtras(bundle);
        startActivity(tobeAnnoIntent);
        killMyself();
    }
}

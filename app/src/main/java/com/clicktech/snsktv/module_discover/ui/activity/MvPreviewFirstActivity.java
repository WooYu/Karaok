package com.clicktech.snsktv.module_discover.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.DeviceUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.entity.KSingRGVEntity;
import com.clicktech.snsktv.entity.MVConfig;
import com.clicktech.snsktv.entity.ScoreResultEntity;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.contract.MvPreviewFirstContract;
import com.clicktech.snsktv.module_discover.inject.component.DaggerMvPreviewFirstComponent;
import com.clicktech.snsktv.module_discover.inject.module.MvPreviewFirstModule;
import com.clicktech.snsktv.module_discover.presenter.MvPreviewFirstPresenter;
import com.clicktech.snsktv.module_discover.ui.adapter.TemplateAndFilterAdapter;
import com.clicktech.snsktv.module_discover.ui.helper.TemplateAndFilterType;
import com.clicktech.snsktv.module_discover.ui.helper.TemplateFilterTypeHelper;
import com.clicktech.snsktv.module_home.ui.activity.AddVideoActivity;
import com.clicktech.snsktv.module_home.ui.activity.KSingWithMVActivity;
import com.clicktech.snsktv.util.StringHelper;
import com.clicktech.snsktv.widget.titlebar.HeaderView;
import com.jaeger.library.StatusBarUtil;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.wysaid.camera.CameraInstance;
import org.wysaid.view.CameraGLSurfaceView;
import org.wysaid.view.CameraRecordGLSurfaceView;

import butterknife.BindView;
import butterknife.OnClick;
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
 * Created by Administrator on 2017-05-26.
 * mv录制第一人
 * 描述：下载歌词、下载伴奏
 */

public class MvPreviewFirstActivity extends WEActivity<MvPreviewFirstPresenter> implements
        MvPreviewFirstContract.View, HeaderView.OnCustomTileListener {

    @BindView(R.id.titleview)
    HeaderView titleview;
    @BindView(R.id.glsurfaceview)
    CameraRecordGLSurfaceView previewSurfaceview;
    @BindView(R.id.rv_filter)
    RecyclerView rvFilter;

    private RxPermissions mRxPermissions;

    private int worksType;  //0普通K歌; 1合唱第一人唱A; 2合唱第一人唱B; 3合唱第二人; 4清唱K歌
    private SongInfoBean mSongInfo;
    private TemplateAndFilterType mTemplateAndFilterType;//选择的滤镜
    private boolean openFrontCamera = true;//开启前置摄像头
    private String lyricSavePath;//歌词保存路径
    private KSingRGVEntity kSingRGVEntity;//视频相关的信息
    private ScoreResultEntity mScoreResult;//打分结果
    private String mRecordPath;//录音路径
    private String mAccompanypath;//伴奏路径

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions(this);
        DaggerMvPreviewFirstComponent
                .builder()
                .appComponent(appComponent)
                .mvPreviewFirstModule(new MvPreviewFirstModule(this)) //请将MvPreviewFirstModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_mvpreviewfirst, null, false);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this,
                getResources().getInteger(R.integer.statusalpha_a), titleview);
    }

    @Override
    protected void initData() {
        titleview.setTitleClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (null == bundle)
            return;

        mSongInfo = bundle.getParcelable("songinfo");
        worksType = bundle.getInt("workstype", getResources().getInteger(R.integer.workstype_common));

        if (null == mSongInfo)
            return;

        mScoreResult = bundle.getParcelable("scoreresult");
        mRecordPath = bundle.getString("audiopath");
        mAccompanypath = bundle.getString("accompanypath");

        titleview.setTitleName(StringHelper.getLau_With_J_U_C(mSongInfo.getSong_name_jp(),
                mSongInfo.getSong_name_us(), mSongInfo.getSong_name_cn()));
        kSingRGVEntity = new KSingRGVEntity();
        initMVPreviewSize();
        mPresenter.requestData(mSongInfo);
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
    protected void onResume() {
        super.onResume();
        previewSurfaceview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCameraPreview();
        killMyself();
    }

    @OnClick(R.id.iv_startrecord)
    public void onViewClicked() {
        if (null == mSongInfo)
            return;

        if (null == mScoreResult) {
            turn2KSingWitMV();
        } else {
            turn2AddVideo();
        }
    }

    @Override
    public void setTitleLeftClick() {
        killMyself();
    }

    @Override
    public void setTitleRightClick() {
        swithCamera();
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    /**
     * 初始化MV预览布局大小
     */
    private void initMVPreviewSize() {
        float screenwidth = DeviceUtils.getScreenWidth(this);
        float screenheight = DeviceUtils.getScreenHeight(this);
        kSingRGVEntity.setInsidewidth((int) screenwidth);
        kSingRGVEntity.setInsideheight((int) screenheight);
        kSingRGVEntity.setLayouttype(R.integer.chorustype_fullscreen);
    }

    /**
     * 初始化预览视图
     */
    @Override
    public void initCameraPreview() {
        previewSurfaceview.presetCameraForward(false);
        previewSurfaceview.presetRecordingSize(480, 640);
        previewSurfaceview.setZOrderOnTop(false);
        previewSurfaceview.setZOrderMediaOverlay(true);
        previewSurfaceview.setFitFullView(true);
        previewSurfaceview.setOnCreateCallback(new CameraGLSurfaceView.OnCreateCallback() {
            @Override
            public void createOver(boolean success) {
                Timber.e("CameraGLSurfaceView create success or failed? " + success);
            }
        });
        //设置默认的滤镜效果
        mTemplateAndFilterType = TemplateAndFilterType.ORIGINAL;
    }

    // 前后摄像头切换
    private void swithCamera() {
        openFrontCamera = !openFrontCamera;
        previewSurfaceview.switchCamera();
    }

    // 释放资源
    private void releaseCameraPreview() {
        CameraInstance.getInstance().stopCamera();
        previewSurfaceview.release(null);
        previewSurfaceview.onPause();
    }

    @Override
    public void setTemplateFilterAdapter(TemplateAndFilterAdapter filterAdapter) {
        rvFilter.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvFilter.setHasFixedSize(true);
        rvFilter.setAdapter(filterAdapter);
    }

    @Override
    public void setFilterEffect(TemplateAndFilterType templateAndFilterType) {
        mTemplateAndFilterType = templateAndFilterType;

        MVConfig filterConfig = TemplateFilterTypeHelper.getMvConfig(templateAndFilterType);
        previewSurfaceview.setFilterWithConfig(filterConfig.getConfig());
    }

    @Override
    public void setLyricSavePath(String path) {
        this.lyricSavePath = path;
        initCameraPreview();
    }

    // 跳转到k歌界面
    private void turn2KSingWitMV() {
        previewSurfaceview.setVisibility(View.GONE);

        kSingRGVEntity.setOpenFrontCamera(openFrontCamera);
        kSingRGVEntity.setWorkstype(worksType);
        kSingRGVEntity.setLyricpath(lyricSavePath);
        kSingRGVEntity.setAccompanypath(mAccompanypath);

        Intent intent = new Intent(this, KSingWithMVActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("songinfo", mSongInfo);
        bundle.putSerializable("mvfilter", mTemplateAndFilterType);
        bundle.putParcelable("ksingrgv", kSingRGVEntity);
        intent.putExtras(bundle);
        startActivity(intent);
        killMyself();
    }

    //跳转到添加视频界面
    private void turn2AddVideo() {
        previewSurfaceview.setVisibility(View.GONE);

        kSingRGVEntity.setOpenFrontCamera(openFrontCamera);
        kSingRGVEntity.setWorkstype(worksType);
        kSingRGVEntity.setLyricpath(lyricSavePath);
        kSingRGVEntity.setAccompanypath(mAccompanypath);
        kSingRGVEntity.setRecordaudiopath(mRecordPath);

        Intent intent = new Intent(this, AddVideoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("songinfo", mSongInfo);
        bundle.putSerializable("mvfilter", mTemplateAndFilterType);
        bundle.putParcelable("ksingrgv", kSingRGVEntity);
        bundle.putParcelable("scoreresult", mScoreResult);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}

package com.clicktech.snsktv.module_discover.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clicktech.snsktv.BuildConfig;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.DeviceUtils;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.entity.KSingRGVEntity;
import com.clicktech.snsktv.entity.MVConfig;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.contract.MvPreviewSecondContract;
import com.clicktech.snsktv.module_discover.inject.component.DaggerMvPreviewSecondComponent;
import com.clicktech.snsktv.module_discover.inject.module.MvPreviewSecondModule;
import com.clicktech.snsktv.module_discover.presenter.MvPreviewSecondPresenter;
import com.clicktech.snsktv.module_discover.ui.adapter.TemplateAndFilterAdapter;
import com.clicktech.snsktv.module_discover.ui.helper.TemplateAndFilterType;
import com.clicktech.snsktv.module_discover.ui.helper.TemplateFilterTypeHelper;
import com.clicktech.snsktv.module_home.ui.activity.KSingWithMVActivity;
import com.clicktech.snsktv.util.StringHelper;
import com.clicktech.snsktv.widget.dialog.DownloadDialog;
import com.clicktech.snsktv.widget.dialog.NetworkDialog;
import com.clicktech.snsktv.widget.titlebar.HeaderView;
import com.clicktech.snsktv.widget.videoplayer.MVPreviewPlayer;
import com.jaeger.library.StatusBarUtil;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.wysaid.camera.CameraInstance;
import org.wysaid.view.CameraGLSurfaceView;
import org.wysaid.view.CameraRecordGLSurfaceView;

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
 * Created by Administrator on 2017-05-06.
 * mv合唱第二人
 */

public class MvPreviewSecondActivity extends WEActivity<MvPreviewSecondPresenter> implements
        MvPreviewSecondContract.View, HeaderView.OnCustomTileListener {

    @BindView(R.id.rv_template)
    RecyclerView rvTemplate;
    @BindView(R.id.rv_filter)
    RecyclerView rvFilter;
    @BindView(R.id.titleview)
    HeaderView titleview;
    @BindView(R.id.mvpreview)
    RelativeLayout mvpreview;
    @BindView(R.id.playerSurfaceView)
    MVPreviewPlayer playerGLSurfaceView;
    @BindView(R.id.sufaceview)
    CameraRecordGLSurfaceView previewGLSurfaceView;
    @BindView(R.id.tv_eeffect)
    TextView tvEEffect;
    @BindView(R.id.ll_templatebg)
    LinearLayout llTemplatebg;
    @BindView(R.id.ll_filterbg)
    LinearLayout llFilterbg;
    @BindView(R.id.tv_template)
    TextView tvTemplate;
    @BindView(R.id.tv_filter)
    TextView tvFilter;

    private RxPermissions mRxPermissions;
    private int mMVPreviewWidth;
    private int mMVPreviewHeigh;
    private int mScreenWidth;

    private int worksType;  //0普通K歌; 1合唱第一人唱A; 2合唱第一人唱B; 3合唱第二人; 4清唱K歌
    private SongInfoBean mSongInfo;
    private TemplateAndFilterType filterConfig;
    private boolean openFrontCamera = true;//开启前置摄像头
    private int volumeOfInitial;//初始音量
    private KSingRGVEntity mKSingRGVEntity;//mv布局信息和歌曲下载信息
    private DownloadDialog downloadDialog;
    private String lyricPath;//歌词路径

    private NetworkDialog mNetworkDialog;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions(this);
        DaggerMvPreviewSecondComponent
                .builder()
                .appComponent(appComponent)
                .mvPreviewSecondModule(new MvPreviewSecondModule(this)) //请将MVPreviewModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_mvpreview, null, false);
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
        if (null == mSongInfo) {
            return;
        }

        titleview.setTitleName(StringHelper.getLau_With_J_U_C(mSongInfo.getSong_name_jp(),
                mSongInfo.getSong_name_us(), mSongInfo.getSong_name_cn()));
        mKSingRGVEntity = new KSingRGVEntity();
        mNetworkDialog = new NetworkDialog(this);
        initMVPreviewSize();

        switchSetTemplateFilter(true, false);
        mPresenter.requestData(mSongInfo);
    }

    /**
     * 初始化MV预览布局大小
     */
    private void initMVPreviewSize() {
        mScreenWidth = (int) DeviceUtils.getScreenWidth(this);
        ViewTreeObserver viewTreeObserver = mvpreview.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mvpreview.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mMVPreviewWidth = mvpreview.getWidth();
                mMVPreviewHeigh = mvpreview.getHeight();
                switch2FullView();
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

    @Override
    protected void onResume() {
        super.onResume();
        soundOff();
        previewGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        CameraInstance.getInstance().stopCamera();
        previewGLSurfaceView.release(null);
        previewGLSurfaceView.onPause();

        JZVideoPlayer.releaseAllVideos();

        soundOn();
        killMyself();
    }

    @OnClick(R.id.tv_template)
    public void onTvTemplateClicked() {
        switchSetTemplateFilter(true, false);
    }

    @OnClick(R.id.tv_filter)
    public void onTvFilterClicked() {
        switchSetTemplateFilter(false, true);
    }

    @OnClick(R.id.ll_startrecord)
    public void onViewClicked() {
        turn2KSing();
    }

    @Override
    public void setTitleLeftClick() {
        killMyself();
    }

    @Override
    public void setTitleRightClick() {
        //翻转摄像头
        previewGLSurfaceView.switchCamera();
        openFrontCamera = !openFrontCamera;
    }

    @Override
    public void setTemplateFilterAdapter(TemplateAndFilterAdapter templateAdapter,
                                         TemplateAndFilterAdapter filterAdapter) {
        rvFilter.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvFilter.setHasFixedSize(true);
        rvFilter.setAdapter(filterAdapter);
        rvTemplate.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvTemplate.setHasFixedSize(true);
        rvTemplate.setAdapter(templateAdapter);
    }

    @Override
    public void setTemplateEffect(TemplateAndFilterType type) {
        switch (type) {
            case STANDARD:
                switch2LeftRightView();
                break;
            case FULLSCREEN:
                switch2FullView();
                break;
            case FRONTBACK:
                switch2FrontBack();
                break;
        }
    }

    @Override
    public void setFilterEffect(TemplateAndFilterType type) {
        filterConfig = type;
        MVConfig mvConfig = TemplateFilterTypeHelper.getMvConfig(type);
        tvEEffect.setText(EmptyUtils.isEmpty(mvConfig.getConfigdesc()) ? "" : mvConfig.getConfigdesc());
        previewGLSurfaceView.setFilterWithConfig(mvConfig.getConfig());
    }

    @Override
    public void setLyricSavePath(String path) {
        this.lyricPath = path;
        initCameraPreview();
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public void initCameraPreview() {
        //设置默认的滤镜效果
        filterConfig = TemplateAndFilterType.ORIGINAL;

        //播放视频
        playerGLSurfaceView.setUpParams(BuildConfig.APP_DOMAIN_File + mSongInfo.getWorks_url()
                , mSongInfo.getSong_image());

        //初始化预览
        previewGLSurfaceView.presetCameraForward(false);
        previewGLSurfaceView.presetRecordingSize(480, 640);
        previewGLSurfaceView.setZOrderOnTop(false);
        previewGLSurfaceView.setZOrderMediaOverlay(true);
        previewGLSurfaceView.setFitFullView(true);
        previewGLSurfaceView.setOnCreateCallback(new CameraGLSurfaceView.OnCreateCallback() {
            @Override
            public void createOver(boolean success) {
                if (!success)
                    Timber.e("CameraGLSurfaceView create failed !");
            }
        });

    }

    //切换设置模板和过滤器
    private void switchSetTemplateFilter(boolean selectTemplate, boolean selectFilter) {
        llFilterbg.setSelected(selectFilter);
        tvFilter.setSelected(selectFilter);
        llTemplatebg.setSelected(selectTemplate);
        tvTemplate.setSelected(selectTemplate);

        if (selectTemplate) {
            rvFilter.setVisibility(View.GONE);
            rvTemplate.setVisibility(View.VISIBLE);
        }

        if (selectFilter) {
            rvFilter.setVisibility(View.VISIBLE);
            rvTemplate.setVisibility(View.GONE);
        }

    }

    /**
     * 切换到左右
     */
    private void switch2LeftRightView() {
        RelativeLayout.LayoutParams recordViewParams = (RelativeLayout.LayoutParams) previewGLSurfaceView.getLayoutParams();
        recordViewParams.width = mMVPreviewWidth * 2 / 3;
        previewGLSurfaceView.setLayoutParams(recordViewParams);

        playerGLSurfaceView.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams playerViewParams = (RelativeLayout.LayoutParams) playerGLSurfaceView.getLayoutParams();
        playerViewParams.width = mMVPreviewWidth / 3;
        playerViewParams.height = mMVPreviewHeigh;
        playerViewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
        playerViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
        playerViewParams.addRule(RelativeLayout.RIGHT_OF, previewGLSurfaceView.getId());
        playerGLSurfaceView.setLayoutParams(playerViewParams);

        /**
         * 两个视频左右合并
         *
         * @param leftPath    左视频
         * @param rightPath   右视频
         * @param outputPath  输出视频
         * @param leftWidth   左视频宽度
         * @param leftHeight  左视频高度
         * @param rigthWidth  右视频宽度
         * @param rightHeight 右视频高度，必须和左视频高度相同
         */
        mKSingRGVEntity.setLeftwidth(mScreenWidth * 2 / 3);
        mKSingRGVEntity.setLeftheight(mScreenWidth);
        mKSingRGVEntity.setRightwidth(mScreenWidth / 3);
        mKSingRGVEntity.setRightheight(mScreenWidth);
        mKSingRGVEntity.setLayouttype(R.integer.chorustype_leftright);
    }

    /**
     * 切换到前后
     */
    private void switch2FrontBack() {
        playerGLSurfaceView.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams playerViewParams = (RelativeLayout.LayoutParams) playerGLSurfaceView.getLayoutParams();
        playerViewParams.width = mMVPreviewWidth / 3;
        playerViewParams.height = mMVPreviewHeigh / 3;
        playerViewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        playerViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        playerViewParams.addRule(RelativeLayout.RIGHT_OF, 0);
        playerGLSurfaceView.setLayoutParams(playerViewParams);

        RelativeLayout.LayoutParams recordViewParams = (RelativeLayout.LayoutParams) previewGLSurfaceView.getLayoutParams();
        recordViewParams.width = mMVPreviewWidth;
        recordViewParams.height = mMVPreviewHeigh;
        previewGLSurfaceView.setLayoutParams(recordViewParams);

        mKSingRGVEntity.setInsidewidth(mScreenWidth);
        mKSingRGVEntity.setInsideheight(mScreenWidth);
        mKSingRGVEntity.setOutsidewidth(mScreenWidth / 3);
        mKSingRGVEntity.setOutsideheight(mScreenWidth / 3);
        mKSingRGVEntity.setLayouttype(R.integer.chorustype_frontback);
    }

    /**
     * 切换到全屏
     */
    private void switch2FullView() {
        playerGLSurfaceView.setVisibility(View.GONE);

        RelativeLayout.LayoutParams recordViewParams = (RelativeLayout.LayoutParams) previewGLSurfaceView.getLayoutParams();
        recordViewParams.width = mMVPreviewWidth;
        recordViewParams.height = mMVPreviewHeigh;
        recordViewParams.addRule(RelativeLayout.RIGHT_OF, 0);
        previewGLSurfaceView.setLayoutParams(recordViewParams);

        mKSingRGVEntity.setInsidewidth(mScreenWidth);
        mKSingRGVEntity.setInsideheight(mScreenWidth);
        mKSingRGVEntity.setOutsidewidth(1);
        mKSingRGVEntity.setOutsideheight(1);
        mKSingRGVEntity.setLayouttype(R.integer.chorustype_fullscreen);
    }

    /**
     * 静音
     */
    private void soundOff() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volumeOfInitial = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
    }

    /**
     * 开启声音
     */
    private void soundOn() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumeOfInitial, 0);
    }

    /**
     * 跳转到k歌界面
     */
    private void turn2KSing() {
        if (null == mSongInfo)
            return;

        previewGLSurfaceView.setVisibility(View.GONE);
        playerGLSurfaceView.setVisibility(View.GONE);
        mKSingRGVEntity.setOpenFrontCamera(openFrontCamera);
        mKSingRGVEntity.setLyricpath(lyricPath);
        mKSingRGVEntity.setWorkstype(worksType);

        Intent intent = new Intent(this, KSingWithMVActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("songinfo", mSongInfo);
        bundle.putSerializable("mvfilter", filterConfig);
        bundle.putParcelable("ksingrgv", mKSingRGVEntity);
        intent.putExtras(bundle);
        launchActivity(intent);
        killMyself();
    }

}

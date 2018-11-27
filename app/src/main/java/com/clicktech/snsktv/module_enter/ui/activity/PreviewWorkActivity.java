package com.clicktech.snsktv.module_enter.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clicktech.snsktv.BuildConfig;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.MediaFile;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_enter.contract.PreviewWorkContract;
import com.clicktech.snsktv.module_enter.inject.component.DaggerPreviewWorkComponent;
import com.clicktech.snsktv.module_enter.inject.module.PreviewWorkModule;
import com.clicktech.snsktv.module_enter.listener.ITimingCallBack;
import com.clicktech.snsktv.module_enter.presenter.PreviewWorkPresenter;
import com.clicktech.snsktv.widget.animaoftiming.TimingAnimaView;
import com.clicktech.snsktv.widget.filetcircleimageview.MLImageView;
import com.clicktech.snsktv.widget.videoplayer.JuceVLCPlayer;
import com.jaeger.library.StatusBarUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;

import static com.clicktech.snsktv.arms.utils.Preconditions.checkNotNull;


public class PreviewWorkActivity extends WEActivity<PreviewWorkPresenter> implements PreviewWorkContract.View {

    @BindView(R.id.list_player)
    JuceVLCPlayer listPlayer;
    @BindView(R.id.iv_cover)
    ImageView ivCover;
    @BindView(R.id.iv_intro_avatar)
    MLImageView ivIntroAvatar;
    @BindView(R.id.tv_intro_singer)
    TextView tvIntroSinger;
    @BindView(R.id.tv_intro_song)
    TextView tvIntroSong;
    @BindView(R.id.ll_intro)
    LinearLayout llIntro;
    @BindView(R.id.timingview)
    TimingAnimaView timingview;

    private int mPlayPosition;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerPreviewWorkComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .previewWorkModule(new PreviewWorkModule(this))
                .build()
                .inject(this);
    }


    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_preview_work, null, false);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTransparentForImageView(this, null);
    }

    @Override
    protected void initData() {
        mPresenter.requestHomeData();
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
    public void setPlayList(final List<SongInfoBean> list) {
        if (EmptyUtils.isEmpty(list)) {
            return;
        }

        mPlayPosition = 0;
        listPlayer.setUpParams(list.get(0).getWorks_url(),
                list.get(0).getWorks_image());
        setInformationOfSinger(list.get(mPlayPosition));

        //开始15秒计时动画
        timingview.startTimeAnima(new ITimingCallBack() {
            @Override
            public void startNextTiming() {
                ++mPlayPosition;
                if (mPlayPosition > list.size() - 1) {
                    mPlayPosition = 0;
                }

                listPlayer.switchNextVideo(list.get(mPlayPosition).getWorks_url(),
                        list.get(mPlayPosition).getWorks_image());
                setInformationOfSinger(list.get(mPlayPosition));
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (JuceVLCPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timingview.onResumeAniam();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
        timingview.onPauseAnima();
        killMyself();
    }

    @Override
    protected void onDestroy() {
        JZVideoPlayer.releaseAllVideos();
        timingview.onDestoryAniam();
        super.onDestroy();
    }

    //设置歌曲信息
    private void setInformationOfSinger(SongInfoBean data) {
        //根据作品类型判断是否展示封面
        String worksUrl = BuildConfig.APP_DOMAIN_File + data.getWorks_url();
        if (MediaFile.isVideoFileType(worksUrl)) {
            ivCover.setVisibility(View.GONE);
        } else {
            ivCover.setVisibility(View.VISIBLE);
        }

        //封面
        mWeApplication.getAppComponent().imageLoader().loadImage(mApplication,
                GlideImageConfig.builder()
                        .url(data.getWorks_image())
                        .imageView(ivCover)
                        .build());
        //头像
        if (Integer.parseInt(data.getUser_sex()) == getResources().getInteger(R.integer.sex_female)) {
            ivIntroAvatar.setBorderColor(getResources().getColor(R.color.pink));
        } else {
            ivIntroAvatar.setBorderColor(getResources().getColor(R.color.green));
        }
        mWeApplication.getAppComponent().imageLoader().loadImage(mApplication,
                GlideImageConfig.builder()
                        .url(data.getUser_photo())
                        .placeholder(R.mipmap.def_circle_border_white_avatar)
                        .errorPic(R.mipmap.def_circle_border_white_avatar)
                        .imageView(ivIntroAvatar)
                        .build());

        //人物信息
        tvIntroSinger.setText(data.getWorks_name());
        //歌曲介绍
        tvIntroSong.setText(data.getWorks_desc());
    }

    @OnClick(R.id.layer)
    public void onClickLayer() {
        launchActivity(new Intent(this, MainActivity.class));
        killMyself();
    }

}

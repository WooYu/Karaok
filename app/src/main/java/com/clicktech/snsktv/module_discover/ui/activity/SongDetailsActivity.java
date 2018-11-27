package com.clicktech.snsktv.module_discover.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clicktech.snsktv.BuildConfig;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.ConvertUtils;
import com.clicktech.snsktv.arms.utils.DeviceUtils;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.MediaFile;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.EventBusTag;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.common.net.CommonService;
import com.clicktech.snsktv.entity.ArtworksDetailResponse;
import com.clicktech.snsktv.entity.GiftInfoEntity;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.contract.SongDetailsContract;
import com.clicktech.snsktv.module_discover.inject.component.DaggerSongDetailsComponent;
import com.clicktech.snsktv.module_discover.inject.module.SongDetailsModule;
import com.clicktech.snsktv.module_discover.presenter.SongDetailsPresenter;
import com.clicktech.snsktv.module_discover.ui.adapter.SongDetailAdapter;
import com.clicktech.snsktv.module_discover.ui.fragment.PlayLeftFragment;
import com.clicktech.snsktv.module_discover.ui.fragment.PlayMiddleFragment;
import com.clicktech.snsktv.module_discover.ui.fragment.PlayRightFragment;
import com.clicktech.snsktv.module_enter.ui.activity.LoginActivity;
import com.clicktech.snsktv.module_home.ui.activity.KSingActivity;
import com.clicktech.snsktv.module_home.ui.fragment.CommentFragment;
import com.clicktech.snsktv.module_home.ui.fragment.ShareWorksFragment;
import com.clicktech.snsktv.module_mine.ui.service.MiniPlayerService;
import com.clicktech.snsktv.util.StringHelper;
import com.clicktech.snsktv.util.transformations.CircleWithBorderTransformation;
import com.clicktech.snsktv.widget.filetcircleimageview.MLImageView;
import com.clicktech.snsktv.widget.titlebar.HeaderView;
import com.clicktech.snsktv.widget.videoplayer.DetailPlayer;
import com.jaeger.library.StatusBarUtil;
import com.jakewharton.rxbinding.view.RxView;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;
import timber.log.Timber;

import static com.clicktech.snsktv.R.id.cb_playstatus;
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
 * Created by Administrator on 2017/4/21.
 * 歌曲详情
 */

public class SongDetailsActivity extends WEActivity<SongDetailsPresenter> implements
        SongDetailsContract.View, HeaderView.OnCustomTileListener {

    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.video)
    DetailPlayer video;
    @BindView(R.id.headerview)
    HeaderView headerview;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.civ_singeravatar)
    MLImageView civSingeravatar;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_accessnum)
    TextView tvAccessnum;
    @BindView(R.id.tv_giftnum)
    TextView tvGiftnum;
    @BindView(R.id.tv_grade)
    TextView tvGrade;
    @BindView(R.id.rl_avatarlist)
    RelativeLayout rlAvatarList;
    @BindView(R.id.avatar_a)
    ImageView avatarA;
    @BindView(R.id.avatar_b)
    ImageView avatarB;
    @BindView(R.id.avatar_c)
    ImageView avatarC;
    @BindView(R.id.avatar_d)
    ImageView avatarD;
    @BindView(R.id.avatar_e)
    ImageView avatarE;
    @BindView(R.id.vp_songdetail)
    ViewPager vpSongdetail;
    @BindView(cb_playstatus)
    CheckBox cbPlaystatus;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_curtime)
    TextView tvCurtime;
    @BindView(R.id.tv_totaltime)
    TextView tvTotaltime;
    @BindView(R.id.iv_quit)
    ImageView ivQuit;
    @BindView(R.id.ll_playcontrolview)
    LinearLayout llPlaycontrolview;
    @BindView(R.id.tv_comment)
    TextView tvComment;
    @BindView(R.id.tv_share)
    TextView tvShare;
    @BindView(R.id.tv_join)
    TextView tvJoin;
    @BindView(R.id.tv_collect)
    TextView tvCollect;
    @BindView(R.id.tv_givegift)
    TextView tvGivegift;
    @BindView(R.id.rl_giftrank)
    RelativeLayout rlGiftRank;

    private List<Fragment> mFragList;
    private PlayMiddleFragment mMiddleFragment;
    private PlayLeftFragment mLeftFragment;
    private PlayRightFragment mRightFragment;

    private String mWorksID;//作品id
    private ArtworksDetailResponse mDetailResponse;//作品详情
    private boolean bVideoFile;//视频文件还是音频文件
    private int mAlpha;

    //获得礼物的列表
    private boolean status_collect;//收藏状态
    private boolean status_attent;//关注状态
    private int mLastScroll;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerSongDetailsComponent
                .builder()
                .appComponent(appComponent)
                .songDetailsModule(new SongDetailsModule(this)) //请将SongDetailsModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_songdetails, null, false);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
    }

    @Override
    protected void initData() {
        headerview.setTitleClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (null == bundle) {
            return;
        }

        SongInfoBean songInfoBean = bundle.getParcelable("songinfo");
        boolean connectionPlay = bundle.getBoolean("connectionplay");
        if (EmptyUtils.isEmpty(songInfoBean)) {
            return;
        }
        action_start(songInfoBean, connectionPlay);
        mWorksID = StringHelper.getIDOfSongDetail(songInfoBean);


        initListener();
        initViewpager();
        mPresenter.requestData(mWorksID);
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
        killMyself();
    }

    @Override
    public void setTitleRightClick() {
        launchActivity(new Intent(this, ReportActivity.class));
    }

    @OnClick(R.id.civ_singeravatar)
    public void onCivSingeravatarClicked() {
        if (EmptyUtils.isEmpty(mDetailResponse)) {
            return;
        }
        Intent singerIntroIntent = new Intent(this, SingerIntroActivity.class);
        singerIntroIntent.putExtra("userid", mDetailResponse.getWorksDetail().getUser_id());
        launchActivity(singerIntroIntent);
    }

    @OnClick(R.id.rl_giftrank)
    public void onRlGiftrankClicked() {
        if (EmptyUtils.isEmpty(mWorksID)) {
            return;
        }
        Intent intent = new Intent(SongDetailsActivity.this, GIftRankActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("workid", mWorksID);
        intent.putExtras(bundle);
        launchActivity(intent);
    }

    @OnClick(R.id.cb_playstatus)
    public void onCbPlaystatusClicked() {
        action_playPause();
    }

    @OnClick(R.id.iv_quit)
    public void onIvQuitClicked() {
        showAndHiddenPlayView(false);
    }

    @OnClick(R.id.tv_comment)
    public void onTvCommentClicked() {
        if (!mWeApplication.loggingStatus()) {
            showMessage(getString(R.string.error_notlogin));
            launchActivity(new Intent(this, LoginActivity.class));
            return;
        }
        if (EmptyUtils.isEmpty(mWorksID)) {
            return;
        }
        CommentFragment commentFragment = new CommentFragment();
        commentFragment.show(getSupportFragmentManager(), "com.clicktech.snsktv.module_home.ui.fragment.CommentFragment");
        commentFragment.setCommentInputCallBack(new CommentFragment.CommentInputCallBack() {
            @Override
            public void sendComment(String content) {
                mPresenter.requestReleaseComment(mWorksID, "", content);
            }
        });
    }

    @OnClick(R.id.tv_share)
    public void onTvShareClicked() {
        if (!mWeApplication.loggingStatus()) {
            showMessage(getString(R.string.error_notlogin));
            launchActivity(new Intent(this, LoginActivity.class));
            return;
        }
        if (null == mDetailResponse) {
            return;
        }
        SongInfoBean workInfo = mDetailResponse.getWorksDetail();
        String worksname = workInfo.getWorks_name();
        String worksdesc = workInfo.getWorks_desc();
        String worksimg = BuildConfig.APP_DOMAIN_File + workInfo.getWorks_image();
        String worksurl = BuildConfig.APP_DOMAIN + CommonService.SHARE_URL +
                "userId=" + (KtvApplication.ktvApplication).getUserID() + "&worksId=" + workInfo.getId();

        Bundle bundle = new Bundle();
        bundle.putString("worksname", worksname);
        bundle.putString("worksdesc", worksdesc);
        bundle.putString("worksurl", worksurl);
        bundle.putString("worksimg", worksimg);
        ShareWorksFragment shareWorksFragment = new ShareWorksFragment();
        shareWorksFragment.setArguments(bundle);
        shareWorksFragment.show(getSupportFragmentManager(), "com.clicktech.snsktv.module_home.ui.fragment.ShareWorksFragment");
    }

    @OnClick(R.id.tv_join)
    public void onTvJoinClicked() {
        turn2Ksing();
    }

    @OnClick(R.id.tv_givegift)
    public void onTvGivegiftClicked() {
        if (!mWeApplication.loggingStatus()) {
            showMessage(getString(R.string.error_notlogin));
            launchActivity(new Intent(this, LoginActivity.class));
            return;
        }
        mPresenter.requestGiftList(getSupportFragmentManager());
    }

    @Override
    public void initUIInterface(ArtworksDetailResponse detailResponse) {

        this.mDetailResponse = detailResponse;
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putParcelable("songinfo", detailResponse.getWorksDetail());
        bundle.putBoolean("status", true);
        message.setData(bundle);
        EventBus.getDefault().post(message, EventBusTag.MINIPLAYER_SHOW);

        SongInfoBean songInfoBean = detailResponse.getWorksDetail();
        List<GiftInfoEntity> giftList = detailResponse.getWorksGiftList();

        if (null == songInfoBean) {
            return;
        }
        //标题
        headerview.setTitleName(StringHelper.getLau_With_J_U_C(songInfoBean.getSong_name_jp(),
                songInfoBean.getSong_name_us(), songInfoBean.getSong_name_cn()));
        //头像
        mWeApplication.getAppComponent().imageLoader().loadImage(mWeApplication,
                GlideImageConfig.builder()
                        .url(songInfoBean.getUser_photo())
                        .placeholder(R.mipmap.def_avatar_round)
                        .errorPic(R.mipmap.def_avatar_round)
                        .imageView(civSingeravatar)
                        .build());
        //昵称
        tvNickname.setText(songInfoBean.getUser_nickname());
        //试听
        tvAccessnum.setText(songInfoBean.getListen_count());
        //礼物
        tvGiftnum.setText(String.valueOf(detailResponse.getTotal_flower_num()));
        //评分
        tvGrade.setText(songInfoBean.getWorks_score());
        //演唱这首歌的前5名
        setTheHeadsOfRankTop(giftList);
        //设置背景图片
        setBackgroundImage(songInfoBean.getWorks_image());
        //收藏状态
        if (EmptyUtils.isNotEmpty(detailResponse.getAttentionTypeWorks())
                && getResources().getInteger(R.integer.attentiontypeworks_no) == Integer.parseInt(detailResponse.getAttentionTypeWorks())) {
            updateStatusOfCollect(false);
        } else {
            updateStatusOfCollect(true);
        }
        //关注
        if (EmptyUtils.isNotEmpty(detailResponse.getAttentionType())
                && getResources().getInteger(R.integer.attentiontype_no) == Integer.parseInt(detailResponse.getAttentionType())) {
            updateStatusOfAttention(false);
        } else {
            updateStatusOfAttention(true);
        }

        //设置中间界面
        setDataOfFragment(detailResponse);
    }

    @Override
    public void downloadTheLyrics(String lyricpath) {
        //初始化右边界面
        Bundle bundle = new Bundle();
        bundle.putString("lyricpath", lyricpath);
        mRightFragment.setData(bundle);
    }

    @Override
    public void updateStatusOfCollect(boolean status) {
        status_collect = status;
        if (status) {
            setIconIfCollectAndAttention(R.mipmap.songdetail_collect_success, true);
        } else {
            setIconIfCollectAndAttention(R.mipmap.songdetail_collect_cancel, true);
        }
    }

    @Override
    public void updateStatusOfAttention(boolean status) {
        status_attent = status;
        if (status) {
            setIconIfCollectAndAttention(R.mipmap.songdetail_attention_ok, false);
        } else {
            setIconIfCollectAndAttention(R.mipmap.songdetail_attention, false);
        }
    }

    /***
     * 初始化viewpager
     */
    private void initViewpager() {
        mFragList = new ArrayList<>();
        mFragList.add(mLeftFragment = PlayLeftFragment.newInstance());
        mFragList.add(mMiddleFragment = PlayMiddleFragment.newInstance());
        mFragList.add(mRightFragment = PlayRightFragment.newInstance());
        SongDetailAdapter mSongAdapter = new SongDetailAdapter(getSupportFragmentManager(), mFragList);
        vpSongdetail.setAdapter(mSongAdapter);
        vpSongdetail.setOffscreenPageLimit(2);
        vpSongdetail.setCurrentItem(1);
        vpSongdetail.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "position = " + position + ",positionOffset = " + positionOffset + ",positionOffsetPixels = " + positionOffsetPixels);

                if (mLastScroll > positionOffsetPixels) {
                    //向左滑动
                    if (position == 0) {
                        //从页面0→1,变透明
                        mAlpha = Math.round((1 - positionOffset) * 100);
                    } else if (position == 1) {
                        //从页面1→2，变模糊
                        mAlpha = Math.round(positionOffset * 100);
                    }
                } else if (mLastScroll < positionOffsetPixels) {
                    //向右滑动
                    if (position == 0) {
                        //从页面1→0，变模糊
                        mAlpha = Math.round((1 - position) * 100);
                    }
                    if (position == 1) {
                        //从页面2→1，变透明
                        mAlpha = Math.round(positionOffset * 100);
                    }
                }

                mLastScroll = positionOffsetPixels;
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected: position = " + position);

                showAndHiddenPlayView(position != 0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d(TAG, "onPageScrollStateChanged: state = " + state);
            }
        });

        vpSongdetail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showAndHiddenPlayView(true);
                }
                return false;
            }
        });
    }

    //防抖动监听
    private void initListener() {
        RxView.clicks(tvCollect)
                .throttleFirst(3, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mPresenter.clickCollect(status_collect);
                    }
                });

        RxView.clicks(tvNickname)
                .throttleFirst(3, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mPresenter.clickAttent(status_attent);
                    }
                });
    }

    //设置前5名头像
    private void setTheHeadsOfRankTop(List<GiftInfoEntity> giftlist) {
        int size = EmptyUtils.isEmpty(giftlist) ? 0 : giftlist.size();
        ImageLoader imageLoader = mWeApplication.getAppComponent().imageLoader();
        if (size == 0) {
            rlAvatarList.setVisibility(View.GONE);
        }
        if (size > 0) {
            imageLoader.loadImage(mApplication,
                    GlideImageConfig.builder()
                            .url(giftlist.get(0).getUser_photo())
                            .placeholder(R.mipmap.def_avatar_round)
                            .errorPic(R.mipmap.def_avatar_round)
                            .transformation(new CircleWithBorderTransformation(mApplication))
                            .imageView(avatarA)
                            .build());
        }
        if (size > 1) {
            avatarB.setVisibility(View.VISIBLE);
            imageLoader.loadImage(mApplication,
                    GlideImageConfig.builder()
                            .url(giftlist.get(1).getUser_photo())
                            .placeholder(R.mipmap.def_avatar_round)
                            .errorPic(R.mipmap.def_avatar_round)
                            .transformation(new CircleWithBorderTransformation(mApplication))
                            .imageView(avatarB)
                            .build());

        }
        if (size > 2) {
            avatarC.setVisibility(View.VISIBLE);
            imageLoader.loadImage(mApplication,
                    GlideImageConfig.builder()
                            .url(giftlist.get(2).getUser_photo())
                            .placeholder(R.mipmap.def_avatar_round)
                            .errorPic(R.mipmap.def_avatar_round)
                            .transformation(new CircleWithBorderTransformation(mApplication))
                            .imageView(avatarC)
                            .build());

        }
        if (size > 3) {
            avatarD.setVisibility(View.VISIBLE);
            imageLoader.loadImage(mApplication,
                    GlideImageConfig.builder()
                            .url(giftlist.get(3).getUser_photo())
                            .placeholder(R.mipmap.def_avatar_round)
                            .errorPic(R.mipmap.def_avatar_round)
                            .transformation(new CircleWithBorderTransformation(mApplication))
                            .imageView(avatarD)
                            .build());
        }
        if (size > 4) {
            avatarD.setVisibility(View.VISIBLE);
            imageLoader.loadImage(mApplication,
                    GlideImageConfig.builder()
                            .url(giftlist.get(4).getUser_photo())
                            .placeholder(R.mipmap.def_avatar_round)
                            .errorPic(R.mipmap.def_avatar_round)
                            .transformation(new CircleWithBorderTransformation(mApplication))
                            .imageView(avatarE)
                            .build());
        }
    }

    //设置背景图
    private void setBackgroundImage(String bgurl) {
        if (EmptyUtils.isEmpty(bgurl))
            return;

        mWeApplication.getAppComponent().imageLoader().loadImage(this,
                GlideImageConfig.builder()
                        .url(bgurl)
                        .errorPic(R.mipmap.def_square_large)
                        .placeholder(R.mipmap.def_square_large)
                        .imageView(ivPhoto)
                        .build());
    }

    @Override
    public void updatePlayProgress(boolean status, int currentProgress, int totalProgress) {
        Timber.d("status = %b,curProgress = %d ,totalProgress =  %d",
                status, currentProgress, totalProgress);

        cbPlaystatus.setChecked(!status);
        if (!status || currentProgress <= 0 || totalProgress < 0) {
            return;
        }

        progressBar.setMax(totalProgress);
        progressBar.setProgress(currentProgress);

        //设置歌曲时长
        String curTimeStr = ConvertUtils.millis2mmss(currentProgress);
        String totalTimeStr = ConvertUtils.millis2mmss(totalProgress);
        tvCurtime.setText(curTimeStr);
        tvTotaltime.setText(totalTimeStr);

        //更新歌词进度
        mRightFragment.seekTo(currentProgress);

        if (curTimeStr.equals(totalTimeStr)) {
            mRightFragment.seekTo(totalProgress);
        }

    }

    // 显示或者隐藏播放界面
    private void showAndHiddenPlayView(boolean show) {
        rlTop.setVisibility(show ? View.VISIBLE : View.GONE);
        llPlaycontrolview.setVisibility(show ? View.VISIBLE : View.GONE);
        if (null != mMiddleFragment) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", 2);
            bundle.putBoolean("fogstate", show);
            mMiddleFragment.setData(bundle);
        }
    }

    //设置收藏和关注的图标
    private void setIconIfCollectAndAttention(int resourceid, boolean collect) {
        Drawable drawable = getResources().getDrawable(resourceid);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        if (collect) {
            tvCollect.setCompoundDrawables(null, drawable, null, null);
        } else {
            tvNickname.setCompoundDrawables(null, null, drawable, null);
        }
    }

    //设置Fragment的数据
    private void setDataOfFragment(ArtworksDetailResponse response) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        bundle.putParcelable("workdetails", response);
        mMiddleFragment.setData(bundle);
        mLeftFragment.setData(bundle);
    }

    //跳转到k歌
    private void turn2Ksing() {
        if (null == mDetailResponse) {
            return;
        }
        Intent ksingIntent = new Intent(this, KSingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("songinfo", mDetailResponse.getWorksDetail());
        bundle.putInt("workstype", getResources().getInteger(R.integer.workstype_common));
        ksingIntent.putExtras(bundle);
        startActivity(ksingIntent);
        killMyself();
    }

    //跳转到更多歌曲
    public void turn2MoreSong() {
        if (null == mDetailResponse) {
            return;
        }
        Intent moreSongIntent = new Intent(this, MoreSongsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("songranking", (ArrayList<SongInfoBean>) mDetailResponse.getSongWorksList());
        moreSongIntent.putExtras(bundle);
        launchActivity(moreSongIntent);
        killMyself();
    }

    //开始播放
    public void action_start(final SongInfoBean songInfoBean, boolean connectionplay) {
        if (null == songInfoBean || EmptyUtils.isEmpty(songInfoBean.getWorks_url())) {
            Timber.e("作品路径异常！");
            return;
        }

        //判断是音频还是视频
        String worksurl = BuildConfig.APP_DOMAIN_File + songInfoBean.getWorks_url();
        bVideoFile = MediaFile.isVideoFileType(worksurl);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) video.getLayoutParams();
        if (bVideoFile) {
            ivPhoto.setVisibility(View.GONE);
            int width = (int) DeviceUtils.getScreenWidth(this);
            int height = (int) DeviceUtils.getScreenHeight(this);
            int worktype = Integer.parseInt(songInfoBean.getWorks_type());
            if (worktype == getResources().getInteger(R.integer.workstype_chrousother)) {
                //合唱
                layoutParams.width = width;
                layoutParams.height = width;
            } else {
                layoutParams.width = width;
                layoutParams.height = height;
            }
        } else {
            ivPhoto.setVisibility(View.VISIBLE);
            layoutParams.width = 1;
            layoutParams.height = 1;
        }
        video.setLayoutParams(layoutParams);
        video.setVideoPlayCallBack(new DetailPlayer.WorkPlayerCallBack() {
            @Override
            public void onStatePreparing() {
                action_changePlayState(true);
                addToPlayList(songInfoBean);
            }

            @Override
            public void onStatePlaying() {
                action_changePlayState(true);
            }

            @Override
            public void onStatePause() {
                action_changePlayState(false);
            }

            @Override
            public void onStateAutoComplete() {
                action_changePlayState(false);
            }

            @Override
            public void onStateError() {
                action_changePlayState(false);
            }

            @Override
            public void onCompletion() {
                action_changePlayState(false);
            }
        });

        video.setUpParams(BuildConfig.APP_DOMAIN_File + songInfoBean.getWorks_url(),
                songInfoBean.getWorks_image(), connectionplay);
    }

    //添加到播放列表
    private void addToPlayList(SongInfoBean songInfoBean) {
        Bundle addSongBundle = new Bundle();
        addSongBundle.putParcelable("songinfo", songInfoBean);
        addSongBundle.putInt("type", 1);
        Message message = new Message();
        message.setData(addSongBundle);
        EventBus.getDefault().post(message, EventBusTag.PLAYLIST_ADDSONG);
    }

    //暂停/播放
    private void action_playPause() {
        video.startButton.performClick();
    }

    //改变播放状态
    private void action_changePlayState(boolean state) {
        if (null != cbPlaystatus) {
            cbPlaystatus.setChecked(state);
        }

        Bundle bundle = new Bundle();
        bundle.putBoolean("status", state);
        if (null != mDetailResponse) {
            bundle.putParcelable("songinfo", mDetailResponse.getWorksDetail());
        }
        Message message = new Message();
        message.setData(bundle);
        EventBus.getDefault().post(message, EventBusTag.MINIPLAYER_TOGGLE);
    }

    @Subscriber(tag = EventBusTag.MINIPLAYER_UPDATEPROGRESS, mode = ThreadMode.MAIN)
    public void eventUpdateMiniPlayProgress(Message message) {
        Bundle bundle = message.getData();
        if (null == bundle) {
            return;
        }

        int currentProgress = bundle.getInt(MiniPlayerService.PROGRESS_CURRENT, 0);
        int totalProgress = bundle.getInt(MiniPlayerService.PROGRESSS_TOTAL, 0);

        if (currentProgress <= 0 || totalProgress <= 0) {
            return;
        }

        progressBar.setProgress(currentProgress);
        progressBar.setMax(totalProgress);

        //设置歌曲时长
        String curTimeStr = ConvertUtils.millis2mmss(currentProgress);
        String totalTimeStr = ConvertUtils.millis2mmss(totalProgress);
        tvCurtime.setText(curTimeStr);
        tvTotaltime.setText(totalTimeStr);

        //更新歌词进度
        mRightFragment.seekTo(currentProgress);

    }
}

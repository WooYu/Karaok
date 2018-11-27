package com.clicktech.snsktv.module_mine.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.DataHelper;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.autolayout.AutoRadioGroup;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.ConstantConfig;
import com.clicktech.snsktv.common.EventBusTag;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.common.WEFragment;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.entity.GiftListResponse;
import com.clicktech.snsktv.entity.MineUserInfoResponse;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.entity.UserInfoBean;
import com.clicktech.snsktv.module_discover.ui.activity.SingerAlbumActivity;
import com.clicktech.snsktv.module_discover.ui.activity.SongDetailsActivity;
import com.clicktech.snsktv.module_enter.ui.activity.LoginActivity;
import com.clicktech.snsktv.module_mine.contract.MineContract;
import com.clicktech.snsktv.module_mine.inject.component.DaggerMineComponent;
import com.clicktech.snsktv.module_mine.inject.module.MineModule;
import com.clicktech.snsktv.module_mine.presenter.MinePresenter;
import com.clicktech.snsktv.module_mine.ui.activity.KCoinActivity;
import com.clicktech.snsktv.module_mine.ui.activity.MessageActivity;
import com.clicktech.snsktv.module_mine.ui.activity.Mine_Att_UserListActivity;
import com.clicktech.snsktv.module_mine.ui.activity.Mine_FansListActivity;
import com.clicktech.snsktv.module_mine.ui.activity.Mine_MyTracksActivity;
import com.clicktech.snsktv.module_mine.ui.activity.Mine_MyWorksListActivity;
import com.clicktech.snsktv.module_mine.ui.activity.SettingActivity;
import com.clicktech.snsktv.module_mine.ui.service.MiniPlayerService;
import com.clicktech.snsktv.util.transformations.CircleWithBorderTransformation;
import com.clicktech.snsktv.widget.progressbar.RoundProgress;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.badgeview.BGABadgeImageView;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGAStickyNavLayout;
import cn.bingoogolapple.refreshlayout.SlidingListener;
import cn.jzvd.JZVideoPlayer;

/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */

/**
 * Created by Administrator on 2017/2/4.
 * 我的
 */

public class MineFragment extends WEFragment<MinePresenter> implements MineContract.View,
        BGARefreshLayout.BGARefreshLayoutDelegate {

    private static final int RequestCode_Message = 0x0014;
    @BindView(R.id.tv_notlogin)
    TextView tvNotLogin;
    @BindView(R.id.ll_logged)
    LinearLayout llLogged;
    @BindView(R.id.banner_zoomStack)
    BGABanner bannerZoomStack;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.iv_account)
    ImageView ivAccount;
    @BindView(R.id.iv_mytracks)
    ImageView ivMytracks;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.iv_grade)
    ImageView ivGrade;
    @BindView(R.id.tv_grade)
    TextView tvGrade;
    @BindView(R.id.ll_nick_grade)
    LinearLayout llNickGrade;
    @BindView(R.id.iv_sex)
    ImageView ivSex;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_addr)
    TextView tvAddr;
    @BindView(R.id.tv_works)
    TextView tvWorks;
    @BindView(R.id.ll_works)
    LinearLayout llWorks;
    @BindView(R.id.tv_fans)
    TextView tvFans;
    @BindView(R.id.ll_fans)
    LinearLayout llFans;
    @BindView(R.id.tv_attention)
    TextView tvAttention;
    @BindView(R.id.ll_attention)
    LinearLayout llAttention;
    @BindView(R.id.ll_album)
    LinearLayout llAlbum;
    @BindView(R.id.rb_attention)
    RadioButton rbAttention;
    @BindView(R.id.rb_popular)
    RadioButton rbPopular;
    @BindView(R.id.rg_works)
    AutoRadioGroup rgWorks;
    @BindView(R.id.mybagnavlayout)
    BGAStickyNavLayout mybagnavlayout;
    @BindView(R.id.refreshLayout)
    BGARefreshLayout refreshLayout;
    @BindView(R.id.statusbar_view)
    View statusbarView;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.ll_miniplayer)
    LinearLayout llMiniPlayer;
    @BindView(R.id.tv_singer)
    TextView tvSinger;
    @BindView(R.id.tv_songname)
    TextView tvSongName;
    @BindView(R.id.iv_moreworks)
    ImageView ivMoreWorks;
    @BindView(R.id.progress)
    RoundProgress mProgressBar;
    private BGABadgeImageView ivMsg;
    private Mine_AttentionFragment mAttentionFragment;
    private Mine_PopularHotFragment mPopularFragment;
    private List<Fragment> mFragments;
    private int mIndexOfFragment;
    private Bundle mBundle;//播放的数据

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void launchActivity(Intent intent) {
        UiUtils.startActivity(getActivity(), intent);
    }

    @Override
    public void killMyself() {

    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        DaggerMineComponent
                .builder()
                .appComponent(appComponent)
                .mineModule(new MineModule(this))//MineModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mine, null, false);
        ivMsg = view.findViewById(R.id.iv_msg);
        return view;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = initView();
        //绑定到butterknife
        mUnbinder = ButterKnife.bind(this, mRootView);
        initBGARefreshLayout();
        return mRootView;
    }

    @Override
    protected void initData() {
        initBGANavLayout();
        initViewpager();
        initRadioGroup();
        initLoginStatus();
    }

    @Override
    public void setData(Object data) {
        super.setData(data);

        //有推送消息
        if (null != ivMsg) {
            ivMsg.showCirclePointBadge();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCode_Message) {
            int totalNum = DataHelper.getIntergerSF(getContext(), ConstantConfig.PUSHMESSAGE_TOTAL);
            if (totalNum > 0) {
                ivMsg.showCirclePointBadge();
            } else {
                ivMsg.hiddenBadge();
            }
        }
    }

    @OnClick({R.id.iv_setting, R.id.iv_account, R.id.iv_msg, R.id.iv_mytracks, R.id.tv_notlogin,
            R.id.ll_works, R.id.ll_fans, R.id.ll_attention, R.id.ll_album})
    public void onViewClicked(View view) {
        if (!mWeApplication.loggingStatus()) {
            turn2Login();
            return;
        }

        switch (view.getId()) {
            case R.id.iv_setting:
                onIvSettingClicked();
                break;
            case R.id.iv_account:
                onIvAccountClicked();
                break;
            case R.id.iv_msg:
                onIvMsgClicked();
                break;
            case R.id.iv_mytracks:
                onIvMytracksClicked();
                break;
            case R.id.ll_works:
                onLlWorksClicked();
                break;
            case R.id.ll_fans:
                onLlFansClicked();
                break;
            case R.id.ll_attention:
                onLlAttentionClicked();
                break;
            case R.id.ll_album:
                onLlAlbumClicked();
                break;
        }
    }

    @OnClick(R.id.progress)
    public void onClickedIvPlayPause() {
        if (null == mBundle) {
            return;
        }
        boolean status = mBundle.getBoolean("status");
        if (status) {
            JZVideoPlayer.goOnPlayOnPause();
        } else {
            JZVideoPlayer.goOnPlayOnResume();
        }
        mBundle.putBoolean("status", !status);
        Message message = new Message();
        message.setData(mBundle);
        EventBus.getDefault().post(message, EventBusTag.MINIPLAYER_TOGGLE);
    }

    @OnClick(R.id.iv_moreworks)
    public void onClickedIvMoreWorks() {
        EventBus.getDefault().post(new Message(), EventBusTag.PLAYLIST_SHOW);
    }

    @OnClick(R.id.ll_miniplayer)
    public void onClickedllMiniPlayer() {
        if (null == mBundle) {
            return;
        }

        SongInfoBean songInfoBean = mBundle.getParcelable("songinfo");
        Intent intent = new Intent(getActivity(), SongDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("songinfo", songInfoBean);
        bundle.putBoolean("connectionplay", true);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void onIvSettingClicked() {
        Intent settingIntent = new Intent(mWeApplication, SettingActivity.class);
        startActivity(settingIntent);
    }

    private void onIvAccountClicked() {
        launchActivity(new Intent(mWeApplication, KCoinActivity.class));
    }

    private void onIvMsgClicked() {
        startActivityForResult(new Intent(mWeApplication, MessageActivity.class), RequestCode_Message);
    }

    private void onIvMytracksClicked() {
        launchActivity(new Intent(mWeApplication, Mine_MyTracksActivity.class));
    }

    private void onLlWorksClicked() {
        Intent worksIntent = new Intent(mWeApplication, Mine_MyWorksListActivity.class);
        worksIntent.putExtra("userid", mWeApplication.getUserID());
        launchActivity(worksIntent);
    }

    private void onLlFansClicked() {
        launchActivity(new Intent(mWeApplication, Mine_FansListActivity.class));
    }

    private void onLlAttentionClicked() {
        launchActivity(new Intent(mWeApplication, Mine_Att_UserListActivity.class));
    }

    private void onLlAlbumClicked() {
        launchActivity(new Intent(mWeApplication, SingerAlbumActivity.class));
    }

    //初始化固定标签的布局
    private void initBGANavLayout() {
        mybagnavlayout.setSlidingListener(new SlidingListener() {
            @Override
            public void scrollChange(float f) {
                if (f < 0.4) {
                    statusbarView.setAlpha(f);
                } else {
                    statusbarView.setAlpha(1);
                }
            }
        });
    }

    //初始化最外层的刷新布局
    private void initBGARefreshLayout() {
        refreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(KtvApplication.ktvApplication, true));
        refreshLayout.setDelegate(this);
    }

    //初始化登录状态
    private void initLoginStatus() {
        if (mWeApplication.loggingStatus()) {
            setViewOfAlreadLogged();
            mPresenter.requestGiftList();
            mPresenter.requestUserInfo();
            mPresenter.requestPhotoAlbum();
        } else {
            setViewOfNotLogin();
        }
    }

    //初始化Fragment
    private void initViewpager() {
        mFragments = new ArrayList<>();
        mFragments.add(mAttentionFragment = Mine_AttentionFragment.newInstance());
        mFragments.add(mPopularFragment = Mine_PopularHotFragment.newInstance());

        viewpager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        });

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        rbAttention.setChecked(true);
                        break;
                    case 1:
                        rbPopular.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    //切换fragment
    private void setIndexSelected(int index) {
        if (mIndexOfFragment == index) {
            return;
        }
        viewpager.setCurrentItem(index);
        mIndexOfFragment = index;

    }

    //初始化RadioGroup
    private void initRadioGroup() {
        rgWorks.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.rb_attention) {
                    setIndexSelected(0);
                } else if (checkedId == R.id.rb_popular) {
                    setIndexSelected(1);
                }
            }
        });
    }

    @Override
    public void updateUserInfoView(MineUserInfoResponse response) {
        tvFans.setText(response.getFansNum());
        tvAttention.setText(response.getAttentionNum());
        tvWorks.setText(response.getWorksNum());

        UserInfoBean userInfoBean = response.getUser();
        if (null == userInfoBean)
            return;

        mWeApplication.getAppComponent().imageLoader().loadImage(mWeApplication, GlideImageConfig
                .builder()
                .transformation(new CircleWithBorderTransformation(mWeApplication, 2, Color.WHITE))
                .errorPic(R.mipmap.def_avatar_round)
                .placeholder(R.mipmap.def_avatar_round)
                .cacheStrategy(2)
                .url(userInfoBean.getUser_photo())
                .imageView(ivAvatar)
                .build());
        tvUsername.setText(userInfoBean.getUser_nickname());
        //等级
        ivGrade.setImageResource(mPresenter.convertGradeToImage(userInfoBean.getUser_level()));
        tvGrade.setText(String.format(getString(R.string.format_class), userInfoBean.getUser_level()));
        int sexType = userInfoBean.getUser_sex();
        if (getResources().getInteger(R.integer.sex_female) == sexType) {
            ivSex.setImageResource(R.mipmap.mine_sex_female);
        } else {
            ivSex.setImageResource(R.mipmap.mine_sex_male);
        }
        tvAge.setText(String.format(getString(R.string.format_age), userInfoBean.getUser_age()));
        tvAddr.setText(userInfoBean.getUser_district_dtl());
    }

    @Override
    public void updateAlbumOfUser(List<String> urls) {
        if (EmptyUtils.isEmpty(urls)) {
            bannerZoomStack.setData(R.mipmap.bg_mine_album);
            bannerZoomStack.stopAutoPlay();
            return;
        }

        bannerZoomStack.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                mWeApplication.getAppComponent().imageLoader().loadImage(mWeApplication, GlideImageConfig
                        .builder()
                        .errorPic(R.mipmap.bg_mine_album)
                        .placeholder(R.mipmap.bg_mine_album)
                        .url(model)
                        .imageView(itemView)
                        .build());
            }
        });

        bannerZoomStack.setData(urls, urls);
        bannerZoomStack.startAutoPlay();
    }

    @Override
    public void setGiftListForChildFragment(GiftListResponse response) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", 2);
        bundle.putParcelable("giftinfo", response);
        if (null != mAttentionFragment) {
            mAttentionFragment.setData(bundle);
        }

        if (null != mPopularFragment) {
            mPopularFragment.setData(bundle);
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout bgaRefreshLayout) {
        initLoginStatus();
        mAttentionFragment.onBGARefreshLayoutBeginRefreshing(refreshLayout);
        mPopularFragment.onBGARefreshLayoutBeginRefreshing(refreshLayout);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout bgaRefreshLayout) {
        switch (mIndexOfFragment) {
            case 0:
                if (!mWeApplication.loggingStatus()) {
                    return false;
                }
                return mAttentionFragment.onBGARefreshLayoutBeginLoadingMore(refreshLayout);
            case 1:
                return mPopularFragment.onBGARefreshLayoutBeginLoadingMore(refreshLayout);
        }
        return false;
    }

    @Subscriber(tag = EventBusTag.LOGIN_SUCCESS, mode = ThreadMode.MAIN)
    public void eventLoginSuccess(Message message) {
        refreshLayout.beginRefreshing();
    }

    @Subscriber(tag = EventBusTag.LOGOUTSUCCESS, mode = ThreadMode.MAIN)
    public void eventLogoutSuccess(Message message) {
        setViewOfNotLogin();
    }

    @Subscriber(tag = EventBusTag.CHANGEAVATAR, mode = ThreadMode.MAIN)
    public void eventChangeAvatar(Message message) {
        String photourl = mWeApplication.getUserInfoBean().getUser_photo();
        mWeApplication.getAppComponent().imageLoader().loadImage(mWeApplication,
                GlideImageConfig.builder()
                        .transformation(new CircleWithBorderTransformation(mWeApplication, 2, Color.WHITE))
                        .url(photourl)
                        .errorPic(R.mipmap.def_avatar_round)
                        .placeholder(R.mipmap.def_avatar_round)
                        .imageView(ivAvatar)
                        .build());
    }

    @Subscriber(tag = EventBusTag.ANNOUNCESUCCESS, mode = ThreadMode.MAIN)
    public void eventWorkPublishSuccess(Message message) {
        mPresenter.requestUserInfo();
    }

    @Subscriber(tag = EventBusTag.PHOTOALBUMCHANGE, mode = ThreadMode.MAIN)
    public void eventAlbumChange(Message message) {
        mPresenter.requestPhotoAlbum();
    }

    @Subscriber(tag = EventBusTag.MINIPLAYER_HIDE, mode = ThreadMode.MAIN)
    public void eventHideMiniPlayer(Message message) {
        llMiniPlayer.setVisibility(View.GONE);
    }

    @Subscriber(tag = EventBusTag.MINIPLAYER_SHOW, mode = ThreadMode.MAIN)
    public void eventShowMiniPlayer(Message message) {
        mBundle = message.getData();
        SongInfoBean songInfoBean = mBundle.getParcelable("songinfo");
        boolean newwork = mBundle.getBoolean("newwork");
        boolean status = mBundle.getBoolean("status");
        if (newwork) {
            mProgressBar.setProgress(0);
        }
        llMiniPlayer.setVisibility(View.VISIBLE);
        if (EmptyUtils.isEmpty(songInfoBean)) {
            return;
        }
        tvSinger.setText(songInfoBean.getUser_nickname());
        tvSongName.setText(songInfoBean.getWorks_name());
        mProgressBar.updatePlayPauseButton(status);
    }

    @Subscriber(tag = EventBusTag.MINIPLAYER_UPDATEPROGRESS, mode = ThreadMode.MAIN)
    public void eventUpdateMiniPlayProgress(Message message) {
        Bundle bundle = message.getData();
        int currentTime = bundle.getInt(MiniPlayerService.PROGRESS_CURRENT, 0);
        int totalTime = bundle.getInt(MiniPlayerService.PROGRESSS_TOTAL, 0);
        mProgressBar.setMax(totalTime);
        mProgressBar.setProgress(currentTime);
    }

    @Subscriber(tag = EventBusTag.MINIPLAYER_STOP, mode = ThreadMode.MAIN)
    public void eventMiniPlayStop(Message message) {
        JZVideoPlayer.goOnPlayOnPause();
        if (null != mBundle) {
            mBundle.putBoolean("status", false);
            message.setData(mBundle);
            EventBus.getDefault().post(message, EventBusTag.MINIPLAYER_TOGGLE);
        }
    }

    @Subscriber(tag = EventBusTag.MINIPLAYER_UPDATEPLAYSTATE, mode = ThreadMode.MAIN)
    public void eventMiniPlayChangePlayState(Message message) {
        mBundle = message.getData();
        if (null != mBundle) {
            mProgressBar.updatePlayPauseButton(mBundle.getBoolean("status"));
        }
    }

    //设置已经登录的界面
    private void setViewOfAlreadLogged() {
        llLogged.setVisibility(View.VISIBLE);
        tvNotLogin.setVisibility(View.GONE);
    }

    //设置没有登录的界面
    private void setViewOfNotLogin() {
        ivAvatar.setImageResource(R.mipmap.def_avatar_round_light);
        updateAlbumOfUser(null);
        llLogged.setVisibility(View.GONE);
        tvNotLogin.setVisibility(View.VISIBLE);
        tvFans.setText("0");
        tvAttention.setText("0");
        tvWorks.setText("0");
        if (null != mAttentionFragment) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", 1);
            mAttentionFragment.setData(bundle);
        }
    }

    //跳转到登录页面
    public void turn2Login() {
        launchActivity(new Intent(mWeApplication, LoginActivity.class));
    }

    //结束刷新
    public void endRefreshing() {
        refreshLayout.endRefreshing();
    }

    //结束加载更多
    public void endLoadingMore() {
        refreshLayout.endLoadingMore();
    }

}
package com.clicktech.snsktv.module_home.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.autolayout.AutoRadioGroup;
import com.clicktech.snsktv.arms.widget.headerviewpager.HeaderScrollHelper;
import com.clicktech.snsktv.arms.widget.headerviewpager.HeaderViewPager;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.EventBusTag;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.entity.SingerInfoEntity;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.ui.activity.MvPreviewFirstActivity;
import com.clicktech.snsktv.module_discover.ui.activity.ReportActivity;
import com.clicktech.snsktv.module_discover.ui.activity.SelectRoleActivity;
import com.clicktech.snsktv.module_home.contract.LearnSingContract;
import com.clicktech.snsktv.module_home.inject.component.DaggerLearnSingComponent;
import com.clicktech.snsktv.module_home.inject.module.LearnSingModule;
import com.clicktech.snsktv.module_home.presenter.LearnSingPresenter;
import com.clicktech.snsktv.module_home.ui.fragment.ChorusRecomListFragment;
import com.clicktech.snsktv.module_home.ui.fragment.ListenListFragment;
import com.clicktech.snsktv.module_home.ui.fragment.PopularListFragment;
import com.clicktech.snsktv.util.HistoryUtils;
import com.clicktech.snsktv.util.StringHelper;
import com.clicktech.snsktv.util.transformations.BlurTransformation;
import com.clicktech.snsktv.widget.titlebar.HeaderView;
import com.jaeger.library.StatusBarUtil;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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
 * Created by Administrator on 2017/1/19.
 * 学唱主页
 */

public class LearnSingActivity extends WEActivity<LearnSingPresenter> implements
        LearnSingContract.View, RadioGroup.OnCheckedChangeListener,
        HeaderView.OnCustomTileListener, View.OnClickListener {

    @BindView(R.id.headerview)
    HeaderView headerView;
    @BindView(R.id.fixedNaviLayout)
    HeaderViewPager mFixedNaviLayout;
    @BindView(R.id.autorg_learnsing)
    AutoRadioGroup mLearnSingRg;
    @BindView(R.id.tv_song_name)
    TextView tv_song_name;
    @BindView(R.id.tv_song_singername)
    TextView tv_song_singername;
    @BindView(R.id.tv_song_songfirstlrc)
    TextView tv_song_songfirstlrc;
    @BindView(R.id.ll_learnsing_topbg)
    LinearLayout ll_learnsing_topbg;
    @BindView(R.id.image_song_album)
    ImageView image_song_album;

    private List<Fragment> mFragments;
    private ListenListFragment mListenFragment;//收听榜
    private PopularListFragment mPopularFragment;//人气榜
    private ChorusRecomListFragment mChorusRecomFragment;//合唱推荐榜
    private Fragment mCurFragment;

    private String mSongId;//歌曲id;
    private SingerInfoEntity mSingerInfo;
    private SongInfoBean mSongInfo;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerLearnSingComponent
                .builder()
                .appComponent(appComponent)
                .learnSingModule(new LearnSingModule(this)) //请将LearnSingModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_learnsing, null, false);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTransparentForImageView(this, null);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mSongId = bundle.getString("songid");
            mSingerInfo = bundle.getParcelable("singer");
            mSongInfo = bundle.getParcelable("song");
        }

        headerView.setTitleClickListener(this);
        headerView.setTitleTextViewAlpha(0);
        initFragments();
        mLearnSingRg.setOnCheckedChangeListener(this);
        initFixedLayout();

        mPresenter.requestData_WithSongId(mSongId);
    }

    @Override
    public void setUpUiWithData(SongInfoBean singInfoBean) {
        if (null == singInfoBean)
            return;

        mSongInfo = singInfoBean;
        //歌曲名
        headerView.setTitleName(StringHelper.getLau_With_J_U_C(singInfoBean.getSong_name_jp(),
                singInfoBean.getSong_name_us(), singInfoBean.getSong_name_cn()));
        //歌曲名
        tv_song_name.setText(StringHelper.getLau_With_J_U_C(singInfoBean.getSong_name_jp(),
                singInfoBean.getSong_name_us(), singInfoBean.getSong_name_cn()));
        //歌手名
        tv_song_singername.setText(StringHelper.getLau_With_J_U_C(singInfoBean.getSinger_name_jp(),
                singInfoBean.getSinger_name_us(), singInfoBean.getSinger_name_cn()));
        //第一句歌词
        tv_song_songfirstlrc.setText(TextUtils.isEmpty(singInfoBean.getLyric_firstline()) ? ""
                : singInfoBean.getLyric_firstline());

        //专辑图片
        ImageLoader imageLoader = mWeApplication.getAppComponent().imageLoader();
        imageLoader.loadImage(mWeApplication, GlideImageConfig
                .builder()
                .errorPic(R.mipmap.def_square_large)
                .placeholder(R.mipmap.def_square_large)
                .url(singInfoBean.getSong_image())
                .imageView(image_song_album)
                .build());

        //背景图片
        Glide.with(mApplication)
                .load(StringHelper.getImageUrl(singInfoBean.getSong_image()))
                .asBitmap()
                .transform(new BlurTransformation(this, 5f))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (null != ll_learnsing_topbg) {
                            ll_learnsing_topbg.setBackground(new BitmapDrawable(null, resource));
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mFixedNaviLayout.setTopOffset((int) (headerView.getHeight() + getResources().getDimension(R.dimen.statusbar_view_height)));
    }

    //解决fragment切换重叠
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    /**
     * 初始化Fragment
     */
    private void initFragments() {
        mFragments = new ArrayList<>();
        mFragments.add(mCurFragment = mListenFragment = ListenListFragment.newInstance(mSongId));
        mFragments.add(mPopularFragment = PopularListFragment.newInstance(mSongId));
        mFragments.add(mChorusRecomFragment = ChorusRecomListFragment.newInstance(mSongId));
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_learnsing, mCurFragment).commit();

    }

    /**
     * 切换Fragment
     *
     * @param fragment
     */
    private void switchFragment(Fragment fragment) {
        if (null != fragment) {
            if (fragment.isAdded()) {
                getSupportFragmentManager().beginTransaction()
                        .hide(mCurFragment)
                        .show(fragment)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .hide(mCurFragment)
                        .add(R.id.fl_learnsing, fragment)
                        .commit();
            }
            mCurFragment = fragment;
            mFixedNaviLayout.setCurrentScrollableContainer(
                    (HeaderScrollHelper.ScrollableContainer) mCurFragment);
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.rb_listen://收听榜
                switchFragment(mListenFragment);
                break;
            case R.id.rb_popular://人气榜
                switchFragment(mPopularFragment);
                break;
            case R.id.rb_chorus://合唱推荐
                switchFragment(mChorusRecomFragment);
                break;
        }
    }

    /**
     * 初始化固定的Tab
     */
    private void initFixedLayout() {
        mFixedNaviLayout.setCurrentScrollableContainer(
                (HeaderScrollHelper.ScrollableContainer) mFragments.get(0));

        mFixedNaviLayout.setOnScrollListener(new HeaderViewPager.OnScrollListener() {
            @Override
            public void onScroll(int currentY, int maxY) {
                //设置文字透明度
                int alpha = 255 * currentY / maxY;
                if (alpha < 125) {
                    StatusBarUtil.setTranslucentForImageView(LearnSingActivity.this, alpha, null);
                } else {
                    StatusBarUtil.setColor(LearnSingActivity.this,
                            getResources().getColor(R.color.titlebar_bg), 255 - alpha);
                }
                headerView.setTitleBgAlpha(alpha);
                headerView.setTitleTextViewAlpha(alpha);

            }
        });
    }

    @Override
    public void setTitleLeftClick() {
        killMyself();
    }

    @Override
    public void setTitleRightClick() {
        launchActivity(new Intent(this, ReportActivity.class));
    }

    @OnClick({R.id.ll_practice, R.id.ll_mv, R.id.ll_ksong, R.id.ll_chorus, R.id.ll_videochorus,
            R.id.iv_externallink})
    public void onClick(View v) {
        if (null == mSongInfo)
            return;

        switch (v.getId()) {
            case R.id.ll_practice://练唱

//                showMessage(getString(R.string.tip_unopened_function));
                turn2KPractice();
                break;
            case R.id.ll_mv://mv
                turn2MVRecord();
                break;
            case R.id.ll_ksong://K歌界面
                turn2KSing();
                break;
            case R.id.ll_chorus://合唱
                if (whetherSupportOrNotChorus()) {
                    turn2Chorus();
                } else {
                    showMessage(getString(R.string.tip_nonsupport_chorus));
                }
                break;
            case R.id.ll_videochorus://视频合唱
                if (whetherSupportOrNotChorus()) {
                    turn2VideoChorus();
                } else {
                    showMessage(getString(R.string.tip_nonsupport_chorus));
                }
                break;
            case R.id.iv_externallink://外部链接
                turn2ExternalLink();
                break;
        }
    }


    @Subscriber(tag = EventBusTag.SWITCHMODE, mode = ThreadMode.MAIN)
    public void onSwitchModeEvent(Message message) {

        switch (message.what) {
            case 0:
                turn2KPractice();
                break;
            case 1:
                turn2MVRecord();
                break;
            case 2:
                turn2KSing();
                break;
            case 3:
                turn2Chorus();
                break;
            case 4:
                turn2VideoChorus();
                break;
            default:
                break;
        }
    }

    /**
     * 跳转到练唱界面
     */
    private void turn2KPractice() {
        Intent ksingIntent = new Intent(this, PracticingSingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("songinfo", mSongInfo);
        ksingIntent.putExtras(bundle);
        launchActivity(ksingIntent);
    }

    /**
     * 跳转到mv录制界面
     */
    private void turn2MVRecord() {
        saveHistory();
        Intent mvPreviewIntent = new Intent(this, MvPreviewFirstActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("songinfo", mSongInfo);
        bundle.putInt("workstype", getResources().getInteger(R.integer.workstype_common));
        mvPreviewIntent.putExtras(bundle);
        startActivity(mvPreviewIntent);
    }

    /**
     * 跳转到合唱界面
     */
    private void turn2Chorus() {
        saveHistory();
        Intent chorusIntent = new Intent(this, SelectRoleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("songinfo", mSongInfo);
        bundle.putBoolean("needmv", false);
        chorusIntent.putExtras(bundle);
        launchActivity(chorusIntent);
    }

    /**
     * 跳转到K歌界面
     */
    private void turn2KSing() {
        saveHistory();
        Intent ksingIntent = new Intent(this, KSingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("songinfo", mSongInfo);
        bundle.putInt("workstype", getResources().getInteger(R.integer.workstype_common));
        ksingIntent.putExtras(bundle);
        launchActivity(ksingIntent);
    }

    /**
     * 跳转到视频合唱
     */
    private void turn2VideoChorus() {
        saveHistory();
        Intent mvPreviewIntent = new Intent(this, SelectRoleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("songinfo", mSongInfo);
        bundle.putBoolean("needmv", true);
        mvPreviewIntent.putExtras(bundle);
        launchActivity(mvPreviewIntent);
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

    /***
     * 是否支持合唱
     * @return
     */
    private boolean whetherSupportOrNotChorus() {
        if (null == mSongInfo)
            return false;
        int allow_chorus = EmptyUtils.isEmpty(mSongInfo.getAllow_chorus()) ?
                getResources().getInteger(R.integer.allowchorus_nonsupport) : Integer.parseInt(mSongInfo.getAllow_chorus());
        return allow_chorus == getResources().getInteger(R.integer.allowchorus_support);
    }

    private void saveHistory() {
        if (mSingerInfo != null) {
            mSingerInfo.setCategory_name(mSongId);
            HistoryUtils.putSingerHistory(mWeApplication, mSingerInfo);
        }

        if (mSongInfo != null) {
            HistoryUtils.putMusicHistory(mWeApplication, mSongInfo);
        }
    }
}

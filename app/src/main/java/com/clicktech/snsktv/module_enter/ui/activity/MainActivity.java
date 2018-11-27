package com.clicktech.snsktv.module_enter.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.ui.fragment.DiscoverFragment;
import com.clicktech.snsktv.module_enter.contract.MainContract;
import com.clicktech.snsktv.module_enter.inject.component.DaggerMainComponent;
import com.clicktech.snsktv.module_enter.inject.module.MainModule;
import com.clicktech.snsktv.module_enter.presenter.MainPresenter;
import com.clicktech.snsktv.module_enter.ui.adapter.MainAdapter;
import com.clicktech.snsktv.module_enter.ui.fragment.OtherSongFragment;
import com.clicktech.snsktv.module_home.ui.fragment.MusicHallFragment;
import com.clicktech.snsktv.module_mine.ui.fragment.MineFragment;
import com.clicktech.snsktv.module_mine.ui.service.MiniPlayerService;
import com.clicktech.snsktv.widget.homepageview.BottomClickListener;
import com.clicktech.snsktv.widget.homepageview.BottomControlView;
import com.clicktech.snsktv.widget.videoplayer.MainPlayer;
import com.clicktech.snsktv.widget.viewpager.ViewPagerScrollListener;
import com.clicktech.snsktv.widget.viewpager.ViewPagerWithListener;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.jzvd.JZVideoPlayer;

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
 * Created by Administrator on 2016/12/21.
 * 主页
 */


public class MainActivity extends WEActivity<MainPresenter> implements MainContract.View {

    public static final String ACTION_MSGPUSH = "com.clicktech.snsktv.pushmessage";
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;
    @BindView(R.id.vp_main)
    ViewPagerWithListener mViewPager;
    @BindView(R.id.bottomControlView)
    BottomControlView mControlView;
    private DiscoverFragment mDiscoverFragment;
    private MusicHallFragment mHomePageFragment;
    private MineFragment mMineFragment;
    private List<Fragment> mFragmentList;

    private Intent mMiniPlayerService;
    private OtherSongFragment mOtherSongfragment;
    private MainPlayer mMusicPlayer;

    private PushMessageReceiver mPushMessageReceiver;

    @Override
    protected void ComponentInject() {
        super.ComponentInject();
        switchLanguage();
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainComponent
                .builder()
                .appComponent(appComponent)
                .mainModule(new MainModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_main, null, false);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTransparentForImageViewInFragment(this, null);
    }

    @Override
    protected void initData() {
        registerPushBroadcast();
        initWidget();
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();
        if (null == bundle) {
            return;
        } else {
            boolean announcefinish = bundle.getBoolean("announcefinish");
            if (announcefinish) {
                mViewPager.setCurrentItem(0);
            }
        }

    }

    /***
     * 初始化Viewpager和BottomControlView
     */
    private void initWidget() {
        mFragmentList = new ArrayList<>();

        mFragmentList.add(mMineFragment = MineFragment.newInstance());
        mFragmentList.add(mHomePageFragment = MusicHallFragment.newInstance());
        mFragmentList.add(mDiscoverFragment = DiscoverFragment.newInstance());

        mViewPager.setAdapter(new MainAdapter(getSupportFragmentManager(), mFragmentList));
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setCurrentItem(1);
        mViewPager.setViewPagerScrollListener(new ViewPagerScrollListener() {
            @Override
            public void onScroll(int position, float offset, int offsetPixels) {
                mControlView.changeViewShrink(offset);
            }

            @Override
            public void left2MidSlide(float offset) {
                mControlView.changeView_left2MidSlide(offset);
            }

            @Override
            public void mid2LeftSlide(float offset) {
                mControlView.changeView_mid2LeftSlide(offset);
            }

            @Override
            public void mid2RightSlide(float offset) {
                mControlView.changeView_mid2RightSlide(offset);
            }

            @Override
            public void right2MidSlide(float offset) {
                mControlView.changeView_right2MidSlide(offset);
            }
        });

        mControlView.setBottomClickListener(new BottomClickListener() {
            @Override
            public void clickLeft() {
                mViewPager.setCurrentItem(0);
            }

            @Override
            public void clickRight() {
                mViewPager.setCurrentItem(2);
            }

            @Override
            public void clickMiddle(boolean jump) {
                mViewPager.setCurrentItem(1);
            }
        });
    }

    //点击返回键返回桌面而不是退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        unRegisterPushBroadcast();
        JZVideoPlayer.releaseAllVideos();
        if (null != mMiniPlayerService) {
            stopService(mMiniPlayerService);
            mMiniPlayerService = null;
        }
        super.onDestroy();
    }

    @Override
    public void stopMonitorService() {
        if (null != mMiniPlayerService) {
            stopService(mMiniPlayerService);
        }
    }

    @Override
    public void openMonitorService() {
        if (null == mMiniPlayerService) {
            mMiniPlayerService = new Intent(getApplicationContext(), MiniPlayerService.class);
        }
        mMiniPlayerService.putExtra(MiniPlayerService.PARAMS_TYPE, 1);
        startService(mMiniPlayerService);
    }

    @Override
    public void showPlayList(ArrayList<SongInfoBean> list) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("songlist", list);
        mOtherSongfragment = new OtherSongFragment();
        mOtherSongfragment.setArguments(bundle);
        mOtherSongfragment.show(getFragmentManager(),
                "com.clicktech.snsktv.module_enter.ui.fragment.OtherSongFragment");
    }

    @Override
    public void updatePlayList(ArrayList<SongInfoBean> list) {
        mOtherSongfragment.updateSongList(list);
    }

    @Override
    public void action_playMusic(ArrayList<SongInfoBean> list, int position) {
        if (null == mMusicPlayer) {
            mMusicPlayer = new MainPlayer(this);
            mMusicPlayer.setVideoPlayCallBack(new MainPlayer.WorkPlayerCallBack() {
                @Override
                public void onStatePreparing() {
                    changePlayStatus(true);
                }

                @Override
                public void onStatePlaying() {
                    changePlayStatus(true);
                }

                @Override
                public void onStatePause() {
                    changePlayStatus(false);
                }

                @Override
                public void onStateAutoComplete() {
                    changePlayStatus(false);
                }

                @Override
                public void onStateError() {
                    changePlayStatus(false);
                }

                @Override
                public void onCompletion() {
                    changePlayStatus(false);
                }
            });

            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.width = 1;
            layoutParams.height = 1;
            rlRoot.addView(mMusicPlayer, layoutParams);
        }
        mMusicPlayer.setUpParams(list, position);
    }

    //改变播放状态
    private void changePlayStatus(boolean state) {
        mPresenter.action_playStatus(state);
    }

    //注册本地广播
    private void registerPushBroadcast() {
        mPushMessageReceiver = new PushMessageReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_MSGPUSH);
        LocalBroadcastManager.getInstance(this).registerReceiver(mPushMessageReceiver, intentFilter);
    }

    //注销本地广播
    private void unRegisterPushBroadcast() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mPushMessageReceiver);
    }

    private class PushMessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != mMineFragment) {
                mMineFragment.setData(null);
            }
        }
    }
}
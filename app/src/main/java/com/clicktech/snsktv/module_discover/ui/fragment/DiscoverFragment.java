package com.clicktech.snsktv.module_discover.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.EventBusTag;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.common.WEFragment;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.entity.BannersEntity;
import com.clicktech.snsktv.entity.DiscoverHomeResponse;
import com.clicktech.snsktv.entity.PopularSingerEntity;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.contract.DiscoverContract;
import com.clicktech.snsktv.module_discover.inject.component.DaggerDiscoverComponent;
import com.clicktech.snsktv.module_discover.inject.module.DiscoverModule;
import com.clicktech.snsktv.module_discover.presenter.DiscoverPresenter;
import com.clicktech.snsktv.module_discover.ui.activity.FriendRankListActivity;
import com.clicktech.snsktv.module_discover.ui.activity.ListenRankActivity;
import com.clicktech.snsktv.module_discover.ui.activity.NationalPopularityActivity;
import com.clicktech.snsktv.module_discover.ui.activity.PopuSingerActivity;
import com.clicktech.snsktv.module_discover.ui.activity.PopuSongActivity;
import com.clicktech.snsktv.module_discover.ui.activity.RichListActivity;
import com.clicktech.snsktv.module_discover.ui.activity.SingerIntroActivity;
import com.clicktech.snsktv.module_discover.ui.activity.SongDetailsActivity;
import com.clicktech.snsktv.module_discover.ui.activity.WebActivity;
import com.clicktech.snsktv.module_discover.ui.adapter.DiscoverHotSingersAdapter;
import com.clicktech.snsktv.module_discover.ui.adapter.DiscoverHotSongsAdapter;
import com.clicktech.snsktv.module_enter.ui.activity.LoginActivity;
import com.clicktech.snsktv.module_home.ui.activity.LearnSingActivity;
import com.clicktech.snsktv.module_home.ui.holder.NetworkImageHolderView;
import com.clicktech.snsktv.module_mine.ui.service.MiniPlayerService;
import com.clicktech.snsktv.widget.progressbar.RoundProgress;
import com.clicktech.snsktv.widget.titlebar.HeaderView;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.jzvd.JZVideoPlayer;

import static com.bigkoo.convenientbanner.ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL;
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
 * Created by Administrator on 2016/12/29.
 */

public class DiscoverFragment extends WEFragment<DiscoverPresenter> implements
        DiscoverContract.View, BGARefreshLayout.BGARefreshLayoutDelegate {


    @BindView(R.id.headerview)
    HeaderView headerview;
    @BindView(R.id.banner_discover)
    ConvenientBanner bannerDiscover;
    @BindView(R.id.tv_all_hotsinger)
    TextView tvAllHotsinger;
    @BindView(R.id.rv_hotsinger)
    RecyclerView rvHotsinger;
    @BindView(R.id.tv_all_hotsong)
    TextView tvAllHotsong;
    @BindView(R.id.rv_hotsong)
    RecyclerView rvHotsong;
    @BindView(R.id.iv_audition)
    ImageView ivAudition;
    @BindView(R.id.tv_click_audition)
    TextView tvClickAudition;
    @BindView(R.id.rl_audition)
    RelativeLayout rlAudition;
    @BindView(R.id.iv_rich)
    ImageView ivRich;
    @BindView(R.id.tv_click_rich)
    TextView tvClickRich;
    @BindView(R.id.rl_rich)
    RelativeLayout rlRich;
    @BindView(R.id.iv_nationwide)
    ImageView ivNationwide;
    @BindView(R.id.tv_click_nationwide)
    TextView tvClickNationwide;
    @BindView(R.id.rl_nationwide)
    RelativeLayout rlNationwide;
    @BindView(R.id.iv_friend)
    ImageView ivFriend;
    @BindView(R.id.tv_click_friend)
    TextView tvClickFriend;
    @BindView(R.id.rl_friend)
    RelativeLayout rlFriend;
    @BindView(R.id.refreshlayout)
    BGARefreshLayout refreshlayout;
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

    private DiscoverHotSingersAdapter mHotSingerAdapter;
    private DiscoverHotSongsAdapter mHotSongsAdapter;
    private Bundle mBundle;

    private int spot[] = {R.mipmap.spot_normal, R.mipmap.spot_selcet};

    public static DiscoverFragment newInstance() {
        DiscoverFragment fragment = new DiscoverFragment();
        return fragment;
    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        DaggerDiscoverComponent
                .builder()
                .appComponent(appComponent)
                .discoverModule(new DiscoverModule(this))//请将DiscoverModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_discover, null, false);
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
        initRecyclerView();
        mPresenter.requestDiscoverData();
    }

    private void initBGARefreshLayout() {

        // 为BGARefreshLayout 设置代理
        refreshlayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        // 设置下拉刷新和上拉加载更多的风格
        refreshlayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(
                KtvApplication.ktvApplication, false));
    }

    //初始化话人气歌手和人气歌曲的RecyclerView
    private void initRecyclerView() {
        rvHotsinger.setLayoutManager(new LinearLayoutManager(mWeApplication, LinearLayoutManager.HORIZONTAL, false));
        rvHotsinger.setHasFixedSize(true);
        rvHotsinger.setAdapter(mHotSingerAdapter = new DiscoverHotSingersAdapter(new ArrayList<PopularSingerEntity>()));
        mHotSingerAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener<PopularSingerEntity>() {
            @Override
            public void onItemClick(View view, PopularSingerEntity data, int position) {
                Intent intent = new Intent(mWeApplication, SingerIntroActivity.class);
                intent.putExtra("userid", data.getId());
                UiUtils.startActivity(getActivity(), intent);
            }
        });

        rvHotsong.setLayoutManager(new LinearLayoutManager(mWeApplication, LinearLayoutManager.HORIZONTAL, false));
        rvHotsong.setHasFixedSize(true);
        rvHotsong.setAdapter(mHotSongsAdapter = new DiscoverHotSongsAdapter(new ArrayList<SongInfoBean>()));
        mHotSongsAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener<SongInfoBean>() {
            @Override
            public void onItemClick(View view, SongInfoBean data, int position) {
                Intent intent = new Intent(mWeApplication, LearnSingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("songid", data.getSong_id());
                intent.putExtras(bundle);
                UiUtils.startActivity(getActivity(), intent);
            }
        });

    }

    /**
     * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一些方法,
     * 建议在有多个需要让外界调用的方法时,统一传bundle,里面存一个what字段,来区分不同的方法,在setData
     * 方法中就可以switch做不同的操作,这样就可以用统一的入口方法做不同的事,和message同理
     * <p>
     * 使用此方法时请注意调用时fragment的生命周期,如果调用此setData方法时onActivityCreated
     * 还没执行,setData里调用presenter的方法时,是会报空的,因为dagger注入是在onActivityCreated
     * 方法中执行的,如果要做一些初始化操作,可以不必让外部调setData,在内部onActivityCreated中
     * 初始化就可以了
     *
     * @param data
     */

    @Override
    public void setData(Object data) {
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
        UiUtils.startActivity(getActivity(), intent);
    }

    @Override
    public void killMyself() {
    }

    @Override
    public void setDiscoverView(DiscoverHomeResponse response) {
        setTopBanner(response.getCarouselKey());
        //人气歌手
        mHotSingerAdapter.setmInfos(response.getPopularSingers());
        //人气歌曲
        mHotSongsAdapter.setmInfos(response.getPopularSongs());

        ImageLoader imageLoader = mWeApplication.getAppComponent().imageLoader();
        //财富
        if (EmptyUtils.isNotEmpty(response.getList1()) && EmptyUtils.isNotEmpty(response.getList1().get(0))) {
            imageLoader.loadImage(getActivity(), GlideImageConfig.builder()
                    .url(response.getList1().get(0).getUser_photo())
                    .imageView(ivRich).build());
        }
        //全国
        if (EmptyUtils.isNotEmpty(response.getList2()) && EmptyUtils.isNotEmpty(response.getList2().get(0))) {
            imageLoader.loadImage(getActivity(), GlideImageConfig.builder()
                    .url(response.getList2().get(0).getUser_photo())
                    .imageView(ivNationwide).build());
        }
        //好友
        if (EmptyUtils.isNotEmpty(response.getList3()) && EmptyUtils.isNotEmpty(response.getList3().get(0))) {
            imageLoader.loadImage(getActivity(), GlideImageConfig.builder()
                    .url(response.getList3().get(0).getUser_photo())
                    .imageView(ivFriend).build());
        }

        //结束下拉刷新
        refreshlayout.endRefreshing();
    }

    //设置发现的banner
    private void setTopBanner(final List<BannersEntity> list) {
        bannerDiscover.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, list);
        bannerDiscover.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(mWeApplication, WebActivity.class);
                intent.putExtra("title", list.get(position).getCarousel_title());
                intent.putExtra("url", list.get(position).getCarousel_link());
                UiUtils.startActivity(getActivity(), intent);

            }
        });
        bannerDiscover.setPageIndicator(spot);
        bannerDiscover.setPointViewVisible(true);
        bannerDiscover.setPageIndicatorAlign(CENTER_HORIZONTAL);
        bannerDiscover.isTurning();
        bannerDiscover.startTurning(3000);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        refreshlayout.beginRefreshing();
        mPresenter.requestDiscoverData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        refreshLayout.endLoadingMore();
        return false;
    }

    @OnClick(R.id.tv_all_hotsinger)
    public void onTvAllHotsingerClicked() {
        UiUtils.startActivity(getActivity(), PopuSingerActivity.class);
    }

    @OnClick(R.id.tv_all_hotsong)
    public void onTvAllHotsongClicked() {
        UiUtils.startActivity(getActivity(), PopuSongActivity.class);
    }

    @OnClick(R.id.rl_audition)
    public void onRlAuditionClicked() {
        UiUtils.startActivity(getActivity(), ListenRankActivity.class);
    }

    @OnClick(R.id.rl_rich)
    public void onRlRichClicked() {
        UiUtils.startActivity(getActivity(), RichListActivity.class);
    }

    @OnClick(R.id.rl_nationwide)
    public void onRlNationwideClicked() {
        UiUtils.startActivity(getActivity(), NationalPopularityActivity.class);
    }

    @OnClick(R.id.rl_friend)
    public void onRlFriendClicked() {
        if (!mWeApplication.loggingStatus()) {
            UiUtils.startActivity(getActivity(), LoginActivity.class);
            return;
        }
        UiUtils.startActivity(getActivity(), FriendRankListActivity.class);
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

    @Subscriber(tag = EventBusTag.MINIPLAYER_HIDE, mode = ThreadMode.MAIN)
    public void eventHideMiniPlayer(Message message) {
        llMiniPlayer.setVisibility(View.GONE);
    }

    @Subscriber(tag = EventBusTag.MINIPLAYER_UPDATEPROGRESS, mode = ThreadMode.MAIN)
    public void eventUpdateMiniPlayProgress(Message message) {
        Bundle bundle = message.getData();
        int currentTime = bundle.getInt(MiniPlayerService.PROGRESS_CURRENT, 0);
        int totalTime = bundle.getInt(MiniPlayerService.PROGRESSS_TOTAL, 0);
        mProgressBar.setMax(totalTime);
        mProgressBar.setProgress(currentTime);
    }

    @Subscriber(tag = EventBusTag.MINIPLAYER_UPDATEPLAYSTATE, mode = ThreadMode.MAIN)
    public void eventMiniPlayChangePlayState(Message message) {
        mBundle = message.getData();
        if (null != mBundle) {
            mProgressBar.updatePlayPauseButton(mBundle.getBoolean("status"));
        }
    }

}
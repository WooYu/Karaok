package com.clicktech.snsktv.module_mine.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.clicktech.snsktv.BuildConfig;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.EventBusTag;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.common.WEFragment;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.common.net.CommonService;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.entity.GiftListResponse;
import com.clicktech.snsktv.entity.PresentBean;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.ui.activity.SingerIntroActivity;
import com.clicktech.snsktv.module_discover.ui.activity.SongDetailsActivity;
import com.clicktech.snsktv.module_enter.ui.activity.LoginActivity;
import com.clicktech.snsktv.module_home.ui.fragment.CommentFragment;
import com.clicktech.snsktv.module_home.ui.fragment.ShareWorksFragment;
import com.clicktech.snsktv.module_mine.contract.Mine_PopularHotContract;
import com.clicktech.snsktv.module_mine.inject.component.DaggerMine_PopularHotComponent;
import com.clicktech.snsktv.module_mine.inject.module.Mine_PopularHotModule;
import com.clicktech.snsktv.module_mine.presenter.Mine_PopularHotPresenter;
import com.clicktech.snsktv.module_mine.ui.adapter.MinePopularListAdapter;
import com.clicktech.snsktv.widget.dialog.SendGiftsDialog;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.jzvd.JZMediaManager;
import cn.jzvd.JZVideoPlayerManager;


/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */

/**
 * Created by Administrator on 2017/3/20.
 * 我的-热门
 */

public class Mine_PopularHotFragment extends WEFragment<Mine_PopularHotPresenter> implements
        Mine_PopularHotContract.View, BGARefreshLayout.BGARefreshLayoutDelegate {

    private static final int TYPE_REFRESH = 1;
    private static final int TYPE_LOADMORE = 2;
    private static final int RequestCode_Detail = 0x0013;

    @BindView(R.id.rv_popular)
    RecyclerView rvPopular;

    private List<SongInfoBean> mHotList;
    private MinePopularListAdapter mHotAdapter;
    private int mCurPlayPosition = -1;//当前播放的位置
    private int mCurrentPage;//当前请求页码
    private int mIndexOfStartPage;//开始页面索引
    private int mTypeOfOperate = 0;//操作类型：0默认，1刷新，2加载更多
    private int mItemPosition;//选中的itemview位置
    private GiftListResponse mGiftInfo;//礼物信息

    public static Mine_PopularHotFragment newInstance() {
        Mine_PopularHotFragment fragment = new Mine_PopularHotFragment();
        return fragment;
    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        DaggerMine_PopularHotComponent
                .builder()
                .appComponent(appComponent)
                .mine_PopularHotModule(new Mine_PopularHotModule(this))//请将Mine_PopularHotModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mine_popular, null, false);
    }

    @Override
    protected void initData() {
        mIndexOfStartPage = getResources().getInteger(R.integer.paging_startindex);
        mCurrentPage = mIndexOfStartPage;

        initRecyclerView();
        mPresenter.requestDataOfHot(mCurrentPage);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {
        UiUtils.makeText(message);
    }

    @Override
    public void launchActivity(Intent intent) {

    }

    @Override
    public void killMyself() {

    }

    @Override
    public void setData(Object data) {
        if (null == data) {
            return;
        }

        if (!(data instanceof Bundle)) {
            return;
        }

        Bundle bundle = (Bundle) data;
        //type 1:更新页面登录状态；2：设置礼物列表
        int type = bundle.getInt("type");
        if (type == 2) {
            mGiftInfo = bundle.getParcelable("giftinfo");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //从作品详情返回
        if (requestCode == RequestCode_Detail) {
            int currentState = 0;
            if (null != JZVideoPlayerManager.getSecondFloor()) {
                currentState = JZVideoPlayerManager.getCurrentJzvd().currentState;
                JZVideoPlayerManager.getSecondFloor().textureViewContainer.removeView(JZMediaManager.textureView);
                ;
                JZVideoPlayerManager.setSecondFloor(null);
            }

            if (null != JZVideoPlayerManager.getFirstFloor()) {
                JZVideoPlayerManager.getFirstFloor().setState(currentState);
                JZVideoPlayerManager.getFirstFloor().addTextureView();
            }
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout bgaRefreshLayout) {
        mTypeOfOperate = TYPE_REFRESH;
        if (null != mPresenter) {
            mPresenter.requestDataOfHot(mIndexOfStartPage);
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout bgaRefreshLayout) {
        mTypeOfOperate = TYPE_LOADMORE;
        if (null != mPresenter) {
            mPresenter.requestDataOfHot(mCurrentPage + 1);
        }
        return true;
    }

    //初始化recyclerview
    private void initRecyclerView() {
        rvPopular.setLayoutManager(new LinearLayoutManager(mWeApplication));
        mHotList = new ArrayList<>();
        mHotAdapter = new MinePopularListAdapter(mHotList);
        rvPopular.setAdapter(mHotAdapter);
        mHotAdapter.setItemEventListener(new MinePopularListAdapter.ItemViewClickListener() {
            @Override
            public void onClickItem(int position) {
                turn2SongDetail(position);
            }

            @Override
            public void onClickSingerIntro(int position) {
                turn2SingerIntro(position);
            }

            @Override
            public void onClickPresent(int position) {
                showPopupOfPresent(position);
            }

            @Override
            public void onClickComment(int position) {
                showCommentPop(position);
            }

            @Override
            public void onClickShare(int position) {
                showSharePop(position);
            }

            @Override
            public void onClickPlayPause(int position) {
                boolean bNewWork = (mCurPlayPosition != position);
                mCurPlayPosition = position;

                SongInfoBean workSingleBean = mHotList.get(position);
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putParcelable("songinfo", workSingleBean);
                bundle.putBoolean("status", workSingleBean.isbPlayStatus());
                bundle.putBoolean("newwork", bNewWork);
                message.setData(bundle);
                EventBus.getDefault().post(message, EventBusTag.MINIPLAYER_SHOW);
            }
        });

    }

    @Override
    public void requestCompletion() {
        MineFragment mineFragment = (MineFragment) getParentFragment();
        switch (mTypeOfOperate) {
            case TYPE_REFRESH://刷新
                mCurrentPage = mIndexOfStartPage;
                mineFragment.endRefreshing();
                break;
            case TYPE_LOADMORE://加载更多
                mCurrentPage++;
                mineFragment.endLoadingMore();
                break;
            default:
                break;
        }

    }

    @Override
    public void updateData(List<SongInfoBean> list) {
        if (EmptyUtils.isEmpty(list)) {
            return;
        }

        if (mTypeOfOperate == TYPE_LOADMORE) {
            mHotList.addAll(list);
        } else {
            mHotList = list;
        }
        mHotAdapter.updateData(mHotList);
    }

    @Override
    public void doCommentSuccess() {
        SongInfoBean songInfoBean = mHotList.get(mItemPosition);
        long numOfComment = Long.parseLong(songInfoBean.getCommentNum());
        songInfoBean.setCommentNum(String.valueOf(++numOfComment));
        mHotAdapter.notifyItemChanged(mItemPosition);
    }

    @Subscriber(tag = EventBusTag.MINIPLAYER_TOGGLE, mode = ThreadMode.MAIN)
    public void eventToggleMiniPlayer(Message message) {
        EventBus.getDefault().post(message, EventBusTag.MINIPLAYER_UPDATEPLAYSTATE);
        if (mCurPlayPosition == -1) {
            return;
        }

        Bundle bundle = message.getData();
        boolean status = bundle.getBoolean("status");
        SongInfoBean songInfoBean = bundle.getParcelable("songinfo");
        if (null == songInfoBean) {
            return;
        }
        SongInfoBean workSingleBean = mHotList.get(mCurPlayPosition);
        if (null == workSingleBean) {
            return;
        }

        if (!workSingleBean.getId().equals(songInfoBean.getId())) {
            return;
        }

        workSingleBean.setbPlayStatus(status);
    }

    //跳转到k歌详情
    private void turn2SongDetail(int position) {
        mItemPosition = position;

        SongInfoBean workInfoEntity = mHotList.get(position);
        long numOfListener = Long.parseLong(workInfoEntity.getListen_count());
        workInfoEntity.setListen_count(String.valueOf(++numOfListener));
        mHotAdapter.notifyItemChanged(mItemPosition);

        Intent intent = new Intent(getActivity(), SongDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("songinfo", workInfoEntity);
        bundle.putBoolean("connectionplay", true);
        intent.putExtras(bundle);
        startActivityForResult(intent, RequestCode_Detail);
    }

    //跳转到歌手介绍
    private void turn2SingerIntro(int position) {
        mItemPosition = position;
        SongInfoBean workInfoEntity = mHotList.get(position);
        Intent intent = new Intent(getActivity(), SingerIntroActivity.class);
        String userid = workInfoEntity.getUser_id();
        intent.putExtra("userid", userid);
        startActivity(intent);
    }

    private void showCommentPop(int position) {
        if (!mWeApplication.loggingStatus()) {
            showMessage(getString(R.string.error_notlogin));
            launchActivity(new Intent(getActivity(), LoginActivity.class));
            return;
        }

        mItemPosition = position;
        CommentFragment commentFragment = new CommentFragment();
        commentFragment.setCommentInputCallBack(new CommentFragment.CommentInputCallBack() {
            @Override
            public void sendComment(String content) {
                SongInfoBean songInfoBean = mHotList.get(mItemPosition);
                mPresenter.userReviews(RequestParams_Maker.getUserReviews(
                        KtvApplication.ktvApplication.getUserID(),
                        songInfoBean.getId(),
                        "0", content, ""));
            }
        });
        commentFragment.show(getChildFragmentManager(), "com.clicktech.snsktv.module_home.ui.fragment.CommentFragment");
    }

    private void showSharePop(int position) {
        SongInfoBean workInfo = mHotList.get(position);
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
        shareWorksFragment.show(getChildFragmentManager(), "com.clicktech.snsktv.module_home.ui.fragment.ShareWorksFragment");
    }

    //展示礼物弹窗
    private void showPopupOfPresent(int position) {
        mItemPosition = position;

        if (EmptyUtils.isEmpty(mGiftInfo)) {
            showMessage(getString(R.string.error_notlogin));
            return;
        }

        Bundle bundle = new Bundle();
        if (EmptyUtils.isNotEmpty(mGiftInfo.getGiftList())) {
            ArrayList<PresentBean> tempList = new ArrayList<>();
            tempList.addAll(mGiftInfo.getGiftList());
            bundle.putParcelableArrayList("giftlist", tempList);
        }
        GiftListResponse.WalletBean walletBean = mGiftInfo.getWallet();
        if (null != walletBean) {
            bundle.putInt("wallet_coin", walletBean.getWallet_coin());
            bundle.putInt("wallet_flower", walletBean.getWallet_flower());
        }
        SendGiftsDialog sendGiftsDialog = new SendGiftsDialog();
        sendGiftsDialog.setArguments(bundle);
        sendGiftsDialog.setOnEvent_ClickRequest(new SendGiftsDialog.OnEvent_ClickReqeust() {
            @Override
            public void buyClicked() {

            }

            @Override
            public void sendFlowersClicked(int flowernum) {
                mPresenter.sendFlowers(mHotList.get(mItemPosition), flowernum);
            }

            @Override
            public void sendGiftsClicked(long giftid, int giftnum) {
                mPresenter.sendGifts(mHotList.get(mItemPosition), String.valueOf(giftid), giftnum);
            }
        });
        sendGiftsDialog.show(getChildFragmentManager(), "com.clicktech.snsktv.widget.dialog.SendGiftsDialog");
    }
}
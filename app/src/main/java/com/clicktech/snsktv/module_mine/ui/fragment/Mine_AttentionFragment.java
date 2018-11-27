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
import com.clicktech.snsktv.entity.MineAttentEntity;
import com.clicktech.snsktv.entity.PresentBean;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.entity.WorkAlbumBean;
import com.clicktech.snsktv.module_discover.ui.activity.SingerIntroActivity;
import com.clicktech.snsktv.module_discover.ui.activity.SongDetailsActivity;
import com.clicktech.snsktv.module_home.ui.fragment.CommentFragment;
import com.clicktech.snsktv.module_home.ui.fragment.ShareWorksFragment;
import com.clicktech.snsktv.module_mine.contract.Mine_AttentionContract;
import com.clicktech.snsktv.module_mine.inject.component.DaggerMine_AttentionComponent;
import com.clicktech.snsktv.module_mine.inject.module.Mine_AttentionModule;
import com.clicktech.snsktv.module_mine.presenter.Mine_AttentionPresenter;
import com.clicktech.snsktv.module_mine.ui.activity.AlbumDetailActivity;
import com.clicktech.snsktv.module_mine.ui.adapter.MineAttentListAdapter;
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
 * Created by Administrator on 2017/3/17.
 * 我的-关注
 */

public class Mine_AttentionFragment extends WEFragment<Mine_AttentionPresenter> implements
        Mine_AttentionContract.View, BGARefreshLayout.BGARefreshLayoutDelegate {

    private static final int TYPE_REFRESH = 1;
    private static final int TYPE_LOADMORE = 2;
    private static final int RequestCode_Detail = 0x0012;

    @BindView(R.id.rv_attention)
    RecyclerView rvAttention;

    private GiftListResponse mGiftInfo;//礼物信息
    private List<MineAttentEntity> mAttentList;
    private MineAttentListAdapter mAttentAdapter;
    private int mCurrentPage;//当前请求页码
    private int mIndexOfStartPage;//开始页面索引
    private int mTypeOfOperate = 0;//操作类型：0默认，1刷新，2加载更多
    private int mItemPosition;//选中的itemview位置
    private int mCurPlayPosition = -1;//当前播放的位置


    public static Mine_AttentionFragment newInstance() {
        Mine_AttentionFragment fragment = new Mine_AttentionFragment();
        return fragment;
    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        DaggerMine_AttentionComponent
                .builder()
                .appComponent(appComponent)
                .mine_AttentionModule(new Mine_AttentionModule(this))//请将Mine_Att_HotModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mine_attent, null, false);
    }

    @Override
    protected void initData() {
        mIndexOfStartPage = getResources().getInteger(R.integer.paging_startindex);
        mCurrentPage = mIndexOfStartPage;

        initRecyclerView();
        mPresenter.requestDataOfAttention(mCurrentPage);
        determineLoginStatus();
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
        if (type == 1) {
            determineLoginStatus();
        } else if (type == 2) {
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

    //初始化recyclerview
    private void initRecyclerView() {
        rvAttention.setLayoutManager(new LinearLayoutManager(mWeApplication));
        mAttentList = new ArrayList<>();
        mAttentAdapter = new MineAttentListAdapter(mAttentList);
        rvAttention.setAdapter(mAttentAdapter);
        mAttentAdapter.setItemEventListener(new MineAttentListAdapter.ItemViewClickListener() {
            @Override
            public void onClickItem(int position) {
                turn2Detail(position);
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

                SongInfoBean workSingleBean = mAttentList.get(position).getWorkInfo();

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

    //判断登陆状态
    private void determineLoginStatus() {
        if (null != rvAttention) {
            rvAttention.setVisibility(mWeApplication.loggingStatus() ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout bgaRefreshLayout) {
        determineLoginStatus();
        mTypeOfOperate = TYPE_REFRESH;
        if (null != mPresenter) {
            mPresenter.requestDataOfAttention(mIndexOfStartPage);
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout bgaRefreshLayout) {
        mTypeOfOperate = TYPE_LOADMORE;
        if (null != mPresenter) {
            mPresenter.requestDataOfAttention(mCurrentPage + 1);
        }

        return true;
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
    public void updateData(List<MineAttentEntity> list) {
        if (EmptyUtils.isEmpty(list)) {
            return;
        }

        if (mTypeOfOperate == TYPE_LOADMORE) {
            mAttentList.addAll(list);
        } else {
            mAttentList = list;
        }
        mAttentAdapter.updateData(mAttentList);
    }

    @Override
    public void doCommentSuccess() {
        WorkAlbumBean workAlbumBean = mAttentList.get(mItemPosition).getWorkAlbum();
        SongInfoBean songInfoBean = mAttentList.get(mItemPosition).getWorkInfo();
        long numOfComment;
        if (null != workAlbumBean) {
            numOfComment = Long.parseLong(workAlbumBean.getComment_sum());
            workAlbumBean.setComment_sum(String.valueOf(++numOfComment));
        }
        if (null != songInfoBean) {
            numOfComment = Long.parseLong(songInfoBean.getCommentNum());
            songInfoBean.setCommentNum(String.valueOf(++numOfComment));
        }
        mAttentAdapter.notifyItemChanged(mItemPosition);
    }

    //跳转到详情
    private void turn2Detail(int position) {
        mItemPosition = position;
        MineAttentEntity mineAttentEntity = mAttentList.get(position);
        WorkAlbumBean workAlbumBean = mineAttentEntity.getWorkAlbum();
        SongInfoBean workSingleBean = mineAttentEntity.getWorkInfo();

        if (EmptyUtils.isNotEmpty(workAlbumBean)) {
            //专辑详情
            Intent intent = new Intent(getActivity(), AlbumDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("userid", workAlbumBean.getUser_id());
            bundle.putString("albumid", workAlbumBean.getId());
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            long numOfListener = Long.parseLong(workSingleBean.getListen_count());
            workSingleBean.setListen_count(String.valueOf(++numOfListener));
            mAttentAdapter.notifyItemChanged(mItemPosition);

            //作品详情
            Intent intent = new Intent(getActivity(), SongDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("songinfo", workSingleBean);
            bundle.putBoolean("connectionplay", true);
            intent.putExtras(bundle);
            startActivityForResult(intent, RequestCode_Detail);
        }

    }

    //跳转到歌手介绍
    private void turn2SingerIntro(int position) {
        mItemPosition = position;
        MineAttentEntity mineAttentEntity = mAttentList.get(position);
        Intent intent = new Intent(getActivity(), SingerIntroActivity.class);
        SongInfoBean workSingleBean = mineAttentEntity.getWorkInfo();
        WorkAlbumBean workAlbumBean = mineAttentEntity.getWorkAlbum();
        String userid = "";
        if (EmptyUtils.isNotEmpty(workSingleBean)) {
            userid = workSingleBean.getUser_id();
        } else if (EmptyUtils.isNotEmpty(workAlbumBean)) {
            userid = workAlbumBean.getUser_id();
        }
        intent.putExtra("userid", userid);
        startActivity(intent);
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
                mPresenter.sendFlowers(mAttentList.get(mItemPosition), flowernum);
            }

            @Override
            public void sendGiftsClicked(long giftid, int giftnum) {
                mPresenter.sendGifts(mAttentList.get(mItemPosition), String.valueOf(giftid), giftnum);
            }
        });
        sendGiftsDialog.show(getChildFragmentManager(), "com.clicktech.snsktv.widget.dialog.SendGiftsDialog");
    }

    private void showCommentPop(int position) {
        mItemPosition = position;
        CommentFragment commentFragment = new CommentFragment();
        commentFragment.setCommentInputCallBack(new CommentFragment.CommentInputCallBack() {
            @Override
            public void sendComment(String content) {
                WorkAlbumBean workAlbumBean = mAttentList.get(mItemPosition).getWorkAlbum();
                SongInfoBean songInfoBean = mAttentList.get(mItemPosition).getWorkInfo();
                String worksid = (null != workAlbumBean) ? workAlbumBean.getId() : songInfoBean.getId();
                mPresenter.userReviews(RequestParams_Maker.getUserReviews(
                        KtvApplication.ktvApplication.getUserID(),
                        worksid,
                        "0", content, ""));
            }
        });
        commentFragment.show(getChildFragmentManager(), "com.clicktech.snsktv.module_home.ui.fragment.CommentFragment");
    }

    private void showSharePop(int position) {
        String worksname;
        String worksimg;
        String worksurl;
        String worksdesc;
        SongInfoBean workInfo = mAttentList.get(position).getWorkInfo();
        WorkAlbumBean albumBean = mAttentList.get(position).getWorkAlbum();
        if (null == albumBean) {
            worksname = workInfo.getWorks_name();
            worksdesc = workInfo.getWorks_desc();
            worksimg = BuildConfig.APP_DOMAIN_File + workInfo.getWorks_image();
            worksurl = BuildConfig.APP_DOMAIN + CommonService.SHARE_URL +
                    "userId=" + (KtvApplication.ktvApplication).getUserID() + "&worksId=" + workInfo.getId();
        } else {
            worksname = albumBean.getAlbum_name();
            worksdesc = albumBean.getAlbum_introduce();
            worksimg = BuildConfig.APP_DOMAIN_File + albumBean.getAlbum_image();
            worksurl = BuildConfig.APP_DOMAIN + CommonService.SHARE_URL +
                    "userId=" + (KtvApplication.ktvApplication).getUserID() + "&worksId=" + albumBean.getId();
        }

        Bundle bundle = new Bundle();
        bundle.putString("worksname", worksname);
        bundle.putString("worksdesc", worksdesc);
        bundle.putString("worksurl", worksurl);
        bundle.putString("worksimg", worksimg);
        ShareWorksFragment shareWorksFragment = new ShareWorksFragment();
        shareWorksFragment.setArguments(bundle);
        shareWorksFragment.show(getChildFragmentManager(), "com.clicktech.snsktv.module_home.ui.fragment.ShareWorksFragment");
    }

    @Subscriber(tag = EventBusTag.ANNOUNCESUCCESS, mode = ThreadMode.MAIN)
    public void eventWorkPublishSuccess(Message message) {
        onBGARefreshLayoutBeginRefreshing(null);
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

        SongInfoBean workSingleBean = mAttentList.get(mCurPlayPosition).getWorkInfo();
        if (null == workSingleBean) {
            return;
        }

        if (!workSingleBean.getId().equals(songInfoBean.getId())) {
            return;
        }
        workSingleBean.setbPlayStatus(status);
    }
}
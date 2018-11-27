package com.clicktech.snsktv.module_mine.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clicktech.snsktv.BuildConfig;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.common.net.CommonService;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.entity.AlbumDetailResponse;
import com.clicktech.snsktv.entity.AlbumGiftListEntity;
import com.clicktech.snsktv.entity.GiftListResponse;
import com.clicktech.snsktv.entity.PresentBean;
import com.clicktech.snsktv.entity.ResponseAttentonCollectResponse;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.ui.activity.GIftRankActivity;
import com.clicktech.snsktv.module_enter.ui.activity.LoginActivity;
import com.clicktech.snsktv.module_enter.ui.fragment.OtherSongFragment;
import com.clicktech.snsktv.module_home.ui.fragment.CommentFragment;
import com.clicktech.snsktv.module_home.ui.fragment.ShareWorksFragment;
import com.clicktech.snsktv.module_mine.contract.AlbumDetailContract;
import com.clicktech.snsktv.module_mine.inject.component.DaggerAlbumDetailComponent;
import com.clicktech.snsktv.module_mine.inject.module.AlbumDetailModule;
import com.clicktech.snsktv.module_mine.presenter.AlbumDetailPresenter;
import com.clicktech.snsktv.module_mine.ui.adapter.AlbumCommentAdapter;
import com.clicktech.snsktv.module_mine.ui.adapter.AlbumGiftAdapter;
import com.clicktech.snsktv.module_mine.ui.adapter.AlbumMusicAdapter;
import com.clicktech.snsktv.util.transformations.CircleWithBorderTransformation;
import com.clicktech.snsktv.widget.dialog.SendGiftsDialog;
import com.clicktech.snsktv.widget.titlebar.HeaderView;

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
 * Created by Administrator on 2017/5/24.
 * 专辑详情
 */

public class AlbumDetailActivity extends WEActivity<AlbumDetailPresenter> implements
        AlbumDetailContract.View, HeaderView.OnCustomTileListener {

    @BindView(R.id.headerview)
    HeaderView headerview;
    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.m_singer_name)
    TextView mSingerName;
    @BindView(R.id.m_song_name)
    TextView mSongName;
    @BindView(R.id.m_comment)
    TextView mComment;
    @BindView(R.id.m_gift_num)
    TextView mGiftNum;
    @BindView(R.id.m_forword)
    TextView mForword;
    @BindView(R.id.m_detail)
    TextView mDetail;
    @BindView(R.id.m_time)
    TextView mTime;
    @BindView(R.id.m_title_single_music)
    TextView mTitleSingleMusic;
    @BindView(R.id.m_play_all)
    TextView mPlayAll;
    @BindView(R.id.m_single_music_recyclerview)
    RecyclerView mSingleMusicRecyclerview;
    @BindView(R.id.m_gift)
    TextView mGift;
    @BindView(R.id.m_gift_recyclerview)
    RecyclerView mGiftRecyclerview;
    @BindView(R.id.m_comment_rl)
    TextView mCommentRl;
    @BindView(R.id.m_commont_recyclerview)
    RecyclerView mCommontRecyclerview;
    @BindView(R.id.m_comment_im)
    ImageView mCommentIm;
    @BindView(R.id.m_gift_im)
    ImageView mGiftIm;
    @BindView(R.id.m_share_im)
    ImageView mShareIm;
    @BindView(R.id.m_attention_im)
    ImageView mAttentionIm;
    @BindView(R.id.m_head_img)
    ImageView headImg;
    @BindView(R.id.m_phone_type)
    TextView mPhoneType;
    @BindView(R.id.gift_noone)
    RelativeLayout noGift;
    @BindView(R.id.iv_giftarrow)
    ImageView ivGiftArrow;
    @BindView(R.id.tv_numofsingle)
    TextView tvNumOfSingle;
    @BindView(R.id.comment_num)
    TextView commentNum;

    private String mAlbumID;
    private String mUserID;
    private SongInfoBean mWorkInfo;//分享时使用
    private boolean isCollect = false;
    private OtherSongFragment mOtherSongfragment;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAlbumDetailComponent
                .builder()
                .appComponent(appComponent)
                .albumDetailModule(new AlbumDetailModule(this)) //请将AlbumDetailModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_albumdetail, null, false);
    }

    @Override
    protected void initData() {
        headerview.setTitleClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (null == bundle) {
            return;
        }

        mUserID = bundle.getString("userid");
        mAlbumID = bundle.getString("albumid");
        if (EmptyUtils.isEmpty(mUserID)) {
            mUserID = mWeApplication.getUserID();
        }
        if (EmptyUtils.isEmpty(mAlbumID)) {
            return;
        }

        mPresenter.getAlbumDetail(RequestParams_Maker.getAlbumDetail(mUserID, mAlbumID));
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
    }

    @Override
    public void setupToUi(AlbumDetailResponse response) {
        if (null == response)
            return;

        List<SongInfoBean> mWorksList = response.getAlbumWorksList();
        List<AlbumDetailResponse.AlbumCommentListBean> mCommentList = response.getAlbumCommentList();
        List<AlbumGiftListEntity> mGiftsList = response.getAlbumGiftList();

        //评论数量
        int numOfComment = EmptyUtils.isEmpty(mCommentList) ? 0 : mCommentList.size();
        mComment.setText(String.format(getString(R.string.format_album_comment), String.valueOf(numOfComment)));
        //礼物数量
        mGiftNum.setText(String.format(getString(R.string.format_album_gift),
                String.valueOf(response.getTotal_flower_num())));

        if (EmptyUtils.isNotEmpty(mWorksList)) {
            SongInfoBean worksListBean = mWorksList.get(0);
            mWorkInfo = worksListBean;
            if (null == worksListBean) {
                return;
            }

            ImageLoader imageLoader = mWeApplication.getAppComponent().imageLoader();
            imageLoader.loadImage(mWeApplication, GlideImageConfig.builder()
                    .url(worksListBean.getAlbum_image())
                    .placeholder(R.mipmap.def_square_large)
                    .errorPic(R.mipmap.def_square_large)
                    .imageView(headImg)
                    .build());

            imageLoader.loadImage(mWeApplication, GlideImageConfig.builder()
                    .url(worksListBean.getUser_photo())
                    .placeholder(R.mipmap.def_avatar_round)
                    .errorPic(R.mipmap.def_avatar_round)
                    .transformation(new CircleWithBorderTransformation(this))
                    .imageView(avatar)
                    .build());

            mSingerName.setText(worksListBean.getUser_nickname());
            mSongName.setText(worksListBean.getAlbum_name());
            mDetail.setText(worksListBean.getAlbum_introduce());
            mTime.setText(worksListBean.getAdd_time());
            mPhoneType.setText(worksListBean.getPhone_type());
            //单曲数量
            tvNumOfSingle.setText(String.format(getString(R.string.format_num_totalsingle),
                    String.valueOf(worksListBean.getWorks_sum())));
        }

        //是否收藏
        if (response.getAttentionTypeAlbum() == getResources().getInteger(R.integer.attentiontype_no)) {
            mAttentionIm.setImageResource(R.mipmap.songdetail_collect_cancel);
            isCollect = false;
        } else {
            mAttentionIm.setImageResource(R.mipmap.songdetail_collect_success);
            isCollect = true;
        }


        //评论数量（底部）
        commentNum.setText(String.valueOf(EmptyUtils.isEmpty(mCommentList) ? 0 : mCommentList.size()));
    }

    @Override
    public void setWorkRecyclerview(AlbumMusicAdapter albumMusicAdapter) {
        mSingleMusicRecyclerview.setLayoutManager(new LinearLayoutManager(mWeApplication) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mSingleMusicRecyclerview.setAdapter(albumMusicAdapter);
    }

    @Override
    public void setGiftRecyclerview(AlbumGiftAdapter albumGiftAdapter) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mWeApplication);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mGiftRecyclerview.setLayoutManager(linearLayoutManager);
        mGiftRecyclerview.setAdapter(albumGiftAdapter);
    }

    @Override
    public void setCommentRecyclerview(AlbumCommentAdapter albumCommentAdapter) {
        mCommontRecyclerview.setLayoutManager(new LinearLayoutManager(mWeApplication) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mCommontRecyclerview.setAdapter(albumCommentAdapter);
    }

    @Override
    public void showEmpetyGiftRecyclerview(Boolean show) {
        if (show) {
            noGift.setVisibility(View.VISIBLE);
            mGiftRecyclerview.setVisibility(View.GONE);
            ivGiftArrow.setVisibility(View.GONE);
        } else {
            noGift.setVisibility(View.GONE);
            mGiftRecyclerview.setVisibility(View.VISIBLE);
            ivGiftArrow.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showPopupOfPresent(GiftListResponse response) {
        Bundle bundle = new Bundle();
        if (EmptyUtils.isNotEmpty(response.getGiftList())) {
            ArrayList<PresentBean> tempList = new ArrayList<>();
            tempList.addAll(response.getGiftList());
            bundle.putParcelableArrayList("giftlist", tempList);
        }
        GiftListResponse.WalletBean walletBean = response.getWallet();
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
                mPresenter.sendingAlbumFlowers(mWeApplication.getUserID(), mAlbumID, flowernum);
            }

            @Override
            public void sendGiftsClicked(long giftid, int giftnum) {
                mPresenter.sendingAlbumGifts(mWeApplication.getUserID(), mAlbumID, String.valueOf(giftid), giftnum);
            }
        });
        sendGiftsDialog.show(getSupportFragmentManager(), "com.clicktech.snsktv.widget.dialog.SendGiftsDialog");
    }

    @Override
    public void userReviewsSuccess() {
        mPresenter.getAlbumDetail(RequestParams_Maker.getAlbumDetail(mUserID, mAlbumID));
    }

    @Override
    public void givingGifsSuccess() {
        mPresenter.getAlbumDetail(RequestParams_Maker.getAlbumDetail(mUserID, mAlbumID));
    }

    @Override
    public void addCollcet(ResponseAttentonCollectResponse responseAttentonCollect) {
        if (responseAttentonCollect == null)
            return;

        mAttentionIm.setImageResource(R.mipmap.songdetail_collect_success);
        isCollect = true;
    }

    @Override
    public void cancleCollcet(ResponseAttentonCollectResponse responseAttentonCollect) {
        if (responseAttentonCollect == null)
            return;

        mAttentionIm.setImageResource(R.mipmap.songdetail_collect_cancel);
        isCollect = false;

    }

    @Override
    public void turn2GiftRank() {
        Intent intent = new Intent(AlbumDetailActivity.this, GIftRankActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userid", mUserID);
        bundle.putString("albumid", mAlbumID);
        intent.putExtras(bundle);
        launchActivity(intent);
    }

    @OnClick(R.id.rl_gift)
    public void onRlGiftClicked() {
        turn2GiftRank();
    }

    @OnClick(R.id.m_comment_im)
    public void onMCommentImClicked() {
        if (!mWeApplication.loggingStatus()) {
            showMessage(getString(R.string.error_notlogin));
            launchActivity(new Intent(this, LoginActivity.class));
            return;
        }

        CommentFragment commentFragment = new CommentFragment();
        commentFragment.setCommentInputCallBack(new CommentFragment.CommentInputCallBack() {
            @Override
            public void sendComment(String content) {
                mPresenter.saveAlbumComment(mWeApplication.getUserID(), mAlbumID, "", content);
            }
        });
        commentFragment.show(getSupportFragmentManager(), "com.clicktech.snsktv.module_home.ui.fragment.CommentFragment");
    }

    @OnClick(R.id.m_gift_im)
    public void onMGiftImClicked() {
        if (!mWeApplication.loggingStatus()) {
            showMessage(getString(R.string.error_notlogin));
            launchActivity(new Intent(this, LoginActivity.class));
            return;
        }
        mPresenter.requestGiftList(mWeApplication.getUserID());
    }

    @OnClick(R.id.m_share_im)
    public void onMShareImClicked() {
        if (null == mWorkInfo) {
            return;
        }
        String worksname = mWorkInfo.getWorks_name();
        String worksdesc = mWorkInfo.getWorks_desc();
        String worksimg = BuildConfig.APP_DOMAIN_File + mWorkInfo.getWorks_image();
        String worksurl = BuildConfig.APP_DOMAIN + CommonService.SHARE_URL +
                "userId=" + (KtvApplication.ktvApplication).getUserID() + "&worksId=" + mWorkInfo.getId();

        Bundle bundle = new Bundle();
        bundle.putString("worksname", worksname);
        bundle.putString("worksdesc", worksdesc);
        bundle.putString("worksurl", worksurl);
        bundle.putString("worksimg", worksimg);
        ShareWorksFragment shareWorksFragment = new ShareWorksFragment();
        shareWorksFragment.setArguments(bundle);
        shareWorksFragment.show(getSupportFragmentManager(), "com.clicktech.snsktv.module_home.ui.fragment.ShareWorksFragment");
    }

    @OnClick(R.id.m_attention_im)
    public void onMAttentionImClicked() {//收藏
        if (!mWeApplication.loggingStatus()) {
            showMessage(getString(R.string.error_notlogin));
            launchActivity(new Intent(this, LoginActivity.class));
            return;
        }

        if (isCollect) {
            mPresenter.cancelCollect(mAlbumID);
        } else {
            mPresenter.addCollect(mAlbumID);
        }
    }

    @OnClick({R.id.rl_createalbum, R.id.m_play_all})
    public void onRlCreatealbumClicked() {
        mPresenter.playAllSingleClicked();
    }

    @Override
    public void showPlayList(ArrayList<SongInfoBean> list) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("songlist", list);
        mOtherSongfragment = new OtherSongFragment();
        mOtherSongfragment.setArguments(bundle);
        mOtherSongfragment.show(getFragmentManager(), getClass().getSimpleName());
    }

    @Override
    public void updatePlayList(ArrayList<SongInfoBean> list) {
        mOtherSongfragment.updateSongList(list);
    }

}

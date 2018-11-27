package com.clicktech.snsktv.module_discover.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.entity.AlbumDetailResponse;
import com.clicktech.snsktv.entity.AlbumGiftListEntity;
import com.clicktech.snsktv.entity.GIftRankResponse;
import com.clicktech.snsktv.entity.GiftListResponse;
import com.clicktech.snsktv.entity.PresentBean;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.contract.GIftRankContract;
import com.clicktech.snsktv.module_discover.inject.component.DaggerGIftRankComponent;
import com.clicktech.snsktv.module_discover.inject.module.GIftRankModule;
import com.clicktech.snsktv.module_discover.presenter.GIftRankPresenter;
import com.clicktech.snsktv.module_discover.ui.adapter.GIftRankAdapter;
import com.clicktech.snsktv.module_enter.ui.activity.LoginActivity;
import com.clicktech.snsktv.util.transformations.CircleWithBorderTransformation;
import com.clicktech.snsktv.util.transformations.RoundedCornersTransformation;
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
 * Created by Administrator on 2017/6/9.
 * 作品收到礼物排行
 */

public class GIftRankActivity extends WEActivity<GIftRankPresenter> implements
        GIftRankContract.View, HeaderView.OnCustomTileListener {

    @BindView(R.id.headerview)
    HeaderView headerview;
    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.m_song_name)
    TextView mSongName;
    @BindView(R.id.m_singer_name)
    TextView mSingerName;
    @BindView(R.id.m_kcoin)
    TextView mKcoin;
    @BindView(R.id.m_flower_num)
    TextView mFlowerNum;
    @BindView(R.id.recyclerview)
    RecyclerView mGiftRankRv;
    @BindView(R.id.m_head)
    ImageView mHead;
    @BindView(R.id.tv_sendgifts)
    TextView tvSendGifts;
    @BindView(R.id.enter_right)
    ImageView enterRight;
    @BindView(R.id.allminesend)
    TextView allminesend;

    private String mUserID;//作者的用户id
    private String mWorksID;//单曲id
    private String mAlbumID;//专辑id

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerGIftRankComponent
                .builder()
                .appComponent(appComponent)
                .gIftRankModule(new GIftRankModule(this)) //请将GIftRankModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_giftrank, null, false);
    }

    @Override
    protected void initData() {
        headerview.setTitleClickListener(this);
        Bundle bundle = getIntent().getExtras();
        if (null == bundle) {
            return;
        }

        mUserID = bundle.getString("userid");
        mWorksID = bundle.getString("workid");
        mAlbumID = bundle.getString("albumid");

        if (EmptyUtils.isEmpty(mWorksID) && EmptyUtils.isEmpty(mAlbumID)) {
            return;
        }

        if (EmptyUtils.isEmpty(mUserID)) {
            mUserID = mWeApplication.getUserID();
        }

        mPresenter.setData(mUserID, mWorksID, mAlbumID);
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
    public void setRecyclerView(GIftRankAdapter adapter) {
        mGiftRankRv.setLayoutManager(new LinearLayoutManager(mWeApplication));
        mGiftRankRv.setAdapter(adapter);
    }

    @Override
    public void fillDataToView_Album(AlbumDetailResponse response) {
        List<SongInfoBean> workSingleBeanList = response.getAlbumWorksList();
        SongInfoBean firstWork = workSingleBeanList.get(0);

        //专辑图片
        mWeApplication.getAppComponent().imageLoader().loadImage(this,
                GlideImageConfig.builder()
                        .url(firstWork.getAlbum_image())
                        .transformation(new RoundedCornersTransformation(this, 6))
                        .errorPic(R.mipmap.def_square_round)
                        .placeholder(R.mipmap.def_square_round)
                        .imageView(avatar)
                        .build());
        //专辑名称
        mSongName.setText(firstWork.getAlbum_name());
        //专辑作者
        mSingerName.setText(firstWork.getAlbum_introduce());
        //专辑金币
        mKcoin.setText(String.valueOf(response.getTotal_coin_num()));
        //专辑鲜花
        mFlowerNum.setText(String.valueOf(response.getTotal_flower_num()));
        //登录用户头像
        mWeApplication.getAppComponent().imageLoader().loadImage(this,
                GlideImageConfig.builder()
                        .url(firstWork.getUser_photo())
                        .transformation(new CircleWithBorderTransformation(this))
                        .errorPic(R.mipmap.def_avatar_round)
                        .placeholder(R.mipmap.def_avatar_round)
                        .imageView(mHead)
                        .build());
        //登录用户送礼情况
        List<AlbumGiftListEntity> albumGiftList = response.getAlbumGiftList();
        String loginUserid = mWeApplication.getUserID();
        AlbumGiftListEntity myGiftBean = null;
        for (AlbumGiftListEntity giftBean : albumGiftList) {
            if (loginUserid.equals(String.valueOf(giftBean.getSend_uid()))) {
                myGiftBean = giftBean;
                break;
            }
        }

        if (EmptyUtils.isEmpty(myGiftBean)) {
            allminesend.setText(getString(R.string.gift_none));
        } else {
            allminesend.setText(String.format(getString(R.string.format_giftsstatistics),
                    myGiftBean.getFlower_num(), myGiftBean.getCoin_num()));
        }

    }

    @Override
    public void fillDataToView_Single(GIftRankResponse response) {
        GIftRankResponse.AuthorInfoBean authorInfo = response.getAuthorInfo();

        //单曲图片
        mWeApplication.getAppComponent().imageLoader().loadImage(this, GlideImageConfig.builder()
                .url(authorInfo.getWorks_image())
                .transformation(new RoundedCornersTransformation(this, 6))
                .placeholder(R.mipmap.def_square_round)
                .errorPic(R.mipmap.def_square_round)
                .imageView(avatar)
                .build());
        //单曲名称
        mSongName.setText(authorInfo.getWorks_name());
        //单曲作者
        mSingerName.setText(authorInfo.getUser_nickname());
        //单曲金币
        mKcoin.setText(String.valueOf(authorInfo.getTotal_coin()));
        //单曲鲜花
        mFlowerNum.setText(String.valueOf(authorInfo.getTotal_flower()));

        //登录用户的送礼
        mWeApplication.getAppComponent().imageLoader().loadImage(this, GlideImageConfig.builder()
                .url(mWeApplication.getUserInfoBean().getUser_photo())
                .transformation(new CircleWithBorderTransformation(this))
                .placeholder(R.mipmap.def_avatar_round)
                .errorPic(R.mipmap.def_avatar_round)
                .imageView(mHead)
                .build());
        GIftRankResponse.MineGiftBean myGiftBean = response.getMineGift();
        if (EmptyUtils.isEmpty(myGiftBean)) {
            allminesend.setText(getString(R.string.gift_none));
        } else {
            allminesend.setText(String.format(getString(R.string.format_giftsstatistics),
                    myGiftBean.getMine_flower(), myGiftBean.getMine_coin()));
        }

    }

    @Override
    public void givingGifsSuccess() {
        mPresenter.requestRankDetail();
    }

    @OnClick({R.id.tv_sendgifts_top, R.id.tv_sendgifts})
    public void onSendGiftsClicked() {
        if (!mWeApplication.loggingStatus()) {
            showMessage(getString(R.string.error_notlogin));
            launchActivity(new Intent(this, LoginActivity.class));
            return;
        }
        mPresenter.requesGiftList(mWeApplication.getUserID());
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
                mPresenter.sendFlowers(mWeApplication.getUserID(), flowernum);
            }

            @Override
            public void sendGiftsClicked(long giftid, int giftnum) {
                mPresenter.sendGifts(mWeApplication.getUserID(), String.valueOf(giftid), giftnum);
            }
        });
        sendGiftsDialog.show(getSupportFragmentManager(), "com.clicktech.snsktv.widget.dialog.SendGiftsDialog");
    }


}

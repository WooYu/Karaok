package com.clicktech.snsktv.module_discover.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.entity.AlbumBean;
import com.clicktech.snsktv.entity.OthersAlbumResponse;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.entity.UserInfoBean;
import com.clicktech.snsktv.module_discover.contract.SingerIntroContract;
import com.clicktech.snsktv.module_discover.inject.component.DaggerSingerIntroComponent;
import com.clicktech.snsktv.module_discover.inject.module.SingerIntroModule;
import com.clicktech.snsktv.module_discover.presenter.SingerIntroPresenter;
import com.clicktech.snsktv.module_discover.ui.fragment.AlbumFragment;
import com.clicktech.snsktv.module_enter.ui.activity.LoginActivity;
import com.clicktech.snsktv.module_enter.ui.fragment.OtherSongFragment;
import com.clicktech.snsktv.module_mine.ui.activity.Mine_Att_UserListActivity;
import com.clicktech.snsktv.module_mine.ui.activity.Mine_FansListActivity;
import com.clicktech.snsktv.module_mine.ui.activity.Mine_MyWorksListActivity;
import com.clicktech.snsktv.util.transformations.CircleWithBorderTransformation;
import com.clicktech.snsktv.widget.titlebar.HeaderView;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;

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
 * Created by Administrator on 2017/1/22.
 * 歌手介绍页面        根据userId 获取专辑列表
 */

public class SingerIntroActivity extends WEActivity<SingerIntroPresenter> implements
        SingerIntroContract.View, View.OnClickListener, HeaderView.OnCustomTileListener {

    public String mCuruserID;
    @BindView(R.id.iv_grade)
    ImageView gradeIv;
    @BindView(R.id.tv_grade)
    TextView gradeTv;
    @BindView(R.id.ll_attention)
    LinearLayout attentionLL;
    @BindView(R.id.iv_attent)
    ImageView attentIV;
    @BindView(R.id.tv_attent)
    TextView attentTv;
    @BindView(R.id.mine_avater)
    ImageView mineAvater;
    @BindView(R.id.tv_username)
    TextView mineUsername;
    @BindView(R.id.mine_sex)
    ImageView mineSex;
    @BindView(R.id.mine_userage)
    TextView mineUserage;
    @BindView(R.id.mine_useraddress)
    TextView mineUseraddress;
    @BindView(R.id.mine_not_loading)
    TextView mineNotLoading;
    @BindView(R.id.mine_msg)
    LinearLayout mineMsg;
    @BindView(R.id.mine_songs_counts)
    TextView mineSongsCounts;
    @BindView(R.id.ll_mine_songs_counts)
    LinearLayout llMineSongsCounts;
    @BindView(R.id.mine_singer_fans)
    TextView mineSingerFans;
    @BindView(R.id.ll_mine_singer_fans)
    LinearLayout llMineSingerFans;
    @BindView(R.id.mine_singer_attention)
    TextView mineSingerAttention;
    @BindView(R.id.ll_mine_singer_attention)
    LinearLayout llMineSingerAttention;
    @BindView(R.id.headerview)
    HeaderView headerView;
    private AlbumFragment mWorksFragment;
    private boolean isAttention = false;
    private OtherSongFragment mOtherSongfragment;
    private boolean bIsFront = false;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerSingerIntroComponent
                .builder()
                .appComponent(appComponent)
                .singerIntroModule(new SingerIntroModule(this)) //请将SingerIntroModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_singerintro, null, false);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTransparentForImageViewInFragment(this, null);
    }

    @Override
    protected void initData() {
        headerView.setTitleClickListener(this);
        mCuruserID = getIntent().getStringExtra("userid");
        if (EmptyUtils.isEmpty(mCuruserID)) {
            killMyself();
            return;
        }

        if (mCuruserID.equals(mWeApplication.getUserID())) {
            attentionLL.setVisibility(View.GONE);
        }
        initFragment();
        mPresenter.requestUserInfo(mCuruserID);
    }

    private void initFragment() {
        mWorksFragment = AlbumFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_bottom, mWorksFragment)
                .commit();
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
    protected void onResume() {
        super.onResume();
        bIsFront = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        bIsFront = false;
    }

    @Override
    public void setDataToTopUI(OthersAlbumResponse response) {
        if (response == null) {
            return;
        }

        UserInfoBean userBean = response.getUser();
        if (null != userBean) {
            //头像
            mWeApplication.getAppComponent().imageLoader().loadImage(this, GlideImageConfig.builder()
                    .url(userBean.getUser_photo())
                    .placeholder(R.mipmap.def_avatar_round)
                    .errorPic(R.mipmap.def_avatar_round)
                    .transformation(new CircleWithBorderTransformation(this))
                    .imageView(mineAvater)
                    .build());
            //姓名
            mineUsername.setText(userBean.getUser_nickname());
            //等级
            gradeIv.setImageResource(mPresenter.convertGradeToImage(userBean.getUser_level()));
            gradeTv.setText(String.format(getString(R.string.format_class), userBean.getUser_level()));
            //性别
            if (userBean.getUser_sex() == getResources().getInteger(R.integer.sex_female)) {
                mineSex.setImageResource(R.mipmap.mine_sex_female);
            } else {
                mineSex.setImageResource(R.mipmap.mine_sex_male);
            }
            //年龄
            mineUserage.setText(String.valueOf(userBean.getUser_age()));
            //地址
            mineUseraddress.setText(String.valueOf(userBean.getUser_district_dtl()));
        }

        //作品数量
        mineSongsCounts.setText(String.valueOf(response.getWorksNum()));
        //粉丝数量
        mineSingerFans.setText(String.valueOf(response.getFansNum()));
        //关注数量
        mineSingerAttention.setText(String.valueOf(response.getAttentionNum()));
        //是否关注
        resultOfAttent(response.getAttentionType() ==
                getResources().getInteger(R.integer.attentiontypeworks_yes));

    }

    @Override
    public void resultOfAttent(Boolean attent) {
        if (attent) {
            isAttention = true;
            attentIV.setImageResource(R.mipmap.songdetail_attention_ok);  //已关注
            attentTv.setText(R.string.mine_attention_ok);
        } else {
            isAttention = false;
            attentIV.setImageResource(R.mipmap.songdetail_attention);  //未关注
            attentTv.setText(R.string.mine_attention_no);
        }
    }

    @Override
    public void setAlbumPart(ArrayList<AlbumBean> albumList) {
        if (null == mWorksFragment) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        bundle.putParcelableArrayList("albumlist", albumList);
        mWorksFragment.setData(bundle);
    }

    @Override
    public void setSinglePart(ArrayList<SongInfoBean> worksList) {
        if (null == mWorksFragment) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putInt("type", 2);
        bundle.putParcelableArrayList("worklist", worksList);
        mWorksFragment.setData(bundle);
    }

    @Override
    public void showPlayList(ArrayList<SongInfoBean> list) {
        if (!bIsFront) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("songlist", list);
        mOtherSongfragment = new OtherSongFragment();
        mOtherSongfragment.setArguments(bundle);
        mOtherSongfragment.show(getFragmentManager(), getClass().getSimpleName());
    }

    @Override
    public void updatePlayList(ArrayList<SongInfoBean> list) {
        if (!bIsFront) {
            return;
        }
        mOtherSongfragment.updateSongList(list);
    }

    @OnClick({R.id.mine_avater, R.id.ll_mine_songs_counts, R.id.ll_mine_singer_fans,
            R.id.ll_mine_singer_attention, R.id.ll_attention})
    public void onClick(View v) {

        Intent intent = null;
        switch (v.getId()) {
            case R.id.mine_avater://相册
                intent = new Intent(mWeApplication, OthersAlbumActivity.class);
                intent.putExtra("userid", mCuruserID);
                launchActivity(intent);
                break;
            case R.id.ll_attention://关注或取消
                addOrCancelAttent();
                break;
            case R.id.ll_mine_songs_counts: //作品
                intent = new Intent(mWeApplication, Mine_MyWorksListActivity.class);
                intent.putExtra("authorname", mineUsername.getText().toString());
                intent.putExtra("userid", mCuruserID);
                launchActivity(intent);
                break;
            case R.id.ll_mine_singer_fans://粉丝
                intent = new Intent(mWeApplication, Mine_FansListActivity.class);
                intent.putExtra("userid", mCuruserID);
                launchActivity(intent);
                break;
            case R.id.ll_mine_singer_attention://关注
                intent = new Intent(mWeApplication, Mine_Att_UserListActivity.class);
                intent.putExtra("userid", mCuruserID);
                launchActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void setTitleLeftClick() {
        killMyself();
    }

    @Override
    public void setTitleRightClick() {
    }

    //添加或取消关注
    private void addOrCancelAttent() {
        if (!mWeApplication.loggingStatus()) {
            launchActivity(new Intent(this, LoginActivity.class));
            return;
        }

        if (isAttention) {
            //取消关注
            mPresenter.requestData_CancleAtten(mCuruserID);
        } else {
            //关注
            mPresenter.requestData_Addtten(mCuruserID);
        }
    }

    //跳转到所有专辑
    public void turn2AllAlbum() {
        Intent intent = new Intent(this, OtherAlbumActivity.class);
        intent.putExtra("userid", mCuruserID);
        startActivity(intent);
    }

}

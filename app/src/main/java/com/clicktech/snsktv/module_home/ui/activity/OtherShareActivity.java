package com.clicktech.snsktv.module_home.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.entity.ShareBean;
import com.clicktech.snsktv.module_home.contract.OtherShareContract;
import com.clicktech.snsktv.module_home.inject.component.DaggerOtherShareComponent;
import com.clicktech.snsktv.module_home.inject.module.OtherShareModule;
import com.clicktech.snsktv.module_home.presenter.OtherSharePresenter;
import com.clicktech.snsktv.module_home.ui.adapter.OtherShareAdapter;
import com.clicktech.snsktv.widget.titlebar.HeaderView;

import java.util.ArrayList;

import butterknife.BindView;

import static com.clicktech.snsktv.arms.utils.Preconditions.checkNotNull;

public class OtherShareActivity extends WEActivity<OtherSharePresenter> implements
        OtherShareContract.View, HeaderView.OnCustomTileListener {

    @BindView(R.id.headerview)
    HeaderView headerview;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private String[] shareType_jpenviro;
    private int[] shareImg_jpenviro = new int[]{R.mipmap.share_facebook, R.mipmap.share_twitter,
            R.mipmap.share_line};
    private String[] shareType_nojpenviro;
    private int[] shareImg_nojpenviro = new int[]{R.mipmap.share_wx, R.mipmap.share_circle,
            R.mipmap.share_qq, R.mipmap.share_sina};

    private ArrayList<ShareBean> mShareList;
    private OtherShareAdapter mShareAdapter;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerOtherShareComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .otherShareModule(new OtherShareModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_other_share, null, false);
    }

    @Override
    protected void initData() {
        headerview.setTitleClickListener(this);
        initRecyclerView();
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

    public void initRecyclerView() {
        shareType_jpenviro = new String[]{
                getString(R.string.shares_supporttypes_facebook),
                getString(R.string.shares_supporttypes_twitter),
                getString(R.string.shares_supporttypes_line)};
        shareType_nojpenviro = new String[]{getString(R.string.shares_supporttypes_wechat),
                getString(R.string.shares_supporttypes_friend),
                getString(R.string.shares_supporttypes_qq),
                getString(R.string.shares_supporttypes_sina)};

        String language = KtvApplication.ktvApplication.getLocaleCode();
        mShareList = new ArrayList<>();
        if (language.equals(getString(R.string.language_japan))) {
            for (int i = 0; i < shareType_nojpenviro.length; i++) {
                ShareBean shareBean = new ShareBean();
                shareBean.setPlatformName(shareType_nojpenviro[i]);
                shareBean.setPlatformImg(shareImg_nojpenviro[i]);
                shareBean.setLanguage(getString(R.string.language_china));
                mShareList.add(shareBean);
            }
        } else {
            for (int i = 0; i < shareType_jpenviro.length; i++) {
                ShareBean shareBean = new ShareBean();
                shareBean.setPlatformName(shareType_jpenviro[i]);
                shareBean.setPlatformImg(shareImg_jpenviro[i]);
                shareBean.setLanguage(getString(R.string.language_japan));
                mShareList.add(shareBean);
            }
        }

        mShareAdapter = new OtherShareAdapter(mShareList);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(mShareAdapter);
    }

}

package com.clicktech.snsktv.module_discover.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.entity.BannersEntity;
import com.clicktech.snsktv.module_discover.contract.ListenRankContract;
import com.clicktech.snsktv.module_discover.inject.component.DaggerListenRankComponent;
import com.clicktech.snsktv.module_discover.inject.module.ListenRankModule;
import com.clicktech.snsktv.module_discover.presenter.ListenRankPresenter;
import com.clicktech.snsktv.module_discover.ui.adapter.RankListenAdapter;
import com.clicktech.snsktv.widget.titlebar.HeaderView;

import java.util.List;

import butterknife.BindView;

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
 * Created by Administrator on 2017/3/15.
 * 视听排行榜
 */

public class ListenRankActivity extends WEActivity<ListenRankPresenter> implements
        ListenRankContract.View, HeaderView.OnCustomTileListener {

    @BindView(R.id.xrv_nation)
    RecyclerView mNationXRV;
    @BindView(R.id.headerview)
    HeaderView headerView;
    @BindView(R.id.iv_header_audition)
    ImageView ivHeaderAudition;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerListenRankComponent
                .builder()
                .appComponent(appComponent)
                .listenRankModule(new ListenRankModule(this)) //请将ListenRankModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_discover_listenrank, null, false);
    }

    @Override
    public void setTitleLeftClick() {
        finish();
    }

    @Override
    public void setTitleRightClick() {
    }

    @Override
    protected void initData() {
        headerView.setTitleClickListener(this);
        mPresenter.requestData();
        mPresenter.requestData_BackGround();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void setListenRankSongs(RankListenAdapter adapter) {
        mNationXRV.setLayoutManager(new LinearLayoutManager(mWeApplication));
        mNationXRV.setAdapter(adapter);
    }

    @Override
    public void setTopBanners(List<BannersEntity> list) {
        if (EmptyUtils.isEmpty(list)) {
            return;
        }

        BannersEntity bannersEntity = list.get(0);
        mWeApplication.getAppComponent().imageLoader().loadImage(this,
                GlideImageConfig.builder()
                        .errorPic(R.mipmap.def_square_large)
                        .placeholder(R.mipmap.def_square_large)
                        .url(bannersEntity.getCarousel_image())
                        .imageView(ivHeaderAudition)
                        .build());
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

}

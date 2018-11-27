package com.clicktech.snsktv.module_mine.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.entity.KCoinResponse;
import com.clicktech.snsktv.module_mine.contract.KCoinContract;
import com.clicktech.snsktv.module_mine.inject.component.DaggerKCoinComponent;
import com.clicktech.snsktv.module_mine.inject.module.KCoinModule;
import com.clicktech.snsktv.module_mine.presenter.KCoinPresenter;
import com.clicktech.snsktv.util.transformations.CircleWithBorderTransformation;
import com.clicktech.snsktv.widget.titlebar.HeaderView;

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
 * Created by Administrator on 2017/5/25.
 * K币账户
 */

public class KCoinActivity extends WEActivity<KCoinPresenter> implements KCoinContract.View,
        HeaderView.OnCustomTileListener {

    @BindView(R.id.headerview)
    HeaderView headerview;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_balance)
    TextView tvBalance;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerKCoinComponent
                .builder()
                .appComponent(appComponent)
                .kCoinModule(new KCoinModule(this)) //请将KCoinModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_kcoin, null, false);
    }

    @Override
    protected void initData() {
        headerview.setTitleClickListener(this);
        mPresenter.requestData(RequestParams_Maker.getKCoin(mWeApplication.getUserID()), 1);
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
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public void setUpToUI(KCoinResponse entity) {
        if (entity == null)
            return;

        mWeApplication.getAppComponent().imageLoader().loadImage(this, GlideImageConfig.builder()
                .url(entity.getAccount().getUser_photo())
                .transformation(new CircleWithBorderTransformation(this, 1,
                        getResources().getColor(R.color.green)))
                .placeholder(R.mipmap.def_avatar_round)
                .errorPic(R.mipmap.def_avatar_round)
                .imageView(ivAvatar)
                .build());

        tvBalance.setText(String.format(getString(R.string.format_kcoin), entity.getAccount().getWallet_coin()));
        tvName.setText(entity.getAccount().getUser_nickname());
    }

    @Override
    public void setTitleLeftClick() {
        killMyself();
    }

    @Override
    public void setTitleRightClick() {

    }

}

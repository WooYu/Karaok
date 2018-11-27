package com.clicktech.snsktv.module_mine.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.DataHelper;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.module_mine.contract.NetworkRemindContract;
import com.clicktech.snsktv.module_mine.inject.component.DaggerNetworkRemindComponent;
import com.clicktech.snsktv.module_mine.inject.module.NetworkRemindModule;
import com.clicktech.snsktv.module_mine.presenter.NetworkRemindPresenter;
import com.clicktech.snsktv.widget.titlebar.HeaderView;

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
 * Created by Administrator on 2017/2/4.
 * 网络提醒
 */

public class NetworkRemindActivity extends WEActivity<NetworkRemindPresenter> implements
        NetworkRemindContract.View, HeaderView.OnCustomTileListener {

    @BindView(R.id.headerview)
    HeaderView headerView;
    @BindView(R.id.all)
    RelativeLayout everytime;
    @BindView(R.id.only_frends)
    RelativeLayout changes;
    @BindView(R.id.none)
    RelativeLayout none;
    @BindView(R.id.im_all)
    ImageView imeverytime;
    @BindView(R.id.im_frends)
    ImageView imchanges;
    @BindView(R.id.im_none)
    ImageView im_none;

    String remindtype = "";

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerNetworkRemindComponent
                .builder()
                .appComponent(appComponent)
                .networkRemindModule(new NetworkRemindModule(this)) //请将NetworkRemindModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_network, null, false);
    }

    @Override
    protected void initData() {
        headerView.setTitleClickListener(this);
        int typeOfRemind = DataHelper.getIntergerSF(mWeApplication, "netremind");
        modifyUI(typeOfRemind);
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
    public void setTitleLeftClick() {
        finish();
    }

    @Override
    public void setTitleRightClick() {

    }

    public void modifyUI(int type) {

        switch (type) {
            case 0:
                imeverytime.setVisibility(View.VISIBLE);
                imchanges.setVisibility(View.GONE);
                im_none.setVisibility(View.GONE);
                remindtype = getString(R.string.setting_network_every);
                break;
            case 1:
                remindtype = getString(R.string.setting_network_switching);
                imchanges.setVisibility(View.VISIBLE);
                imeverytime.setVisibility(View.GONE);
                im_none.setVisibility(View.GONE);
                break;
            case 2:
                remindtype = getString(R.string.setting_network_noprompt);
                im_none.setVisibility(View.VISIBLE);
                imeverytime.setVisibility(View.GONE);
                imchanges.setVisibility(View.GONE);
                break;
        }

    }

    @OnClick({R.id.all, R.id.only_frends, R.id.none})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all:
                modifyUI(0);
                break;
            case R.id.only_frends:
                modifyUI(1);
                break;
            case R.id.none:
                modifyUI(2);
                break;
        }
        postBackData();
    }

    //回传数据
    private void postBackData() {
        getIntent().putExtra("netremind", remindtype);
        setResult(Activity.RESULT_OK, getIntent());
        killMyself();
    }

}

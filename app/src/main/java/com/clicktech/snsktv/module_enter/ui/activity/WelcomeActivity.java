package com.clicktech.snsktv.module_enter.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.module_enter.contract.WelcomeContract;
import com.clicktech.snsktv.module_enter.inject.component.DaggerWelcomeComponent;
import com.clicktech.snsktv.module_enter.inject.module.WelcomeModule;
import com.clicktech.snsktv.module_enter.presenter.WelcomePresenter;
import com.jaeger.library.StatusBarUtil;
import com.tbruyelle.rxpermissions.RxPermissions;

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
 * Created by Administrator on 2017/1/30.
 * 欢迎
 */

public class WelcomeActivity extends WEActivity<WelcomePresenter> implements
        WelcomeContract.View, View.OnClickListener {
    private RxPermissions mRxPermissions;


    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions(this);
        DaggerWelcomeComponent
                .builder()
                .appComponent(appComponent)
                .welcomeModule(new WelcomeModule(this)) //请将WelcomeModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_welcome, null, false);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTransparent(this);
    }

    @Override
    protected void initData() {
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


    @OnClick({R.id.btn_experience})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_experience://立即体验
                mPresenter.requestPermissions();
                break;
        }
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public void enterApp() {
        UiUtils.startActivity(this, PreviewWorkActivity.class);
        finish();
    }
}

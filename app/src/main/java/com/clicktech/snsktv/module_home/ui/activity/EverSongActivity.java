package com.clicktech.snsktv.module_home.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.autolayout.AutoRadioGroup;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.module_home.contract.EverSongContract;
import com.clicktech.snsktv.module_home.inject.component.DaggerEverSongComponent;
import com.clicktech.snsktv.module_home.inject.module.EverSongModule;
import com.clicktech.snsktv.module_home.presenter.EverSongPresenter;
import com.clicktech.snsktv.module_home.ui.fragment.EverAccompanimentFragment;
import com.clicktech.snsktv.module_home.ui.fragment.PracticingSongFragment;
import com.clicktech.snsktv.widget.titlebar.HeaderView;
import com.clicktech.snsktv.widget.titlebar.HeaderView.OnCustomTileListener;

import java.util.ArrayList;
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
 * Created by Administrator on 2017/1/16.
 * 已点
 */

public class EverSongActivity extends WEActivity<EverSongPresenter> implements
        EverSongContract.View, RadioGroup.OnCheckedChangeListener,
        OnCustomTileListener {

    @BindView(R.id.headerview)
    HeaderView headerView;
    @BindView(R.id.autoRg_ever)
    AutoRadioGroup autoRadioGroup;

    private Fragment mCurrentFragment;
    private PracticingSongFragment mPracticFragment;
    private EverAccompanimentFragment mAccompanyFragment;
    private List<Fragment> mFragments;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerEverSongComponent
                .builder()
                .appComponent(appComponent)
                .everSongModule(new EverSongModule(this)) //请将EverSongModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_ever, null, false);
    }

    @Override
    protected void initData() {
        headerView.setTitleClickListener(this);
        autoRadioGroup.setOnCheckedChangeListener(this);
        initFragmentList();
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
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.rb_accompany://已点伴奏
                switchFragment(mAccompanyFragment);
                break;
            case R.id.rb_practic://练唱列表
                switchFragment(mPracticFragment);
                break;
        }

    }

    private void initFragmentList() {
        mFragments = new ArrayList<>();
        mFragments.add(mCurrentFragment = mAccompanyFragment = EverAccompanimentFragment.newInstance());
        mFragments.add(mPracticFragment = PracticingSongFragment.newInstance());
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_ever, mCurrentFragment).commit();
    }

    /**
     * 切换Fragment
     *
     * @param fragment
     */
    private void switchFragment(Fragment fragment) {
        if (mCurrentFragment != fragment) {
            if (fragment.isAdded()) {
                getSupportFragmentManager().beginTransaction().hide(mCurrentFragment)
                        .show(fragment).commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .hide(mCurrentFragment)
                        .replace(R.id.fl_ever, fragment)
                        .commit();
            }
            mCurrentFragment = fragment;
        }

    }

    @Override
    public void setTitleLeftClick() {
        killMyself();
    }

    @Override
    public void setTitleRightClick() {
        showMessage("搜索");
    }
}

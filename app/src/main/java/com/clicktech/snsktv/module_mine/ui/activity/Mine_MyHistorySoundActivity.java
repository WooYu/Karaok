package com.clicktech.snsktv.module_mine.ui.activity;


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
import com.clicktech.snsktv.module_mine.contract.Mine_MyHistorySoundContract;
import com.clicktech.snsktv.module_mine.inject.component.DaggerMine_MyHistorySoundComponent;
import com.clicktech.snsktv.module_mine.inject.module.Mine_MyHistorySoundModule;
import com.clicktech.snsktv.module_mine.presenter.Mine_MyHistorySoundPresenter;
import com.clicktech.snsktv.module_mine.ui.fragment.Mine_MyHistorySound_FRFragment;
import com.clicktech.snsktv.widget.titlebar.HeaderView;

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
 * Created by Administrator on 2017/3/24.
 * 已点伴奏
 */

public class Mine_MyHistorySoundActivity extends WEActivity<Mine_MyHistorySoundPresenter> implements Mine_MyHistorySoundContract.View
        , View.OnClickListener, RadioGroup.OnCheckedChangeListener, HeaderView.OnCustomTileListener {

    @BindView(R.id.headerview)
    HeaderView headerView;
    @BindView(R.id.rg_rich)
    AutoRadioGroup richRGroup;

    private List<Fragment> mFragments;
    private Fragment mCurFrag;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMine_MyHistorySoundComponent
                .builder()
                .appComponent(appComponent)
                .mine_MyHistorySoundModule(new Mine_MyHistorySoundModule(this)) //请将Mine_MyHistorySoundModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_mine_myhistory_souds, null, false);
    }

    @Override
    protected void initData() {
        headerView.setTitleClickListener(this);
        initTabIndicator();
    }

    @Override
    public void onClick(View v) {

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

    /**
     * 初始化 的TabLayout
     */
    private void initTabIndicator() {
        mFragments = new ArrayList<>();
        mFragments.add(Mine_MyHistorySound_FRFragment.newInstance(0));
        mFragments.add(Mine_MyHistorySound_FRFragment.newInstance(1));
        mCurFrag = mFragments.get(0);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_rich, mCurFrag)
                .commit();

        richRGroup.setOnCheckedChangeListener(this);
    }

    /**
     * 切换Fragment
     *
     * @param position
     */
    private void switchFragment(int position) {
        Fragment newFrag = mFragments.get(position);
        if (null != newFrag) {
            if (newFrag.isAdded()) {
                getSupportFragmentManager().beginTransaction()
                        .hide(mCurFrag)
                        .show(newFrag)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .hide(mCurFrag)
                        .add(R.id.fl_rich, newFrag)
                        .commit();
            }
            mCurFrag = newFrag;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_newjin://已点伴奏
                switchFragment(0);
                break;
            case R.id.rb_total://练唱列表
                switchFragment(1);//
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
}

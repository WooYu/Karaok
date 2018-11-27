package com.clicktech.snsktv.module_home.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.recyclerview.FullyLinearLayoutManager;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.module_home.contract.TopListContract;
import com.clicktech.snsktv.module_home.inject.component.DaggerTopListComponent;
import com.clicktech.snsktv.module_home.inject.module.TopListModule;
import com.clicktech.snsktv.module_home.presenter.TopListPresenter;
import com.clicktech.snsktv.module_home.ui.adapter.SpecialAdapter;
import com.clicktech.snsktv.module_home.ui.adapter.TopListAdapter;
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
 * Created by Administrator on 2017/1/13.
 * 首页横幅点击后，跳转的界面
 */

public class TopListActivity extends WEActivity<TopListPresenter> implements
        TopListContract.View, HeaderView.OnCustomTileListener {

    @BindView(R.id.headerview)
    HeaderView headerView;
    @BindView(R.id.rv_special)
    RecyclerView specialRv;
    @BindView(R.id.rv_recommend)
    RecyclerView recommendRv;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerTopListComponent
                .builder()
                .appComponent(appComponent)
                .topListModule(new TopListModule(this)) //请将TopListModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_toplist, null, false);
    }

    @Override
    protected void initData() {
        mPresenter.requestData();
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
    public void setTopListRecyclerView(TopListAdapter adapter) {
        recommendRv.setLayoutManager(new FullyLinearLayoutManager(mWeApplication));
        recommendRv.setAdapter(adapter);
    }

    @Override
    public void setSpecialRecyclerView(SpecialAdapter adapter) {
        specialRv.setLayoutManager(new FullyLinearLayoutManager(mWeApplication));
        specialRv.setAdapter(adapter);
    }

    @Override
    public void setTitleLeftClick() {
        killMyself();
    }

    @Override
    public void setTitleRightClick() {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("classtype", "song");
        UiUtils.startActivity(intent);
    }
}
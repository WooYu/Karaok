package com.clicktech.snsktv.module_mine.ui.activity;

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
import com.clicktech.snsktv.module_mine.contract.Mine_MyListenHistoryContract;
import com.clicktech.snsktv.module_mine.inject.component.DaggerMine_MyListenHistoryComponent;
import com.clicktech.snsktv.module_mine.inject.module.Mine_MyListenHistoryModule;
import com.clicktech.snsktv.module_mine.presenter.Mine_MyListenHistoryPresenter;
import com.clicktech.snsktv.module_mine.ui.adapter.Mine_MyListenHistoryListAdapter;
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
 * Created by Administrator on 2017/3/24.
 * 播放历史
 */

public class Mine_MyListenHistoryActivity extends WEActivity<Mine_MyListenHistoryPresenter> implements
        Mine_MyListenHistoryContract.View,
        View.OnClickListener, HeaderView.OnCustomTileListener {

    @BindView(R.id.headerview)
    HeaderView headerView;
    @BindView(R.id.mine_fans_rcv)
    RecyclerView mine_fans_rcv;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMine_MyListenHistoryComponent
                .builder()
                .appComponent(appComponent)
                .mine_MyListenHistoryModule(new Mine_MyListenHistoryModule(this)) //请将Mine_MyListenHistoryModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_mine_mylisten_history, null, false);
    }

    @Override
    protected void initData() {
        headerView.setTitleClickListener(this);
        mPresenter.requestData();

    }

    @Override
    public void setRecyclerView(Mine_MyListenHistoryListAdapter adapter) {
        mine_fans_rcv.setLayoutManager(new FullyLinearLayoutManager(mWeApplication));
        mine_fans_rcv.setAdapter(adapter);
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
    public void onClick(View v) {

    }

    @Override
    public void setTitleLeftClick() {
        finish();
    }

    @Override
    public void setTitleRightClick() {

    }
}

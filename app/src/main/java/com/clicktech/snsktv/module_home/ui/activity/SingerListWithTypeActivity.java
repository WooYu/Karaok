package com.clicktech.snsktv.module_home.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.module_home.contract.SingerListWithTypeContract;
import com.clicktech.snsktv.module_home.inject.component.DaggerSingerListWithTypeComponent;
import com.clicktech.snsktv.module_home.inject.module.SingerListWithTypeModule;
import com.clicktech.snsktv.module_home.presenter.SingerListWithTypePresenter;
import com.clicktech.snsktv.widget.quicksearch.SearchIndexView;
import com.clicktech.snsktv.widget.quicksearch.SortAdapter;
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
 * Created by Administrator on 2017/3/13.
 * 歌手 的歌曲页面    中英日排序       (男性女性组合海外)
 */

public class SingerListWithTypeActivity extends WEActivity<SingerListWithTypePresenter> implements
        SingerListWithTypeContract.View, HeaderView.OnCustomTileListener {

    @BindView(R.id.headerview)
    HeaderView headerView;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.tv_indextip)
    TextView tvIndexTip;
    @BindView(R.id.searchindex)
    SearchIndexView searchIndexView;

    String categoryId = "0";

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerSingerListWithTypeComponent
                .builder()
                .appComponent(appComponent)
                .singerListWithTypeModule(new SingerListWithTypeModule(this)) //请将SingerListWithTypeModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_singerlist_withtype, null, false);
    }

    @Override
    public void setTitleLeftClick() {
        killMyself();
    }

    @Override
    public void setTitleRightClick() {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("classtype", "singer");
        launchActivity(intent);
    }

    @Override
    protected void initData() {
        headerView.setTitleClickListener(this);
        categoryId = getIntent().getStringExtra("categoryid");
        String categoryName = getIntent().getStringExtra("categoryname");
        if (categoryId == null) {
            categoryId = "0";//全部
        }

        headerView.setTitleName(TextUtils.isEmpty(categoryName) ? getString(R.string.singer_all) : categoryName);
        mPresenter.requestData(categoryId);

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
    public void setRecyclerView(SortAdapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}

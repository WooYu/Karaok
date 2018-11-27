package com.clicktech.snsktv.module_home.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.entity.SingerInfoEntity;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_home.contract.ClassifyListContract;
import com.clicktech.snsktv.module_home.inject.component.DaggerClassifyListComponent;
import com.clicktech.snsktv.module_home.inject.module.ClassifyListModule;
import com.clicktech.snsktv.module_home.presenter.ClassifyListPresenter;
import com.clicktech.snsktv.module_home.ui.adapter.ClassifyAdapter;
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
 * Created by Administrator on 2017/1/16.
 * 歌曲列表：包括主题、年代搜索、歌手搜索联想、已点歌手、歌手分类
 */

public class ClassifyListActivity extends WEActivity<ClassifyListPresenter> implements
        ClassifyListContract.View, HeaderView.OnCustomTileListener {

    @BindView(R.id.headerview)
    HeaderView headerView;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    int incomeType = 0;//进入此页面的方式类型，0，歌手进入，1歌曲分类进入，2年代选区进入。。。
    //当incomeType = 0时，传来的歌手信息
    private SingerInfoEntity mSingerInfo;
    //当incomeType = 1; 时，需要获取歌曲分类ID
    private String categoryId;
    //当incomeType = 1时，传来的开始和结束年份
    private String beginYear;
    private String endYear;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerClassifyListComponent
                .builder()
                .appComponent(appComponent)
                .classifyListModule(new ClassifyListModule(this)) //请将ClassifyListModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_classifylist_new, null, false);
    }

    @Override
    protected void initData() {
        headerView.setTitleClickListener(this);
        headerView.setTitleName(getResources().getString(R.string.hall_classfiy));

        getPramersData();
        getDataFromNet();
    }

    private void getPramersData() {
        String titlename = getIntent().getStringExtra("title");
        if (EmptyUtils.isNotEmpty(titlename)) {
            headerView.setTitleName(titlename);
        }

        incomeType = getIntent().getIntExtra("incometype", 0);

        switch (incomeType) {
            case 0:
                mSingerInfo = getIntent().getParcelableExtra("singerinfo");
                break;
            case 1:
                categoryId = getIntent().getStringExtra("categoryid");
                break;
            case 2:
                beginYear = getIntent().getStringExtra("beginyear");
                endYear = getIntent().getStringExtra("endyear");
                break;
        }
    }

    //加载数据下拉刷新
    private void getDataFromNet() {

        switch (incomeType) {
            case 0:
                mPresenter.requestData_WithSinger(mSingerInfo.getSinger_id());
                break;
            case 1:
                mPresenter.requestData_WithCategory(categoryId);
                break;
            case 2:
                mPresenter.requestData_WithYearsBetween(beginYear, endYear);
                break;
        }
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
    public void setRecyclerView(ClassifyAdapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(mWeApplication));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void turn2KSing(SongInfoBean data) {
        Intent ksingIntent = new Intent(this, KSingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("songinfo", data);
        bundle.putInt("workstype", mWeApplication.getResources().getInteger(R.integer.workstype_common));
        ksingIntent.putExtras(bundle);
        launchActivity(ksingIntent);
    }

    @Override
    public void turn2Learnsing(SongInfoBean data) {
        Intent intent = new Intent(this, LearnSingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("songid", data.getSong_id());
        if (incomeType == 0) {
            bundle.putParcelable("singer", mSingerInfo);
        }
        intent.putExtras(bundle);
        launchActivity(intent);
    }

    @Override
    public void setTitleLeftClick() {
        finish();
    }

    @Override
    public void setTitleRightClick() {
    }

}

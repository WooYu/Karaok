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
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.module_home.contract.ChorusWorksContract;
import com.clicktech.snsktv.module_home.inject.component.DaggerChorusWorksComponent;
import com.clicktech.snsktv.module_home.inject.module.ChorusWorksModule;
import com.clicktech.snsktv.module_home.presenter.ChorusWorksPresenter;
import com.clicktech.snsktv.module_home.ui.adapter.ChorusWorksAdapter;
import com.clicktech.snsktv.widget.quicksearch.SearchIndexView;
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
 * 音乐馆-合唱分类（类型）-歌曲
 */


public class ChorusWorksActivity extends WEActivity<ChorusWorksPresenter> implements
        ChorusWorksContract.View, HeaderView.OnCustomTileListener {

    @BindView(R.id.headerview)
    HeaderView mHeaderView;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.tv_indextip)
    TextView tvIndexTip;
    @BindView(R.id.searchindex)
    SearchIndexView searchIndexView;

    private String mSingerCategoryId;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerChorusWorksComponent
                .builder()
                .appComponent(appComponent)
                .chorusWorksModule(new ChorusWorksModule(this)) //请将ChorusWorksModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_chorus_work, null, false);
    }

    @Override
    protected void initData() {
        mSingerCategoryId = getIntent().getStringExtra("singercategoryid");
        String categoryName = getIntent().getStringExtra("categoryname");

        mHeaderView.setTitleClickListener(this);
        mHeaderView.setTitleName(TextUtils.isEmpty(categoryName) ? getString(R.string.singer_music_all) : categoryName);

        if (EmptyUtils.isNotEmpty(mSingerCategoryId)) {
            mPresenter.requestData(mSingerCategoryId);
        }

        searchIndexView.setOnTouchingLetterChangedListener(new SearchIndexView.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                int position = mPresenter.getPositionOfSelectLetter(s);
                if (-1 != position) {
                    recyclerView.scrollToPosition(position);
                }
            }
        });
        searchIndexView.setTextView(tvIndexTip);
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
    public void setTitleLeftClick() {
        killMyself();
    }

    @Override
    public void setTitleRightClick() {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("classtype", "song");
        UiUtils.startActivity(intent);
    }

    @Override
    public void setRecyclerView(ChorusWorksAdapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
package com.clicktech.snsktv.module_home.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.module_home.contract.SongSortContract;
import com.clicktech.snsktv.module_home.inject.component.DaggerSongSortComponent;
import com.clicktech.snsktv.module_home.inject.module.SongSortModule;
import com.clicktech.snsktv.module_home.presenter.SongSortPresenter;
import com.clicktech.snsktv.module_home.ui.adapter.SongsortAdapter;
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
 * Created by Administrator on 2017/5/19.
 * 曲名-分类-歌曲列表
 */

public class SongSortActivity extends WEActivity<SongSortPresenter> implements
        SongSortContract.View, HeaderView.OnCustomTileListener {

    @BindView(R.id.headerview)
    HeaderView mHeaderView;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.tv_indextip)
    TextView tvIndexTip;
    @BindView(R.id.searchindex)
    SearchIndexView searchIndexView;

    private String mCategoryid;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerSongSortComponent
                .builder()
                .appComponent(appComponent)
                .songSortModule(new SongSortModule(this)) //请将SongSortModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_songsort, null, false);
    }

    @Override
    protected void initData() {
        mCategoryid = getIntent().getStringExtra("categoryid");
        String title = getIntent().getStringExtra("title");
        if (EmptyUtils.isNotEmpty(title)) {
            mHeaderView.setTitleName(title);
        }
        mHeaderView.setTitleClickListener(this);

        if (EmptyUtils.isNotEmpty(mCategoryid)) {
            mPresenter.requestData_WithCategory(mCategoryid);
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
    public void setRecyclerView(SongsortAdapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

}

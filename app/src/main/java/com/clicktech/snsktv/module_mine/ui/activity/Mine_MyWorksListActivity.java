package com.clicktech.snsktv.module_mine.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.ConvertUtils;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.entity.WorkAlbumBean;
import com.clicktech.snsktv.module_mine.contract.Mine_MyWorksListContract;
import com.clicktech.snsktv.module_mine.inject.component.DaggerMine_MyWorksListComponent;
import com.clicktech.snsktv.module_mine.inject.module.Mine_MyWorksListModule;
import com.clicktech.snsktv.module_mine.presenter.Mine_MyWorksListPresenter;
import com.clicktech.snsktv.module_mine.ui.adapter.AlbumAdapter;
import com.clicktech.snsktv.module_mine.ui.adapter.MineSingleListAdapter;
import com.clicktech.snsktv.widget.pagetransformer.RotateYTransformer;
import com.clicktech.snsktv.widget.pagetransformer.ScaleInTransformer;
import com.clicktech.snsktv.widget.titlebar.HeaderView;
import com.jaeger.library.StatusBarUtil;

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
 * Created by Administrator on 2017/3/21.
 * 我的作品
 */

public class Mine_MyWorksListActivity extends WEActivity<Mine_MyWorksListPresenter> implements
        Mine_MyWorksListContract.View, HeaderView.OnCustomTileListener {

    @BindView(R.id.headerview)
    HeaderView headerview;
    @BindView(R.id.ll_albuminfo)
    LinearLayout llAlbumInfo;
    @BindView(R.id.tv_album_index)
    TextView tvAlbumIndex;
    @BindView(R.id.albumviewpager)
    ViewPager albumviewpager;
    @BindView(R.id.tv_album_name)
    TextView tvAlbumName;
    @BindView(R.id.tv_album_single)
    TextView tvAlbumSingle;
    @BindView(R.id.tv_album_comment)
    TextView tvAlbumComment;
    @BindView(R.id.tv_album_gift)
    TextView tvAlbumGift;
    @BindView(R.id.rv_single)
    RecyclerView rvSingle;
    @BindView(R.id.tv_single)
    TextView tvSingle;

    private String mUserID;
    private AlbumAdapter mAlbumAdapter;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMine_MyWorksListComponent
                .builder()
                .appComponent(appComponent)
                .mine_MyWorksListModule(new Mine_MyWorksListModule(this)) //请将Mine_MyWorksListModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_mine_myworkslist,
                null, false);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucent(this,
                getResources().getInteger(R.integer.statusalpha_a));
    }

    @Override
    protected void initData() {

        headerview.setTitleClickListener(this);
        mUserID = getIntent().getStringExtra("userid");
        String authorname = getIntent().getStringExtra("authorname");
        if (EmptyUtils.isNotEmpty(authorname)) {
            headerview.setTitleName(String.format(getString(R.string.format_worktitle), authorname));
        } else {
            headerview.setTitleRightText(getString(R.string.album_make));
        }

        if (EmptyUtils.isEmpty(mUserID)) {
            return;
        }

        mPresenter.requestWorks(mUserID);
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
        finish();
    }

    @Override
    public void setTitleRightClick() {
        UiUtils.startActivity(AlbumProductActivity.class);
    }

    @Override
    public void updateSingleView(int size) {
        tvSingle.setText(String.format(getString(R.string.format_numofsingle),
                size));
    }

    @Override
    public void initAlbumView(List<WorkAlbumBean> list, ViewPager.OnPageChangeListener listener) {
        mAlbumAdapter = new AlbumAdapter(getSupportFragmentManager(), list, this);
        albumviewpager.setPageMargin(ConvertUtils.dp2px(this, -170
        ));
        albumviewpager.setAdapter(mAlbumAdapter);
        albumviewpager.setPageTransformer(true, new
                ScaleInTransformer(new RotateYTransformer()));
        albumviewpager.addOnPageChangeListener(listener);
    }

    @Override
    public void initSingleView(MineSingleListAdapter adapter) {
        rvSingle.setLayoutManager(new LinearLayoutManager(this));
        rvSingle.setAdapter(adapter);
    }

    @Override
    public void scrollAlbum(List<WorkAlbumBean> albumList, int albumposition) {
        if (EmptyUtils.isEmpty(albumList)) {
            return;
        }

        WorkAlbumBean workAlbumBean = albumList.get(albumposition);
        tvAlbumIndex.setText(String.format(getString(R.string.format_numofalbum), albumposition,
                albumList.size()));
        tvAlbumName.setText(workAlbumBean.getAlbum_name());
        tvAlbumSingle.setText(String.format(getString(R.string.format_singleofablum),
                workAlbumBean.getWorks_sum()));
        tvAlbumComment.setText(String.format(getString(R.string.format_commentofalbum),
                workAlbumBean.getComment_sum()));
        tvAlbumGift.setText(String.format(getString(R.string.format_giftsofalbume),
                workAlbumBean.getGift_sum()));
    }

    @Override
    public void updateAlbumView(List<WorkAlbumBean> albumlist) {
        mAlbumAdapter.setData(albumlist);
        albumviewpager.setCurrentItem(albumlist.size() - 1);
        if (EmptyUtils.isEmpty(albumlist)) {
            llAlbumInfo.setVisibility(View.GONE);
        } else {
            llAlbumInfo.setVisibility(View.VISIBLE);
        }
    }

}

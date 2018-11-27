package com.clicktech.snsktv.module_discover.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.contract.MoreSongsContract;
import com.clicktech.snsktv.module_discover.inject.component.DaggerMoreSongsComponent;
import com.clicktech.snsktv.module_discover.inject.module.MoreSongsModule;
import com.clicktech.snsktv.module_discover.presenter.MoreSongsPresenter;
import com.clicktech.snsktv.module_discover.ui.adapter.MoreSongsAdapter;
import com.clicktech.snsktv.widget.titlebar.HeaderView;

import java.util.ArrayList;

import butterknife.BindView;

import static com.clicktech.snsktv.arms.utils.Preconditions.checkNotNull;

/**
 * 歌曲详情-更多歌曲
 */
public class MoreSongsActivity extends WEActivity<MoreSongsPresenter> implements
        MoreSongsContract.View, HeaderView.OnCustomTileListener {


    @BindView(R.id.headerview)
    HeaderView headerview;
    @BindView(R.id.rv_moresongs)
    RecyclerView rvMoresongs;

    private ArrayList<SongInfoBean> mSongList;
    private MoreSongsAdapter mSongAdapter;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMoreSongsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .moreSongsModule(new MoreSongsModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_more_songs, null, false);
    }

    @Override
    protected void initData() {
        headerview.setTitleClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (null == bundle) {
            return;
        }

        mSongList = bundle.getParcelableArrayList("songranking");
        if (EmptyUtils.isEmpty(mSongList)) {
            return;
        }

        setRecyclerView(null);
//        mPresenter.getMoreSongs("");
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
    public void setRecyclerView(MoreSongsAdapter adapter) {

        mSongAdapter = new MoreSongsAdapter(mSongList);
        mSongAdapter.setOnItemClickListener(new DefaultAdapter.
                OnRecyclerViewItemClickListener<SongInfoBean>() {
            @Override
            public void onItemClick(View view, SongInfoBean data, int position) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("songinfo", data);

                Intent intent = new Intent(mApplication, SongDetailsActivity.class);
                intent.putExtras(bundle);
                launchActivity(intent);
            }
        });

        rvMoresongs.setLayoutManager(new LinearLayoutManager(this));
        rvMoresongs.setAdapter(mSongAdapter);
    }

    @Override
    public void setTitleLeftClick() {
        killMyself();
    }

    @Override
    public void setTitleRightClick() {

    }
}

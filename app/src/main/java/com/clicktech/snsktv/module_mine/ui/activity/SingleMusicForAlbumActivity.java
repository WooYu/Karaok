package com.clicktech.snsktv.module_mine.ui.activity;

import android.app.Activity;
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
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_mine.contract.SingleMusicForAlbumContract;
import com.clicktech.snsktv.module_mine.inject.component.DaggerSingleMusicForAlbumComponent;
import com.clicktech.snsktv.module_mine.inject.module.SingleMusicForAlbumModule;
import com.clicktech.snsktv.module_mine.presenter.SingleMusicForAlbumPresenter;
import com.clicktech.snsktv.module_mine.ui.adapter.AddSingleMusicAdapter;
import com.clicktech.snsktv.module_mine.ui.adapter.AddSingleMusicHeadAdapter;
import com.clicktech.snsktv.widget.titlebar.HeaderView;

import java.util.ArrayList;

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
 * 制作专辑的添加作品
 */

public class SingleMusicForAlbumActivity extends WEActivity<SingleMusicForAlbumPresenter> implements
        SingleMusicForAlbumContract.View, HeaderView.OnCustomTileListener {

    @BindView(R.id.headerview)
    HeaderView headerview;
    @BindView(R.id.headrecyclerview)
    RecyclerView headrecyclerview;
    @BindView(R.id.singlerecyclerview)
    RecyclerView singlerecyclerview;

    private ArrayList<SongInfoBean> mSongListOfSelected;
    private AddSingleMusicHeadAdapter mSongOfSelectedAdapter;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerSingleMusicForAlbumComponent
                .builder()
                .appComponent(appComponent)
                .singleMusicForAlbumModule(new SingleMusicForAlbumModule(this)) //请将SingleMusicForAlbumModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_singlemusicforalbum, null, false);
    }

    @Override
    protected void initData() {
        headerview.setTitleClickListener(this);
        headerview.setTitleRightText(String.format(getString(R.string.format_single_album), 0,
                getResources().getInteger(R.integer.single_upperlimit)));
        initHeadRecyclerview();
        mPresenter.requestSingle();
    }

    private void initHeadRecyclerview() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mWeApplication);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        headrecyclerview.setLayoutManager(linearLayoutManager);
        headrecyclerview.setAdapter(mSongOfSelectedAdapter = new AddSingleMusicHeadAdapter(mSongListOfSelected));
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
        int sizeOfSelect = EmptyUtils.isEmpty(mSongListOfSelected) ? 0 : mSongListOfSelected.size();
        if (sizeOfSelect < getResources().getInteger(R.integer.single_lowerlimit)
                || sizeOfSelect > getResources().getInteger(R.integer.single_upperlimit)) {
            showMessage(getString(R.string.error_singlelimit));
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("songlist", mSongListOfSelected);
        getIntent().putExtras(bundle);
        setResult(Activity.RESULT_OK, getIntent());
        killMyself();
    }

    @Override
    public void setSingleRecyclerView(AddSingleMusicAdapter adapter) {
        singlerecyclerview.setLayoutManager(new LinearLayoutManager(mWeApplication));
        singlerecyclerview.setAdapter(adapter);
    }

    @Override
    public void updateSongListOfSelected(boolean contain, SongInfoBean songInfoBean) {
        if (EmptyUtils.isEmpty(mSongListOfSelected)) {
            mSongListOfSelected = new ArrayList<>();
        }
        if (contain) {
            mSongListOfSelected.add(songInfoBean);
        } else {
            mSongListOfSelected.remove(songInfoBean);
        }
        mSongOfSelectedAdapter.setmInfos(mSongListOfSelected);
        headerview.setTitleRightText(String.format(getString(R.string.format_single_album),
                mSongListOfSelected.size(), getResources().getInteger(R.integer.single_upperlimit)));
    }

}

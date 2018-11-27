package com.clicktech.snsktv.module_home.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.MediaFile;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.ui.activity.SelectRoleActivity;
import com.clicktech.snsktv.module_home.contract.ChorusTypeContract;
import com.clicktech.snsktv.module_home.inject.component.DaggerChorusTypeComponent;
import com.clicktech.snsktv.module_home.inject.module.ChorusTypeModule;
import com.clicktech.snsktv.module_home.presenter.ChorusTypePresenter;
import com.clicktech.snsktv.module_home.ui.adapter.ChorusTypeAdapter;
import com.clicktech.snsktv.module_home.ui.adapter.PopularChorusAdapter;
import com.clicktech.snsktv.util.HistoryUtils;
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
 * 音乐馆-合唱分类（类型）
 */
public class ChorusTypeActivity extends WEActivity<ChorusTypePresenter> implements
        ChorusTypeContract.View, HeaderView.OnCustomTileListener {

    @BindView(R.id.headerview)
    HeaderView headerView;
    @BindView(R.id.songcategory_rcv_his)
    RecyclerView songcategory_rcv_his;
    @BindView(R.id.songcategory_rcv)
    RecyclerView songcategory_rcv;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerChorusTypeComponent
                .builder()
                .appComponent(appComponent)
                .chorusTypeModule(new ChorusTypeModule(this)) //请将ChorusTypeModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_chorus_type, null, false);
    }

    @Override
    protected void initData() {
        headerView.setTitleClickListener(this);
        mPresenter.requestData(RequestParams_Maker.getSingerCategory());
        mPresenter.requestChorusList(RequestParams_Maker.getChorusTypeList(1, 3));
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
    public void setRecyclerView(PopularChorusAdapter popularChorusAdapter, ChorusTypeAdapter adapter) {
        songcategory_rcv_his.setLayoutManager(new LinearLayoutManager(mWeApplication) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        songcategory_rcv_his.setAdapter(popularChorusAdapter);
        songcategory_rcv.setLayoutManager(new LinearLayoutManager(mWeApplication));
        songcategory_rcv.setAdapter(adapter);
    }

    @Override
    public void turn2SelectRole(SongInfoBean bean) {
        Intent chorusIntent = new Intent(this, SelectRoleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("songinfo", bean);
        bundle.putInt("workstype", getResources().getInteger(R.integer.workstype_chrousother));
        bundle.putBoolean("needmv", MediaFile.isVideoFileType(bean.getWorks_url()));
        chorusIntent.putExtras(bundle);
        HistoryUtils.putMusicHistory(mWeApplication, bean);  //保存历史记录
        UiUtils.startActivity(this, chorusIntent);

    }

    @Override
    public void setTitleLeftClick() {
        finish();
    }

    @Override
    public void setTitleRightClick() {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("classtype", "song");
        UiUtils.startActivity(intent);
    }

}
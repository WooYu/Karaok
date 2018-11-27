package com.clicktech.snsktv.module_discover.ui.activity;

import android.app.Activity;
import android.content.Intent;
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
import com.clicktech.snsktv.module_discover.contract.OtherAlbumContract;
import com.clicktech.snsktv.module_discover.inject.component.DaggerOtherAlbumComponent;
import com.clicktech.snsktv.module_discover.inject.module.OtherAlbumModule;
import com.clicktech.snsktv.module_discover.presenter.OtherAlbumPresenter;
import com.clicktech.snsktv.module_discover.ui.adapter.OthersAlbumAdapter;
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
 * Created by Administrator on 2017/5/18.
 * 其它专辑
 */

public class OtherAlbumActivity extends WEActivity<OtherAlbumPresenter> implements
        OtherAlbumContract.View, HeaderView.OnCustomTileListener {


    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.headerview)
    HeaderView headerview;

    private String userId;
    private boolean mNeedAlbumID;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerOtherAlbumComponent
                .builder()
                .appComponent(appComponent)
                .otherAlbumModule(new OtherAlbumModule(this)) //请将OtherAlbumModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_otheralbum, null, false);
    }

    @Override
    protected void initData() {
        headerview.setTitleClickListener(this);
        userId = getIntent().getStringExtra("userid");
        mNeedAlbumID = getIntent().getBooleanExtra("need", false);
        if (EmptyUtils.isEmpty(userId)) {
            return;
        }
        mPresenter.requestData(userId);
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
    public void setAlbumListRecyclerView(OthersAlbumAdapter adapter) {
        recyclerview.setLayoutManager(new LinearLayoutManager(mWeApplication));
        recyclerview.setAdapter(adapter);
    }

    @Override
    public void returnAlbumID(String albumid) {
        if (mNeedAlbumID) {
            getIntent().putExtra("albumid", albumid);
            setResult(Activity.RESULT_OK, getIntent());
            killMyself();
        }
    }

    @Override
    public void setTitleLeftClick() {
        killMyself();
    }

    @Override
    public void setTitleRightClick() {

    }
}

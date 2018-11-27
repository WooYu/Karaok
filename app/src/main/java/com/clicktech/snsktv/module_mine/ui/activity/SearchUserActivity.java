package com.clicktech.snsktv.module_mine.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.module_mine.contract.SearchUserContract;
import com.clicktech.snsktv.module_mine.inject.component.DaggerSearchUserComponent;
import com.clicktech.snsktv.module_mine.inject.module.SearchUserModule;
import com.clicktech.snsktv.module_mine.presenter.SearchUserPresenter;
import com.clicktech.snsktv.module_mine.ui.adapter.SearchUserAdapter;
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
 * Created by Administrator on 2017/6/29.
 */

public class SearchUserActivity extends WEActivity<SearchUserPresenter> implements SearchUserContract.View, HeaderView.OnCustomTileListener {


    @BindView(R.id.headerview)
    HeaderView headerview;
    @BindView(R.id.et_keyword)
    EditText etKeyword;
    @BindView(R.id.rv_friend)
    RecyclerView rvFriend;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerSearchUserComponent
                .builder()
                .appComponent(appComponent)
                .searchUserModule(new SearchUserModule(this)) //请将SearchUserModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_serachuser, null, false);
    }

    @Override
    protected void initData() {
        headerview.setTitleClickListener(this);
        initEdit();
    }

    private void initEdit() {
        etKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String key = s.toString();
                if (EmptyUtils.isEmpty(key)) {
                    return;
                }

                mPresenter.searchUser(RequestParams_Maker.getSearchUser(key));
            }
        });
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
    public void setRecyclerView(SearchUserAdapter adapter) {
        rvFriend.setLayoutManager(new LinearLayoutManager(mWeApplication));
        rvFriend.setHasFixedSize(true);
        rvFriend.setAdapter(adapter);
    }

    @Override
    public void setTitleLeftClick() {
        killMyself();
    }

    @Override
    public void setTitleRightClick() {

    }

}

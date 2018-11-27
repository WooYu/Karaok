package com.clicktech.snsktv.module_mine.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.module_mine.contract.Mine_Att_UserListContract;
import com.clicktech.snsktv.module_mine.inject.component.DaggerMine_Att_UserListComponent;
import com.clicktech.snsktv.module_mine.inject.module.Mine_Att_UserListModule;
import com.clicktech.snsktv.module_mine.presenter.Mine_Att_UserListPresenter;
import com.clicktech.snsktv.module_mine.ui.adapter.Mine_AttentionUserListAdapter;
import com.clicktech.snsktv.widget.titlebar.HeaderView;
import com.flyco.animation.ZoomEnter.ZoomInEnter;
import com.flyco.animation.ZoomExit.ZoomOutExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

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
 * 关注
 */

public class Mine_Att_UserListActivity extends WEActivity<Mine_Att_UserListPresenter> implements
        Mine_Att_UserListContract.View, HeaderView.OnCustomTileListener {


    @BindView(R.id.headerview)
    HeaderView headerview;
    @BindView(R.id.et_searchkey)
    EditText etSearchkey;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.rv_attent)
    RecyclerView rvAttent;

    private String userId;
    private boolean bOtherConcern = true;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMine_Att_UserListComponent
                .builder()
                .appComponent(appComponent)
                .mine_Att_UserListModule(new Mine_Att_UserListModule(this)) //请将Mine_Att_UserListModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_mine_attent, null, false);
    }

    @Override
    protected void initData() {
        headerview.setTitleClickListener(this);
        userId = getIntent().getStringExtra("userid");

        if (EmptyUtils.isEmpty(userId)) {
            bOtherConcern = false;
        }
        mPresenter.initAdapter(userId, bOtherConcern);
        initSearchKeyWord();
    }

    @Override
    public void setRecyclerView(Mine_AttentionUserListAdapter adapter) {
        rvAttent.setLayoutManager(new LinearLayoutManager(mWeApplication));
        rvAttent.setAdapter(adapter);
    }

    @Override
    public void showAttentionDialog(final String attentid) {
        final MaterialDialog dialog = new MaterialDialog(this);
        dialog.content(getString(R.string.mine_attentiondialog_cancle))
                .btnText(getString(R.string.dialog_cancel), getString(R.string.dialog_sure))
                .showAnim(new ZoomInEnter())
                .dismissAnim(new ZoomOutExit())
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {//right btn click listener
                    @Override
                    public void onBtnClick() {
                        mPresenter.requestData_CancelAttent(attentid);
                        dialog.dismiss();
                    }
                }
        );
    }

    @Override
    public void cancleAttentSuccess() {
        etSearchkey.setText("");
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
    }

    @OnClick(R.id.iv_clear)
    public void onViewClicked() {
        etSearchkey.setText("");
        mPresenter.searchUser_clearKey();
    }

    private void initSearchKeyWord() {
        RxTextView.textChangeEvents(etSearchkey)
                .debounce(300, TimeUnit.MILLISECONDS)  //debounce:每次文本更改后有300毫秒的缓冲时间，默认在computation调度器
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<TextViewTextChangeEvent>() {
                    @Override
                    public void call(TextViewTextChangeEvent textViewTextChangeEvent) {
                        String key = etSearchkey.getText().toString().trim();
                        if (EmptyUtils.isNotEmpty(key)) {
                            ivClear.setVisibility(View.VISIBLE);
                            mPresenter.searchUser(textViewTextChangeEvent.text().toString().trim());
                        } else {
                            ivClear.setVisibility(View.GONE);
                        }

                    }
                });

    }

}

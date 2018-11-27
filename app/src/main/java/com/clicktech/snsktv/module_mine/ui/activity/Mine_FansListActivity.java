package com.clicktech.snsktv.module_mine.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.recyclerview.FullyLinearLayoutManager;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.entity.Att_Fans_UserEntity;
import com.clicktech.snsktv.module_mine.contract.Mine_FansListContract;
import com.clicktech.snsktv.module_mine.inject.component.DaggerMine_FansListComponent;
import com.clicktech.snsktv.module_mine.inject.module.Mine_FansListModule;
import com.clicktech.snsktv.module_mine.presenter.Mine_FansListPresenter;
import com.clicktech.snsktv.module_mine.ui.adapter.Mine_FansUserListAdapter;
import com.clicktech.snsktv.widget.titlebar.HeaderView;
import com.flyco.animation.ZoomEnter.ZoomInEnter;
import com.flyco.animation.ZoomExit.ZoomOutExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;

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
 * 我的---粉丝
 */

public class Mine_FansListActivity extends WEActivity<Mine_FansListPresenter> implements
        Mine_FansListContract.View, HeaderView.OnCustomTileListener {

    @BindView(R.id.headerview)
    HeaderView headerView;
    @BindView(R.id.mine_fans_rcv)
    RecyclerView mine_fans_rcv;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMine_FansListComponent
                .builder()
                .appComponent(appComponent)
                .mine_FansListModule(new Mine_FansListModule(this)) //请将Mine_FansListModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_mine_fanslist, null, false);
    }

    @Override
    protected void initData() {
        headerView.setTitleClickListener(this);
        String userid = getIntent().getStringExtra("userid");
        mPresenter.requestFansList(userid);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void setRecyclerView(Mine_FansUserListAdapter adapter) {
        mine_fans_rcv.setLayoutManager(new FullyLinearLayoutManager(mWeApplication));
        mine_fans_rcv.setAdapter(adapter);
    }

    @Override
    public void showAttentionDialog(final Att_Fans_UserEntity data, final int position) {
        int attenttype = Integer.parseInt(data.getAttention_type());
        final boolean isAddAttent = attenttype == getResources().getInteger(R.integer.attentiontype_no);
        String tipContent = isAddAttent ?
                getString(R.string.mine_attentiondialog_add) : getString(R.string.mine_attentiondialog_cancle);

        final MaterialDialog dialog = new MaterialDialog(this);
        dialog.content(tipContent)
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
                        dialog.dismiss();
                        if (isAddAttent) {
                            mPresenter.requestData_AddAttent(data.getId(), position);
                        } else {
                            mPresenter.requestData_CancelAttent(data.getId(), position);
                        }
                    }
                }
        );
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

}

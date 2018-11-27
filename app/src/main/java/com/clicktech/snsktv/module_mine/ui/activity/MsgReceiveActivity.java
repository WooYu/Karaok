package com.clicktech.snsktv.module_mine.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.module_mine.contract.MsgReceiveContract;
import com.clicktech.snsktv.module_mine.inject.component.DaggerMsgReceiveComponent;
import com.clicktech.snsktv.module_mine.inject.module.MsgReceiveModule;
import com.clicktech.snsktv.module_mine.presenter.MsgReceivePresenter;
import com.clicktech.snsktv.widget.titlebar.HeaderView;

import butterknife.BindView;
import butterknife.OnClick;

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
 * Created by Administrator on 2017/2/4.
 * 接受消息
 */

public class MsgReceiveActivity extends WEActivity<MsgReceivePresenter> implements
        MsgReceiveContract.View, HeaderView.OnCustomTileListener {

    @BindView(R.id.headerview)
    HeaderView headerView;
    @BindView(R.id.all)
    RelativeLayout all;
    @BindView(R.id.only_frends)
    RelativeLayout onlyFrends;
    @BindView(R.id.none)
    RelativeLayout none;
    @BindView(R.id.im_all)
    ImageView imAll;
    @BindView(R.id.im_frends)
    ImageView imFrends;
    @BindView(R.id.im_none)
    ImageView imNone;

    private int mType;
    private String mDescOfMsg;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMsgReceiveComponent
                .builder()
                .appComponent(appComponent)
                .msgReceiveModule(new MsgReceiveModule(this)) //请将MsgReceiveModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_msgswitch, null, false);
    }

    @Override
    protected void initData() {
        headerView.setTitleClickListener(this);
        mPresenter.requestMsgType(RequestParams_Maker.getChangeMsgSetting(mWeApplication.getUserID(), "-1"));
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
    public void setTitleLeftClick() {
        finish();
    }

    @Override
    public void setTitleRightClick() {
    }

    @OnClick({R.id.all, R.id.only_frends, R.id.none})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all:
                mType = 0;
                mDescOfMsg = getString(R.string.setting_inform_switch_all);
                break;
            case R.id.only_frends:
                mType = 1;
                mDescOfMsg = getString(R.string.setting_inform_switch_friend);
                break;
            case R.id.none:
                mType = 2;
                mDescOfMsg = getString(R.string.setting_inform_switch_none);
                break;
        }

        modifyUI(mType);
        mPresenter.updateMsgType(RequestParams_Maker.getChangeMsgSetting(mWeApplication.getUserID(), String.valueOf(mType)));
    }

    @Override
    public void modifyUI(int type) {
        mType = type;
        switch (type) {
            case 0:
                imAll.setVisibility(View.VISIBLE);
                imFrends.setVisibility(View.GONE);
                imNone.setVisibility(View.GONE);
                break;
            case 1:
                imFrends.setVisibility(View.VISIBLE);
                imAll.setVisibility(View.GONE);
                imNone.setVisibility(View.GONE);
                break;
            case 2:
                imNone.setVisibility(View.VISIBLE);
                imAll.setVisibility(View.GONE);
                imFrends.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void modifySuccessfully() {
        showMessage(getString(R.string.tip_receivemsg));
        getIntent().putExtra("msgtype", mDescOfMsg);
        setResult(Activity.RESULT_OK, getIntent());
        killMyself();
    }
}

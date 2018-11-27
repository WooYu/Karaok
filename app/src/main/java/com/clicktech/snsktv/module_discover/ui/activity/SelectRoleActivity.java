package com.clicktech.snsktv.module_discover.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.contract.SelectRoleContract;
import com.clicktech.snsktv.module_discover.inject.component.DaggerSelectRoleComponent;
import com.clicktech.snsktv.module_discover.inject.module.SelectRoleModule;
import com.clicktech.snsktv.module_discover.presenter.SelectRolePresenter;
import com.clicktech.snsktv.module_home.ui.activity.KSingActivity;
import com.clicktech.snsktv.widget.titlebar.HeaderView;
import com.jaeger.library.StatusBarUtil;
import com.library.multimedia.lyricscontrols.parser.LrcRow;
import com.library.multimedia.lyricscontrols.view.LyricOfPreviewImpl;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

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
 * Created by Administrator on 2017/4/26.
 * 选择角色
 */

public class SelectRoleActivity extends WEActivity<SelectRolePresenter> implements
        SelectRoleContract.View, HeaderView.OnCustomTileListener {
    @BindView(R.id.headerview)
    HeaderView mHeaderView;
    @BindView(R.id.lrcview)
    LyricOfPreviewImpl lrcview;
    @BindView(R.id.ll_chorusfirst)
    LinearLayout llChorusfirst;
    @BindView(R.id.ll_chorussecond)
    LinearLayout llChorussecond;
    @BindView(R.id.tv_secondchorustip)
    TextView tvSecondchorustip;
    @BindView(R.id.tv_startchorus)
    TextView tvStartchorus;

    private boolean isFirstPerson;
    private boolean needmv;//是否有mv
    private SongInfoBean mSongInfo;
    private int mychorustype;  //当前演唱者的workstype
    private int songworkstype;//当前歌曲的workstype

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerSelectRoleComponent
                .builder()
                .appComponent(appComponent)
                .selectRoleModule(new SelectRoleModule(this)) //请将SelectRoleModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucent(this,
                getResources().getInteger(R.integer.statusalpha_a));
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_selectrole, null, false);
    }

    @Override
    protected void initData() {
        mHeaderView.setTitleClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (null == bundle)
            return;

        mSongInfo = bundle.getParcelable("songinfo");
        needmv = bundle.getBoolean("needmv", false);
        mychorustype = bundle.getInt("workstype", getResources().getInteger(R.integer.workstype_common));

        if (null == mSongInfo)
            return;

        songworkstype = EmptyUtils.isEmpty(mSongInfo.getWorks_type()) ?
                0 : Integer.parseInt(mSongInfo.getWorks_type());
        isFirstPerson = getResources().getInteger(R.integer.workstype_chrousother) != mychorustype;

        initializeInterface();
        mPresenter.requestData(mSongInfo);
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

    /**
     * 初始化界面
     */
    private void initializeInterface() {
        llChorusfirst.setVisibility(isFirstPerson ? View.VISIBLE : View.GONE);
        llChorussecond.setVisibility(isFirstPerson ? View.GONE : View.VISIBLE);

        //如果已经合唱了一部分，那么更改提示

        if (getResources().getInteger(R.integer.workstype_chrousa) == songworkstype) {//已经演唱了红色部分
            tvSecondchorustip.setText(getString(R.string.selectrole_chorustip_blue));
            tvStartchorus.setBackgroundResource(R.drawable.shape_bg_selectrole_blue);
        } else if (getResources().getInteger(R.integer.workstype_chrousb) == songworkstype) {//已经演唱了蓝色部分
            tvSecondchorustip.setText(getString(R.string.selectrole_chorustip_red));
            tvStartchorus.setBackgroundResource(R.drawable.shape_bg_selectrole_red);
        }
    }

    @OnClick({R.id.tv_chorusred, R.id.tv_chorusblue, R.id.tv_startchorus})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.tv_chorusred://合唱红色部分
                mychorustype = getResources().getInteger(R.integer.workstype_chrousa);
                break;
            case R.id.tv_chorusblue://合唱蓝色部分
                mychorustype = getResources().getInteger(R.integer.workstype_chrousb);
                break;
            case R.id.tv_startchorus://开始合唱
                mychorustype = getResources().getInteger(R.integer.workstype_chrousother);
                break;
        }

        Bundle bundle = new Bundle();
        bundle.putParcelable("songinfo", mSongInfo);
        bundle.putInt("workstype", mychorustype);

        Intent intent;
        if (needmv) {
            if (isFirstPerson) {
                intent = new Intent(this, MvPreviewFirstActivity.class);
            } else {
                intent = new Intent(this, MvPreviewSecondActivity.class);
            }
        } else {
            intent = new Intent(mWeApplication, KSingActivity.class);
        }
        intent.putExtras(bundle);
        launchActivity(intent);
        killMyself();
    }

    @Override
    public void updateLrc(List<LrcRow> lrcRowList) {
        if (EmptyUtils.isNotEmpty(lrcRowList)) {
            lrcview.setLrcRows(lrcRowList);
        } else {
            lrcview.reset();
            Timber.e("解析歌词异常：lrcRowList = null");
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

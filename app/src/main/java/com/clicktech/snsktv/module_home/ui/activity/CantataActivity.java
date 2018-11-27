package com.clicktech.snsktv.module_home.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.ConvertUtils;
import com.clicktech.snsktv.arms.utils.TimeUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.EventBusTag;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.entity.ScoreResultEntity;
import com.clicktech.snsktv.module_home.contract.CantataContract;
import com.clicktech.snsktv.module_home.inject.component.DaggerCantataComponent;
import com.clicktech.snsktv.module_home.inject.module.CantataModule;
import com.clicktech.snsktv.module_home.presenter.CantataPresenter;
import com.clicktech.snsktv.widget.titlebar.HeaderView;
import com.jaeger.library.StatusBarUtil;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;

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
 * Created by Administrator on 2017-06-30.
 * 清唱
 */

public class CantataActivity extends WEActivity<CantataPresenter> implements CantataContract.View
        , HeaderView.OnCustomTileListener {


    @BindView(R.id.headerview)
    HeaderView mHeaderView;
    @BindView(R.id.tv_curtime)
    TextView tvCurtime;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_totaltime)
    TextView tvTotaltime;
    @BindView(R.id.tv_recordtime)
    TextView tvRecordtime;
    @BindView(R.id.tv_playstate)
    TextView tvPlaystate;
    @BindView(R.id.tv_mppaused)
    TextView tvMppaused;
    @BindView(R.id.tv_tone)
    TextView tvTone;
    @BindView(R.id.tv_reset)
    TextView tvReset;
    @BindView(R.id.tv_over)
    TextView tvOver;
    @BindView(R.id.tv_countdown)
    TextView tvCountDown;
    @BindView(R.id.rl_cantata_status)
    RelativeLayout rlCantataStatus;

    private RxPermissions mRxPermissions;
    private int cantataTime;//清唱时长限制（单位:毫秒）

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions(this);
        DaggerCantataComponent
                .builder()
                .appComponent(appComponent)
                .cantataModule(new CantataModule(this)) //请将CantataModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_cantata, null, false);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucent(this,
                getResources().getInteger(R.integer.statusalpha_a));
    }

    @Override
    protected void initData() {
        JZVideoPlayer.releaseAllVideos();
        EventBus.getDefault().post(new Message(), EventBusTag.MINIPLAYER_HIDE);
        mPresenter.examinePermission();
        mHeaderView.setTitleClickListener(this);

        cantataTime = getResources().getInteger(R.integer.cantata_upperlimit) * 1000;
        String timeLimit = TimeUtils.millis2String(cantataTime, "mm:ss");
        tvTotaltime.setText(timeLimit);
        progressBar.setMax(cantataTime);
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
    protected void onPause() {
        super.onPause();
        mPresenter.onPauseRecord();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResumeRecord();
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public void updateCountDown(int count) {
        if (0 == count) {
            tvCountDown.setVisibility(View.GONE);
            rlCantataStatus.setVisibility(View.VISIBLE);
            return;
        }
        tvCountDown.setText(String.valueOf(count));
    }

    @Override
    public void updateRecodingTime(int time) {

        progressBar.setProgress(time);
        String recordTime = TimeUtils.millis2String(time, "mm:ss");
        tvCurtime.setText(recordTime);
        tvRecordtime.setText(recordTime);

        if (time >= cantataTime) {
            mPresenter.stopRecording();
        }
    }

    @Override
    public void updateReordingStatus(boolean status) {
        if (status) {
            tvPlaystate.setText(R.string.cantata_pause);
            tvMppaused.setText(R.string.cantata_pause);
        } else {
            tvPlaystate.setText(R.string.cantata_recording);
            tvMppaused.setText(R.string.cantata_play);
        }
    }

    @Override
    public void turn2Publish(String recordPath, String accompanyPath) {
        Intent tobeAnnoIntent = new Intent(this, ToBeAnnounceActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("songinfo", mPresenter.getSongInfoBean());
        bundle.putInt("workstype", getResources().getInteger(R.integer.workstype_cantata));
        bundle.putString("audiopath", recordPath);
        bundle.putString("accompanypath", accompanyPath);
        bundle.putParcelable("scoreresult", new ScoreResultEntity());
        tobeAnnoIntent.putExtras(bundle);
        launchActivity(tobeAnnoIntent);
        killMyself();
    }

    @OnClick(R.id.tv_mppaused)
    public void onTvMppausedClicked() {
        mPresenter.playPauseRecording();
    }

    @OnClick(R.id.tv_tone)
    public void onTvToneClicked() {
        mPresenter.setRisingFallingTone(this, tvTone);
    }

    @OnClick(R.id.tv_reset)
    public void onTvResetClicked() {
        mPresenter.showTipDialog(this, getString(R.string.dialog_cantata_resetsong), 2);
    }

    @OnClick(R.id.tv_over)
    public void onTvOverClicked() {

        String recordtime = tvRecordtime.getText().toString();
        long time = ConvertUtils.mmssString2millis(recordtime);
        int lowerlimit = getResources().getInteger(R.integer.cantata_lowerlimit) * 1000;
        if (time < lowerlimit) {
            showMessage(getString(R.string.error_recordmvtime));
            return;
        }

        mPresenter.showTipDialog(this, getString(R.string.dialog_cantata_finish), 3);
    }

    @Override
    public void setTitleLeftClick() {
        mPresenter.showTipDialog(this, getString(R.string.dialog_cantata_back), 4);
    }

    @Override
    public void setTitleRightClick() {
    }


}

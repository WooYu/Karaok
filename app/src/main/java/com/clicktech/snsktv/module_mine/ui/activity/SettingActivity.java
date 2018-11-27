package com.clicktech.snsktv.module_mine.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.clicktech.snsktv.BuildConfig;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.CacheUtils;
import com.clicktech.snsktv.arms.utils.DeviceUtils;
import com.clicktech.snsktv.arms.utils.FileUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.EventBusTag;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.entity.VersionMessageResponse;
import com.clicktech.snsktv.module_enter.ui.activity.LoginActivity;
import com.clicktech.snsktv.module_mine.contract.SettingContract;
import com.clicktech.snsktv.module_mine.inject.component.DaggerSettingComponent;
import com.clicktech.snsktv.module_mine.inject.module.SettingModule;
import com.clicktech.snsktv.module_mine.presenter.SettingPresenter;
import com.clicktech.snsktv.util.StringHelper;
import com.clicktech.snsktv.util.transformations.CircleWithBorderTransformation;
import com.clicktech.snsktv.widget.titlebar.HeaderView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.io.File;

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
 * Created by Administrator on 2017/2/4.
 * 设置
 */

public class SettingActivity extends WEActivity<SettingPresenter> implements
        SettingContract.View, View.OnClickListener,
        HeaderView.OnCustomTileListener {

    static final String[] PERMISSION = new String[]{
            Manifest.permission.READ_CONTACTS,// 写入权限
            Manifest.permission.READ_EXTERNAL_STORAGE,  //读取权限
            Manifest.permission.WRITE_CALL_LOG,        //读取设备信息
    };
    private static final int ReqeustCode_NetRemind = 0x0003;
    private static final int RequestCode_ReceiveMsg = 0x0006;
    public TextView mProgress;
    @BindView(R.id.headerview)
    HeaderView headerView;
    @BindView(R.id.signout)
    Button signOut;
    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.tv_receive_msg)
    TextView tvReceiveMsg;
    @BindView(R.id.tv_netremind)
    TextView tvNetRemind;
    @BindView(R.id.tv_cachesize)
    TextView tvCacheSize;
    @BindView(R.id.tv_accredit)
    TextView tvAccredit;
    private TextView update;
    private ProgressBar progressBar;
    private PopupWindow updateWindow;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerSettingComponent
                .builder()
                .appComponent(appComponent)
                .settingModule(new SettingModule(this)) //请将SettingModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_setting, null, false);
    }

    @Override
    protected void initData() {
        headerView.setTitleClickListener(this);
        initUpdatePopuwindow();
        initUserInfo();
    }

    private void initUserInfo() {
        if (null == mWeApplication.getUserInfoBean()) {
            return;
        }

        //授权模式
        int accreditMode = StringHelper.getLoginMode(mWeApplication.getUserInfoBean());
        tvAccredit.setText(accreditMode);

        //头像
        mWeApplication.getAppComponent().imageLoader().loadImage(this,
                GlideImageConfig.builder()
                        .transformation(new CircleWithBorderTransformation(this))
                        .url(mWeApplication.getUserInfoBean().getUser_photo())
                        .errorPic(R.mipmap.def_avatar_round)
                        .placeholder(R.mipmap.def_avatar_round)
                        .imageView(avatar)
                        .build());
        //昵称
        name.setText(mWeApplication.getUserInfoBean().getUser_nickname());
        //缓存
        try {
            tvCacheSize.setText(CacheUtils.getCacheSize(FileUtils
                    .getCacheDirectory(this, Environment.DIRECTORY_MUSIC)));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
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

    @OnClick({R.id.ll_acceptmsg, R.id.ll_network, R.id.signout, R.id.rl_feekback, R.id.mine_info,
            R.id.msg_receive_setting, R.id.search_friends, R.id.about_our, R.id.rl_clearcache,
            R.id.rl_updates})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_clearcache://清除缓存
                CacheUtils.cleanExternalCache(mWeApplication);
                CacheUtils.cleanInternalCache(mWeApplication);
                tvCacheSize.setText("0KB");
                break;
            case R.id.msg_receive_setting://接受消息
                Intent msgIntent = new Intent(mWeApplication, MsgReceiveActivity.class);
                startActivityForResult(msgIntent, RequestCode_ReceiveMsg);
                break;
            case R.id.ll_network://网络
                onClickedNetwork();
                break;
//            case R.id.ll_wipecache://清除缓存
//                UiUtils.startActivity(WipeCacheActivity.class);
////                break;
            case R.id.rl_feekback://意见反馈
                UiUtils.startActivity(FeedBackActivity.class);
                break;
            case R.id.rl_updates:  //检查版本
                mPresenter.getVersionMessage(RequestParams_Maker.getCheckVersionRequest());
                break;
            case R.id.signout:    // 退出
                mPresenter.signOut(RequestParams_Maker.getLoginOutReqeust(mWeApplication.getUserID()));
                break;
            case R.id.mine_info://个人资料
                if (mWeApplication.getUserInfoBean() != null) {
                    UiUtils.startActivity(DatumEditActivity.class);
                } else {
                    UiUtils.startActivity(LoginActivity.class);
                }
                break;
            case R.id.search_friends:    //搜索用户
                Intent intent = new Intent(SettingActivity.this, SearchUserActivity.class);
                UiUtils.startActivity(intent);
                break;
            case R.id.about_our:
                UiUtils.startActivity(AboutOurActivity.class);
                break;
        }
    }

    @Override
    public void setTitleLeftClick() {
        finish();
    }

    @Override
    public void setTitleRightClick() {

    }

    /**
     * 设置Android6.0的权限申请
     */
    private void setPermissions() {
        if (ContextCompat.checkSelfPermission(mWeApplication, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //Android 6.0申请权限
            ActivityCompat.requestPermissions(this, PERMISSION, 1);
        } else {
            Log.i(TAG, "权限申请ok");
        }
    }

    @Override
    public void checkVersion(VersionMessageResponse message) {

        if (message == null)
            return;

        if (String.valueOf(message.getVersion().getApp_version()).equals(DeviceUtils.getVersionCode(mWeApplication))) {    //  版本号相同无最新版本

            Timber.e("版本号相同，无新版本");

        } else {
            updateWindow.showAsDropDown(headerView);
            OkGo.<File>get(BuildConfig.APP_DOMAIN_File + message.getVersion().getApp_url())
                    .tag(this)
                    .execute(new FileCallback("kmusic.apk") {
                        @Override
                        public void onStart(Request<File, ? extends Request> request) {
                            super.onStart(request);
                            update.setText("正在下载中");
                        }

                        @Override
                        public void onSuccess(Response<File> response) {

                            File file2 = new File(response.body().getPath());
                            try {
                                installLoadedApkFile(file2);
                            } catch (Exception e) {
                                Timber.e(e);
                            }
                            update.setText("下载完成");
                        }

                        @Override
                        public void downloadProgress(Progress progress) {
                            super.downloadProgress(progress);
                            mProgress.setText(progress.fraction + "%");
                            progressBar.setMax(100);
                            progressBar.setProgress((int) (progress.fraction * 100));
                        }

                        @Override
                        public void onError(Response<File> response) {
                            super.onError(response);
                            update.setText("网络异常下载出错");
                        }
                    });
        }
    }

    @Override
    public void exitCurrentAccount() {
        EventBus.getDefault().post(new Message(), EventBusTag.LOGOUTSUCCESS);
        killMyself();
    }

    private void initUpdatePopuwindow() {
        View view = LayoutInflater.from(mWeApplication).inflate(R.layout.popwindow_update_version, null);
        update = (TextView) view.findViewById(R.id.tv_update);
        update.setText("开始下载最新版本");
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        mProgress = (TextView) view.findViewById(R.id.tv_progress);

        updateWindow = new PopupWindow(view);
        updateWindow.setFocusable(true);
        updateWindow.setOutsideTouchable(false);
        updateWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        updateWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        ColorDrawable dw = new ColorDrawable(0000000000);
        updateWindow.setBackgroundDrawable(dw);
        updateWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
    }


    /**
     * 打开下载完之后的apk
     *
     * @param file
     */
    private void installLoadedApkFile(File file) {
//        Intent installIntent = new Intent();
//        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        installIntent.setAction(Intent.ACTION_VIEW);
//        installIntent.setDataAndType(Uri.fromFile(file), "applicationnd.android.package-archive");
//        mWeApplication.startActivity(installIntent);
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setAction(Intent.ACTION_VIEW);
            Uri contentUri = FileProvider.getUriForFile(mWeApplication, BuildConfig.APPLICATION_ID + ".fileProvider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
        }
//        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.fromFile(file),
//                "application/vnd.android.package-archive");
        startActivity(intent);
    }

    //切换网络提醒
    private void onClickedNetwork() {
        Intent networkRemindIntent = new Intent(mWeApplication, NetworkRemindActivity.class);
        startActivityForResult(networkRemindIntent, ReqeustCode_NetRemind);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ReqeustCode_NetRemind && Activity.RESULT_OK == resultCode) {
            String typeOfNetRemind = data.getStringExtra("netremind");
            tvNetRemind.setText(typeOfNetRemind);
        }

        if (requestCode == RequestCode_ReceiveMsg && Activity.RESULT_OK == resultCode) {
            String explain = data.getStringExtra("msgtype");
            tvReceiveMsg.setText(explain);
        }
    }

    @Subscriber(tag = EventBusTag.CHANGEAVATAR, mode = ThreadMode.MAIN)
    public void eventChangeAvatar(Message message) {
        String photourl = mWeApplication.getUserInfoBean().getUser_photo();
        mWeApplication.getAppComponent().imageLoader().loadImage(this,
                GlideImageConfig.builder()
                        .transformation(new CircleWithBorderTransformation(this))
                        .url(photourl)
                        .errorPic(R.mipmap.def_avatar_round)
                        .placeholder(R.mipmap.def_avatar_round)
                        .imageView(avatar)
                        .build());
    }

}

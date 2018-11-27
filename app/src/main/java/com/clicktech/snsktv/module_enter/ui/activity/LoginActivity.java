package com.clicktech.snsktv.module_enter.ui.activity;

import android.content.Intent;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.EventBusTag;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.entity.UserInfoBean;
import com.clicktech.snsktv.module_enter.contract.LoginContract;
import com.clicktech.snsktv.module_enter.inject.component.DaggerLoginComponent;
import com.clicktech.snsktv.module_enter.inject.module.LoginModule;
import com.clicktech.snsktv.module_enter.presenter.LoginPresenter;
import com.clicktech.snsktv.util.sharesdklogin.LoginApi;
import com.clicktech.snsktv.util.sharesdklogin.OnLoginListener;
import com.clicktech.snsktv.util.sharesdklogin.ThirdUserInfo;
import com.clicktech.snsktv.util.sharesdklogin.Tool;

import org.simple.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.line.Line;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.twitter.Twitter;
import cn.sharesdk.wechat.friends.Wechat;

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
 * Created by Administrator on 2017/1/30.
 * 登录
 */

public class LoginActivity extends WEActivity<LoginPresenter> implements
        LoginContract.View {

    @BindView(R.id.language_nonchinese)
    LinearLayout languageNonchinese;
    @BindView(R.id.language_chinese)
    LinearLayout languageChinese;
    @BindView(R.id.tv_protocol)
    TextView tvProtocol;

    private String thirdtype = "";
    private String loginmode = "";//登录模式
    private Platform mPlatform;//选择登录的第三方平台

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerLoginComponent
                .builder()
                .appComponent(appComponent)
                .loginModule(new LoginModule(this)) //请将LoginModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_login, null, false);
    }

    @Override
    protected void initData() {
        judgeLanguageEnvironment();
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
    public void turn2Main(UserInfoBean infoEntity) {
        EventBus.getDefault().post(new Message(), EventBusTag.LOGIN_SUCCESS);
        getIntent().putExtra("islogin", true);
        setResult(RESULT_OK, getIntent());
        killMyself();
    }

    @Override
    public void turn2Regist() {
        if (null == mPlatform) {
            return;
        }

        PlatformDb platformDb = mPlatform.getDb();
        ThirdUserInfo thirdUserInfo = new ThirdUserInfo();
        thirdUserInfo.setExpiresin(platformDb.getExpiresIn());
        thirdUserInfo.setExpirestime(platformDb.getExpiresTime());
        thirdUserInfo.setGender(platformDb.getUserGender());
        thirdUserInfo.setPlatformnname(platformDb.getPlatformNname());
        thirdUserInfo.setPlatformversion(platformDb.getPlatformVersion());
        thirdUserInfo.setToken(platformDb.getToken());
        thirdUserInfo.setSecret(platformDb.getTokenSecret());
        thirdUserInfo.setUsericon(platformDb.getUserIcon());
        thirdUserInfo.setUserid(platformDb.getUserId());
        thirdUserInfo.setUsername(platformDb.getUserName());
        thirdUserInfo.setThirdtype(thirdtype);
        thirdUserInfo.setLoginmode(loginmode);

        Intent intent = new Intent(this, RegistUserActivity.class);
        intent.putExtra("thirdinfo", thirdUserInfo);
        UiUtils.startActivity(intent);
    }

    @OnClick(R.id.tv_protocol)
    public void onProtocolClicked() {
        Intent intent = new Intent(this, ProtocolActivity.class);
        launchActivity(intent);
    }

    @OnClick(R.id.login_facebook)
    public void onLoginFacebookClicked() {
        login(Facebook.NAME, R.string.thirdtype_fb, R.string.loginmode_fb);
    }

    @OnClick(R.id.login_tw)
    public void onLoginTwClicked() {
        login(Twitter.NAME, R.string.thirdtype_tw, R.string.loginmode_tw);
    }

    @OnClick(R.id.login_line)
    public void onLoginLineClicked() {
        login(Line.NAME, R.string.thirdtype_ln, R.string.loginmode_ln);
    }

    @OnClick(R.id.login_yahu)
    public void onLoginYahuClicked() {
        thirdtype = getString(R.string.thirdtype_yh);
        loginmode = getString(R.string.loginmode_yh);
        Intent intent = new Intent(this, RegistUser_YaHooActivity.class);
        startActivity(intent);

    }

    @OnClick(R.id.login_qq)
    public void onLoginQqClicked() {
        login(QQ.NAME, R.string.thirdtype_qq, R.string.loginmode_qq);
    }

    @OnClick(R.id.login_wx)
    public void onLoginWxClicked() {
        login(Wechat.NAME, R.string.thirdtype_wx, R.string.loginmode_wx);
    }

    @OnClick(R.id.login_sina)
    public void onLoginSinaClicked() {
        login(SinaWeibo.NAME, R.string.thirdtype_wb, R.string.loginmode_wb);
    }

    @OnClick(R.id.login_more)
    public void onLoginMoreClicked() {
        if (languageChinese.isShown()) {
            languageChinese.setVisibility(View.GONE);
            languageNonchinese.setVisibility(View.VISIBLE);
        } else {
            languageChinese.setVisibility(View.VISIBLE);
            languageNonchinese.setVisibility(View.GONE);
        }
    }

    //判断语言环境
    private void judgeLanguageEnvironment() {
        String languagetype = mWeApplication.getLocaleCode();
        if (languagetype.equals(getString(R.string.language_china))) {
            languageNonchinese.setVisibility(View.GONE);
            languageChinese.setVisibility(View.VISIBLE);
        } else {
            languageChinese.setVisibility(View.GONE);
            languageNonchinese.setVisibility(View.VISIBLE);
        }
    }

    //登录到指定平台
    private void login(String platformname, int type, int mode) {
        thirdtype = getString(type);
        loginmode = getString(mode);
        mPlatform = ShareSDK.getPlatform(platformname);
        if (!Tool.canGetUserInfo(mPlatform)) {
            showMessage(getString(R.string.error_getuserinfo));
            return;
        }

        LoginApi api = new LoginApi();
        //设置登陆的平台后执行登陆的方法
        api.setPlatform(mPlatform.getName());
        api.setOnLoginListener(new ThirdLoginListener(mPresenter, mPlatform, thirdtype));
//        api.setOnLoginListener(new OnLoginListener() {
//            public boolean onLogin(String platform, HashMap<String, Object> res) {
//                //获取到用户资料后的回调
//                String thirdUserid = mPlatform.getDb().getUserId();
//                mPresenter.doLogin(thirdtype, thirdUserid);
//                return true;
//            }
//
//            public boolean onRegister(ThirdUserInfo info) {
//                // 填写处理注册信息的代码，返回true表示数据合法，注册页面可以关闭
//                return true;
//            }
//        });
        api.login(mWeApplication);
    }

    //防止内存泄漏的写法
    private static class ThirdLoginListener implements OnLoginListener {
        private WeakReference<Platform> mLoginPlatform;
        private WeakReference<LoginPresenter> mLoginPresenter;
        private WeakReference<String> mLoginType;

        public ThirdLoginListener(LoginPresenter presenter, Platform platform, String thirdtype) {
            mLoginPresenter = new WeakReference<LoginPresenter>(presenter);
            mLoginPlatform = new WeakReference<Platform>(platform);
            mLoginType = new WeakReference<String>(thirdtype);
        }

        @Override
        public boolean onLogin(String platform, HashMap<String, Object> res) {
            //获取到用户资料后的回调
            String thirdUserid = mLoginPlatform.get().getDb().getUserId();
            mLoginPresenter.get().doLogin(mLoginType.get(), thirdUserid);
            return true;
        }

        @Override
        public boolean onRegister(ThirdUserInfo info) {
            return true;
        }
    }

}

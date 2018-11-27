package com.clicktech.snsktv.common;

import android.content.Context;

import com.clicktech.snsktv.BuildConfig;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseApplication;
import com.clicktech.snsktv.arms.http.GlobeHttpHandler;
import com.clicktech.snsktv.arms.inject.module.GlobeConfigModule;
import com.clicktech.snsktv.arms.utils.DataHelper;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.EncryptUtils;
import com.clicktech.snsktv.arms.utils.TimeUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.common.inject.component.DaggerAppComponent;
import com.clicktech.snsktv.common.inject.module.CacheModule;
import com.clicktech.snsktv.common.inject.module.ServiceModule;
import com.clicktech.snsktv.entity.AuthEntity;
import com.clicktech.snsktv.entity.UserInfoBean;
import com.clicktech.snsktv.rxerrorhandler.handler.listener.ResponseErroListener;
import com.clicktech.snsktv.widget.imagepicker.GlideImageLoader;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okgo.OkGo;
import com.mob.MobSDK;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

import static com.clicktech.snsktv.arms.utils.ConstUtils.TimeUnit.MSEC;

/**
 * Created by Administrator on 2016/12/20.
 */

public class KtvApplication extends BaseApplication {
    public static KtvApplication ktvApplication = null;
    private AppComponent mAppComponent;
    private RefWatcher mRefWatcher;//leakCanary观察器
    private UserInfoBean userInfoBean;

    /**
     * 获得leakCanary观察器
     *
     * @param context
     * @return
     */
    public static RefWatcher getRefWatcher(Context context) {
        KtvApplication application = (KtvApplication) context.getApplicationContext();
        return application.mRefWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ktvApplication = this;
        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(getAppModule())//baseApplication提供
                .clientModule(getClientModule())//baseApplication提供
                .imageModule(getImageModule())//baseApplication提供
                .globeConfigModule(getGlobeConfigModule())//全局配置
                .serviceModule(new ServiceModule())//需自行创建
                .cacheModule(new CacheModule())//需自行创建
                .build();

        if (BuildConfig.LOG_DEBUG) {//Timber日志打印
            Timber.plant(new Timber.DebugTree());
        }

        installLeakCanary();//leakCanary内存泄露检查
        initOkGo();
        initJpush();
        initBugly();
        initShareSDk();
        initImagePicker();
    }

    private void initShareSDk() {
        // 通过代码注册你的AppKey和AppSecret
        MobSDK.init(this, "20745560b0faf", "c9eaf0561047644a809aa17b909b2762");
    }

    private void initJpush() {
        JPushInterface.setDebugMode(BuildConfig.LOG_DEBUG);
        JPushInterface.init(this);
    }

    //将AppComponent返回出去,供其它地方使用, AppComponent接口中声明的方法返回的实例,
    // 在getAppComponent()拿到对象后都可以直接使用
    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    /**
     * 安装leakCanary检测内存泄露
     */
    protected void installLeakCanary() {
        this.mRefWatcher = BuildConfig.USE_CANARY ? LeakCanary.install(this) :
                RefWatcher.DISABLED;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mAppComponent != null)
            this.mAppComponent = null;
        if (mRefWatcher != null)
            this.mRefWatcher = null;
    }

    /**
     * 初始化OKGO
     */
    private void initOkGo() {
        OkGo.getInstance().init(this);
    }

    /**
     * 初始化bugly
     */
    private void initBugly() {
        CrashReport.initCrashReport(getApplicationContext(), "3043bb7ee9", true);
    }

    //初始化图片选择器
    public ImagePicker initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setShowCamera(true);
        imagePicker.setCrop(true);
        imagePicker.setSaveRectangle(true);
        imagePicker.setSelectLimit(1);
        imagePicker.setStyle(CropImageView.Style.CIRCLE);
        imagePicker.setFocusHeight(500);
        imagePicker.setFocusWidth(500);
        imagePicker.setOutPutX(800);
        imagePicker.setOutPutY(800);
        return imagePicker;
    }

    //**********************TODO：设置Auth\Sign的代码放到ApiModule中*****************

    /**
     * 校准时间
     *
     * @param time_stamp 服务器当前时间秒，用来统计手机与服务器的时间差
     */
    private void adjustApiTime(String time_stamp) {
        //计算差值，存到SF
        long diffTime = TimeUtils.getTimeSpanByNow(Long.parseLong(time_stamp) * 1000, MSEC);
        DataHelper.setLongSF(this, ConstantConfig.TIME_DIFF, diffTime);
    }

    /**
     * 访问接口时获取时间（单位：秒）
     *
     * @return
     */
    private String getApiTime() {
        long diffTime = DataHelper.getLongSF(this, ConstantConfig.TIME_DIFF);
        if (diffTime == -1) {
            Timber.e("未获取到时间差值:diffTime = %d", diffTime);
            return String.valueOf(TimeUtils.getNowTimeMills() / 1000);
        }
        return String.valueOf(TimeUtils.getTimeSpanByNow(diffTime, MSEC) / 1000);

    }

    /**
     * 获取登录状态
     *
     * @return
     */
    public boolean loggingStatus() {
        return !getString(R.string.userid_notlogin).equals(getUserID());
    }

    /**
     * 获取用户ID
     *
     * @return
     */
    public String getUserID() {
        if (null == getUserInfoBean()) {//用户未登陆
            return getString(R.string.userid_notlogin);
        } else {//用户已登陆
            return getUserInfoBean().getId();
        }
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public UserInfoBean getUserInfoBean() {
        if (null == userInfoBean) {
            userInfoBean = DataHelper.getDeviceData(this, ConstantConfig.USERINFO);
            if (null == userInfoBean)
                Timber.e("获取用户信息异常");
        }
        return userInfoBean;
    }

    /**
     * 登陆成功后，设置用户信息
     *
     * @param userInfoBean
     */
    public void setUserInfoBean(UserInfoBean userInfoBean) {
        this.userInfoBean = userInfoBean;
        if (null != userInfoBean)
            DataHelper.saveDeviceData(this, ConstantConfig.USERINFO, userInfoBean);
    }

    /**
     * 获取网络访问时的Auth信息
     */
    public String getApiAuth() {
        AuthEntity authEntity = mAppComponent.authManager();
        authEntity.setTime_stamp(getApiTime());
        authEntity.setUser_id(getUserID());
        String auth = mAppComponent.gson().toJson(authEntity);
        return auth;
    }

    /**
     * 获取请求的Sign
     *
     * @param auth
     * @return
     */
    public String getApiSign(String auth) {
        String token = DataHelper.getStringSF(this, ConstantConfig.TOKEN);
        String signStr = auth + token;
        return EncryptUtils.encryptMD5ToString(signStr).toLowerCase();
    }

    //获取语言环境代码
    public String getLocaleCode() {
        String language = "";
        if (null != getUserInfoBean()) {
            language = getUserInfoBean().getLanguage();
        }
        if (EmptyUtils.isEmpty(language)) {
            language = Locale.getDefault().getLanguage();
        } else {
            int languageType = Integer.parseInt(language);
            if (getResources().getInteger(R.integer.languagecode_cn) == languageType) {
                language = getString(R.string.language_china);
            } else if (getResources().getInteger(R.integer.languagecode_en) == languageType) {
                language = getString(R.string.language_english);
            } else {
                language = getString(R.string.language_japan);
            }
        }
        return language;
    }
    //***************************************

    /**
     * app的全局配置信息封装进module(使用Dagger注入到需要配置信息的地方)
     *
     * @return
     */
    @Override
    protected GlobeConfigModule getGlobeConfigModule() {
        return GlobeConfigModule
                .buidler()
                .baseurl(BuildConfig.APP_DOMAIN)
                .globeHttpHandler(new GlobeHttpHandler() {// 这里可以提供一个全局处理http响应结果的处理类,
                    // 这里可以比客户端提前一步拿到服务器返回的结果,可以做一些操作,比如token超时,重新获取
                    @Override
                    public Response onHttpResultResponse(String httpResult, Interceptor.Chain chain,
                                                         Response response) {

                        //这里可以先客户端一步拿到每一次http请求的结果,可以解析成json,做一些操作,如检测到token过期后
                        try {
                            JSONObject jsonObject = new JSONObject(httpResult);
                            if (jsonObject.has("time_stamp")) {
                                String time_stamp = jsonObject.getString("time_stamp");
                                adjustApiTime(time_stamp);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return response;
                    }

                    // 这里可以在请求服务器之前可以拿到request,做一些操作比如给request统一添加token或者header
                    @Override
                    public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
                        //如果需要再请求服务器之前做一些操作,则重新返回一个做过操作的的requeat如增加header,不做操作则返回request
                        //return chain.request().newBuilder().header("token", tokenId)
//                .build();
                        return request;
                    }
                })
                .responseErroListener(new ResponseErroListener() {
                    //     用来提供处理所有错误的监听
                    //     rxjava必要要使用ErrorHandleSubscriber(默认实现Subscriber的onError方法),此监听才生效
                    @Override
                    public void handleResponseError(Context context, Exception e) {
                        UiUtils.SnackbarText("net error");
                    }
                }).build();
    }
}

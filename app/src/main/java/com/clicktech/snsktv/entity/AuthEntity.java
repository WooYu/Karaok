package com.clicktech.snsktv.entity;

import android.app.Application;
import android.os.Build;

import com.clicktech.snsktv.arms.utils.DeviceUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Administrator on 2017/3/9.
 */
@Singleton
public class AuthEntity {

    /**
     * 客户端唯一标识
     */
    private String imei;
    /**
     * 操作系统名称
     */
    private String os;
    /**
     * 操作系统版本
     */
    private String os_version;
    /**
     * 软件身份标识
     */
    private String app_key;
    /**
     * APP版本
     */
    private String app_version;
    /**
     * 时间戳格式，精确到秒（本地时间秒值加上服务器与手机时间的差值）
     */
    private String time_stamp;
    /**
     * 用户标识
     */
    private String user_id;

    @Inject
    public AuthEntity(Application application) {
        this.imei = DeviceUtils.getIMEI(application);
        this.os = DeviceUtils.getPhoneType();
        this.os_version = Build.VERSION.RELEASE;
        this.app_key = DeviceUtils.getUniqueID();
        this.app_version = DeviceUtils.getVersionName(application);
//        this.user_id = KtvApplication.ktvApplication.getUserID();      //2017/4/17
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOs_version() {
        return os_version;
    }

    public void setOs_version(String os_version) {
        this.os_version = os_version;
    }

    public String getApp_key() {
        return app_key;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}

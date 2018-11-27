package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/2.
 */

public class UserInfoResponse extends BaseResponse implements Serializable {

    /**
     * msg : success
     * time_stamp : 1488423789
     * errCode : 0
     * thirdUser : {"add_time":"2017-03-01 19:11:34.108","del_status":1,"id":1,"is_lead":0,"language":"","latitude":"","login_count":0,"login_ip":"127.0.0.1","longitude":"","third_facebook":"","third_line":"","third_qq":"123456","third_twitter":"","third_wb":"","third_wx":"","third_yahoo":"","user_age":6,"user_birthday":"2010-10-10","user_district_dtl":"中国北京","user_district_id":"","user_nickname":"123456","user_photo":"","user_realname":"","user_sex":1}
     * token : 6027f020a2c0852920fe7937446bd2b4
     */

    private int time_stamp;
    private UserInfoBean thirdUser;
    private String token;
    private String needInfo;//是否需要先完成注册信息

    public String getNeedInfo() {
        return needInfo;
    }

    public void setNeedInfo(String needInfo) {
        this.needInfo = needInfo;
    }

    public int getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(int time_stamp) {
        this.time_stamp = time_stamp;
    }

    public UserInfoBean getThirdUser() {
        return thirdUser;
    }

    public void setThirdUser(UserInfoBean thirdUser) {
        this.thirdUser = thirdUser;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}

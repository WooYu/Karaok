package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

/**
 * Created by Administrator on 2016/12/26.
 */

public class MineUserInfoResponse extends BaseResponse {


    /**
     * 参数名	类型	说明
     * fansNum	int	粉丝数量
     * attentionNum	int	关注数量
     * worksNum	int	作品数量
     * user	int	用户信息
     */
    private UserInfoBean user;

    private String fansNum;
    private String attentionNum;
    private String worksNum;

    public UserInfoBean getUser() {
        return user;
    }

    public void setUser(UserInfoBean user) {
        this.user = user;
    }

    public String getFansNum() {
        return fansNum;
    }

    public void setFansNum(String fansNum) {
        this.fansNum = fansNum;
    }

    public String getAttentionNum() {
        return attentionNum;
    }

    public void setAttentionNum(String attentionNum) {
        this.attentionNum = attentionNum;
    }

    public String getWorksNum() {
        return worksNum;
    }

    public void setWorksNum(String worksNum) {
        this.worksNum = worksNum;
    }
}

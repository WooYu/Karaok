package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2016/12/26.
 * 我的--关注 列表
 */

public class Att_Fans_UserListResponse extends BaseResponse {


    private List<Att_Fans_UserEntity> attentions;//关注的
    private List<Att_Fans_UserEntity> fans;//粉丝

    public List<Att_Fans_UserEntity> getAttentions() {
        return attentions;
    }

    public void setAttentions(List<Att_Fans_UserEntity> attentions) {
        this.attentions = attentions;
    }

    public List<Att_Fans_UserEntity> getFans() {
        return fans;
    }

    public void setFans(List<Att_Fans_UserEntity> fans) {
        this.fans = fans;
    }
}

package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2016/12/26.
 * 我的--关注 列表
 */

public class Att_ListResponse extends BaseResponse {


    private List<MineAttentEntity> list;

    public List<MineAttentEntity> getList() {
        return list;
    }

    public void setList(List<MineAttentEntity> list) {
        this.list = list;
    }
}

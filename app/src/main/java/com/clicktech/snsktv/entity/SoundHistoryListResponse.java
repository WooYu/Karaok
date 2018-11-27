package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2016/12/26.
 * 我的--已点伴奏
 */

public class SoundHistoryListResponse extends BaseResponse {


    private List<SoundHistoryEntity> list;

    public List<SoundHistoryEntity> getList() {
        return list;
    }

    public void setList(List<SoundHistoryEntity> list) {
        this.list = list;
    }
}

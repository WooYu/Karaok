package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2016/12/26.
 * 我的--播放历史
 */

public class ListenHistoryListResponse extends BaseResponse {


    private List<SongInfoBean> list;

    public List<SongInfoBean> getList() {
        return list;
    }

    public void setList(List<SongInfoBean> list) {
        this.list = list;
    }
}

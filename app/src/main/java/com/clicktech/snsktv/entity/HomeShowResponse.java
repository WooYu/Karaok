package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/4/6.
 */

public class HomeShowResponse extends BaseResponse {

    private List<SongInfoBean> worksInfoList;

    public List<SongInfoBean> getWorksInfoList() {
        return worksInfoList;
    }

    public void setWorksInfoList(List<SongInfoBean> worksInfoList) {
        this.worksInfoList = worksInfoList;
    }
}

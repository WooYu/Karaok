package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by wy201 on 2018-02-02.
 * 创建专辑选择作品列表
 */

public class WorksForAlbumResponse extends BaseResponse {


    private List<SongInfoBean> worksList;

    public List<SongInfoBean> getWorksList() {
        return worksList;
    }

    public void setWorksList(List<SongInfoBean> worksList) {
        this.worksList = worksList;
    }

}

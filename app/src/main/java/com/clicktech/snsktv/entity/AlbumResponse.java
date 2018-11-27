package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/1/22.
 * 专辑
 */

public class AlbumResponse extends BaseResponse {


    private List<AlbumBean> worksAlbumList;

    public List<AlbumBean> getWorksAlbumList() {
        return worksAlbumList;
    }

    public void setWorksAlbumList(List<AlbumBean> worksAlbumList) {
        this.worksAlbumList = worksAlbumList;
    }
}

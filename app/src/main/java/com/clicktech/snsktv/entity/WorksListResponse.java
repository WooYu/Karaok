package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 */

public class WorksListResponse extends BaseResponse {


    private List<WorkAlbumBean> albums;
    private List<SongInfoBean> works;

    public List<WorkAlbumBean> getAlbums() {
        return albums;
    }

    public void setAlbums(List<WorkAlbumBean> albums) {
        this.albums = albums;
    }

    public List<SongInfoBean> getWorks() {
        return works;
    }

    public void setWorks(List<SongInfoBean> works) {
        this.works = works;
    }
}

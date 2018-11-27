package com.clicktech.snsktv.entity;

/**
 * Created by Administrator on 2017/1/19.
 * 我的--关注-列表
 */

public class MineAttentEntity {

    private SongInfoBean workInfo;
    private WorkAlbumBean workAlbum;

    public WorkAlbumBean getWorkAlbum() {
        return workAlbum;
    }

    public void setWorkAlbum(WorkAlbumBean workAlbum) {
        this.workAlbum = workAlbum;
    }

    public SongInfoBean getWorkInfo() {
        return workInfo;
    }

    public void setWorkInfo(SongInfoBean workInfo) {
        this.workInfo = workInfo;
    }
}

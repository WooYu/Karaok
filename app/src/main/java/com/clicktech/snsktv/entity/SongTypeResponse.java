package com.clicktech.snsktv.entity;


import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2016/12/26.
 */

public class SongTypeResponse extends BaseResponse {

    private String backgroud;
    private List<SongTypeBeanEntity> songCategoryList;

    public List<SongTypeBeanEntity> getSongCategoryList() {
        return songCategoryList;
    }

    public void setSongCategoryList(List<SongTypeBeanEntity> songCategoryList) {
        this.songCategoryList = songCategoryList;
    }

    public String getBackgroud() {
        return backgroud;
    }

    public void setBackgroud(String backgroud) {
        this.backgroud = backgroud;
    }
}

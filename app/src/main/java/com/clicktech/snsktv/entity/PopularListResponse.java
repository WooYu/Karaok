package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2016/12/26.
 * 我的--热门 列表
 */

public class PopularListResponse extends BaseResponse {


    private List<SongInfoBean> popularSongs;

    public List<SongInfoBean> getPopularSongs() {
        return popularSongs;
    }

    public void setPopularSongs(List<SongInfoBean> popularSongs) {
        this.popularSongs = popularSongs;
    }
}

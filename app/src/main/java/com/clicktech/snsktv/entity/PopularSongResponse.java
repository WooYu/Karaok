package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/5/10.
 * 发现页面   人气歌曲-全部
 */

public class PopularSongResponse extends BaseResponse {


    private List<SongInfoBean> popularSongs;

    public List<SongInfoBean> getPopularSongs() {
        return popularSongs;
    }

    public void setPopularSongs(List<SongInfoBean> popularSongs) {
        this.popularSongs = popularSongs;
    }

}

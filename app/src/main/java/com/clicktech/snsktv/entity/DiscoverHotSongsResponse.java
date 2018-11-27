package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/1/19.
 * 人气歌曲排行榜
 */

public class DiscoverHotSongsResponse extends BaseResponse {

    private List<SongInfoBean> popularSongs;

    public List<SongInfoBean> getPopularSongs() {
        return popularSongs;
    }

    public void setPopularSongs(List<SongInfoBean> popularSongs) {
        this.popularSongs = popularSongs;
    }
}

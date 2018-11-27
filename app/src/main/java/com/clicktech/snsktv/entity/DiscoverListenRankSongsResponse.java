package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/1/19.
 * 试听排行榜（歌曲）
 */

public class DiscoverListenRankSongsResponse extends BaseResponse {

    private List<SongInfoBean> auditionCharts;

    public List<SongInfoBean> getAuditionCharts() {
        return auditionCharts;
    }

    public void setAuditionCharts(List<SongInfoBean> auditionCharts) {
        this.auditionCharts = auditionCharts;
    }
}

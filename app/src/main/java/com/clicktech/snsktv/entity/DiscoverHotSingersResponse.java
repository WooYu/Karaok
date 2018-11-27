package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/1/19.
 * 人气歌手排行榜
 */

public class DiscoverHotSingersResponse extends BaseResponse {

    private List<PopularSingerEntity> popularSingers;

    public List<PopularSingerEntity> getPopularSingers() {
        return popularSingers;
    }

    public void setPopularSingers(List<PopularSingerEntity> popularSingers) {
        this.popularSingers = popularSingers;
    }
}

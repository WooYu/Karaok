package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/1/19.
 * 学唱主页排行榜
 */

public class RankingResponse extends BaseResponse {

    List<PopularSingerEntity> popularSingerInCountry;

    public List<PopularSingerEntity> getPopularSingerInCountry() {
        return popularSingerInCountry;
    }

    public void setPopularSingerInCountry(List<PopularSingerEntity> popularSingerInCountry) {
        this.popularSingerInCountry = popularSingerInCountry;
    }
}

package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/1/19.
 * 学唱主页排行榜
 */

public class RichListResponse extends BaseResponse {

    List<PopularSingerEntity> fortuneList;

    public List<PopularSingerEntity> getFortuneList() {
        return fortuneList;
    }

    public void setFortuneList(List<PopularSingerEntity> fortuneList) {
        this.fortuneList = fortuneList;
    }
}

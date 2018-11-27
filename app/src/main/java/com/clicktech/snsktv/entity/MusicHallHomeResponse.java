package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/1/19.
 * k2音乐馆首页
 */

public class MusicHallHomeResponse extends BaseResponse {

    private List<BannersEntity> carouselFigureList;//轮播图

    public List<BannersEntity> getCarouselFigureList() {
        return carouselFigureList;
    }

    public void setCarouselFigureList(List<BannersEntity> carouselFigureList) {
        this.carouselFigureList = carouselFigureList;
    }
}

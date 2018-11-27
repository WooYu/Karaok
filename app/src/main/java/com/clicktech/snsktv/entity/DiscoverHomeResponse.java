package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/1/19.
 * 发现首页
 */

public class DiscoverHomeResponse extends BaseResponse {

    private List<BannersEntity> carouselKey;
    private List<PopularSingerEntity> popularSingers;
    private List<SongInfoBean> popularSongs;
    private List<PopularSingerEntity> list1;
    private List<PopularSingerEntity> list2;
    private List<PopularSingerEntity> list3;

    public List<BannersEntity> getCarouselKey() {
        return carouselKey;
    }

    public void setCarouselKey(List<BannersEntity> carouselKey) {
        this.carouselKey = carouselKey;
    }

    public List<PopularSingerEntity> getPopularSingers() {
        return popularSingers;
    }

    public void setPopularSingers(List<PopularSingerEntity> popularSingers) {
        this.popularSingers = popularSingers;
    }

    public List<SongInfoBean> getPopularSongs() {
        return popularSongs;
    }

    public void setPopularSongs(List<SongInfoBean> popularSongs) {
        this.popularSongs = popularSongs;
    }

    public List<PopularSingerEntity> getList1() {
        return list1;
    }

    public void setList1(List<PopularSingerEntity> list1) {
        this.list1 = list1;
    }

    public List<PopularSingerEntity> getList2() {
        return list2;
    }

    public void setList2(List<PopularSingerEntity> list2) {
        this.list2 = list2;
    }

    public List<PopularSingerEntity> getList3() {
        return list3;
    }

    public void setList3(List<PopularSingerEntity> list3) {
        this.list3 = list3;
    }
}

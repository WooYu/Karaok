package com.clicktech.snsktv.entity;


import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2016/12/26.
 */

public class SongListWithSingerIDResponse extends BaseResponse {


    private List<SongInfoBean> singerSongList;

    public List<SongInfoBean> getSingerSongList() {
        return singerSongList;
    }

    public void setSingerSongList(List<SongInfoBean> singerSongList) {
        this.singerSongList = singerSongList;
    }
}

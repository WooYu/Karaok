package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/4/24.
 */

public class SearchBySongNameResponse extends BaseResponse {


    private List<SongInfoBean> songList;

    public List<SongInfoBean> getSongList() {
        return songList;
    }

    public void setSongList(List<SongInfoBean> songList) {
        this.songList = songList;
    }

}

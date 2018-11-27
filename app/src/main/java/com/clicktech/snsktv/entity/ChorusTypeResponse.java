package com.clicktech.snsktv.entity;


import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

public class ChorusTypeResponse extends BaseResponse {
    private List<SongInfoBean> songList;

    public List<SongInfoBean> getList() {
        return songList;
    }

    public void setList(List<SongInfoBean> list) {
        this.songList = list;
    }

}

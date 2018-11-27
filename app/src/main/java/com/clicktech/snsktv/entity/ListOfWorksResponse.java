package com.clicktech.snsktv.entity;


import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

public class ListOfWorksResponse extends BaseResponse {


    private List<SongInfoBean> songWorksList;

    public List<SongInfoBean> getSongWorksList() {
        return songWorksList;
    }

    public void setSongWorksList(List<SongInfoBean> songWorksList) {
        this.songWorksList = songWorksList;
    }
}

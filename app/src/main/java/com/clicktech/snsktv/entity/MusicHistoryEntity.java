package com.clicktech.snsktv.entity;


import java.util.ArrayList;
import java.util.List;

public class MusicHistoryEntity {

    private List<SongInfoBean> musicList = new ArrayList<>();

    public List<SongInfoBean> getSingerList() {
        return musicList;
    }

    public void setSingerList(List<SongInfoBean> musicList) {
        this.musicList = musicList;
    }


}

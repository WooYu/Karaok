package com.clicktech.snsktv.entity;


import com.clicktech.snsktv.arms.base.BaseResponse;

/**
 * Created by Administrator on 2016/12/26.
 */

public class SongInfoWithSongIDResponse extends BaseResponse {

    private SongInfoBean songInfo;

    public SongInfoBean getSongInfo() {
        return songInfo;
    }

    public void setSongInfo(SongInfoBean songInfo) {
        this.songInfo = songInfo;
    }

}

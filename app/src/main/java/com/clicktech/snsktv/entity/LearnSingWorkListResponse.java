package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/1/19.
 * 学唱主页  排行榜
 */

public class LearnSingWorkListResponse extends BaseResponse {

    List<LearnSing_WorkListEntity> songWorksList;

    public List<LearnSing_WorkListEntity> getSongWorksList() {
        return songWorksList;
    }

    public void setSongWorksList(List<LearnSing_WorkListEntity> songWorksList) {
        this.songWorksList = songWorksList;
    }
}

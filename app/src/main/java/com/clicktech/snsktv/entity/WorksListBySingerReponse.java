package com.clicktech.snsktv.entity;


import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

public class WorksListBySingerReponse extends BaseResponse {

    private List<WorksListBySingerBeanEntity> songList;

    public List<WorksListBySingerBeanEntity> getList() {
        return songList;
    }

    public void setList(List<WorksListBySingerBeanEntity> list) {
        this.songList = list;
    }


}

package com.clicktech.snsktv.entity;


import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/26.
 */

public class SingerListWithCatecoryResponse extends BaseResponse {


    private List<SingerInfoEntity> singerList = new ArrayList<>();

    public List<SingerInfoEntity> getSingerList() {
        return singerList;
    }

    public void setSingerList(List<SingerInfoEntity> singerList) {
        this.singerList = singerList;
    }
}

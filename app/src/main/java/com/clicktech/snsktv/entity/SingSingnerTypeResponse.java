package com.clicktech.snsktv.entity;


import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2016/12/26.
 */

public class SingSingnerTypeResponse extends BaseResponse {


    private List<SingSingnerTypeBeanEntity> singerCategoryList;

    public List<SingSingnerTypeBeanEntity> getSingerCategoryList() {
        return singerCategoryList;
    }

    public void setSingerCategoryList(List<SingSingnerTypeBeanEntity> singerCategoryList) {
        this.singerCategoryList = singerCategoryList;
    }


}

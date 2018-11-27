package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/4/18.
 * 评论列表
 */

public class WorksCommentListResponse extends BaseResponse {


    private List<WorksCommentListBeanEntity> worksCommentList;

    public List<WorksCommentListBeanEntity> getWorksCommentList() {
        return worksCommentList;
    }

    public void setWorksCommentList(List<WorksCommentListBeanEntity> worksCommentList) {
        this.worksCommentList = worksCommentList;
    }
}

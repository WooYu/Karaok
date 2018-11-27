package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/1/9.
 */

public class CommentListResponse extends BaseResponse {
    List<CommentEntity> list;

    public List<CommentEntity> getList() {
        return list;
    }

    public void setList(List<CommentEntity> list) {
        this.list = list;
    }
}

package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

/**
 * Created by Administrator on 2017/4/23.
 * 关注或者收藏
 */

public class ResponseAttentonCollectResponse extends BaseResponse {

    /**
     * result : 1
     */

    private int result;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}

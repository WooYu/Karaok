package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/6/20.
 */

public class MineCollectionResponse extends BaseResponse {

    private List<SongInfoBean> myStore;

    public List<SongInfoBean> getMyStore() {
        return myStore;
    }

    public void setMyStore(List<SongInfoBean> myStore) {
        this.myStore = myStore;
    }

}

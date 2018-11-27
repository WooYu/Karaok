package com.clicktech.snsktv.module_home.ui.holder;

import android.view.View;

import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.entity.RankingResponse;

/**
 * Created by Administrator on 2017/1/19.
 */

public class EmptyHolder extends BaseHolder<RankingResponse> {
    public EmptyHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(RankingResponse data, int position) {

    }
}

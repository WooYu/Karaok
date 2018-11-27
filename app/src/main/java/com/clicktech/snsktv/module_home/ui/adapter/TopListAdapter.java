package com.clicktech.snsktv.module_home.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.TopListEntity;
import com.clicktech.snsktv.module_home.ui.holder.TopListHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/1/13.
 * 推荐列表
 */

public class TopListAdapter extends DefaultAdapter<TopListEntity> {

    public TopListAdapter(List<TopListEntity> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<TopListEntity> getHolder(View v) {
        return new TopListHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_toplist;
    }
}

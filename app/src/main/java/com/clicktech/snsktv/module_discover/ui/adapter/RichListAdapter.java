package com.clicktech.snsktv.module_discover.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.PopularSingerEntity;
import com.clicktech.snsktv.module_discover.ui.holder.RichListHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/1/20.
 * 财富榜的Adapter
 */

public class RichListAdapter extends DefaultAdapter<PopularSingerEntity> {
    public RichListAdapter(List<PopularSingerEntity> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<PopularSingerEntity> getHolder(View v) {
        return new RichListHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_find_richlist;
    }
}

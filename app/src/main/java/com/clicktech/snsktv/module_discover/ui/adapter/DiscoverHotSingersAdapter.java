package com.clicktech.snsktv.module_discover.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.PopularSingerEntity;
import com.clicktech.snsktv.module_discover.ui.holder.Discover_Home_HotSingersHolder;

import java.util.List;

/**
 * 发现-人气歌手
 */

public class DiscoverHotSingersAdapter extends DefaultAdapter<PopularSingerEntity> {
    public DiscoverHotSingersAdapter(List<PopularSingerEntity> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<PopularSingerEntity> getHolder(View v) {
        return new Discover_Home_HotSingersHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_discover_singers_item;
    }

}

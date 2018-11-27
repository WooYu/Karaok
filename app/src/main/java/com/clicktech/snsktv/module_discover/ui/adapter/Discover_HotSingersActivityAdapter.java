package com.clicktech.snsktv.module_discover.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.PopularSingerEntity;
import com.clicktech.snsktv.module_discover.ui.holder.Discover_Home_HotSingersActivityHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/1/19.
 * 发现页面排行榜
 */

public class Discover_HotSingersActivityAdapter extends DefaultAdapter<PopularSingerEntity> {
    public Discover_HotSingersActivityAdapter(List<PopularSingerEntity> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<PopularSingerEntity> getHolder(View v) {
        return new Discover_Home_HotSingersActivityHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_discover_hotsinger;
    }

    @Override
    public int getItemCount() {
        return mInfos.size();
    }
}

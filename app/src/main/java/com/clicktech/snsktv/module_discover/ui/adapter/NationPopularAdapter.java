package com.clicktech.snsktv.module_discover.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.PopularSingerEntity;
import com.clicktech.snsktv.module_discover.ui.holder.NationPopularHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/1/20.
 * 全国人气榜
 */

public class NationPopularAdapter extends DefaultAdapter<PopularSingerEntity> {

    public NationPopularAdapter(List<PopularSingerEntity> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<PopularSingerEntity> getHolder(View v) {
        return new NationPopularHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_find_nationlist;
    }
}

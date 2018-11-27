package com.clicktech.snsktv.module_home.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.ChorusInfoEntity;
import com.clicktech.snsktv.module_home.ui.holder.ChorusStarHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/1/16.
 * 明星合唱的Adapter
 */

public class ChorusStarAdapter extends DefaultAdapter<ChorusInfoEntity> {

    public ChorusStarAdapter(List<ChorusInfoEntity> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<ChorusInfoEntity> getHolder(View v) {
        return new ChorusStarHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_star;
    }
}

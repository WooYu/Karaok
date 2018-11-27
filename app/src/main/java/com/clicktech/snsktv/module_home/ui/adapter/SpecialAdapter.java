package com.clicktech.snsktv.module_home.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.PastSpecialEntity;
import com.clicktech.snsktv.module_home.ui.holder.PastSpecialHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/1/13.
 * 往期专题推荐
 */

public class SpecialAdapter extends DefaultAdapter<PastSpecialEntity> {
    public SpecialAdapter(List<PastSpecialEntity> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<PastSpecialEntity> getHolder(View v) {
        return new PastSpecialHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_special;
    }
}

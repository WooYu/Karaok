package com.clicktech.snsktv.module_home.ui.adapter;


import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.SingSingnerTypeBeanEntity;
import com.clicktech.snsktv.module_home.ui.holder.ChorusTypeHolder;

import java.util.List;


//       k2下的     合唱类型adapter

public class ChorusTypeAdapter extends DefaultAdapter<SingSingnerTypeBeanEntity> {
    public ChorusTypeAdapter(List<SingSingnerTypeBeanEntity> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SingSingnerTypeBeanEntity> getHolder(View v) {
        return new ChorusTypeHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_chours_type;
    }


}

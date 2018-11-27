package com.clicktech.snsktv.module_home.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.SingSingnerTypeBeanEntity;
import com.clicktech.snsktv.module_home.ui.holder.SingerCategoryHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/1/13.
 * 已点歌手
 */

public class SingerCategoryAdapter extends DefaultAdapter<SingSingnerTypeBeanEntity> {

    public SingerCategoryAdapter(List<SingSingnerTypeBeanEntity> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SingSingnerTypeBeanEntity> getHolder(View v) {
        return new SingerCategoryHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_singercategory;
    }
}

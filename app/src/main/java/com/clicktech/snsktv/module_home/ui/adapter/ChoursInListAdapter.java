package com.clicktech.snsktv.module_home.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.CommonSingerEntity;
import com.clicktech.snsktv.module_home.ui.holder.ChoursInListHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/2/3.
 * 合唱参与列表
 */

public class ChoursInListAdapter extends DefaultAdapter<CommonSingerEntity> {

    public ChoursInListAdapter(List<CommonSingerEntity> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<CommonSingerEntity> getHolder(View v) {
        return new ChoursInListHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_choursinlist;
    }
}

package com.clicktech.snsktv.module_home.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.ChorusInfoEntity;
import com.clicktech.snsktv.module_home.ui.holder.ChorusRecomHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/1/16.
 * 合唱推荐的Adapter
 */


public class ChorusRecomAdapter extends DefaultAdapter<ChorusInfoEntity> {

    public ChorusRecomAdapter(List<ChorusInfoEntity> infos) {
        super(infos);
    }

    @Override
    public BaseHolder getHolder(View v) {
        return new ChorusRecomHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_recommend;
    }

}

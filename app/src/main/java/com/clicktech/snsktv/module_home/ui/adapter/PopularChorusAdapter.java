package com.clicktech.snsktv.module_home.ui.adapter;


import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_home.ui.holder.PopularChorusHolder;
import com.clicktech.snsktv.module_home.ui.listener.JoinInChorusListener;

import java.util.List;

//   人气合唱

public class PopularChorusAdapter extends DefaultAdapter<SongInfoBean> {

    private JoinInChorusListener mListener;

    public PopularChorusAdapter(List<SongInfoBean> infos, JoinInChorusListener listener) {
        super(infos);
        this.mListener = listener;
    }

    @Override
    public BaseHolder<SongInfoBean> getHolder(View v) {
        return new PopularChorusHolder(v, mListener);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_popular_chorus;
    }
}

package com.clicktech.snsktv.module_home.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_home.ui.holder.PopularListHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/1/19.
 * 人气榜的Adapter
 */

public class PopularListAdapter extends DefaultAdapter<SongInfoBean> {
    PopularListHolder.onItemClickListener mListener;

    public PopularListAdapter(List<SongInfoBean> infos, PopularListHolder.onItemClickListener listener) {
        super(infos);
        this.mListener = listener;
    }

    @Override
    public BaseHolder<SongInfoBean> getHolder(View v) {
        return new PopularListHolder(v, mListener);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_listen_nation;
    }
}

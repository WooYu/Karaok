package com.clicktech.snsktv.module_discover.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.ui.holder.SingleListHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/1/22.
 * 单曲列表
 */

public class SingleListAdapter extends DefaultAdapter<SongInfoBean> {
    public SingleListAdapter(List<SongInfoBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SongInfoBean> getHolder(View v) {
        return new SingleListHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_single_song;
    }
}

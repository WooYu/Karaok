package com.clicktech.snsktv.module_discover.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.AlbumBean;
import com.clicktech.snsktv.module_discover.ui.holder.AlbumListHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/1/22.
 * 专辑列表
 */

public class AlbumListAdapter extends DefaultAdapter<AlbumBean> {

    public AlbumListAdapter(List<AlbumBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<AlbumBean> getHolder(View v) {
        return new AlbumListHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_albumlist;
    }
}

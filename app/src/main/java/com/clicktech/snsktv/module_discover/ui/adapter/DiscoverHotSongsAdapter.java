package com.clicktech.snsktv.module_discover.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.ui.holder.Discover_Home_HotSongsHolder;

import java.util.List;

/**
 * 发现-人气歌曲
 */

public class DiscoverHotSongsAdapter extends DefaultAdapter<SongInfoBean> {
    public DiscoverHotSongsAdapter(List<SongInfoBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SongInfoBean> getHolder(View v) {
        return new Discover_Home_HotSongsHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_discover_songs_item;
    }
}

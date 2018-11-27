package com.clicktech.snsktv.module_discover.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.ui.holder.Discover_Home_HotSongsActivityHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/1/19.
 * 发现页面排行榜
 */

public class Discover_HotSongsActivityAdapter extends DefaultAdapter<SongInfoBean> {
    public Discover_HotSongsActivityAdapter(List<SongInfoBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SongInfoBean> getHolder(View v) {
        return new Discover_Home_HotSongsActivityHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_discover_hotsong;
    }

    @Override
    public int getItemCount() {
        return mInfos.size();
    }
}

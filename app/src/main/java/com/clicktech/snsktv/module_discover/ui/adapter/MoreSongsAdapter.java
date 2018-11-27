package com.clicktech.snsktv.module_discover.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.ui.holder.MoreSongsHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/1/22.
 * 更多歌曲
 */

public class MoreSongsAdapter extends DefaultAdapter<SongInfoBean> {

    public MoreSongsAdapter(List<SongInfoBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SongInfoBean> getHolder(View v) {
        return new MoreSongsHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_moresongs;
    }
}

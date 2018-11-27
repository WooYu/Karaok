package com.clicktech.snsktv.module_home.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_home.ui.holder.SongSelectHistoryHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/1/13.
 * 已点歌手
 */

public class SongSelectHistoryAdapter extends DefaultAdapter<SongInfoBean> {

    public SongSelectHistoryAdapter(List<SongInfoBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SongInfoBean> getHolder(View v) {
        return new SongSelectHistoryHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_songselecthistory;
    }
}

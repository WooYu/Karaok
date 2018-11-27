package com.clicktech.snsktv.module_mine.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.SoundHistoryEntity;
import com.clicktech.snsktv.module_mine.ui.holder.Mine_MySoundHistoryListHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/1/20.
 * 我的合唱Adapter
 */

public class Mine_MySoundHistoryListAdapter extends DefaultAdapter<SoundHistoryEntity> {


    public Mine_MySoundHistoryListAdapter(List<SoundHistoryEntity> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SoundHistoryEntity> getHolder(View v) {
        return new Mine_MySoundHistoryListHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_activity_mine_mysound_historylist;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}

package com.clicktech.snsktv.module_mine.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_mine.ui.holder.Mine_MyListenHistoryListHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/1/20.
 * 我的合唱Adapter
 */

public class Mine_MyListenHistoryListAdapter extends DefaultAdapter<SongInfoBean> {


    public Mine_MyListenHistoryListAdapter(List<SongInfoBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SongInfoBean> getHolder(View v) {
        return new Mine_MyListenHistoryListHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_activity_mine_mylisten_historylist;
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

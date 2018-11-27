package com.clicktech.snsktv.module_home.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.SearchHistory;
import com.clicktech.snsktv.module_home.ui.holder.SearchHistoryHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/1/11.
 * 历史记录的Adapter
 */

public class SearchHistoryAdapter extends DefaultAdapter<SearchHistory.SearchHistoryBean> {
    public SearchHistoryAdapter(List<SearchHistory.SearchHistoryBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SearchHistory.SearchHistoryBean> getHolder(View v) {
        return new SearchHistoryHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_search_history;
    }
}

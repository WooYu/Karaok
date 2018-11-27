package com.clicktech.snsktv.module_home.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.entity.SearchHistory;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/1/11.
 */

public class SearchHistoryHolder extends BaseHolder<SearchHistory.SearchHistoryBean> {

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.m_singer_name)
    TextView singerName;

    public SearchHistoryHolder(View itemView) {
        super(itemView);
    }


    @Override
    public void setData(SearchHistory.SearchHistoryBean data, int position) {

        name.setText(data.getName());
//        singerName.setText(data.get);


    }
}

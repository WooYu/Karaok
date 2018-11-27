package com.clicktech.snsktv.module_home.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_home.ui.holder.ListenNationHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/1/19.
 * 收听榜：全国排行
 */

public class ListenNationAdapter extends DefaultAdapter<SongInfoBean> {
    public ListenNationAdapter(List<SongInfoBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SongInfoBean> getHolder(View v) {
        return new ListenNationHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_listen_nation;
    }

}

package com.clicktech.snsktv.module_home.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_home.ui.holder.AssociateHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/1/12.
 * 搜索联想的Adapter
 */

public class AssociateAdapter extends DefaultAdapter<SongInfoBean> {

    public AssociateAdapter(List<SongInfoBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SongInfoBean> getHolder(View v) {
        return new AssociateHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_associate;
    }

}

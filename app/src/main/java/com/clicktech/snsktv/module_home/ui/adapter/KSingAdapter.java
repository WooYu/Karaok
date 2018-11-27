package com.clicktech.snsktv.module_home.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_home.ui.holder.SingItemHolder;

import java.util.List;

/**
 * Created by Administrator on 2016/12/26.
 */

public class KSingAdapter extends DefaultAdapter<SongInfoBean> {
    public KSingAdapter(List<SongInfoBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SongInfoBean> getHolder(View v) {
        return new SingItemHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_song;
    }

}

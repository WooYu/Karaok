package com.clicktech.snsktv.module_home.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.SingerInfoEntity;
import com.clicktech.snsktv.module_home.ui.holder.SingerSelectHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/1/13.
 * 已点歌手
 */

public class SingerSelectAdapter extends DefaultAdapter<SingerInfoEntity> {

    public SingerSelectAdapter(List<SingerInfoEntity> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SingerInfoEntity> getHolder(View v) {
        return new SingerSelectHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_singerselect;
    }
}

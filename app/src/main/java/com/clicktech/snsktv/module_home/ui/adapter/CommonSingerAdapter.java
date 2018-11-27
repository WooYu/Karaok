package com.clicktech.snsktv.module_home.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.SingerInfoEntity;
import com.clicktech.snsktv.module_home.ui.holder.CommonSingerHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/1/11.
 * 常用歌手Adapter
 */

public class CommonSingerAdapter extends DefaultAdapter<SingerInfoEntity> {

    public CommonSingerAdapter(List<SingerInfoEntity> infos) {
        super(infos);
    }

    @Override
    public BaseHolder getHolder(View v) {
        return new CommonSingerHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_singer;
    }

}

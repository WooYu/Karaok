package com.clicktech.snsktv.module_home.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.SoundEffectEntity;
import com.clicktech.snsktv.module_home.ui.holder.SoundEffectHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/3/14.
 * 音效的Adapter
 */

public class SoundEffectAdapter extends DefaultAdapter<SoundEffectEntity> {

    public SoundEffectAdapter(List<SoundEffectEntity> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SoundEffectEntity> getHolder(View v) {
        return new SoundEffectHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_soundeffect;
    }
}

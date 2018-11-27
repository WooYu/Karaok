package com.clicktech.snsktv.module_home.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.SongTypeBeanEntity;
import com.clicktech.snsktv.module_home.ui.holder.ThemeHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/5/15.
 */

public class ThemeAdapter extends DefaultAdapter<SongTypeBeanEntity> {

    private ThemeHolder.ThemeClickListener mThemeClick;

    public ThemeAdapter(List<SongTypeBeanEntity> infos, ThemeHolder.ThemeClickListener listener) {
        super(infos);
        this.mThemeClick = listener;
    }

    @Override
    public BaseHolder<SongTypeBeanEntity> getHolder(View v) {
        return new ThemeHolder(v, mThemeClick);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_theme;
    }

}

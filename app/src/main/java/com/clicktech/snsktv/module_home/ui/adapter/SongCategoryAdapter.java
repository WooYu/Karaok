package com.clicktech.snsktv.module_home.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.SingSingnerTypeBeanEntity;
import com.clicktech.snsktv.module_home.ui.holder.SongCategoryHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/1/13.
 * 已点歌手
 */

public class SongCategoryAdapter extends DefaultAdapter<SingSingnerTypeBeanEntity> {

    private boolean mLargeSpace;

    public SongCategoryAdapter(List<SingSingnerTypeBeanEntity> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SingSingnerTypeBeanEntity> getHolder(View v) {
        return new SongCategoryHolder(v);
    }

    @Override
    public int getLayoutId() {
        if (mLargeSpace) {
            return R.layout.adapter_songcategory2;
        }
        return R.layout.adapter_songcategory;
    }

    @Override
    public int getItemViewType(int position) {
        mLargeSpace = position == 1 || position == mInfos.size() - 1;
        return super.getItemViewType(position);
    }

}

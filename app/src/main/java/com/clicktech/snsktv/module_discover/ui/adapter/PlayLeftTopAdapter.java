package com.clicktech.snsktv.module_discover.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.ui.holder.PlayLeftTopHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/4/26.
 */

public class PlayLeftTopAdapter extends DefaultAdapter<SongInfoBean> {
    public PlayLeftTopAdapter(List<SongInfoBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SongInfoBean> getHolder(View v) {
        return new PlayLeftTopHolder(v);
    }


    @Override
    public int getLayoutId() {
        return R.layout.adapter_playleft_top;
    }

    @Override
    public int getItemCount() {
        if (EmptyUtils.isEmpty(mInfos)) {
            return 0;
        }
        return mInfos.size() > 3 ? 3 : mInfos.size();
    }
}

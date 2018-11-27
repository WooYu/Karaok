package com.clicktech.snsktv.module_mine.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.Att_Fans_UserEntity;
import com.clicktech.snsktv.module_mine.ui.holder.Mine_FansUserListHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/1/20.
 * 我的粉丝/我的关注  用户Adapter
 */

public class Mine_FansUserListAdapter extends DefaultAdapter<Att_Fans_UserEntity> {
    boolean isfans = false;//是否是粉丝列表。false代表关注列表

    public Mine_FansUserListAdapter(List<Att_Fans_UserEntity> infos, boolean isFans) {
        super(infos);
        this.isfans = isFans;
    }

    @Override
    public BaseHolder<Att_Fans_UserEntity> getHolder(View v) {
        return new Mine_FansUserListHolder(v, isfans);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_activity_mine_fansuserlist;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}

package com.clicktech.snsktv.module_mine.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.Att_Fans_UserEntity;
import com.clicktech.snsktv.module_mine.ui.holder.Mine_AttentionUserListHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/1/20.
 * 我的粉丝/我的关注  用户Adapter
 */

public class Mine_AttentionUserListAdapter extends DefaultAdapter<Att_Fans_UserEntity> {
    boolean hideAttent = false;

    public Mine_AttentionUserListAdapter(List<Att_Fans_UserEntity> infos, boolean hideAttent) {
        super(infos);
        this.hideAttent = hideAttent;
    }

    @Override
    public BaseHolder<Att_Fans_UserEntity> getHolder(View v) {
        return new Mine_AttentionUserListHolder(v, hideAttent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_activity_mine_attenuserlist;
    }

}
